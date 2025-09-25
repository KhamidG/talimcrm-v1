document.addEventListener('DOMContentLoaded', () => {
    const registrationForm = document.getElementById('registrationForm');
    const messageDisplay = document.getElementById('message');

    registrationForm.addEventListener('submit', async (event) => {
        event.preventDefault();

        const formData = new FormData(registrationForm);
        const teacherData = Object.fromEntries(formData.entries());

        const API_BASE_URL = 'http://localhost:8080/v1/teacher';

        try {
            const response = await fetch(`${API_BASE_URL}/register`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(teacherData),
            });

            if (response.ok) {
                const newTeacher = await response.json();
                showMessage('Преподаватель успешно зарегистрирован!', 'success');
                console.log('Новый преподаватель:', newTeacher);
                registrationForm.reset();
            } else {
                const errorData = await response.json();
                showMessage(`Ошибка регистрации: ${errorData.message}`, 'error');
            }
        } catch (error) {
            console.error('Ошибка при отправке данных:', error);
            showMessage('Ошибка сети. Пожалуйста, попробуйте снова.', 'error');
        }
    });

    function showMessage(text, type) {
        messageDisplay.textContent = text;
        messageDisplay.className = '';
        messageDisplay.classList.add(type);
        messageDisplay.classList.remove('hidden');
    }
});
