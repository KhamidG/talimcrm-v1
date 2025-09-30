@echo off
setlocal enabledelayedexpansion

echo ========================================
echo   TalimCRM - Auto Backup to Cloud
echo ========================================
echo.

:: Создать бэкап
call backup.bat

:: Путь к облачному хранилищу (ИЗМЕНИТЕ НА СВОЙ!)
set CLOUD_PATH=C:\Users\%USERNAME%\Google Drive\TalimCRM-Backups

:: Создать папку в облаке если её нет
if not exist "%CLOUD_PATH%" mkdir "%CLOUD_PATH%"

:: Скопировать последний бэкап в облако
echo.
echo Copying to cloud storage...
copy backups\*.sql "%CLOUD_PATH%\" /Y

if errorlevel 1 (
    echo.
    echo ERROR: Failed to copy to cloud!
    echo Please check the cloud path in auto-backup.bat
    pause
    exit /b 1
)

:: Удалить старые бэкапы (оставить последние 7)
echo.
echo Cleaning old backups...
for /f "skip=7 delims=" %%F in ('dir /b /o-d backups\*.sql') do (
    del "backups\%%F"
    echo Deleted old backup: %%F
)

echo.
echo ========================================
echo   Backup completed successfully!
echo ========================================
echo.
echo Local:  backups\
echo Cloud:  %CLOUD_PATH%\
echo.
pause
