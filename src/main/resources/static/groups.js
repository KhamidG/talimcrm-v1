document.addEventListener('DOMContentLoaded', () => {
    // Получаем ссылки на элементы DOM, с которыми будем работать
    const groupsTableBody = document.getElementById('groupsTableBody');
    const searchInput = document.getElementById('searchInput');
    const modal = document.getElementById('modal');
    const modalMessage = document.getElementById('modal-message');
    const modalButtons = document.getElementById('modal-buttons');
    const confirmBtn = document.getElementById('confirm-btn');
    const cancelBtn = document.getElementById('cancel-btn');

    // Базовый URL для вашего API групп
    const API_BASE_URL = 'http://localhost:8080/v1/group';

    // Функция для отображения сообщений в модальном окне
    function showMessageModal(message, type) {
        modalMessage.textContent = message;
        modalMessage.className = `modal-message ${type}`;
        modalButtons.classList.add('hidden');
        modal.style.display = 'flex';
        setTimeout(() => {
            modal.style.display = 'none';
        }, 3000);
    }

    // Функция для отображения модального окна подтверждения
    function showConfirmationModal(message) {
        return new Promise(resolve => {
            modalMessage.textContent = message;
            modalMessage.className = 'modal-message';
            modalButtons.classList.remove('hidden');
            modal.style.display = 'flex';

            confirmBtn.onclick = () => {
                modal.style.display = 'none';
                resolve(true);
            };

            cancelBtn.onclick = () => {
                modal.style.display = 'none';
                resolve(false);
            };
        });
    }

    // Функция для получения и отображения данных о группах
    async function fetchAndRenderGroups(url) {
        try {
            const response = await fetch(url);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const groups = await response.json();
            groupsTableBody.innerHTML = '';

            if (groups.length === 0) {
                groupsTableBody.innerHTML = `<tr><td colspan="4" class="no-data">Список групп пуст.</td></tr>`;
                return;
            }

            groups.forEach(group => {
                const row = document.createElement('tr');
                const trainerName = group.trainer ? group.trainer.fullName : '-';
                const studentCount = group.students ? group.students.length : 0;

                row.innerHTML = `
                    <td>${group.name}</td>
                    <td>${trainerName}</td>
                    <td>${studentCount}</td>
                    <td class="actions">
                        <button class="action-btn edit-btn" data-id="${group.id}">Изменить</button>
                        <button class="action-btn delete-btn" data-id="${group.id}">Удалить</button>
                    </td>
                `;
                groupsTableBody.appendChild(row);
            });
        } catch (error) {
            console.error('Ошибка при получении групп:', error);
            groupsTableBody.innerHTML = `<tr><td colspan="4" class="error-message">Ошибка загрузки данных.</td></tr>`;
        }
    }

    // Обработчик событий для поиска
    searchInput.addEventListener('input', () => {
        const searchTerm = searchInput.value.trim();
        if (searchTerm.length > 2) {
            fetchAndRenderGroups(`${API_BASE_URL}/findByName/${searchTerm}`);
        } else {
            fetchAndRenderGroups(`${API_BASE_URL}/list`);
        }
    });

    // Обработчик событий для кнопок "Удалить" и "Изменить"
    groupsTableBody.addEventListener('click', async (event) => {
        if (event.target.classList.contains('delete-btn')) {
            const groupId = event.target.getAttribute('data-id');

            const confirmed = await showConfirmationModal(`Вы уверены, что хотите удалить группу с ID ${groupId}?`);

            if (confirmed) {
                try {
                    const response = await fetch(`${API_BASE_URL}/delete/${groupId}`, {
                        method: 'DELETE',
                    });

                    if (response.ok) {
                        const row = event.target.closest('tr');
                        if (row) {
                            row.remove();
                            showMessageModal('Группа успешно удалена.', 'success');
                        }
                    } else {
                        const errorText = await response.text();
                        throw new Error(`Не удалось удалить группу. Статус: ${response.status}. Ответ сервера: ${errorText}`);
                    }
                } catch (error) {
                    console.error('Ошибка при удалении группы:', error);
                    showMessageModal(`Произошла ошибка при удалении группы: ${error.message}`, 'error');
                }
            }
        } else if (event.target.classList.contains('edit-btn')) {
            const groupId = event.target.getAttribute('data-id');
            window.location.href = `/v1/group/edit/${groupId}`;
        }
    });

    // Загрузка начального списка групп при загрузке страницы
    fetchAndRenderGroups(`${API_BASE_URL}/list`);
});
