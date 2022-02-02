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

# exemple de définition d'une fonction
test_cont_invalide () {
    # $1 = premier argument.
    if test_context "$1" 2>&1 | grep -e "$1:[0-9][0-9]*:"
    echo -n "  Resultat : "
    then
        echo "Echec attendu"
    else
        echo "Succes inattendu"
        exit 1
    fi
    echo " "
} 

test_cont_valide () {
    # $1 = premier argument.
    test_context "$1" > "$1".lis 
    if grep -q -e "$1:[0-9][0-9]*:" "$1".lis
    then
        echo "Erreur inattendue"
        exit 1
    else
        echo "  Execution test_context : OK"
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
    echo " "
}    

echo "                  -- Tests invalides --\n"
for cas_de_test in src/test/deca/context/invalid/SO/*.deca
do
    echo " -------- $cas_de_test --------"
    test_cont_invalide "$cas_de_test"
done

echo " "
for cas_de_test3 in src/test/deca/context/invalid/LE/*.deca
do
    echo " -------- $cas_de_test3 --------"
    test_cont_invalide "$cas_de_test3"
done

echo "                   -- Tests valides --\n"

for cas_de_test2 in src/test/deca/context/valid/*/*.deca
do
    echo " -------- $cas_de_test2 --------"
    test_cont_valide "$cas_de_test2"
done

for extension in src/test/deca/context/extension/valid/*.deca
do
    echo " -------- $cas_de_test2 --------"
    test_cont_valide "$extension"
done
