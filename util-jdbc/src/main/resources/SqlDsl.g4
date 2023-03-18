grammar SqlDsl;

@header{package com.antlr.mysql.query;}

AS                              : A S;
SELECT                       : S E L E C T;
FROM                        : F R O M;
TABLE                        : T A B L E;
MAX                         : M A X;
SUM                         : S U M;
AVG                          : A V G;
MIN                          : M I N;
COUNT                     : C O U N T;
ALL                            : A L L;
DISTINCT                  : D I S T I N C T;
WHERE                     : W H E R E;
GROUP                    : G R O U P;
BY                             : B Y ;
ORDER                     : O R D E R;
HAVING                   : H A V I N G;
NOT                          : N O T;
IS                               :  I S ;
TRUE                         : T R U E;
FALSE                        : F A L S E;
UNKNOWN               : U N K N O W N;
 BETWEEN                  : B E T W E E N;
 AND                           :  A N D;
 IN                                :   I N;
 NULL                           : N U L L;
 OR                             : O R ;
 ASC                          : A S C;
 DESC                       : D E S C;
 LIMIT                      : L I M I T ;
 OFFSET                    : O F F S E T;

fragment A      : [aA];
fragment B      : [bB];
fragment C      : [cC];
fragment D      : [dD];
fragment E      : [eE];
fragment F      : [fF];
fragment G      : [gG];
fragment H      : [hH];
fragment I      : [iI];
fragment J      : [jJ];
fragment K      : [kK];
fragment L      : [lL];
fragment M      : [mM];
fragment N      : [nN];
fragment O      : [oO];
fragment P      : [pP];
fragment Q      : [qQ];
fragment R      : [rR];
fragment S      : [sS];
fragment T      : [tT];
fragment U      : [uU];
fragment V      : [vV];
fragment W      : [wW];
fragment X      : [xX];
fragment Y      : [yY];
fragment Z      : [zZ];
fragment HEX_DIGIT:                  [0-9A-F];
fragment DEC_DIGIT:                  [0-9];
fragment LETTER:                         [a-zA-Z];


ID:    ( 'A'..'Z' | 'a'..'z' | '_' | '$') ( 'A'..'Z' | 'a'..'z' | '_' | '$' | '0'..'9' )*;
TEXT_STRING :    (  '\'' ( ('\\' '\\') | ('\'' '\'') | ('\\' '\'') | ~('\'') )* '\''  );
ID_LITERAL:   '*'|('@'|'_'|LETTER)(LETTER|DEC_DIGIT|'_')*;
REVERSE_QUOTE_ID :   '`' ~'`'+ '`';
DECIMAL_LITERAL:     DEC_DIGIT+;


tableName            : tmpName=ID;
column_name            :ID;
function_name            : tmpName=ID ;

 selectStatement:
       SELECT
        selectElements
    (
        FROM tableSources
        ( whereClause )?
        ( groupByCaluse )?
        ( havingCaluse )?
    ) ?
    ( orderByClause )?
    ( limitClause )?
;


 selectElements
    : (star='*' | selectElement ) (',' selectElement)*
    ;


tableSources
    : tableName (',' tableName)*
    ;

whereClause
    : WHERE    logicExpression
    ;

 logicExpression
     : logicExpression logicalOperator logicExpression
     | fullColumnName comparisonOperator value
     | fullColumnName BETWEEN value AND value
     | fullColumnName NOT? IN '(' value (',' value)*  ')'
     | '(' logicExpression ')'
     ;


groupByCaluse
    :   GROUP BY   groupByItem (',' groupByItem)*
    ;
havingCaluse
    :    HAVING  logicExpression
   ;

 orderByClause
    : ORDER BY orderByExpression (',' orderByExpression)*
    ;

 limitClause
    : LIMIT
    (
      (offset=decimalLiteral ',')? limit=decimalLiteral
      | limit=decimalLiteral OFFSET offset=decimalLiteral
    )
    ;

orderByExpression
    : fullColumnName order=(ASC | DESC)?
    ;



groupByItem
    : fullColumnName order=(ASC | DESC)?
    ;

logicalOperator
    : AND | '&' '&'  | OR | '|' '|'
    ;

comparisonOperator
    : '=' | '>' | '<' | '<' '=' | '>' '='
    | '<' '>' | '!' '=' | '<' '=' '>'
    ;


value
    : uid
    | textLiteral
    | decimalLiteral
    ;

decimalLiteral
    : DECIMAL_LITERAL
    ;
textLiteral
    : TEXT_STRING
    ;

selectElement
    : fullColumnName (AS? uid)?      #selectColumnElement
    | functionCall (AS? uid)?               #selectFunctionElement
    ;


fullColumnName
    : column_name
    ;

functionCall
   :  aggregateWindowedFunction     #aggregateFunctionCall
    ;

aggregateWindowedFunction
    : (AVG | MAX | MIN | SUM) '(' functionArg ')'
    | COUNT '(' (starArg='*' |  functionArg?) ')'
    | COUNT '(' aggregator=DISTINCT functionArgs ')'
    ;

functionArg
    :  column_name
    ;

functionArgs
    : column_name (',' column_name)*
    ;

uid
    : ID
    ;



 WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines