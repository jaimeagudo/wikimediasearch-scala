#!/bin/sh
sbt assembly; scp target/scala-2.11/*.jar dev.vegatic.com:; ssh dev.vegatic.com "killall java; rm *.pid; ./start.sh ./wikimediasearch-*.jar"