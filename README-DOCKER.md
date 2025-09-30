# TalimCRM - Docker Deployment Guide

## 📋 Требования

- Docker Desktop (Windows/Mac) или Docker Engine (Linux)
- Docker Compose v2.0+
- Минимум 2GB свободной RAM
- Порты 8080 и 5432 должны быть свободны

## 🚀 Быстрый старт

### 1. Запуск приложения

```bash
# Запустить все сервисы (база данных + приложение)
docker-compose up -d

# Или с пересборкой образа
docker-compose up -d --build
```

### 2. Проверка статуса

```bash
# Посмотреть запущенные контейнеры
docker-compose ps

# Посмотреть логи
docker-compose logs -f

# Логи только приложения
docker-compose logs -f app

# Логи только базы данных
docker-compose logs -f postgres
```

### 3. Доступ к приложению

- **Приложение:** http://localhost:8080
- **База данных:** localhost:5432
  - Database: `talimcrm_db`
  - Username: `talimcrm_user`
  - Password: `talimcrm_password_2024`

## 🛠️ Управление

### Остановка сервисов

```bash
# Остановить без удаления контейнеров
docker-compose stop

# Остановить и удалить контейнеры (данные сохранятся)
docker-compose down

# Остановить и удалить контейнеры + volumes (УДАЛИТ ВСЕ ДАННЫЕ!)
docker-compose down -v
```

### Перезапуск

```bash
# Перезапустить все сервисы
docker-compose restart

# Перезапустить только приложение
docker-compose restart app

# Перезапустить только базу данных
docker-compose restart postgres
```

### Пересборка

```bash
# Пересобрать образ приложения
docker-compose build app

# Пересобрать и запустить
docker-compose up -d --build
```

## 🗄️ Работа с базой данных

### Подключение к PostgreSQL

```bash
# Войти в контейнер PostgreSQL
docker-compose exec postgres psql -U talimcrm_user -d talimcrm_db

# Или через psql на хосте
psql -h localhost -p 5432 -U talimcrm_user -d talimcrm_db
```

### Резервное копирование

```bash
# Создать backup
docker-compose exec postgres pg_dump -U talimcrm_user talimcrm_db > backup.sql

# Восстановить из backup
docker-compose exec -T postgres psql -U talimcrm_user -d talimcrm_db < backup.sql
```

### Выполнение SQL-скриптов

```bash
# Выполнить SQL-файл
docker-compose exec -T postgres psql -U talimcrm_user -d talimcrm_db < your_script.sql

# Например, исправить constraint
docker-compose exec -T postgres psql -U talimcrm_user -d talimcrm_db < fix_group_constraint.sql
```

## 🔧 Настройка

### Изменение портов

Отредактируйте `docker-compose.yml`:

```yaml
services:
  postgres:
    ports:
      - "5433:5432"  # Изменить внешний порт
  
  app:
    ports:
      - "8081:8080"  # Изменить внешний порт
```

### Изменение паролей

1. Отредактируйте `docker-compose.yml`
2. Удалите старый volume: `docker-compose down -v`
3. Запустите заново: `docker-compose up -d`

### Использование .env файла

```bash
# Скопировать пример
cp .env.example .env

# Отредактировать .env
# Затем запустить
docker-compose --env-file .env up -d
```

## 📊 Мониторинг

### Использование ресурсов

```bash
# Посмотреть использование CPU/RAM
docker stats talimcrm-app talimcrm-postgres
```

### Проверка здоровья

```bash
# Проверить health check базы данных
docker-compose exec postgres pg_isready -U talimcrm_user

# Проверить приложение
curl http://localhost:8080/actuator/health
```

## 🐛 Решение проблем

### Приложение не запускается

```bash
# Проверить логи
docker-compose logs app

# Проверить, что база данных запущена
docker-compose ps postgres

# Перезапустить
docker-compose restart app
```

### База данных не доступна

```bash
# Проверить логи PostgreSQL
docker-compose logs postgres

# Проверить health check
docker-compose exec postgres pg_isready

# Пересоздать контейнер
docker-compose up -d --force-recreate postgres
```

### Порты заняты

```bash
# Найти процесс, использующий порт (Windows)
netstat -ano | findstr :8080

# Остановить процесс или изменить порт в docker-compose.yml
```

### Очистка всего

```bash
# Удалить все контейнеры, образы и volumes
docker-compose down -v --rmi all

# Очистить неиспользуемые ресурсы Docker
docker system prune -a --volumes
```

## 🚢 Production Deployment

### Для production рекомендуется:

1. **Использовать secrets для паролей:**
   ```yaml
   secrets:
     db_password:
       file: ./secrets/db_password.txt
   ```

2. **Настроить reverse proxy (nginx):**
   ```yaml
   services:
     nginx:
       image: nginx:alpine
       ports:
         - "80:80"
         - "443:443"
   ```

3. **Включить SSL/TLS**

4. **Настроить автоматические backups**

5. **Использовать внешний volume для данных**

## 📝 Структура проекта

```
talimcrm-v1/
├── Dockerfile              # Образ приложения
├── docker-compose.yml      # Оркестрация сервисов
├── init.sql               # Инициализация БД
├── .dockerignore          # Исключения для Docker
├── .env.example           # Пример переменных окружения
└── README-DOCKER.md       # Эта документация
```

## 🆘 Поддержка

Если возникли проблемы:
1. Проверьте логи: `docker-compose logs`
2. Проверьте статус: `docker-compose ps`
3. Перезапустите: `docker-compose restart`
4. Пересоберите: `docker-compose up -d --build`

---

**Версия:** 1.0  
**Дата:** 2025-09-30
