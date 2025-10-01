# 🎨 TalimCRM - Modern Design Update

## ✨ Что обновлено

### 1. **Новый CSS Framework**
Создан файл `modern-style.css` с современной системой дизайна:
- ✅ CSS Variables для цветов и размеров
- ✅ Градиенты и тени
- ✅ Анимации и переходы
- ✅ Responsive дизайн
- ✅ Dark sidebar с градиентом

### 2. **Иконки Font Awesome**
Добавлены иконки для всех элементов:
- 🏠 Home - Reception
- 👨‍🎓 User Graduate - Students
- ➕ User Plus - New Student
- 👥 Users - Groups
- ➕ Plus Circle - New Group
- 👨‍🏫 Chalkboard Teacher - Teachers
- 👔 User Tie - New Teacher
- 💳 Credit Card - Payments
- 💵 Money Bill - New Payment
- 📊 Chart Line - Statistics

### 3. **Обновленные компоненты**

#### **Sidebar**
- Темный градиентный фон
- Иконки для каждого пункта меню
- Hover эффекты
- Активное состояние с градиентом
- Сворачивание (collapse) - показывает только иконки
- Мобильная версия

#### **Кнопки**
- `btn-primary` - Основная кнопка с градиентом
- `btn-secondary` - Вторичная кнопка
- `btn-success` - Зеленая кнопка
- `btn-warning` - Желтая кнопка
- `btn-danger` - Красная кнопка
- `btn-outline` - Прозрачная с обводкой
- `btn-sm` / `btn-lg` - Размеры
- `btn-icon` - Круглая кнопка для иконок

#### **Карточки (Cards)**
- Белый фон с тенью
- Hover эффект (поднимается)
- `card-header` с иконками
- `stats-card` - Карточка с градиентом для статистики

#### **Формы**
- Современные input поля
- Иконки в labels
- Focus состояния с тенью
- Placeholder стили
- Select с кастомной стрелкой

#### **Таблицы**
- Иконки в заголовках
- Hover эффект на строках
- Чередующиеся цвета
- Responsive overflow

#### **Badges (Значки)**
- `badge-success` - Зеленый
- `badge-warning` - Желтый
- `badge-danger` - Красный
- `badge-info` - Синий
- `badge-secondary` - Серый

### 4. **Page Header**
Новый компонент для заголовков страниц:
```html
<div class="page-header">
    <h1 class="page-title">
        <i class="fas fa-icon"></i>
        Page Title
    </h1>
    <p class="page-subtitle">Description</p>
</div>
```

### 5. **Grid System**
Адаптивная сетка:
- `grid` - Основной контейнер
- `grid-cols-1` / `grid-cols-2` / `grid-cols-3` / `grid-cols-4`
- Автоматическая адаптация на мобильных

### 6. **Utility Classes**
- `text-center` / `text-left` / `text-right`
- `mt-1` / `mt-2` / `mt-3` / `mt-4` - Margin top
- `mb-1` / `mb-2` / `mb-3` / `mb-4` - Margin bottom
- `p-1` / `p-2` / `p-3` / `p-4` - Padding
- `flex` / `flex-col` / `items-center` / `justify-between`
- `gap-1` / `gap-2` / `gap-3`

## 📁 Структура файлов

```
src/main/resources/
├── static/
│   └── modern-style.css          # Новый CSS framework
└── templates/
    └── reception.html             # Обновленная страница (пример)
```

## 🎨 Цветовая палитра

### Primary Colors
- **Primary:** `#667eea` (Фиолетовый)
- **Secondary:** `#764ba2` (Темно-фиолетовый)
- **Accent:** `#f093fb` (Розовый)

### Status Colors
- **Success:** `#10b981` (Зеленый)
- **Warning:** `#f59e0b` (Желтый)
- **Danger:** `#ef4444` (Красный)
- **Info:** `#3b82f6` (Синий)

