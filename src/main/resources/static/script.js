document.addEventListener('DOMContentLoaded', () => {
    // Получаем ссылки на элементы DOM, с которыми будем работать
    const studentTableBody = document.getElementById('studentTableBody');
    const searchInput = document.getElementById('searchInput');
    const statusFilter = document.getElementById('statusFilter');

    // Модальные окна
    const modal = document.getElementById('modal');
    const modalMessage = document.getElementById('modal-message');
    const modalButtons = document.getElementById('modal-buttons');
    const confirmBtn = document.getElementById('confirm-btn');
    const cancelBtn = document.getElementById('cancel-btn');

    // Элементы для отображения счетчиков
    const activeCountDisplay = document.getElementById('activeCount');
    const stopLearningCountDisplay = document.getElementById('stopLearningCount');
    const inRegisterCountDisplay = document.getElementById('inRegisterCount');

    // Базовый URL для вашего API
    const API_BASE_URL = 'http://localhost:8080/v1/student';

    // Сопоставление статусов бэкенда с названиями классов фронтенда
    const statusMapping = {
        'ACTIVE': 'active',
        'STOP_LEARNING': 'stopped',
        'IN_REGISTER': 'block'
    };

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

    // Функция для получения и отображения данных о студентах
    async function fetchAndRenderStudents(url) {
        try {
            const response = await fetch(url);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const students = await response.json();
            studentTableBody.innerHTML = '';

            if (students.length === 0) {
                studentTableBody.innerHTML = `<tr><td colspan="5" class="no-data">Список пуст.</td></tr>`;
                return;
            }

            students.forEach(student => {
                const row = document.createElement('tr');
                const groupName = student.group ? student.group.name : '-';
                const statusClass = statusMapping[student.status] || '';

                row.innerHTML = `
                    <td>${student.fullName}</td>
                    <td>${student.phoneNum}</td>
                    <td><span class="status ${statusClass}">${student.status}</span></td>
                    <td>${groupName}</td>
                    <td class="actions">
                        <button class="action-btn edit-btn" data-id="${student.id}">Изменить</button>
                        <button class="action-btn delete-btn" data-id="${student.id}">Удалить</button>
                    </td>
                `;
                studentTableBody.appendChild(row);
            });
        } catch (error) {
            console.error('Ошибка при получении студентов:', error);
            studentTableBody.innerHTML = `<tr><td colspan="5" class="error-message">Ошибка загрузки данных.</td></tr>`;
        }
    }

    // Функция для получения и отображения счетчиков студентов
    async function fetchAndRenderCounts() {
        try {
            const statuses = ['ACTIVE', 'STOP_LEARNING', 'IN_REGISTER'];
            const fetchPromises = statuses.map(status =>
                fetch(`${API_BASE_URL}/count/${status}`).then(res => res.json())
            );

            const counts = await Promise.all(fetchPromises);

            activeCountDisplay.textContent = counts[0];
            stopLearningCountDisplay.textContent = counts[1];
            inRegisterCountDisplay.textContent = counts[2];

        } catch (error) {
            console.error('Ошибка при получении счетчиков студентов:', error);
            const statusDisplays = [activeCountDisplay, stopLearningCountDisplay, inRegisterCountDisplay];
            statusDisplays.forEach(el => {
                if (el) el.textContent = 'Ошибка';
            });
        }
    }

    // Обработчики событий для поиска и фильтрации
    searchInput.addEventListener('input', () => {
        const searchTerm = searchInput.value.trim();
        if (searchTerm.length > 2) {
            fetchAndRenderStudents(`${API_BASE_URL}/findByName/${searchTerm}`);
        } else {
            fetchAndRenderStudents(`${API_BASE_URL}/list`);
        }
    });

    statusFilter.addEventListener('change', () => {
        const selectedStatus = statusFilter.value;
        if (selectedStatus) {
            fetchAndRenderStudents(`${API_BASE_URL}/sortByStatus/${selectedStatus}`);
        } else {
            fetchAndRenderStudents(`${API_BASE_URL}/list`);
        }
    });

    // Обработчик событий для кнопок "Удалить" и "Изменить"
    studentTableBody.addEventListener('click', async (event) => {
        if (event.target.classList.contains('delete-btn')) {
            const studentId = event.target.getAttribute('data-id');

            const confirmed = await showConfirmationModal(`Вы уверены, что хотите удалить ученика с ID ${studentId}?`);

            if (confirmed) {
                try {
                    const response = await fetch(`${API_BASE_URL}/delete/${studentId}`, {
                        method: 'DELETE',
                    });

                    if (response.ok) {
                        const row = event.target.closest('tr');
                        if (row) {
                            row.remove();
                            showMessageModal('Ученик успешно удален.', 'success');
                            fetchAndRenderCounts(); // Обновляем счетчики после удаления
                        }
                    } else {
                        // Если ответ сервера не 2xx, обрабатываем ошибку
                        const errorText = await response.text();
                        throw new Error(`Не удалось удалить ученика. Статус: ${response.status}. Ответ сервера: ${errorText}`);
                    }
                } catch (error) {
                    console.error('Ошибка при удалении студента:', error);
                    showMessageModal(`Произошла ошибка при удалении ученика: ${error.message}`, 'error');
                }
            }
        } else if (event.target.classList.contains('edit-btn')) {
            const studentId = event.target.getAttribute('data-id');
            window.location.href = `/v1/student/edit/${studentId}`;
        }
    });

    // Загрузка начального списка студентов и счетчиков при загрузке страницы
    fetchAndRenderStudents(`${API_BASE_URL}/list`);
    fetchAndRenderCounts();
});
