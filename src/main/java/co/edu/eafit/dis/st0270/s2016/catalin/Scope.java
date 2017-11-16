package co.edu.eafit.dis.st0270.s2016.catalin;

import java.util.HashSet;

class Scope extends HashSet<String> {
    private final Scope parent;

    public Scope(Scope parent) {
	this.parent = parent;
    }
    
    public boolean containsVar(String variable) {
	if (super.contains(variable))
	    return true;
	return parent == null ? false : parent.containsVar(variable);
    }
}
