package co.edu.eafit.dis.st0270.s2016.catalin;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.Token;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.FileReader;

public class LexerMain {
    private static PrintWriter out;
    private static BufferedReader in;
    private static boolean fileMode;
    private static int row;

    public static void main (String[] args) throws IOException{
	out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
	int filesNumber = args.length;
	fileMode = filesNumber > 0;
	if (fileMode) {
	    out.println("Input: Files - one line -> one expression");
	    int fileCounter = 1;
	    for (String file : args) {
		out.printf("File #%d/%d: %s\n", fileCounter++, filesNumber, file);
		in = new BufferedReader(new FileReader(file));		
		read();
	    }	    
	} else {
	    out.println("Input: Standard - one line -> one expression");
	    out.flush();
	    in = new BufferedReader(new InputStreamReader(System.in));
	    read();
	}	
	in.close();
	out.close();
    }

    private static void read() throws IOException {
	row = 0;
	String line;
	while ((line = in.readLine()) != null) {
	    if (fileMode)
		out.printf("Row: %d Expression:\n%s\n", ++row, line);
	    tokenize(line);
	}    	
    }

    private static void printTokenInfo(Token token, String type) {
	out.printf("Token: %s Type: %s Row: %d Column: %d\n", token.getText(), type, row, token.getCharPositionInLine()+1);
    }
    
    private static void printError(Token token) throws IOException{
	String text = token.getText();
	int code = (int) text.charAt(0);
	boolean printable = code >= 32 && code <= 126;
	out.printf("Error: Token: %s Type: unrecognized Row: %d Column: %d\n", printable ? text : "not printable", row, token.getCharPositionInLine()+1);
	if (!fileMode) {
	    in.close();
	    out.close();
	    System.exit(1);
	}	
    }

    private static void tokenize(String line) throws IOException{
	CatalinLexer lexer = new CatalinLexer(new ANTLRInputStream(line));
	Token token = lexer.nextToken();
	while(token.getType() != Token.EOF) {
	    switch(token.getType()) {
	    case CatalinLexer.DECLARE:
		printTokenInfo(token, "'decl'");
		break;
	    case CatalinLexer.USE:
		printTokenInfo(token, "'use'");
		break;
	    case CatalinLexer.VERZERO:
		printTokenInfo(token, "'izfero'");
		break;
	    case CatalinLexer.VERNEG:
		printTokenInfo(token, "'ifneg'");
		break;
	    case CatalinLexer.VERPOS:
		printTokenInfo(token, "'ifpos'");
		break;
	    case CatalinLexer.VERNZERO:
		printTokenInfo(token, "'ifnzero'");
		break;
	    case CatalinLexer.NEGATE:
		printTokenInfo(token, "'neg'");
		break;
	    case CatalinLexer.NAME:
		printTokenInfo(token, "identifier");
		break;
	    case CatalinLexer.NUMERO:
		printTokenInfo(token, "integer number");		
		break;
	    case CatalinLexer.SUMOP:
		printTokenInfo(token, "plus operator");
		break;
	    case CatalinLexer.LBRACKET:
		printTokenInfo(token, "left bracket");
		break;
	    case CatalinLexer.RBRACKET:
		printTokenInfo(token, "right bracket");
		break;
	    case CatalinLexer.SEPARATOR:
		printTokenInfo(token, "separator");
		break;
	    case CatalinLexer.MULOP:
		printTokenInfo(token, "times operator");
		break;
	    default: 
		printError(token);
		break;
	    }	    
	    token = lexer.nextToken();
	}
    }
}
