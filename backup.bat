@echo off
setlocal enabledelayedexpansion

echo ========================================
echo   TalimCRM - Database Backup
echo ========================================
echo.

:: Создать папку для бэкапов если её нет
if not exist "backups" mkdir backups

:: Получить текущую дату и время
for /f "tokens=2 delims==" %%I in ('wmic os get localdatetime /value') do set datetime=%%I
set BACKUP_DATE=%datetime:~0,8%-%datetime:~8,6%

:: Имя файла бэкапа
set BACKUP_FILE=backups\talimcrm_backup_%BACKUP_DATE%.sql

echo Creating backup: %BACKUP_FILE%
echo.

:: Создать бэкап
docker-compose exec -T postgres pg_dump -U talimcrm_user talimcrm_db > %BACKUP_FILE%

if errorlevel 1 (
    echo.
    echo ERROR: Backup failed!
    pause
    exit /b 1
)

echo.
echo ========================================
echo   Backup created successfully!
echo ========================================
echo.
echo File: %BACKUP_FILE%
echo.
echo You can copy this file to another PC and restore it there.
echo.
pause
