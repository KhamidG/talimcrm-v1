document.addEventListener('DOMContentLoaded', () => {
    const statusFilter = document.getElementById('statusFilter');

    async function fetchStatuses() {
        try {
            const response = await fetch('http://localhost:8080/v1/student/statuses');
            const statuses = await response.json();

            // Clear any existing options
            statusFilter.innerHTML = '<option value="">All Statuses</option>';

            // Add new options from the backend
            statuses.forEach(status => {
                const option = document.createElement('option');
                option.value = status;
                option.textContent = status;
                statusFilter.appendChild(option);
            });
        } catch (error) {
            console.error('Error fetching statuses:', error);
        }
    }

    fetchStatuses();
});
// A single object to manage API endpoints
const API_URLS = {
    BASE: 'http://localhost:8080/v1/student',
    LIST: 'http://localhost:8080/v1/student/list',
    REGISTER: 'http://localhost:8080/v1/student/register',
    DELETE: 'http://localhost:8080/v1/student/delete',
    FIND_BY_NAME: 'http://localhost:8080/v1/student/findByName',
    SORT_BY_STATUS: 'http://localhost:8080/v1/student/sortByStatus'
};

document.addEventListener('DOMContentLoaded', () => {
    const studentTableBody = document.getElementById('studentTableBody');
    const searchInput = document.getElementById('searchInput');
    const statusFilter = document.getElementById('statusFilter');

    // Function to fetch and render students based on search/filter criteria
    async function fetchAndRenderStudents() {
        let url = API_URLS.LIST;
        const searchTerm = searchInput.value;
        const filterStatus = statusFilter.value;

        // Determine which API endpoint to use
        if (filterStatus) {
            url = `${API_URLS.SORT_BY_STATUS}/${filterStatus}`;
        } else if (searchTerm) {
            url = `${API_URLS.FIND_BY_NAME}/${searchTerm}`;
        }

        try {
            const response = await fetch(url);
            const students = await response.json();

            studentTableBody.innerHTML = ''; // Clear the table

            students.forEach(student => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${student.id}</td>
                    <td>${student.fullName}</td>
                    <td>${student.status}</td>
                    <td class="actions">
                        <a href="#" onclick="showDetails(${student.id})" class="btn view-btn">View</a>
                        <button class="btn delete-btn" onclick="deleteStudent(${student.id})">Delete</button>
                    </td>
                `;
                studentTableBody.appendChild(row);
            });
        } catch (error) {
            console.error('Error fetching students:', error);
            studentTableBody.innerHTML = '<tr><td colspan="4">Could not load student data.</td></tr>';
        }
    }

    // Event listeners to trigger fetching
    searchInput.addEventListener('keyup', fetchAndRenderStudents);
    statusFilter.addEventListener('change', fetchAndRenderStudents);

    // Initial load of all students
    fetchAndRenderStudents();
});

// These functions should be placed in your script.js file to be accessible
async function deleteStudent(studentId) {
    if (confirm('Are you sure you want to delete this student?')) {
        try {
            const response = await fetch(`${API_URLS.DELETE}/${studentId}`, { method: 'DELETE' });
            if (response.ok) {
                alert('Student deleted successfully.');
                // Refresh the table
                location.reload();
            } else {
                alert('Failed to delete student.');
            }
        } catch (error) {
            console.error('Error deleting student:', error);
        }
    }
}

// You will also need to create a function to handle the registration form submission on register.html.
// (Example of form submission handling was provided in a previous response)