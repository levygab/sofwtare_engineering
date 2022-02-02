#! /bin/sh

# Auteur : gl18
# Version initiale : 07/01/2022

# On se place dans le répertoire du projet (quel que soit le
# répertoire d'où est lancé le script) :
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

test_lex_invalide () {
    # $1 = premier argument.
    if test_lex "$1" 2>&1 | grep -q -e "$1:[0-9][0-9]*:"
    echo -n "  Resultat : "
    then
        echo "Echec attendu"
    else
        echo "Succes inattendu"
        exit 1
    fi
    echo " "
} 

test_lex_valide () {
    # $1 = premier argument.
    test_lex "$1" > "$1".lis 
    if grep -q -e "$1:[0-9][0-9]*:" "$1".lis
    then
        echo "Erreur inattendue"
        exit 1
    else
        echo "  Execution test_lex : OK"
        echo -n "  Différence au résultat attendu : " 
        if diff -q "$1".lis resultat/"$1".lis | grep -e "différents"
        then 
            diff --suppress-common-lines "$1".lis resultat/"$1".lis
            rm -f "$1".lis
            exit 1
        else
            echo "RAS"
        fi
    fi
    rm -f "$1".lis
    echo "\n"
}    


echo "                  -- Tests invalides --\n"
for cas_de_test in src/test/deca/syntax/invalid/SO/lex/*.deca
do
    echo " -------- $cas_de_test --------"
    test_lex_invalide "$cas_de_test"
done

echo "                   -- Tests valides --\n"

for cas_de_test2 in src/test/deca/syntax/valid/SO/lex/*.deca
do
    echo " -------- $cas_de_test2 --------"
    test_lex_valide "$cas_de_test2"
done

for cas_de_test3 in src/test/deca/syntax/extension/valid/lex/*.deca
do
    echo " -------- $cas_de_test3 --------"
    test_lex_valide "$cas_de_test3"
done

