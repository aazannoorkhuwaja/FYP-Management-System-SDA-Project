#!/usr/bin/env sh
set -eu

cd "$(dirname "$0")/.."

javac -d bin src/Main.java src/model/*.java src/view/*.java src/controller/*.java src/api/*.java src/util/*.java src/test/*.java
java -cp bin test.DashboardControllerTest
java -cp bin test.IdeaBankControllerTest
