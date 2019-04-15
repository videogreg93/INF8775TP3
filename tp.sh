#!/bin/bash

OPTIONS=""
SOLUTION=false
TIME=false
while [[ $# -gt 0 ]]
do
key="$1"

case $key in
    -e)
    EX_PATH="$2"
    shift
    ;;
    *)
        echo "Argument inconnu: ${1}"
        exit
    ;;
esac
shift
done

java -jar out/artifacts/projet_jar/projet.jar $EX_PATH 
python ./sol_check.py $EX_PATH "solution"