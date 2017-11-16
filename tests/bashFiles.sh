#!/bin/bash

printf "Modo: $mode\n\n"

printf "Semántica: (presione enter)\n\n"
read
FILES=../offline/tests/semantica/*.in
todos=""
for f in $FILES
do
    todos="$todos $f"
done
mvn exec:java@$mode -Dexec.args="$todos"

printf "Sintáctica: (presione enter)\n\n"
read
FILES=../offline/tests/sintactica/*.in
todos=""
for f in $FILES
do
    todos="$todos $f"
done
mvn exec:java@$mode -Dexec.args="$todos"

printf "Léxica: (presione enter)\n\n"
read
FILES=../offline/tests/lexica/*.in
todos=""
for f in $FILES
do
    todos="$todos $f"
done
mvn exec:java@$mode -Dexec.args="$todos"

printf "Ninguno: (presione enter)\n\n"
read
FILES=../offline/tests/ninguno/*.in
todos=""
for f in $FILES
do
    todos="$todos $f"
done
mvn exec:java@$mode -Dexec.args="$todos"

