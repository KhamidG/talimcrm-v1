document.addEventListener('DOMContentLoaded', () => {
    const teacherTableBody = document.getElementById('teacherTableBody');
    const searchInput = document.getElementById('searchInput');

    const TEACHER_API_URL = 'http://localhost:8080/v1/teacher';

    let debounceTimer;

    // Функция для отображения сообщений
    function showMessage(text, type) {
        // У нас нет отдельного элемента для сообщений на этой странице
        // Но мы можем использовать console.log для отладки
        console.log(`[${type}] ${text}`);
    }

    // Функция для загрузки и отображения преподавателей
    async function fetchAndRenderTeachers(searchTerm = '') {
        try {
            const url = searchTerm
                ? `${TEACHER_API_URL}/findByName/${searchTerm}`
                : `${TEACHER_API_URL}/list`;

            const response = await fetch(url);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const teachers = await response.json();

            renderTeachers(teachers);

        } catch (error) {
            console.error('Ошибка при загрузке преподавателей:', error);
            showMessage('Ошибка при загрузке преподавателей. Пожалуйста, попробуйте снова.', 'error');
        }
    }

    // Функция для рендеринга данных в таблицу
    function renderTeachers(teachers) {
        teacherTableBody.innerHTML = '';
        if (teachers.length === 0) {
            const row = teacherTableBody.insertRow();
            const cell = row.insertCell(0);
            cell.colSpan = 3;
            cell.textContent = 'Нет данных для отображения.';
            cell.classList.add('no-data');
            return;
        }

        teachers.forEach(teacher => {
            const row = teacherTableBody.insertRow();
            row.dataset.teacherId = teacher.id;

            row.insertCell(0).textContent = teacher.fullName;
            row.insertCell(1).textContent = teacher.phoneNum;

            const actionsCell = row.insertCell(2);
            actionsCell.classList.add('actions');

            const editButton = document.createElement('button');
            editButton.textContent = 'Изменить';
            editButton.classList.add('action-btn', 'edit-btn');
            editButton.onclick = () => {
                window.location.href = `${TEACHER_API_URL}/edit/${teacher.id}`;
            };

            const deleteButton = document.createElement('button');
            deleteButton.textContent = 'Удалить';
            deleteButton.classList.add('action-btn', 'delete-btn');
            deleteButton.onclick = () => {
                // Временно удаляем только с экрана
                row.remove();
            };

            actionsCell.appendChild(editButton);
            actionsCell.appendChild(deleteButton);
        });
    }

    // Обработчик события ввода в поле поиска
    searchInput.addEventListener('input', () => {
        clearTimeout(debounceTimer);
        debounceTimer = setTimeout(() => {
            const searchTerm = searchInput.value.trim();
            fetchAndRenderTeachers(searchTerm);
        }, 300); // Задержка в 300 мс для предотвращения лишних запросов
    });

    // Инициализация при загрузке страницы
    fetchAndRenderTeachers();
});
