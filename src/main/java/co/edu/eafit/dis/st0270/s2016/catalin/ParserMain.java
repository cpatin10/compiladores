package co.edu.eafit.dis.st0270.s2016.catalin;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.tree.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.FileReader;

public class ParserMain {
    private static PrintWriter out;
    private static BufferedReader in;
    private static ArrayList<Integer> colCount;
    private static HashMap<Integer, Integer> getRow;
    private static boolean fileMode;

    public static void main(String[] args) throws IOException {
    	out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
	int filesNumber = args.length;
	fileMode = filesNumber > 0;
	if (fileMode) {
	    out.println("Input: Files - one expression per file");
	    int fileCounter = 1;
	    for (String file : args) {
		out.printf("File #%d/%d: %s\n", fileCounter++, filesNumber, file);
		in = new BufferedReader(new FileReader(file));		
		read();
	    }
	    
	} else {
	    out.println("Input: Standard - limited to one expression only");
	    out.flush();
	    in = new BufferedReader(new InputStreamReader(System.in));	
	    read();
	}
	in.close();
	out.close();
    }

    private static void read() throws IOException {
	colCount = new ArrayList<>();
	getRow = new HashMap<>();
	String line;
	int last = 0, row = 0;
	StringBuilder sb = new StringBuilder();
	while ((line = in.readLine()) != null) {
	    colCount.add(last-row+line.length());
	    for (int i = 0; i < line.length()+1; ++i)
		getRow.put(last+i, row);
	    last += line.length()+1;
	    ++row;
	    sb.append(line).append(' ');
	    if (fileMode)
		out.println(line);
	}
	parse(sb.toString());	
	out.println();
    }

    private static void parse(String text) throws IOException {
	try {
	    CatalinLexer lexer = new CatalinLexer(new ANTLRInputStream(text));
	    CatalinParser parser = new CatalinParser(new CommonTokenStream(lexer));
	    ParseTree tree = parser.prog();	    
	    out.println("No syntax errors -> string ACCEPTED");
	} catch(ParserException e) {
	    int code = (int) e.getToken().charAt(0);
	    int row = getRow.get(e.getColumn());
	    if (e.getType().equals("ERROR")) 
		out.print("Unrecognized token ");
	    else
		out.print("Parse ");
	    out.printf("error: Token: %s Row: %d Column: %d\n", 
		       code >= 32 && code <= 126 ? e.getToken() : "not printable",  
		       row+1, 
		       (e.getColumn()%colCount.get(row))+1);    
	    if (!fileMode) {
		in.close();
		out.close();
		System.exit(1);
	    }
	} catch (ArrayIndexOutOfBoundsException e) {
	    out.println("Incomplete input");
	    if (!fileMode) {
		in.close();
		out.close();
		System.exit(1);
	    }
	} catch(Exception e) {
	    out.println("This shouldn't happed :(");
	    e.printStackTrace();
	    if (!fileMode) {
		in.close();
		out.close();
		System.exit(1);
	    }
	}
    }
}
