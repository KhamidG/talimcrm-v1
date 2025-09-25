document.addEventListener('DOMContentLoaded', () => {
    const editForm = document.getElementById('editForm');

    // Если форма существует на странице, добавляем обработчик
    if (editForm) {
        editForm.addEventListener('submit', async (event) => {
            // Предотвращаем стандартную отправку формы
            event.preventDefault();

            // Собираем данные из полей формы
            const studentId = editForm.querySelector('input[type="hidden"]').value;
            const fullName = editForm.querySelector('#fullName').value;
            const phoneNum = editForm.querySelector('#phoneNum').value;
            const status = editForm.querySelector('#status').value;

            // Создаем объект с данными для отправки на сервер
            const studentData = {
                id: studentId, // Убедитесь, что ID корректно передается!
                fullName: fullName,
                phoneNum: phoneNum,
                status: status
            };

            // Логируем данные, которые будут отправлены на сервер
            console.log('Отправка данных на сервер:', studentData);

            try {
                // Отправляем PUT-запрос на бэкенд для обновления данных
                const response = await fetch('/v1/student/edit', {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(studentData),
                });

                if (response.ok) {
                    alert('Данные ученика успешно обновлены.');
                    // Перенаправляем пользователя на страницу со списком
                    window.location.href = '/v1/student/listPage';
                } else {
                    alert('Не удалось обновить данные ученика.');
                }
            } catch (error) {
                console.error('Ошибка при обновлении:', error);
                alert('Произошла ошибка при обновлении данных.');
            }
        });
    }
});
