package co.edu.eafit.dis.st0270.s2016.catalin;

import java.lang.Exception;

public class ParserException extends RuntimeException {
    private String token, type;
    private int column;

    public ParserException(String token, String type, int column) {
	super();
	this.token = token;
	this.type = type;
	this.column = column;
    }

    public String getToken() {
	return token;
    }

    public String getType() {
	return type;
    }

    public int getColumn() {
	return column;
    }
}
