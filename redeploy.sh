#!/usr/bin/env bash

export LAUNCHER="io.vertx.core.Launcher"
export CMD="mvn compile"
export VERTX_CMD="run"

mvn compile dependency:copy-dependencies
java \
  -cp  $(echo target/dependency/*.jar | tr ' ' ':'):"target/classes" \
  $LAUNCHER $VERTX_CMD \
  --redeploy="src/main/**/*" --on-redeploy="$CMD" \
  --launcher-class="io.vertx.main.MainHttpClient" --java-opts="-Dport=8081 -Dother=8082,8083"