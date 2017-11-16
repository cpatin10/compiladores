package co.edu.eafit.dis.st0270.s2016.catalin;

import org.antlr.v4.runtime.Token;
import java.util.Stack;
import java.io.PrintWriter;

public class BackEndVisitor extends CatalinBaseVisitor<Boolean> {
    private Stack<Scope> scopes;
    private PrintWriter out;

    public BackEndVisitor(PrintWriter out) {
	super();
	scopes = new Stack<>();
	this.out = out;
    }

    @Override
    public Boolean visitProg(CatalinParser.ProgContext ctx) {
	scopes.push(new Scope(null));
	boolean valid = visit(ctx.stats());
	scopes.pop();
	return valid;
    }
    @Override
    public Boolean visitStats(CatalinParser.StatsContext ctx) {
	if (ctx.stat() != null  && !ctx.stat().isEmpty()) {
	    boolean valid = true;
	    for (CatalinParser.StatContext s : ctx.stat()) 
		valid = valid && visit(s);
	    return valid;
	}
	return true;
    }
    @Override
    public Boolean visitDeclStat(CatalinParser.DeclStatContext ctx) {
	VisitorMain.anyVariables = true;
	Token token = ctx.NAME().getSymbol();
	String var = token.getText();
	if (scopes.peek().containsVar(var)) {
	    int row = VisitorMain.getRow.get(token.getCharPositionInLine());
	    int col = (token.getCharPositionInLine()%VisitorMain.colCount.get(row))+1;
	    out.printf("Error: redeclaration of variable %s. Row %d Column: %d\n", var, row+1, col);
	    return false;
	}
	scopes.peek().add(var);	
	return true;
    }
    @Override
    public Boolean visitUseStat(CatalinParser.UseStatContext ctx) {
	Token token = ctx.NAME().getSymbol();
	if (checkVariable(token.getText(), token.getCharPositionInLine()))
	    return visit(ctx.expr());
	return false;	
    }
    @Override
    public Boolean visitStatsStat(CatalinParser.StatsStatContext ctx) {
	scopes.add(new Scope(scopes.peek()));	
	boolean valid = visit(ctx.stats());
	scopes.pop();
	return valid;
    }
    @Override
    public Boolean visitIfStat(CatalinParser.IfStatContext ctx) {
	Token token = ctx.NAME().getSymbol();
	if (checkVariable(token.getText(), token.getCharPositionInLine()))
	    return visit(ctx.stat());
	return false;	
    }
    @Override
    public Boolean visitExpr(CatalinParser.ExprContext ctx) {
	if (visit(ctx.termino()))
	    return ctx.expr1P() == null ? true : visit(ctx.expr1P());
	return false;
    }
    @Override
    public Boolean visitExpr1P(CatalinParser.Expr1PContext ctx) {
	if (visit(ctx.termino()))
	    return ctx.expr1P() == null ? true : visit(ctx.expr1P());
	return false;
    }
    @Override
    public Boolean visitTermino(CatalinParser.TerminoContext ctx) {
	if (visit(ctx.factor()))
	    return ctx.termino1P() == null ? true : visit(ctx.termino1P());
	return false;
    }
    @Override
    public Boolean visitTermino1P(CatalinParser.Termino1PContext ctx) {
	if (visit(ctx.factor()))
	    return ctx.termino1P() == null ? true : visit(ctx.termino1P());
	return false;
    }
    @Override
    public Boolean visitNumero(CatalinParser.NumeroContext ctx) {
	return true;
    }
    @Override
    public Boolean visitName(CatalinParser.NameContext ctx) {
	Token token = ctx.NAME().getSymbol();
	return checkVariable(token.getText(), token.getCharPositionInLine());
    }
    private boolean checkVariable(String variable, int column) {
	VisitorMain.anyVariables = true;
	if (!scopes.peek().containsVar(variable)) {
	    int row = VisitorMain.getRow.get(column);
	    int col = (column%VisitorMain.colCount.get(row))+1;
	    out.printf("Error: Variable %s was not declared in its scope. Row %d Column: %d\n", variable, row+1, col);
	    return false;
	}
	return true;
    }    
}
