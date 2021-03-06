@echo off

SET LAUNCHER="io.vertx.core.Launcher"
SET CONFIG = 8080 8081 8082
SET CMD="mvn compile"
SET VERTX_CMD="run"
SET CMD_LINE_ARGS=%*

call mvn compile dependency:copy-dependencies

java -cp  "target\dependency\*;target\classes" %LAUNCHER% %VERTX_CMD% --redeploy="src\main\**\*" --on-redeploy=%CMD% --launcher-class=io.vertx.main.MainHttpClient --java-opts="-Dport=8083 -Dother=8081,8082"