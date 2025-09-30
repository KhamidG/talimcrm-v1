@echo off
echo ========================================
echo   TalimCRM - Database Restore
echo ========================================
echo.

:: Проверить наличие папки backups
if not exist "backups" (
    echo ERROR: Backups folder not found!
    echo Please create a backup first or copy backup files to 'backups' folder.
    pause
    exit /b 1
)

:: Показать список бэкапов
echo Available backups:
echo.
dir /b backups\*.sql
echo.

:: Запросить имя файла
set /p BACKUP_FILE="Enter backup filename (or full path): "

:: Проверить существование файла
if not exist "%BACKUP_FILE%" (
    if not exist "backups\%BACKUP_FILE%" (
        echo ERROR: Backup file not found!
        pause
        exit /b 1
    )
    set BACKUP_FILE=backups\%BACKUP_FILE%
)

echo.
echo WARNING: This will replace all current data!
set /p CONFIRM="Are you sure? (yes/no): "

if /i not "%CONFIRM%"=="yes" (
    echo Restore cancelled.
    pause
    exit /b 0
)

echo.
echo Restoring from: %BACKUP_FILE%
echo.

:: Восстановить бэкап
docker-compose exec -T postgres psql -U talimcrm_user -d talimcrm_db < %BACKUP_FILE%

if errorlevel 1 (
    echo.
    echo ERROR: Restore failed!
    pause
    exit /b 1
)

echo.
echo ========================================
echo   Database restored successfully!
echo ========================================
echo.
pause
