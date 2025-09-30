@echo off
echo ========================================
echo   TalimCRM - Starting Application
echo ========================================
echo.

echo Checking Docker...
docker --version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Docker is not installed or not running!
    echo Please install Docker Desktop and try again.
    pause
    exit /b 1
)

echo Docker is running!
echo.

echo Starting services...
docker-compose up -d --build

if errorlevel 1 (
    echo.
    echo ERROR: Failed to start services!
    pause
    exit /b 1
)

echo.
echo ========================================
echo   Services started successfully!
echo ========================================
echo.
echo Application: http://localhost:8080
echo Database:    localhost:5432
echo.
echo To view logs: docker-compose logs -f
echo To stop:      docker-compose down
echo.
pause
