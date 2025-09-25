document.addEventListener('DOMContentLoaded', () => {
    const registrationForm = document.getElementById('registrationForm');
    const messageDisplay = document.getElementById('message');

    registrationForm.addEventListener('submit', async (event) => {
        // Предотвращаем стандартное поведение отправки формы
        event.preventDefault();

        // Собираем данные из формы
        const formData = new FormData(registrationForm);
        const studentData = Object.fromEntries(formData.entries());

        // Базовый URL для вашего API
        const API_BASE_URL = 'http://localhost:8080/v1/student';

        // Отправляем данные на бэкенд
        try {
            const response = await fetch(`${API_BASE_URL}/register`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(studentData),
            });

            if (response.ok) {
                // Если регистрация прошла успешно
                const newStudent = await response.json();
                showMessage('Ученик успешно зарегистрирован!', 'success');
                console.log('Новый ученик:', newStudent);
                registrationForm.reset(); // Сбросить форму
            } else {
                // Если произошла ошибка на стороне сервера
                const errorData = await response.json();
                showMessage(`Ошибка регистрации: ${errorData.message}`, 'error');
            }
        } catch (error) {
            // Если произошла ошибка сети
            console.error('Ошибка при отправке данных:', error);
            showMessage('Ошибка сети. Пожалуйста, попробуйте снова.', 'error');
        }
    });

    function showMessage(text, type) {
        messageDisplay.textContent = text;
        messageDisplay.className = ''; // Сброс классов
        messageDisplay.classList.add(type);
        messageDisplay.classList.remove('hidden');
    }
});
