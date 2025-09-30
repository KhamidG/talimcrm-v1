# 🚀 TalimCRM - Быстрый старт с Docker

## ✅ Что нужно установить

1. **Docker Desktop** - скачать с https://www.docker.com/products/docker-desktop/

## 📦 Запуск приложения

### Вариант 1: Через скрипт (самый простой)
```bash
# Просто запустите файл
start.bat
```

### Вариант 2: Через командную строку
```bash
# Запустить все сервисы
docker-compose up -d --build

# Посмотреть логи
docker-compose logs -f
```

## 🌐 Доступ

После запуска откройте браузер:
- **Приложение:** http://localhost:8080

## 🛑 Остановка

```bash
# Через скрипт
stop.bat

# Или через команду
docker-compose down
```

## 📊 Просмотр логов

```bash
# Через скрипт
logs.bat

# Или через команду
docker-compose logs -f
```

## 🗄️ База данных

Автоматически создается PostgreSQL база:
- **Host:** localhost
- **Port:** 5432
- **Database:** talimcrm_db
- **Username:** talimcrm_user
- **Password:** talimcrm_password_2024

## 🔧 Исправление ошибки с группами

Если при добавлении студента в группу возникает ошибка:

```bash
# Выполните SQL-скрипт
docker-compose exec -T postgres psql -U talimcrm_user -d talimcrm_db < fix_group_constraint.sql
```

## ❓ Проблемы?

1. **Docker не запускается** - убедитесь, что Docker Desktop запущен
2. **Порт 8080 занят** - измените порт в `docker-compose.yml`
3. **Ошибки при сборке** - выполните `docker-compose down -v` и попробуйте снова

## 📚 Подробная документация

Смотрите [README-DOCKER.md](README-DOCKER.md) для детальной информации.

---

**Готово!** Приложение работает в Docker с отдельной базой данных! 🎉
