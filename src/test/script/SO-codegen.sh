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


test_compile_invalide () {
    # $1 = premier argument.
    if decac "$1" 2>&1 | grep -e "$1:[0-9][0-9]*:"
    echo -n "  Resultat : "
    then
        echo "Echec attendu"
    else
        echo "Succes inattendu"
        exit 1
    fi
    echo " "
}

test_compile_classique () {
    # $1 = premier argument.
    if decac "$1" 2>&1 | grep -e "deca"
    then
        echo "  Echec compilation"
        exit 1
    else
        echo "  Compilation réussie\n"
    fi
}

test_compile () {
    # $1 = premier argument.
    if decac "$1" 2>&1 | grep -e "deca"
    then
        echo "  Echec compilation"
        exit 1
    else
        echo "  Compilation réussie\n"
    fi
    echo "  Compilation avec decac -p"
    decac -p "$1" > test.deca
    if decac test.deca 2>&1 | grep -e "deca"
    then
        echo "  Echec compilation"
        exit 1
    else
        echo "  Compilation réussie"
    fi
    echo " "

    echo "  Recompilation avec decac -p"
    decac -p test.deca > test.deca
    if decac test.deca 2>&1 | grep -e "deca"
    then
        echo "  Echec compilation"
        exit 1
    else
        echo "  Compilation réussie"
    fi
    rm -f test.deca
    rm -f test.ass
    echo " "
}    


test_compile_r () {
    # $1 = premier argument.
    if decac -r 4 "$1" 2>&1 | grep -e "deca"
    then
        echo "  Echec compilation avec -r 4"
        exit 1
    else
        echo "  Compilation réussie avec -r 4\n"
    fi
    echo "  Compilation avec decac -p"
    decac -p "$1" > test.deca
    if decac test.deca 2>&1 | grep -e "deca"
    then
        echo "  Echec compilation"
        exit 1
    else
        echo "  Compilation réussie"
    fi
    echo " "
    echo "  Recompilation avec decac -p"
    decac -p test.deca > test.deca
    if decac test.deca 2>&1 | grep -e "deca"
    then
        echo "  Echec compilation"
        exit 1
    else
        echo "  Compilation réussie"
    fi
    rm -f test.deca
    rm -f test.ass
    echo " "
}    

echo " ------ Compilation ------\n"

echo "                  -- Tests invalides --\n"
for cas_de_test in src/test/deca/codegen/invalid/*.deca
do
    echo " -------- $cas_de_test --------"
    test_cont_invalide "$cas_de_test"
done

echo " ------ src/test/deca/codegen/valid/ ------\n"

for cas_de_test in src/test/deca/codegen/valid/*/*.deca
do
    echo " -------- $cas_de_test --------"
    test_compile "$cas_de_test"
done

for cas1 in src/test/deca/codegen/valid/*/*.ass
do
    echo " -------- $cas1 --------\n"
    echo -n " Différence de fichier assembleur : "
    if diff -q "$cas1" resultat/"$cas1" | grep -e "différents"
        then 
            diff --suppress-common-lines "$cas1" resultat/"$cas1"
        else
            echo "RAS"
        fi
    ima "$cas1" > "$cas1".res
    echo "  Resultat :"
    cat "$cas1".res
    echo -n " Difference au résultat attendu : "
    if diff -q "$cas1".res resultat/"$cas1".res | grep -e "différents"
        then 
            diff --suppress-common-lines "$cas1".res resultat/"$cas1".res
            exit 1
        else
            echo "RAS"
        fi
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
    echo -n " Différence de fichier assembleur : "
    ima "$cas2" > "$cas2".r.res
    if diff -q "$cas2".res "$cas2".r.res | grep -e "différents"
        then 
            diff --suppress-common-lines "$cas2".res "$cas2".r.res
        else
            echo "RAS\n"
        fi
done
echo " \n"


rm -f src/test/deca/codegen/valid/*/*.ass
rm -f src/test/deca/codegen/valid/*/*.res

echo " ------ src/test/deca/codegen/perf/provided ------\n"

for cas_de_test3 in src/test/deca/codegen/perf/provided/*.deca
do
    echo " -------- $cas_de_test3 --------"
    test_compile "$cas_de_test3"
done

for cas3 in src/test/deca/codegen/perf/provided/*.ass
do
    echo " -------- $cas3 --------\n"
    echo -n " Différence de fichier assembleur : "
    if diff -q "$cas3" resultat/"$cas3" | grep -e "différents"
        then 
            diff --suppress-common-lines "$cas3" resultat/"$cas3"
            exit 1
        else
            echo "RAS"
        fi
    ima "$cas3" > "$cas3".res
    echo "  Resultat :"
    cat "$cas3".res
    echo -n " Difference au résultat attendu : "
    if diff -q "$cas3".res resultat/"$cas3".res | grep -e "différents"
        then 
            diff --suppress-common-lines "$cas3".res resultat/"$cas3".res
            exit 1
        else
            echo "RAS\n"
        fi
done
echo " "

for cas_de_test4 in src/test/deca/codegen/perf/provided/*.deca
do
    echo " -------- $cas_de_test4 --------"
    test_compile_r "$cas_de_test4"
done

for cas4 in src/test/deca/codegen/perf/provided/*.ass
do
    echo " -------- $cas4 --------"
    echo -n " Différence de fichier assembleur : "
    ima "$cas4" > "$cas4".r.res
    if diff -q "$cas4".res "$cas4".r.res | grep -e "différents"
        then 
            diff --suppress-common-lines "$cas4".res "$cas4".r.res
            exit 1
        else
            echo "RAS\n"
        fi
done
echo " \n"


rm -f src/test/deca/codegen/perf/provided/*.ass
rm -f src/test/deca/codegen/perf/provided/*.res

echo " ------ src/test/deca/codegen/interactive ------\n"

for cas_de_test5 in src/test/deca/codegen/interactive/*.deca
do
    echo " -------- $cas_de_test5 --------"
    test_compile "$cas_de_test5"
done

for cas_de_test6 in src/test/deca/codegen/interactive/*.deca
do
    echo " -------- $cas_de_test6 --------"
    test_compile_r "$cas_de_test6"
done

echo " ------ src/test/deca/codegen/extension/valid ------\n"

for extension in src/test/deca/codegen/extension/valid/*.deca
do
    echo " -------- $extension --------"
    test_compile_classique "$extension"
done

for ext in src/test/deca/codegen/extension/valid/*.ass
do
    echo " -------- $ext --------\n"
    echo -n " Différence de fichier assembleur : "
    if diff -q "$ext" resultat/"$ext" | grep -e "différents"
        then 
            diff --suppress-common-lines "$ext" resultat/"$ext"
            exit 1
        else
            echo "RAS"
        fi
    ima "$ext" > "$ext".res
    echo "  Resultat :"
    cat "$ext".res
    echo -n " Difference au résultat attendu : "
    if diff -q "$ext".res resultat/"$ext".res | grep -e "différents"
        then 
            diff --suppress-common-lines "$ext".res resultat/"$ext".res
            exit 1
        else
            echo "RAS\n"
        fi
done
echo " "


rm -f src/test/deca/codegen/interactive/*.ass
rm -f src/test/deca/codegen/interactive/*.res

rm -f src/test/deca/codegen/extension/valid/*.ass
rm -f src/test/deca/codegen/extension/valid/*.res