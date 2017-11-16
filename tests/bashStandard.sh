#!/bin/bash

printf "Modo: $mode\n\n"

printf "Semántica: (presione enter)\n\n"
read
FILES=../offline/tests/semantica/*.in
for f in $FILES
do
    printf "File: $f\n" 
    cat $f 
    printf "\n"
    mvn exec:java@$mode < $f
    printf "\n"
done

printf "Sintáctica: (presione enter)\n\n"
read
FILES=../offline/tests/sintactica/*.in
for f in $FILES
do
    printf "File: $f\n" 
    cat $f 
    printf "\n"
    mvn exec:java@$mode < $f
    printf "\n"
done

printf "Léxica: (presione enter)\n\n"
read
FILES=~/27016catalin/offline/tests/lexica/*.in
for f in $FILES
do
    printf "File: $f\n" 
    cat $f 
    printf "\n"
    mvn exec:java@$mode < $f
    printf "\n"
done

printf "Ninguno: (presione enter)\n\n"
read
FILES=~/27016catalin/offline/tests/ninguno/*.in
for f in $FILES
do
    printf "File: $f\n" 
    cat $f 
    printf "\n"
    mvn exec:java@$mode < $f
    printf "\n"
done
