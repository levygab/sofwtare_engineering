// Generated from /home/gabi/Documents/ecole/gl18/src/main/antlr4/fr/ensimag/deca/syntax/DecaLexer.g4 by ANTLR 4.8
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class DecaLexer extends AbstractDecaLexer {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		PLUS=1, MINUS=2, TIMES=3, EXCLAM=4, NEQ=5, GEQ=6, GT=7, LT=8, LEQ=9, EQEQ=10, 
		SLASH=11, PERCENT=12, EQUALS=13, NULL=14, DOT=15, CPARENT=16, OPARENT=17, 
		COMMA=18, OBRACE=19, CBRACE=20, SEMI=21, THIS=22, NEW=23, TRUE=24, FALSE=25, 
		IF=26, ELSE=27, AND=28, OR=29, WHILE=30, READINT=31, READFLOAT=32, PRINTLN=33, 
		PRINT=34, PRINTLNX=35, PRINTX=36, RETURN=37, INSTANCEOF=38, ASM=39, CLASS=40, 
		EXTENDS=41, PROTECTED=42, FLOAT=43, INT=44, STRING=45, MULTI_LINE_STRING=46, 
		INCLUDE=47, IDENT=48, WS=49;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"PLUS", "MINUS", "TIMES", "EXCLAM", "NEQ", "GEQ", "GT", "LT", "LEQ", 
			"EQEQ", "SLASH", "PERCENT", "EQUALS", "NULL", "DOT", "CPARENT", "OPARENT", 
			"COMMA", "OBRACE", "CBRACE", "SEMI", "THIS", "NEW", "TRUE", "FALSE", 
			"IF", "ELSE", "AND", "OR", "WHILE", "READINT", "READFLOAT", "PRINTLN", 
			"PRINT", "PRINTLNX", "PRINTX", "RETURN", "INSTANCEOF", "ASM", "CLASS", 
			"EXTENDS", "PROTECTED", "CHIFFRE", "CHIFFREPOSITIF", "LETTRE", "NUM", 
			"SIGN", "EXP", "DEC", "FLOATDEC", "DIGITHEX", "NUMHEX", "FLOATHEX", "FLOAT", 
			"INT", "EOL", "STRING_CAR", "STRING", "MULTI_LINE_STRING", "COMMENTAIRE", 
			"FILENAME", "INCLUDE", "IDENT", "WS"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'+'", "'-'", "'*'", "'!'", "'!='", "'>='", "'>'", "'<'", "'<='", 
			"'=='", "'/'", "'%'", "'='", "'null'", "'.'", "')'", "'('", "','", "'{'", 
			"'}'", "';'", "'this'", "'new'", "'true'", "'false'", "'if'", "'else'", 
			"'&&'", "'||'", "'while'", "'readInt'", "'readFloat'", "'println'", "'print'", 
			"'printlnx'", "'printx'", "'return'", "'instanceof'", "'asm'", "'class'", 
			"'extends'", "'protected'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "PLUS", "MINUS", "TIMES", "EXCLAM", "NEQ", "GEQ", "GT", "LT", "LEQ", 
			"EQEQ", "SLASH", "PERCENT", "EQUALS", "NULL", "DOT", "CPARENT", "OPARENT", 
			"COMMA", "OBRACE", "CBRACE", "SEMI", "THIS", "NEW", "TRUE", "FALSE", 
			"IF", "ELSE", "AND", "OR", "WHILE", "READINT", "READFLOAT", "PRINTLN", 
			"PRINT", "PRINTLNX", "PRINTX", "RETURN", "INSTANCEOF", "ASM", "CLASS", 
			"EXTENDS", "PROTECTED", "FLOAT", "INT", "STRING", "MULTI_LINE_STRING", 
			"INCLUDE", "IDENT", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}




	public DecaLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "DecaLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 55:
			EOL_action((RuleContext)_localctx, actionIndex);
			break;
		case 61:
			INCLUDE_action((RuleContext)_localctx, actionIndex);
			break;
		case 63:
			WS_action((RuleContext)_localctx, actionIndex);
			break;
		}
	}
	private void EOL_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0:

				skip();

			break;
		}
	}
	private void INCLUDE_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 1:

				skip() ;
				doInclude( getText() );

			break;
		}
	}
	private void WS_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 2:

			              skip(); // avoid producing a token
			          
			break;
		}
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\63\u01ee\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t"+
		"+\4,\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64"+
		"\t\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t"+
		"=\4>\t>\4?\t?\4@\t@\4A\tA\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\6"+
		"\3\7\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\n\3\13\3\13\3\13\3\f\3\f\3\r\3"+
		"\r\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\21\3\21\3\22\3\22\3"+
		"\23\3\23\3\24\3\24\3\25\3\25\3\26\3\26\3\27\3\27\3\27\3\27\3\27\3\30\3"+
		"\30\3\30\3\30\3\31\3\31\3\31\3\31\3\31\3\32\3\32\3\32\3\32\3\32\3\32\3"+
		"\33\3\33\3\33\3\34\3\34\3\34\3\34\3\34\3\35\3\35\3\35\3\36\3\36\3\36\3"+
		"\37\3\37\3\37\3\37\3\37\3\37\3 \3 \3 \3 \3 \3 \3 \3 \3!\3!\3!\3!\3!\3"+
		"!\3!\3!\3!\3!\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3#\3#\3#\3#\3#\3#\3$\3$"+
		"\3$\3$\3$\3$\3$\3$\3$\3%\3%\3%\3%\3%\3%\3%\3&\3&\3&\3&\3&\3&\3&\3\'\3"+
		"\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3(\3(\3(\3(\3)\3)\3)\3)\3)\3)\3"+
		"*\3*\3*\3*\3*\3*\3*\3*\3+\3+\3+\3+\3+\3+\3+\3+\3+\3+\3,\3,\3-\3-\3.\3"+
		".\3/\6/\u0142\n/\r/\16/\u0143\3\60\3\60\3\60\5\60\u0149\n\60\3\61\3\61"+
		"\3\61\3\61\3\62\3\62\3\62\3\62\3\63\3\63\3\63\3\63\5\63\u0157\n\63\3\63"+
		"\3\63\5\63\u015b\n\63\3\64\3\64\3\65\6\65\u0160\n\65\r\65\16\65\u0161"+
		"\3\66\3\66\3\66\3\66\5\66\u0168\n\66\3\66\3\66\3\66\3\66\3\66\3\66\3\66"+
		"\3\66\5\66\u0172\n\66\3\67\3\67\5\67\u0176\n\67\38\38\38\78\u017b\n8\f"+
		"8\168\u017e\138\58\u0180\n8\39\39\39\3:\3:\3;\3;\3;\3;\3;\3;\3;\3;\3;"+
		"\7;\u0190\n;\f;\16;\u0193\13;\3;\3;\3<\3<\3<\3<\3<\3<\3<\3<\3<\3<\7<\u01a1"+
		"\n<\f<\16<\u01a4\13<\3<\3<\3=\3=\3=\3=\7=\u01ac\n=\f=\16=\u01af\13=\3"+
		"=\3=\3=\3=\3=\3=\7=\u01b7\n=\f=\16=\u01ba\13=\3=\5=\u01bd\n=\3>\3>\3>"+
		"\3>\3>\3>\6>\u01c5\n>\r>\16>\u01c6\3?\3?\3?\3?\3?\3?\3?\3?\3?\3?\7?\u01d3"+
		"\n?\f?\16?\u01d6\13?\3?\3?\3?\3?\3?\3@\3@\5@\u01df\n@\3@\3@\3@\7@\u01e4"+
		"\n@\f@\16@\u01e7\13@\3A\3A\5A\u01eb\nA\3A\3A\4\u01ad\u01b8\2B\3\3\5\4"+
		"\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22"+
		"#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\359\36;\37= ?!A\"C"+
		"#E$G%I&K\'M(O)Q*S+U,W\2Y\2[\2]\2_\2a\2c\2e\2g\2i\2k\2m-o.q\2s\2u/w\60"+
		"y\2{\2}\61\177\62\u0081\63\3\2\13\4\2C\\c|\4\2GGgg\4\2HHhh\5\2\62;CHc"+
		"h\4\2RRrr\5\2\f\f$$^^\5\2//\61\61aa\4\2&&aa\5\2\13\f\17\17\"\"\2\u01fe"+
		"\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2"+
		"\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2"+
		"\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2"+
		"\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2"+
		"\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3"+
		"\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2"+
		"\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2"+
		"U\3\2\2\2\2m\3\2\2\2\2o\3\2\2\2\2u\3\2\2\2\2w\3\2\2\2\2}\3\2\2\2\2\177"+
		"\3\2\2\2\2\u0081\3\2\2\2\3\u0083\3\2\2\2\5\u0085\3\2\2\2\7\u0087\3\2\2"+
		"\2\t\u0089\3\2\2\2\13\u008b\3\2\2\2\r\u008e\3\2\2\2\17\u0091\3\2\2\2\21"+
		"\u0093\3\2\2\2\23\u0095\3\2\2\2\25\u0098\3\2\2\2\27\u009b\3\2\2\2\31\u009d"+
		"\3\2\2\2\33\u009f\3\2\2\2\35\u00a1\3\2\2\2\37\u00a6\3\2\2\2!\u00a8\3\2"+
		"\2\2#\u00aa\3\2\2\2%\u00ac\3\2\2\2\'\u00ae\3\2\2\2)\u00b0\3\2\2\2+\u00b2"+
		"\3\2\2\2-\u00b4\3\2\2\2/\u00b9\3\2\2\2\61\u00bd\3\2\2\2\63\u00c2\3\2\2"+
		"\2\65\u00c8\3\2\2\2\67\u00cb\3\2\2\29\u00d0\3\2\2\2;\u00d3\3\2\2\2=\u00d6"+
		"\3\2\2\2?\u00dc\3\2\2\2A\u00e4\3\2\2\2C\u00ee\3\2\2\2E\u00f6\3\2\2\2G"+
		"\u00fc\3\2\2\2I\u0105\3\2\2\2K\u010c\3\2\2\2M\u0113\3\2\2\2O\u011e\3\2"+
		"\2\2Q\u0122\3\2\2\2S\u0128\3\2\2\2U\u0130\3\2\2\2W\u013a\3\2\2\2Y\u013c"+
		"\3\2\2\2[\u013e\3\2\2\2]\u0141\3\2\2\2_\u0148\3\2\2\2a\u014a\3\2\2\2c"+
		"\u014e\3\2\2\2e\u0156\3\2\2\2g\u015c\3\2\2\2i\u015f\3\2\2\2k\u0167\3\2"+
		"\2\2m\u0175\3\2\2\2o\u017f\3\2\2\2q\u0181\3\2\2\2s\u0184\3\2\2\2u\u0186"+
		"\3\2\2\2w\u0196\3\2\2\2y\u01bc\3\2\2\2{\u01c4\3\2\2\2}\u01c8\3\2\2\2\177"+
		"\u01de\3\2\2\2\u0081\u01ea\3\2\2\2\u0083\u0084\7-\2\2\u0084\4\3\2\2\2"+
		"\u0085\u0086\7/\2\2\u0086\6\3\2\2\2\u0087\u0088\7,\2\2\u0088\b\3\2\2\2"+
		"\u0089\u008a\7#\2\2\u008a\n\3\2\2\2\u008b\u008c\7#\2\2\u008c\u008d\7?"+
		"\2\2\u008d\f\3\2\2\2\u008e\u008f\7@\2\2\u008f\u0090\7?\2\2\u0090\16\3"+
		"\2\2\2\u0091\u0092\7@\2\2\u0092\20\3\2\2\2\u0093\u0094\7>\2\2\u0094\22"+
		"\3\2\2\2\u0095\u0096\7>\2\2\u0096\u0097\7?\2\2\u0097\24\3\2\2\2\u0098"+
		"\u0099\7?\2\2\u0099\u009a\7?\2\2\u009a\26\3\2\2\2\u009b\u009c\7\61\2\2"+
		"\u009c\30\3\2\2\2\u009d\u009e\7\'\2\2\u009e\32\3\2\2\2\u009f\u00a0\7?"+
		"\2\2\u00a0\34\3\2\2\2\u00a1\u00a2\7p\2\2\u00a2\u00a3\7w\2\2\u00a3\u00a4"+
		"\7n\2\2\u00a4\u00a5\7n\2\2\u00a5\36\3\2\2\2\u00a6\u00a7\7\60\2\2\u00a7"+
		" \3\2\2\2\u00a8\u00a9\7+\2\2\u00a9\"\3\2\2\2\u00aa\u00ab\7*\2\2\u00ab"+
		"$\3\2\2\2\u00ac\u00ad\7.\2\2\u00ad&\3\2\2\2\u00ae\u00af\7}\2\2\u00af("+
		"\3\2\2\2\u00b0\u00b1\7\177\2\2\u00b1*\3\2\2\2\u00b2\u00b3\7=\2\2\u00b3"+
		",\3\2\2\2\u00b4\u00b5\7v\2\2\u00b5\u00b6\7j\2\2\u00b6\u00b7\7k\2\2\u00b7"+
		"\u00b8\7u\2\2\u00b8.\3\2\2\2\u00b9\u00ba\7p\2\2\u00ba\u00bb\7g\2\2\u00bb"+
		"\u00bc\7y\2\2\u00bc\60\3\2\2\2\u00bd\u00be\7v\2\2\u00be\u00bf\7t\2\2\u00bf"+
		"\u00c0\7w\2\2\u00c0\u00c1\7g\2\2\u00c1\62\3\2\2\2\u00c2\u00c3\7h\2\2\u00c3"+
		"\u00c4\7c\2\2\u00c4\u00c5\7n\2\2\u00c5\u00c6\7u\2\2\u00c6\u00c7\7g\2\2"+
		"\u00c7\64\3\2\2\2\u00c8\u00c9\7k\2\2\u00c9\u00ca\7h\2\2\u00ca\66\3\2\2"+
		"\2\u00cb\u00cc\7g\2\2\u00cc\u00cd\7n\2\2\u00cd\u00ce\7u\2\2\u00ce\u00cf"+
		"\7g\2\2\u00cf8\3\2\2\2\u00d0\u00d1\7(\2\2\u00d1\u00d2\7(\2\2\u00d2:\3"+
		"\2\2\2\u00d3\u00d4\7~\2\2\u00d4\u00d5\7~\2\2\u00d5<\3\2\2\2\u00d6\u00d7"+
		"\7y\2\2\u00d7\u00d8\7j\2\2\u00d8\u00d9\7k\2\2\u00d9\u00da\7n\2\2\u00da"+
		"\u00db\7g\2\2\u00db>\3\2\2\2\u00dc\u00dd\7t\2\2\u00dd\u00de\7g\2\2\u00de"+
		"\u00df\7c\2\2\u00df\u00e0\7f\2\2\u00e0\u00e1\7K\2\2\u00e1\u00e2\7p\2\2"+
		"\u00e2\u00e3\7v\2\2\u00e3@\3\2\2\2\u00e4\u00e5\7t\2\2\u00e5\u00e6\7g\2"+
		"\2\u00e6\u00e7\7c\2\2\u00e7\u00e8\7f\2\2\u00e8\u00e9\7H\2\2\u00e9\u00ea"+
		"\7n\2\2\u00ea\u00eb\7q\2\2\u00eb\u00ec\7c\2\2\u00ec\u00ed\7v\2\2\u00ed"+
		"B\3\2\2\2\u00ee\u00ef\7r\2\2\u00ef\u00f0\7t\2\2\u00f0\u00f1\7k\2\2\u00f1"+
		"\u00f2\7p\2\2\u00f2\u00f3\7v\2\2\u00f3\u00f4\7n\2\2\u00f4\u00f5\7p\2\2"+
		"\u00f5D\3\2\2\2\u00f6\u00f7\7r\2\2\u00f7\u00f8\7t\2\2\u00f8\u00f9\7k\2"+
		"\2\u00f9\u00fa\7p\2\2\u00fa\u00fb\7v\2\2\u00fbF\3\2\2\2\u00fc\u00fd\7"+
		"r\2\2\u00fd\u00fe\7t\2\2\u00fe\u00ff\7k\2\2\u00ff\u0100\7p\2\2\u0100\u0101"+
		"\7v\2\2\u0101\u0102\7n\2\2\u0102\u0103\7p\2\2\u0103\u0104\7z\2\2\u0104"+
		"H\3\2\2\2\u0105\u0106\7r\2\2\u0106\u0107\7t\2\2\u0107\u0108\7k\2\2\u0108"+
		"\u0109\7p\2\2\u0109\u010a\7v\2\2\u010a\u010b\7z\2\2\u010bJ\3\2\2\2\u010c"+
		"\u010d\7t\2\2\u010d\u010e\7g\2\2\u010e\u010f\7v\2\2\u010f\u0110\7w\2\2"+
		"\u0110\u0111\7t\2\2\u0111\u0112\7p\2\2\u0112L\3\2\2\2\u0113\u0114\7k\2"+
		"\2\u0114\u0115\7p\2\2\u0115\u0116\7u\2\2\u0116\u0117\7v\2\2\u0117\u0118"+
		"\7c\2\2\u0118\u0119\7p\2\2\u0119\u011a\7e\2\2\u011a\u011b\7g\2\2\u011b"+
		"\u011c\7q\2\2\u011c\u011d\7h\2\2\u011dN\3\2\2\2\u011e\u011f\7c\2\2\u011f"+
		"\u0120\7u\2\2\u0120\u0121\7o\2\2\u0121P\3\2\2\2\u0122\u0123\7e\2\2\u0123"+
		"\u0124\7n\2\2\u0124\u0125\7c\2\2\u0125\u0126\7u\2\2\u0126\u0127\7u\2\2"+
		"\u0127R\3\2\2\2\u0128\u0129\7g\2\2\u0129\u012a\7z\2\2\u012a\u012b\7v\2"+
		"\2\u012b\u012c\7g\2\2\u012c\u012d\7p\2\2\u012d\u012e\7f\2\2\u012e\u012f"+
		"\7u\2\2\u012fT\3\2\2\2\u0130\u0131\7r\2\2\u0131\u0132\7t\2\2\u0132\u0133"+
		"\7q\2\2\u0133\u0134\7v\2\2\u0134\u0135\7g\2\2\u0135\u0136\7e\2\2\u0136"+
		"\u0137\7v\2\2\u0137\u0138\7g\2\2\u0138\u0139\7f\2\2\u0139V\3\2\2\2\u013a"+
		"\u013b\4\62;\2\u013bX\3\2\2\2\u013c\u013d\4\63;\2\u013dZ\3\2\2\2\u013e"+
		"\u013f\t\2\2\2\u013f\\\3\2\2\2\u0140\u0142\5W,\2\u0141\u0140\3\2\2\2\u0142"+
		"\u0143\3\2\2\2\u0143\u0141\3\2\2\2\u0143\u0144\3\2\2\2\u0144^\3\2\2\2"+
		"\u0145\u0149\5\3\2\2\u0146\u0149\5\5\3\2\u0147\u0149\3\2\2\2\u0148\u0145"+
		"\3\2\2\2\u0148\u0146\3\2\2\2\u0148\u0147\3\2\2\2\u0149`\3\2\2\2\u014a"+
		"\u014b\t\3\2\2\u014b\u014c\5_\60\2\u014c\u014d\5]/\2\u014db\3\2\2\2\u014e"+
		"\u014f\5]/\2\u014f\u0150\7\60\2\2\u0150\u0151\5]/\2\u0151d\3\2\2\2\u0152"+
		"\u0157\5c\62\2\u0153\u0154\5c\62\2\u0154\u0155\5a\61\2\u0155\u0157\3\2"+
		"\2\2\u0156\u0152\3\2\2\2\u0156\u0153\3\2\2\2\u0157\u015a\3\2\2\2\u0158"+
		"\u015b\t\4\2\2\u0159\u015b\3\2\2\2\u015a\u0158\3\2\2\2\u015a\u0159\3\2"+
		"\2\2\u015bf\3\2\2\2\u015c\u015d\t\5\2\2\u015dh\3\2\2\2\u015e\u0160\5g"+
		"\64\2\u015f\u015e\3\2\2\2\u0160\u0161\3\2\2\2\u0161\u015f\3\2\2\2\u0161"+
		"\u0162\3\2\2\2\u0162j\3\2\2\2\u0163\u0164\7\62\2\2\u0164\u0168\7z\2\2"+
		"\u0165\u0166\7\62\2\2\u0166\u0168\7Z\2\2\u0167\u0163\3\2\2\2\u0167\u0165"+
		"\3\2\2\2\u0168\u0169\3\2\2\2\u0169\u016a\5i\65\2\u016a\u016b\7\60\2\2"+
		"\u016b\u016c\5i\65\2\u016c\u016d\t\6\2\2\u016d\u016e\5_\60\2\u016e\u0171"+
		"\5]/\2\u016f\u0172\t\4\2\2\u0170\u0172\3\2\2\2\u0171\u016f\3\2\2\2\u0171"+
		"\u0170\3\2\2\2\u0172l\3\2\2\2\u0173\u0176\5k\66\2\u0174\u0176\5e\63\2"+
		"\u0175\u0173\3\2\2\2\u0175\u0174\3\2\2\2\u0176n\3\2\2\2\u0177\u0180\7"+
		"\62\2\2\u0178\u017c\5Y-\2\u0179\u017b\5W,\2\u017a\u0179\3\2\2\2\u017b"+
		"\u017e\3\2\2\2\u017c\u017a\3\2\2\2\u017c\u017d\3\2\2\2\u017d\u0180\3\2"+
		"\2\2\u017e\u017c\3\2\2\2\u017f\u0177\3\2\2\2\u017f\u0178\3\2\2\2\u0180"+
		"p\3\2\2\2\u0181\u0182\7\f\2\2\u0182\u0183\b9\2\2\u0183r\3\2\2\2\u0184"+
		"\u0185\n\7\2\2\u0185t\3\2\2\2\u0186\u0191\7$\2\2\u0187\u0190\5s:\2\u0188"+
		"\u0189\7^\2\2\u0189\u018a\7^\2\2\u018a\u0190\7$\2\2\u018b\u018c\7^\2\2"+
		"\u018c\u018d\7^\2\2\u018d\u018e\7^\2\2\u018e\u0190\7^\2\2\u018f\u0187"+
		"\3\2\2\2\u018f\u0188\3\2\2\2\u018f\u018b\3\2\2\2\u0190\u0193\3\2\2\2\u0191"+
		"\u018f\3\2\2\2\u0191\u0192\3\2\2\2\u0192\u0194\3\2\2\2\u0193\u0191\3\2"+
		"\2\2\u0194\u0195\7$\2\2\u0195v\3\2\2\2\u0196\u01a2\7$\2\2\u0197\u01a1"+
		"\5s:\2\u0198\u01a1\5q9\2\u0199\u019a\7^\2\2\u019a\u019b\7^\2\2\u019b\u01a1"+
		"\7$\2\2\u019c\u019d\7^\2\2\u019d\u019e\7^\2\2\u019e\u019f\7^\2\2\u019f"+
		"\u01a1\7^\2\2\u01a0\u0197\3\2\2\2\u01a0\u0198\3\2\2\2\u01a0\u0199\3\2"+
		"\2\2\u01a0\u019c\3\2\2\2\u01a1\u01a4\3\2\2\2\u01a2\u01a0\3\2\2\2\u01a2"+
		"\u01a3\3\2\2\2\u01a3\u01a5\3\2\2\2\u01a4\u01a2\3\2\2\2\u01a5\u01a6\7$"+
		"\2\2\u01a6x\3\2\2\2\u01a7\u01a8\7\61\2\2\u01a8\u01a9\7,\2\2\u01a9\u01ad"+
		"\3\2\2\2\u01aa\u01ac\13\2\2\2\u01ab\u01aa\3\2\2\2\u01ac\u01af\3\2\2\2"+
		"\u01ad\u01ae\3\2\2\2\u01ad\u01ab\3\2\2\2\u01ae\u01b0\3\2\2\2\u01af\u01ad"+
		"\3\2\2\2\u01b0\u01b1\7,\2\2\u01b1\u01bd\7\61\2\2\u01b2\u01b3\7\61\2\2"+
		"\u01b3\u01b4\7\61\2\2\u01b4\u01b8\3\2\2\2\u01b5\u01b7\13\2\2\2\u01b6\u01b5"+
		"\3\2\2\2\u01b7\u01ba\3\2\2\2\u01b8\u01b9\3\2\2\2\u01b8\u01b6\3\2\2\2\u01b9"+
		"\u01bb\3\2\2\2\u01ba\u01b8\3\2\2\2\u01bb\u01bd\7\f\2\2\u01bc\u01a7\3\2"+
		"\2\2\u01bc\u01b2\3\2\2\2\u01bdz\3\2\2\2\u01be\u01c5\5[.\2\u01bf\u01c5"+
		"\5W,\2\u01c0\u01c5\7\60\2\2\u01c1\u01c2\7\60\2\2\u01c2\u01c5\7\60\2\2"+
		"\u01c3\u01c5\t\b\2\2\u01c4\u01be\3\2\2\2\u01c4\u01bf\3\2\2\2\u01c4\u01c0"+
		"\3\2\2\2\u01c4\u01c1\3\2\2\2\u01c4\u01c3\3\2\2\2\u01c5\u01c6\3\2\2\2\u01c6"+
		"\u01c4\3\2\2\2\u01c6\u01c7\3\2\2\2\u01c7|\3\2\2\2\u01c8\u01c9\7%\2\2\u01c9"+
		"\u01ca\7k\2\2\u01ca\u01cb\7p\2\2\u01cb\u01cc\7e\2\2\u01cc\u01cd\7n\2\2"+
		"\u01cd\u01ce\7w\2\2\u01ce\u01cf\7f\2\2\u01cf\u01d0\7g\2\2\u01d0\u01d4"+
		"\3\2\2\2\u01d1\u01d3\7\"\2\2\u01d2\u01d1\3\2\2\2\u01d3\u01d6\3\2\2\2\u01d4"+
		"\u01d2\3\2\2\2\u01d4\u01d5\3\2\2\2\u01d5\u01d7\3\2\2\2\u01d6\u01d4\3\2"+
		"\2\2\u01d7\u01d8\7$\2\2\u01d8\u01d9\5{>\2\u01d9\u01da\7$\2\2\u01da\u01db"+
		"\b?\3\2\u01db~\3\2\2\2\u01dc\u01df\5[.\2\u01dd\u01df\t\t\2\2\u01de\u01dc"+
		"\3\2\2\2\u01de\u01dd\3\2\2\2\u01df\u01e5\3\2\2\2\u01e0\u01e4\5[.\2\u01e1"+
		"\u01e4\t\t\2\2\u01e2\u01e4\5W,\2\u01e3\u01e0\3\2\2\2\u01e3\u01e1\3\2\2"+
		"\2\u01e3\u01e2\3\2\2\2\u01e4\u01e7\3\2\2\2\u01e5\u01e3\3\2\2\2\u01e5\u01e6"+
		"\3\2\2\2\u01e6\u0080\3\2\2\2\u01e7\u01e5\3\2\2\2\u01e8\u01eb\t\n\2\2\u01e9"+
		"\u01eb\5y=\2\u01ea\u01e8\3\2\2\2\u01ea\u01e9\3\2\2\2\u01eb\u01ec\3\2\2"+
		"\2\u01ec\u01ed\bA\4\2\u01ed\u0082\3\2\2\2\33\2\u0143\u0148\u0156\u015a"+
		"\u0161\u0167\u0171\u0175\u017c\u017f\u018f\u0191\u01a0\u01a2\u01ad\u01b8"+
		"\u01bc\u01c4\u01c6\u01d4\u01de\u01e3\u01e5\u01ea\5\39\2\3?\3\3A\4";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}