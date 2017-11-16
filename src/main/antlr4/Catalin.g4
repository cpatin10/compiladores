grammar Catalin;

// Dirección en la cual crear el lexer
@lexer::header{
package co.edu.eafit.dis.st0270.s2016.catalin;
}
        
// Dirección en la cual crear el lexer
@parser::header{
package co.edu.eafit.dis.st0270.s2016.catalin;
}

// No se recupere de los errores
@parser::members {
@Override
public void notifyErrorListeners(Token offendingToken, String msg, RecognitionException ex) {
    throw new ParserException(offendingToken.getText(), 
                              CatalinParser._SYMBOLIC_NAMES[offendingToken.getType()], 
                              offendingToken.getCharPositionInLine()); 
}
}


//Gramática exprReg

prog      : '['stats']' EOF
          ;

stats     : (stat (';' stat)*)?
          ;

stat      : 'decl' NAME                                              # declStat
          | 'use' NAME expr                                          # useStat
          | '[' stats ']'                                            # statsStat
          | ('ifzero' | 'ifneg' | 'ifpos' | 'ifnzero') NAME stat     # ifStat
          ; 
/*
          | 'ifzero' NAME stat              # ifzeroStat
          | 'ifneg' NAME stat               # ifnegStat
          | 'ifpos' NAME stat               # ifposStat
          | 'ifnzero' NAME stat             # ifnzeroStat
*/

expr      : termino (expr1P)?
          ;

expr1P    : '+' termino (expr1P)?
          ;

termino   : factor (termino1P)?
          ;

termino1P : '*' factor (termino1P)?
          ;

factor    : ('neg')? NUMERO                  # numero
          | NAME                             # name
          ;


LBRACKET : '[';
RBRACKET : ']';
SEPARATOR : ';';
DECLARE : 'decl';
USE     : 'use';
VERZERO : 'ifzero';
VERNEG  : 'ifneg';
VERPOS  : 'ifpos';
VERNZERO : 'ifnzero';
NEGATE  : 'neg';
NAME    : ('a'..'z'|'A'..'Z') ('a'..'z'|'A'..'Z'|'0'..'9'|'_'|'$')*;
NUMERO  : ('0' | ('1'..'9') ('0'..'9')*);
SUMOP   :'+';
MULOP   : '*';
WS  : ( ' '
      | '\t'
      | '\r'
      | '\n'
      | '\f'
      )+ -> skip
      ;
ERROR: . ;
