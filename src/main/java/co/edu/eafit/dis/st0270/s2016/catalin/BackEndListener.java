package co.edu.eafit.dis.st0270.s2016.catalin;

import org.antlr.v4.runtime.Token;
import java.util.Stack;
import java.io.PrintWriter;

public class BackEndListener extends CatalinBaseListener {
    private Stack<Scope> scopes;
    private PrintWriter out;

    public BackEndListener(PrintWriter out) {
	super();
	scopes = new Stack<>();
	this.out = out;
    }

    @Override
    public void enterProg(CatalinParser.ProgContext ctx) {
	scopes.add(new Scope(null));
    }    
    @Override
    public void exitProg(CatalinParser.ProgContext ctx) {
	scopes.pop();
    }
    @Override
    public void enterStatsStat(CatalinParser.StatsStatContext ctx) {
	scopes.add(new Scope(scopes.peek()));
    }
    @Override
    public void exitStatsStat(CatalinParser.StatsStatContext ctx) {
	scopes.pop();
    }
    @Override
    public void enterDeclStat(CatalinParser.DeclStatContext ctx) {
	ListenerMain.anyVariables = true;
	Token token = ctx.NAME().getSymbol();
	String var = token.getText();
	if (scopes.peek().containsVar(var)){
	    ListenerMain.invalidVariables = true;
	    int row = ListenerMain.getRow.get(token.getCharPositionInLine());
	    int col = (token.getCharPositionInLine()%ListenerMain.colCount.get(row))+1;
	    out.printf("Error: redeclaration of variable %s. Row %d Column: %d\n", var, row+1, col);   
	} else
	    scopes.peek().add(var);
    }
    @Override
    public void enterUseStat(CatalinParser.UseStatContext ctx) {
	Token token = ctx.NAME().getSymbol();
	checkVariable(token.getText(), token.getCharPositionInLine());
    }
    @Override
    public void enterIfStat(CatalinParser.IfStatContext ctx) {
	Token token = ctx.NAME().getSymbol();
	checkVariable(token.getText(), token.getCharPositionInLine());
    }
    @Override 
    public void enterName(CatalinParser.NameContext ctx) {
	Token token = ctx.NAME().getSymbol();
	checkVariable(token.getText(), token.getCharPositionInLine());
    }
    private void checkVariable(String variable, int column) {
	ListenerMain.anyVariables = true;
	if (!scopes.peek().containsVar(variable)) {
	    ListenerMain.invalidVariables = true;
	    int row = ListenerMain.getRow.get(column);
	    int col = (column%ListenerMain.colCount.get(row))+1;
	    out.printf("Error: Variable %s was not declared in its scope. Row %d Column: %d\n", variable, row+1, col);
	}
    }    
}
