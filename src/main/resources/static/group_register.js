document.addEventListener('DOMContentLoaded', () => {
    const groupRegistrationForm = document.getElementById('groupRegistrationForm');
    const studentSearchInput = document.getElementById('studentSearch');
    const studentsSelect = document.getElementById('students');
    const messageDisplay = document.getElementById('message');

    const API_BASE_URL = 'http://localhost:8080/v1/group';
    const STUDENT_API_URL = 'http://localhost:8080/v1/student';

    let debounceTimer;

    // Функция для отображения сообщений
    function showMessage(text, type) {
        messageDisplay.textContent = text;
        messageDisplay.className = '';
        messageDisplay.classList.add(type);
        messageDisplay.classList.remove('hidden');
    }

    // Функция для загрузки и отображения студентов
    async function fetchAndRenderStudents(searchTerm) {
        try {
            const url = searchTerm.length > 2
                ? `${STUDENT_API_URL}/findByName/${searchTerm}`
                : `${STUDENT_API_URL}/list`;

            const response = await fetch(url);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const students = await response.json();

            studentsSelect.innerHTML = '';
            students.forEach(student => {
                const option = document.createElement('option');
                option.value = student.id;
                option.textContent = student.fullName;
                studentsSelect.appendChild(option);
            });

        } catch (error) {
            console.error('Ошибка при поиске студентов:', error);
            showMessage('Ошибка при загрузке студентов. Пожалуйста, попробуйте снова.', 'error');
        }
    }

    // Обработчик события ввода в поле поиска
    studentSearchInput.addEventListener('input', () => {
        clearTimeout(debounceTimer);
        debounceTimer = setTimeout(() => {
            const searchTerm = studentSearchInput.value.trim();
            fetchAndRenderStudents(searchTerm);
        }, 300); // Задержка в 300 мс для предотвращения лишних запросов
    });

    // Обработчик отправки формы
    groupRegistrationForm.addEventListener('submit', async (event) => {
        event.preventDefault();

        const groupName = document.getElementById('groupName').value;
        const trainerId = document.getElementById('trainer').value;

        // Собираем ID выбранных студентов
        const studentIds = Array.from(studentsSelect.options)
                                 .filter(option => option.selected)
                                 .map(option => option.value);

        const groupData = {
            name: groupName,
            trainerId: trainerId,
            studentIds: studentIds
        };

        try {
            const response = await fetch(`${API_BASE_URL}/register`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(groupData),
            });

            if (response.ok) {
                const newGroup = await response.json();
                showMessage('Группа успешно создана!', 'success');
                console.log('Новая группа:', newGroup);
                groupRegistrationForm.reset();
            } else {
                const errorData = await response.json();
                showMessage(`Ошибка создания группы: ${errorData.message}`, 'error');
            }
        } catch (error) {
            console.error('Ошибка при отправке данных:', error);
            showMessage('Ошибка сети. Пожалуйста, попробуйте снова.', 'error');
        }
    });
});
