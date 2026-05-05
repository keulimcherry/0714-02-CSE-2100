@echo off
title Building PAC-MAN Installer...
echo.
echo ========================================
echo   Building PAC-MAN Windows Installer
echo ========================================
echo.

echo [1/3] Building game JAR...
call .\gradlew lwjgl3:jar
if %errorlevel% neq 0 (
    echo ERROR: JAR build failed.
    pause
    exit /b 1
)
echo Done.
echo.

echo [2/3] Creating Windows executable...

"C:\Program Files\Java\jdk-24\bin\jpackage.exe" ^
  --input "lwjgl3\build\libs" ^
  --name "PacmanGame" ^
  --main-jar "PacmanGame-1.0.0.jar" ^
  --type app-image ^
  --dest "installer-output" ^
  --app-version "1.0.0" ^
  --vendor "CSE Group"

if %errorlevel% neq 0 (
    echo ERROR: jpackage failed.
    pause
    exit /b 1
)

echo.
echo [3/3] Zipping for distribution...
powershell -command "Compress-Archive -Path 'installer-output\PacmanGame' -DestinationPath 'PacmanGame-Windows.zip' -Force"

echo.
echo ========================================
echo   DONE!
echo ========================================
echo.
echo Your file is ready: PacmanGame-Windows.zip
echo.
echo Send this ZIP to anyone.
echo They extract it and double-click PacmanGame.exe
echo No Java needed. No installation. Just works.
echo.
pause