### Neutral Colors
- **Dark:** `#1e293b`
- **Gray:** `#64748b`
- **Light:** `#f8fafc`
- **White:** `#ffffff`

## 🚀 Как использовать

### 1. Подключить CSS и иконки

```html
<link rel="stylesheet" th:href="@{/modern-style.css}">
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
```

### 2. Структура страницы

```html
<div class="app-container">
    <aside class="sidebar" id="sidebar">
        <div class="sidebar-header">
            <i class="fas fa-graduation-cap"></i>
            <span>Talim CRM</span>
        </div>
        <nav class="sidebar-nav">
            <a class="nav-link active" href="#">
                <i class="fas fa-home"></i>
                <span>Home</span>
            </a>
        </nav>
    </aside>
    
    <button class="btn btn-icon menu-toggle" id="sidebarToggle">
        <i class="fas fa-bars"></i>
    </button>
    
    <main class="main-content">
        <div class="container">
            <!-- Контент -->
        </div>
    </main>
</div>
```

### 3. Добавить JavaScript для sidebar

```javascript
document.addEventListener('DOMContentLoaded', function() {
    const sidebar = document.getElementById('sidebar');
    const toggle = document.getElementById('sidebarToggle');
    const key = 'talimcrm.sidebar.collapsed';
    
    const apply = () => {
        const collapsed = localStorage.getItem(key) === '1';
        if (collapsed) {
            sidebar.classList.add('collapsed');
        } else {
            sidebar.classList.remove('collapsed');
        }
    };
    
    apply();
    
    toggle.addEventListener('click', () => {
        const collapsed = !(localStorage.getItem(key) === '1');
        localStorage.setItem(key, collapsed ? '1' : '0');
        apply();
    });
});
```

## 📱 Responsive Design

### Desktop (> 768px)
- Sidebar: 280px
- Collapsed sidebar: 80px
- Full grid layouts

### Mobile (≤ 768px)
- Sidebar: Hidden by default
- Toggle button visible
- Single column grids
- Touch-friendly buttons

## 🎯 Следующие шаги

Обновите остальные страницы:

1. **Students page** (`students.html`)
2. **Groups page** (`groups.html`)
3. **Teachers page** (`teachers.html`)
4. **Payments page** (`payments.html`)
5. **Statistics page** (`statistics.html`)
6. **Teacher Dashboard** (`teacher_dashboard.html`)
7. **Teacher Login** (`teacher_login.html`)

### Шаблон для обновления:

1. Замените `<link rel="stylesheet" th:href="@{/style.css}">` на:
   ```html
   <link rel="stylesheet" th:href="@{/modern-style.css}">
   <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
   ```

2. Обновите sidebar с иконками

3. Добавьте `page-header` компонент

4. Обновите кнопки с классами `btn btn-primary` и иконками

5. Обновите таблицы с классом `table` и иконками в заголовках

6. Добавьте JavaScript для sidebar toggle

## 💡 Примеры использования

### Кнопка с иконкой
```html
<button class="btn btn-primary">
    <i class="fas fa-save"></i>
    Save
</button>
```

### Карточка со статистикой
```html
<div class="card stats-card">
    <div class="stats-value">150</div>
    <div class="stats-label">Total Students</div>
</div>
```

### Badge
```html
<span class="badge badge-success">
    <i class="fas fa-check"></i>
    Active
</span>
```

### Grid
```html
<div class="grid grid-cols-3">
    <div class="card">Card 1</div>
    <div class="card">Card 2</div>
    <div class="card">Card 3</div>
</div>
```

## 🎨 Кастомизация

Измените CSS variables в `modern-style.css`:

```css
:root {
    --primary: #667eea;        /* Ваш цвет */
    --secondary: #764ba2;      /* Ваш цвет */
    --success: #10b981;        /* Ваш цвет */
    /* ... */
}
```

---

**Версия:** 1.0  
**Дата:** 2025-10-01  
**Автор:** TalimCRM Team
