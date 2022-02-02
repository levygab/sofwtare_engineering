#! /bin/sh

# Auteur : gl18
# Version initiale : 01/01/2022

for cas_de_test in src/test/deca/syntax/valid/SO/lex/*.deca
do
    test_lex "$cas_de_test" > resultat/"$cas_de_test".lis
done

for extension in src/test/deca/syntax/extension/valid/lex/*.deca
do
    test_lex "$extension" > resultat/"$extension".lis
done

for cas_de_test1 in src/test/deca/syntax/valid/HW/lex/*.deca
do
    test_lex "$cas_de_test1" > resultat/"$cas_de_test1".lis
done

for cas_de_test2 in src/test/deca/syntax/valid/SO/synt/*.deca
do
    test_synt "$cas_de_test2" > resultat/"$cas_de_test2".lis
done

for cas_de_test4 in src/test/deca/syntax/valid/HW/synt/*.deca
do
    test_synt "$cas_de_test4" > resultat/"$cas_de_test4".lis
done

for le in src/test/deca/syntax/valid/LE/*.deca
do
    test_synt "$le" > resultat/"$le".lis
done

for extension2 in src/test/deca/syntax/extension/valid/synt/*.deca
do
    test_synt "$extension2" > resultat/"$extension2".lis
done

for cas_de_test3 in src/test/deca/context/valid/*/*.deca
do
    test_context "$cas_de_test3" > resultat/"$cas_de_test3".lis
done

for extension3 in src/test/deca/context/extension/valid/*.deca
do
    test_context "$extension3" > resultat/"$extension3".lis
done

for cas_de_test5 in src/test/deca/codegen/valid/*/*.deca
do
    decac "$cas_de_test5"
done

for cas_de_test6 in src/test/deca/codegen/perf/provided/*.deca
do
    decac "$cas_de_test6"
done

for extension6 in src/test/deca/codegen/extension/valid/*.deca
do
    decac "$extension6"
done

for cas_de_test7 in src/test/deca/codegen/valid/*/*.ass
do
    cat "$cas_de_test7" > resultat/"$cas_de_test7"
    ima resultat/"$cas_de_test7" > resultat/"$cas_de_test7".res
done

for cas_de_test8 in src/test/deca/codegen/perf/provided/*.ass
do
    cat "$cas_de_test8" > resultat/"$cas_de_test8"
    ima resultat/"$cas_de_test8" > resultat/"$cas_de_test8".res
done

for extension4 in src/test/deca/codegen/extension/valid/*.ass
do
    cat "$extension4" > resultat/"$extension4"
    ima resultat/"$extension4" > resultat/"$extension4".res
done

rm -f src/test/deca/codegen/valid/*/*.ass
rm -f src/test/deca/codegen/perf/provided/*.ass
rm -f src/test/deca/codegen/extension/valid/*.ass

echo Generation faite