#! /bin/sh

# Auteur : gl18
# Version initiale : 01/01/2022

# Test minimaliste de la syntaxe.
# On lance test_synt sur un fichier valide, et les tests invalides.

# dans le cas du fichier valide, on teste seulement qu'il n'y a pas eu
# d'erreur. Il faudrait tester que l'arbre donné est bien le bon. Par
# exemple, en stoquant la valeur attendue quelque part, et en
# utilisant la commande unix "diff".
#
# Il faudrait aussi lancer ces tests sur tous les fichiers deca
# automatiquement. Un exemple d'automatisation est donné avec une
# boucle for sur les tests invalides, il faut aller encore plus loin.

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"


test_compile () {
    # $1 = premier argument.
    if decac "$1" 2>&1 | grep -q -e "deca"
    then
        echo "  Echec compilation"
        exit 1
    else
        echo "  Compilation réussie\n"
    fi
    echo "  Comparaison avec decac -p"
    decac -p "$1" > test.txt
    if decac text.txt 2>&1 | grep -q -e "deca"
    then
        echo "  Echec compilation"
        exit 1
    else
        echo "  Compilation réussie"
    fi
    echo " "
}    


test_compile_r () {
    # $1 = premier argument.
    if decac -r 4 "$1" 2>&1 | grep -q -e "deca"
    then
        exit 1
        echo "  Echec compilation avec -r 4"
    else
        echo "  Compilation réussie avec -r 4"
    fi
    echo " "
}    

echo " ------ Compilation ------\n"

echo " ------ src/test/deca/codegen/valid/ ------\n"

for cas_de_test in src/test/deca/codegen/valid/*/*.deca
do
    echo " -------- $cas_de_test --------"
    test_compile "$cas_de_test"
done

for cas1 in src/test/deca/codegen/valid/*/*.ass
do
    echo " -------- $cas1 --------"
    ima "$cas1" > "$cas1".res
    echo "  Resultat :"
    cat "$cas1".res
done
echo " "

for cas_de_test1 in src/test/deca/codegen/valid/*/*.deca
do
    echo " -------- $cas_de_test1 --------"
    test_compile_r "$cas_de_test1"
done

for cas2 in src/test/deca/codegen/valid/*/*.ass
do
    echo " -------- $cas2 --------"
    ima "$cas2" > "$cas2".r.res
    if diff -q "$cas2".res "$cas2".r.res | grep -e "différents"
        then 
            diff --suppress-common-lines "$cas2".res "$cas2".r.res
            exit 1
        else
            echo "RAS"
        fi
done
echo " "
rm -f src/test/deca/context/*/*/*.ass
rm -f src/test/deca/context/*/*/*.res