lexer grammar DecaLexer;
options {
	language = Java;
	// Tell ANTLR to make the generated lexer class extend the the named class, which is where any
	// supporting code and variables will be placed.
	superClass = AbstractDecaLexer;
}

@members {
}




// Deca lexer rules. DUMMY_TOKEN: .; // A FAIRE : Règle bidon qui reconnait tous les caractères. A
// FAIRE : Il faut la supprimer et la remplacer par les vraies règles.

PLUS : '+' ;
MINUS : '-' ; 
TIMES : '*' ; 
EXCLAM : '!'; 
NEQ : '!='; 
GEQ : '>='; 
GT : '>'; 
LT : '<'; 
LEQ : '<='; 
EQEQ : '=='; 
SLASH : '/'; 
PERCENT : '%'; 
EQUALS : '=' ;




NULL : 'null'; 



DOT : '.';
CPARENT: ')';
OPARENT: '(';
COMMA : ',';
OBRACE: '{';
CBRACE: '}';
OBRACKET : '[';
CBRACKET : ']';
SEMI: ';';

THIS : 'this'; 
NEW : 'new';

TRUE : 'true' ; 
FALSE : 'false'; 


IF : 'if'; 
ELSE : 'else'; 
AND :'&&'; 
OR : '||'; 
WHILE : 'while'; 

READINT : 'readInt' ; 
READFLOAT : 'readFloat';
PRINTLN: 'println';
PRINT: 'print';
PRINTLNX : 'printlnx' ;
PRINTX: 'printx';
LENGTH: 'length';
RETURN : 'return';
INSTANCEOF : 'instanceof'; 









ASM : 'asm' ;
CLASS : 'class' ;
EXTENDS : 'extends';
PROTECTED : 'protected' ;



fragment CHIFFRE: '0' .. '9';
fragment CHIFFREPOSITIF: '1' .. '9' ;
fragment LETTRE: 'a' .. 'z' | 'A' .. 'Z' ;





fragment NUM: CHIFFRE+ ;
fragment SIGN : PLUS | MINUS |  ;
fragment EXP : ('E' | 'e') SIGN NUM ;
fragment DEC : NUM '.' NUM ;
fragment FLOATDEC : ( DEC | DEC EXP ) ( 'F' | 'f' |  ) ;
fragment DIGITHEX : '0' .. '9' | 'A' .. 'F' | 'a' .. 'f' ;
fragment NUMHEX : DIGITHEX+ ;
fragment FLOATHEX : ('0x' | '0X') NUMHEX '.' NUMHEX ('P' | 'p' ) SIGN NUM ('F' | 'f' |  ) ;
FLOAT : FLOATHEX | FLOATDEC ;
INT: '0' | CHIFFREPOSITIF CHIFFRE* ;
// TABLEAU: OBRACE SPACE* (IDENT | INT | FLOAT) SPACE* (COMMA SPACE* (IDENT | INT | FLOAT))* SPACE* CBRACE;

fragment EOL : '\n' {
	skip();
};

fragment STRING_CAR : ~( '\\' |  '"' | '\n' ) ; //ou peut etre double backslash n
STRING : '"' (STRING_CAR | '\\' '\\' '"' | '\\' '\\' '\\' '\\')* '"' ;
MULTI_LINE_STRING : '"' (STRING_CAR | EOL | '\\' '\\' '"' | '\\' '\\' '\\' '\\')* '"' ;

fragment COMMENTAIRE : ('/*' .*? '*/') | ('//' .*? '\n') ;

fragment FILENAME : (LETTRE | CHIFFRE | '.' | '..' | '/' | '-' | '_' )+ ;
INCLUDE : '#include' (' ')* '"' FILENAME '"' {
	skip() ;
	doInclude( getText() );
};



//a rajouter : float, bool, plrs caracteres.... 
IDENT : (LETTRE | '_' | '$') (LETTRE | '_' | '$' | CHIFFRE )* (OBRACKET CBRACKET)? (OBRACKET CBRACKET)?; 
// INCLUDE : '#include' {doInclude(ClassLoader.getSystemResource("include/" + name));};

// a refaire 
/*
;  ; ; MULTI_LINE_STRING
 : 'plein de string';
 */
fragment SPACE: (' ');

WS: ( SPACE | '\t' | '\r' | '\n' | COMMENTAIRE ) {
              skip(); // avoid producing a token
          };