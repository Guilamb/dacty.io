#!/bin/bash
cp ressources/* classes/
cd classes
export CLASSPATH=`find ../lib -name "*.jar"`
java -cp ${CLASSPATH}:. Dacty_io
cd ..
