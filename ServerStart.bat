@ECHO OFF
COLOR 0E

REM 分享包未附 jdelay class，啟動時不執行延遲工具。
REM java jdelay 1
cls
goto begin

:noupdate
echo 無新的核心可更新
goto endud

:ifupdate
IF EXIST update\Server_Game.jar goto movezip
IF NOT EXIST update\Server_Game.jar goto noupdate

:movezip
del Server_Game.jar
move /Y update\Server_Game.jar Server_Game.jar

echo 更新核心成功
goto endud

:begin
title 880

REM ===================================參數設定=====================================
SET L1J_RAM=-server -Xms8192m -Xmx8192m -Xmn3072m -XX:+UseFastAccessorMethods -XX:MaxGCPauseMillis=50

REM ==================================Time相關配置==================================
SET L1J_TIME=-Duser.timezone=Asia/Taipei

REM ===================================固定參數=====================================
REM 程式路徑(勿動)
SET L1J_PATH=-cp Server_Game.jar;jar\c3p0-0.9.1.2.jar;jar\commons-logging-1.1.1.jar;jar\javolution-5.5.1.jar;jar\log4j-1.2.16.jar;jar\mysql-connector-java-5.1.20-bin.jar;jar\netty-3.7.0.Final.jar;jar\protobuf-java-2.5.0.jar com.lineage.Server

:sta
REM ===================================核心更新=====================================
goto ifupdate

:endud
REM ===================================核心啟動=====================================
echo 核心啟動
call :checkport2000
IF ERRORLEVEL 1 exit /b 1
java %L1J_RAM% %L1J_TIME% %L1J_PATH%

cls
rem ===================================重新啟動=====================================
goto sta

:checkport2000
for /f "tokens=5" %%P in ('netstat -ano ^| findstr /R /C:":2000 .*LISTENING"') do (
  echo Port 2000 is already LISTENING by PID %%P.
  echo Close the old 880 server before starting this one.
  exit /b 1
)
exit /b 0