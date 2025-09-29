@echo off
setlocal
:: Force JDK 17 for this build
set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.16.8-hotspot"
set "PATH=%JAVA_HOME%\bin;%PATH%"

:: (optional) show which compiler we're using
echo Using javac at: 
where javac
javac -version

rem === CONFIG ===
set "APP_NAME=BW2-TCME"
set "MAIN_CLASS=ui.EditorUI"
set "SRC=src\main\java"
set "RES=src\main\resources"
set "BIN=build\classes"
set "DIST=dist"

echo [build] Checking tools...
where javac >nul 2>nul
if errorlevel 1 (
  echo [build] ERROR: JDK ^(javac^) not found in PATH.
  exit /b 1
)
where jar >nul 2>nul
if errorlevel 1 (
  echo [build] ERROR: 'jar' tool not found. Install a full JDK and add it to PATH.
  exit /b 1
)

echo [build] Cleaning...
if exist "%BIN%"  rmdir /s /q "%BIN%"
if exist "%DIST%" rmdir /s /q "%DIST%"
mkdir "%BIN%"
mkdir "%DIST%"

echo [build] Collecting sources...
dir /s /b "%SRC%\*.java" > "%BIN%\sources.txt" 2>nul
if not exist "%BIN%\sources.txt" (
  echo [build] ERROR: No .java files under "%SRC%".
  exit /b 1
)

echo [build] Compiling...
javac --release 17 -encoding UTF-8 -d "%BIN%" @"%BIN%\sources.txt" || (
  echo [build] ERROR: Compile failed.
  exit /b 1
)

echo [build] Copying resources (if any)...
if exist "%RES%" xcopy /e /i /y "%RES%\*" "%BIN%\" >nul

echo [build] Creating JAR...
jar cfe "%DIST%\%APP_NAME%.jar" %MAIN_CLASS% -C "%BIN%" . || (
  echo [build] ERROR: jar failed.
  exit /b 1
)

echo [build] SUCCESS: "%DIST%\%APP_NAME%.jar"
exit /b 0