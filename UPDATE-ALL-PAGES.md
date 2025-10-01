# 🔄 Обновление всех страниц - Инструкция

## ✅ Уже обновлено:
- ✅ `reception.html` - Reception Dashboard
- ✅ `students.html` - Students Management

## 📋 Нужно обновить:

### 1. Groups Page
- `groups.html`
- `group_register.html`

### 2. Teachers Page
- `teachers.html`
- `teacher_register.html`
- `teacher_login.html` (уже обновлен)
- `teacher_dashboard.html` (уже обновлен)
- `teacher_statistics.html` (уже обновлен)

### 3. Payments Page
- `payments.html`
- `payment_create.html`
- `monthly_stats.html`

### 4. Statistics Page
- `statistics.html` (частично обновлен)

### 5. Registration Pages
- `register.html` (student registration)

## 🎨 Шаблон для обновления

### Шаг 1: Замените HEAD секцию

```html
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Page Title - Talim CRM</title>
    <link rel="stylesheet" th:href="@{/modern-style.css}">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
</head>
```

### Шаг 2: Замените SIDEBAR

```html
<aside class="sidebar" id="sidebar">
    <div class="sidebar-header">
        <i class="fas fa-graduation-cap"></i>
        <span>Talim CRM</span>
    </div>
    <nav class="sidebar-nav">
        <a class="nav-link" th:href="@{/v1/auth/reception}">
            <i class="fas fa-home"></i>
            <span>Reception</span>
        </a>
        <a class="nav-link" th:href="@{/v1/student/listPage}">
            <i class="fas fa-user-graduate"></i>
            <span>Students</span>
        </a>
        <a class="nav-link" th:href="@{/v1/student/register}">
            <i class="fas fa-user-plus"></i>
            <span>New Student</span>
        </a>
        <a class="nav-link" th:href="@{/v1/group/listPage}">
            <i class="fas fa-users"></i>
            <span>Groups</span>
        </a>
        <a class="nav-link" th:href="@{/v1/group/register}">
            <i class="fas fa-plus-circle"></i>
            <span>New Group</span>
        </a>
        <a class="nav-link" th:href="@{/v1/teacher/listPage}">
            <i class="fas fa-chalkboard-teacher"></i>
            <span>Teachers</span>
        </a>
        <a class="nav-link" th:href="@{/v1/teacher/register}">
            <i class="fas fa-user-tie"></i>
            <span>New Teacher</span>
        </a>
        <a class="nav-link" th:href="@{/v1/pay/listPage}">
            <i class="fas fa-credit-card"></i>
            <span>Payments</span>
        </a>
        <a class="nav-link" th:href="@{/v1/pay/createPage}">
            <i class="fas fa-money-bill-wave"></i>
            <span>New Payment</span>
        </a>
        <a class="nav-link" th:href="@{/v1/stats/page}">
            <i class="fas fa-chart-line"></i>
            <span>Statistics</span>
        </a>
    </nav>
</aside>

<button class="btn btn-icon menu-toggle" id="sidebarToggle">
    <i class="fas fa-bars"></i>
</button>
```

### Шаг 3: Добавьте PAGE HEADER

```html
<div class="page-header">
    <h1 class="page-title">
        <i class="fas fa-icon-name"></i>
        Page Title
    </h1>
    <p class="page-subtitle">Page description</p>
</div>
```

### Шаг 4: Обновите КНОПКИ

```html
<!-- Было -->
<button class="btn">Save</button>

<!-- Стало -->
<button class="btn btn-primary">
    <i class="fas fa-save"></i>
    Save
</button>
```

### Шаг 5: Обновите ТАБЛИЦЫ

```html
<div class="card">
    <div class="card-header">
        <h3 class="card-title">
            <i class="fas fa-list"></i>
            Table Title
        </h3>
    </div>
    <div class="card-body">
        <div class="table-container">
            <table class="table">
                <thead>
                <tr>
                    <th><i class="fas fa-hashtag"></i> ID</th>
                    <th><i class="fas fa-user"></i> Name</th>
                    <!-- ... -->
                </tr>
                </thead>
                <tbody>
                <!-- ... -->
                </tbody>
            </table>
        </div>
    </div>
</div>
```

### Шаг 6: Обновите ФОРМЫ

```html
<div class="form-group">
    <label for="fieldName" class="form-label">
        <i class="fas fa-icon"></i> Field Label
    </label>
    <input type="text" id="fieldName" class="form-control" placeholder="Enter value">
</div>
```

### Шаг 7: Добавьте SIDEBAR TOGGLE SCRIPT

```html
<script>
    // Sidebar toggle
    const sidebar = document.getElementById('sidebar');
    const toggle = document.getElementById('sidebarToggle');
    const key = 'talimcrm.sidebar.collapsed';
    const apply = () => {
        const collapsed = localStorage.getItem(key) === '1';
        if (collapsed) sidebar.classList.add('collapsed');
        else sidebar.classList.remove('collapsed');
    };
    apply();
    toggle.addEventListener('click', () => {
        const collapsed = !(localStorage.getItem(key) === '1');
        localStorage.setItem(key, collapsed ? '1' : '0');
        apply();
    });
</script>
```

## 🎯 Иконки для каждой страницы

| Страница | Иконка |
|----------|--------|
| Reception | `fa-door-open` |
| Students | `fa-user-graduate` |
| New Student | `fa-user-plus` |
| Groups | `fa-users` |
| New Group | `fa-plus-circle` |
| Teachers | `fa-chalkboard-teacher` |
| New Teacher | `fa-user-tie` |
| Payments | `fa-credit-card` |
| New Payment | `fa-money-bill-wave` |
| Statistics | `fa-chart-line` |

## 📊 Stats Cards

```html
<div class="grid grid-cols-4 mb-4">
    <div class="card stats-card">
        <div class="stats-label"><i class="fas fa-icon"></i> Label</div>
        <div class="stats-value">123</div>
    </div>
    <!-- Gradient cards -->
    <div class="card" style="background: linear-gradient(135deg, #10b981 0%, #059669 100%); color: white;">
        <div class="stats-label"><i class="fas fa-check"></i> Active</div>
        <div class="stats-value">45</div>
    </div>
</div>
```

## 🎨 Цветовые градиенты

- **Success (Green):** `linear-gradient(135deg, #10b981 0%, #059669 100%)`
- **Warning (Yellow):** `linear-gradient(135deg, #f59e0b 0%, #d97706 100%)`
- **Danger (Red):** `linear-gradient(135deg, #ef4444 0%, #dc2626 100%)`
- **Info (Blue):** `linear-gradient(135deg, #3b82f6 0%, #2563eb 100%)`
- **Primary (Purple):** `linear-gradient(135deg, #667eea 0%, #764ba2 100%)`

## ✅ Checklist для каждой страницы

- [ ] Заменить CSS на `modern-style.css`
- [ ] Добавить Font Awesome
- [ ] Обновить sidebar с иконками
- [ ] Добавить кнопку toggle
- [ ] Добавить page-header
- [ ] Обновить все кнопки с иконками
- [ ] Обновить таблицы с иконками
- [ ] Обновить формы с иконками
- [ ] Добавить JavaScript для sidebar toggle
- [ ] Проверить responsive дизайн

---

**Следуйте этому шаблону для обновления каждой страницы!**
