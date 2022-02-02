#! /bin/sh

# Auteur : gl18
# Version initiale : 01/01/2022

# Encore un test simpliste. On compile un fichier (cond0.deca), on
# lance ima dessus, et on compare le résultat avec la valeur attendue.

# Ce genre d'approche est bien sûr généralisable, en conservant le
# résultat attendu dans un fichier pour chaque fichier source.
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:./src/main/bin:"$PATH"

# On ne teste qu'un fichier. Avec une boucle for appropriée, on
# pourrait faire bien mieux ...
rm -f ./src/test/deca/codegen/valid/provided/cond0.ass 2>/dev/null
decac ./src/test/deca/codegen/valid/provided/cond0.deca || exit 1
if [ ! -f ./src/test/deca/codegen/valid/provided/cond0.ass ]; then
    echo "Fichier cond0.ass non généré."
fi

decac ./src/test/deca/codegen/interactive/readInt.deca || exit 1
if [ ! -f ./src/test/deca/codegen/interactive/readInt.ass ]; then
    echo "Fichier cond0.ass non généré."
    else
        decac -p ./src/test/deca/codegen/interactive/readFloat.deca 2>&1
fi

decac ./src/test/deca/codegen/interactive/readFloat.deca || exit 1
if [ ! -f ./src/test/deca/codegen/interactive/readFloat.ass ]; then
    echo "Fichier cond0.ass non généré."
    else 
        decac -p ./src/test/deca/codegen/interactive/readFloat.deca 2>&1
fi

rm -f ./src/test/deca/codegen/interactive/*.ass