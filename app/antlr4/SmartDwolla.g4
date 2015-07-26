grammar SmartDwolla;

sendSentence :
     SEND '$'?AMOUNT DOLLARS? TO? ID+
     | SEND ID+ '$'?AMOUNT;

SEND : [sS]'end' ;
AMOUNT : [0-9]+'.'?[0-9]\{0,2} | '.'[0-9]\{0,2} ;
TO : [Tt]'o' ;
DOLLARS : [Dd]'ollar''s'? ;
ID : [a-zA-Z]+;
WS : [\t\r\n' ']+ -> skip ;
