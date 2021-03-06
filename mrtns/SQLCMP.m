SQLCMP	;Public;Library of functions for use with SQL
	;;Copyright(c)2003 Sanchez Computer Associates, Inc.  All Rights Reserved - 11/03/03 12:43:01 - SPIER
	; ORIG:  Bob Chiang - 09/23/96
	; 
	; LIBRARY:
	;       . COLLA         Returns file collating code
	;	. SELECT	Returns run-time code for &&SQLSELECT command
	;	. OPEN		Returns run-time code for &&SQLOPEN command
	;	. INSERT	Returns run-time code for &&SQLINSERT command
	;	. UPDATE	Returns run-time code for &&SQLUPDATE command
	;       . DELETE        Returns run-time code for &&SQLDELETE command
	;
	;---------- Revision History ------------------------------------------
	;
	; 06/24/2009 - Sha Mirza - CR 40944
	;	* Modified section UPDATE and DELETE to fix an issue where we receive sqlcnt 
	;	  improperly,and also correcting the issue of sucess for no record update
	;	* Modified RTN and DELETE sections to increment sqlcnt.
	;
	; 06/16/2009 - GIRIDHARANB - CR41317
	;	Modified section update to fix an issue where we were unconditionally
	;	loading ACN table data when updating a child table.
	;
	; 06/03/2009 - Pete Chenard
	;	Modified INSERT to ensure nodes -161 and -162 are defined for
	;	blob and memo columns to prevent underfinedd errors.
	;
	; 05/01/2009 - GIRIDHARANB - CR38852
	;	Modified section delete to flag the record not defined, so an appropriate
	;	sql count is returned when attempting to delete a record that does not
	;	exist.
	;
	; 04/09/2009 - Pete Chenard
	;	Modified UPDATE section to protect the value of 'v' while collating
	;	through the nodes array.  Prior to this, the value of v was being 
	;	inside the for loop, which caused some nodes to be skipped when
	;	generating the node load code.
	;
	; 04/06/2008 - RussellDS - CR30801
	;	Modified RTN section to handle change in filer.  Now
	;	is vSave^RecordTABLE and will always exist.  Added new parameter
	;	vauditlogseq to .save() method calls.
	;
	;	Eliminated PINS section.  This includes elimination of code to
	;	check record is defined. This check will be handled in RecordTABLE
	;	code, which we want to handle it anyway since it will also validate
	;	INSERT access which we want to check first, otherwise, if check for
	;	is defined first creates a security hole with regard to INSERT access.
	;
	;	Eliminated status function - no longer called.
	;
	;	Modified INSERT to remove use of status variable - was set to always
	;	be true.  Removed VALDD checking since that will be handled by
	;	RecordTABLE code.
	;
	;	Eliminated LOOKTBL code and code that called it since no longer
	;	used.  Handled by RecordTABLE code.
	;
	;	Modified statusd to consider if there is access rights associated
	;	with the table.  If so, need to user RecordTABLE calls, not fast
	;	delete.
	;
	; 03/25/08 - GIRIDHARANB - 33055
	;
	;	Modified several section to pass in mode to getNewCode^UCXDD.
	;
	;	Modified section UPDATE to not include M default node in the node array if on rdb.
	;
	;	Modified section UPDATE to pop quotes around node, before calling getDataNode^UCXDD.
	;
	;	Modified section CONVFMT to remove the inline error check for clock dat type.
	;
	;	Modified section RlodWide To add building of -162 level for wide tables on incremental loadin.
	;
	; 01/15/2008 - RussellDS - CR29295
	;	Modified calls to getUpdCode^UCXDD to add new parameter.
	;
	;	Modified calls to getLodCode^UCXDD to set lvpm(-161/-162) for
	;	RDB uses and pass lvpm to getLodCode.
	;
	;	Modified to call getNewCODE^UCXDD to get code to instantiate
	;	a record object.  Added section reset161 and code to call it
	;	for cases where an object is reused, e.g., MULSEL.
	;
	;	Modified section CONVFMT to handle Currency datatypes being filed
	;	filed with a decimal precision. (See CR30963)
	;
	;	Fixed problems in UDPATE section related to loading data into
	;	vobj().  Removed a bunch of commented out code in UPDATE.
	;
	;	Modified PINS section to eliminate code that prevented data
	;	validation on insert.
	;
	;	Eliminated all remaining calls to ^UCUTIL.
	;
	;	Removed old revision history.
	;
	; 10/16/07 - GIRIDHARANB - 29398
	;	    * Modified section INSERT to handle quoted numeric values.
	;	    * Deleted Pre - 2005 revision history.
	;
	; 08/21/07 - GIRIDHARANB - 28382
	;	    * Modified section INSERT to correct handling of currency datatypes.
	;	    * Also included logic to build -150 level for MB datatypes.
	;	    
	; 08/02/07 - Pete Chenard - 28575
	;	    Modified PINS section to call ^SQLFILER if running on Oracle
	;	    database and the table has no filer linked to it.  Also 
	;	    changed to begin collating vobj nodes at 0, rather than 
	;	    values <0.
	;
	; 05/10/07 - GIRIDHARANB - 25455
	;	    Modified section UPDATE to correct error on a MEMO/BLOB
	;	    update. Also fixed an undefined error on rdbvx variable.
	;
	; 05/07/07 - GIRIDHARANB - 26976
	;	    Modified section INSERT to correct undefined error. 
	;
	; 05/01/07 - GIRIDHARANB - 26840
	;	    Modified section UPDATE to correct the case where a 
	;	    MEMO / BLOB value was not being bound correctly.
	;
	; 04/17/07 - GIRIDHARANB - 25850 
	;	    Modified section INSERT to correct errors where the key values 
	;	    were not loaded when computing the nextval/currval values.
	;
	; 03/09/07 - GIRIDHARANB - 25824
	;	    Modified section INSERT to set variable sqldta with the value computed
	;	    for nextval.
	;	    Also removed call to CNTRL^SQL for Memo/Blob datatypes.
	;
	; 02/28/07 - GIRIDHARANB - 25264
	;	     Modified section insert to fix errors with ".CURRVAL" syntax used
	;	     in a sql insert statement.
	;
	; 02/02/07 - GiridharanB - 25163
	;	     Modified section INSERT to correct logic that adds the expression
	;	     to compute NEXTVAL into the exe array.
	;
	; 01/12/07 - GiridharanB - 24840
	;	     Added section RkeyVal to enable returning the value
	;	     of current/nextval key value. Moved call to getUpdcode^UCXDD
	;	     to properly set the -150 level in these cases.
	;
	;	     Modified DELETE to let the filer deal with non-existing 
	;	     records (it is not an error to try to delete a row that doesn't
	;	     exist).
	;
	;----------------------------------------------------------------------
	; I18N=QUIT
	;----------------------------------------------------------------------
COLLA(fid,dolist,exe)	; Return collating code 
	;----------------------------------------------------------------------
	;
	; ARGUMENTS:
	;	. fid		File name		/TBL=[DBTBL1]/MECH=VAL
	;	. dolist	Dispatch code		/TYP=T/MECH=VAL
	;       . exe           Procedural code		/TYP=T/MECH=REFNAM:W
	;
	; EXAMPLE:
	;
	;	D COLLA^SQLCMP("DEP","D abc",.exe)
	;----------------------------------------------------------------------
	;
	N expr,fsn,i,keys,setkey,sn
	D fsn^SQLDD(.fsn,fid)		; File attributes
	S sn=$P($P(fsn(fid),"|",1),"(",1)		; File short name
	S keys=$P(fsn(fid),"|",3)			; Access keys
	S setkey=""
	I keys["," F i=1:1:$L(keys,",") D		; Define access keys
	.	S setkey=setkey_","_$P(keys,",",i)_"=$P(v,$C(9),"_i_")"
	S setkey="S "_$E(setkey,2,9999)
	S expr=keys_" From "_fid			; Select keys only
	;
	D OPEN(expr,999,.exe,1)				; Use cursor 999
	;
	S exe(0.01)=" I '$$vo999() Q"
	S exe(0.02)=" N v F  Q:'$$vf999(.v)  "_dolist
	S exe(0.03)=" Q"
	Q
	;----------------------------------------------------------------------
SELECT(expr,tag,exe)	; Private; Compile SQL SELECT statement into MUMPS code
	;----------------------------------------------------------------------
	;
	; ARGUMENTS:
	;	. expr		SQL SELECT expression	/TYP=T/MECH=VAL
	;	. tag		Routine Tag name	/TYP=N/DFT=1/MECH=VAL
	;	. exe		Procedural code		/TYP=T/MECH=REFNAM:W
	;
	; RETURNS:
	;	. ER		Error indicator
	;	. RM            Error message
	;
	; EXAMPLE:
	;	D SELECT("CID,BAL FROM DEP WHERE CID=:CID",5,.exe)
	;
	; Returns: exe()
	;
	;v0005(vc) ; CID,BAL FROM DEP WHERE CID=:CID
	; N v,vd,v1,v2,v3
	; S v="",vc=0
	; S v1=$G(CID) I v1="" S ER=1,RM=$$^MSG(8592,":CID") Q ""
	; I '$D(^ACN(v1)) Q ""
	; S v2=$G(^ACN(v1,50))
	; I '($P(v2,"|",2)="D") Q ""
	; S v3=$G(^ACN(v1,51))
	; S vd=v1_$C(9)_$P(v3,"|",1)
	; S vc=1
	; Q vd
	;----------------------------------------------------------------------
	N (%LIBS,ER,RM,expr,expr1,tag,exe,zexpr)
	K exe
	S par("DQMODE")=1				; Define DQMODE
	S par("CACHE")=0				; Disable CACHE option
	S $P(line,"-",70)="",line=" ;"_line
	I expr["..." S expr1=$G(expr1)_$P(expr,"...",1) Q  ; Long expression
	I $D(expr1) S expr=expr1_expr k expr1
	I '$G(tag) S tag="vf001"
	E  S tag="vf"_$E(1000+tag,2,4)			; Base routine tag
	D HOSTVAR(expr) Q:ER				; Invalid host variable
	K exe
	D SELECT^SQL(expr,.par,,,,,,-1)			; Parse expression
	I ER K exe D  Q
	.	S exe(1)=line
	.	S exe(2)=tag_"(vc) ; Select "_$E(expr,1,512)
	.	S exe(3)=line
	.	S exe(4)=" ; "_RM
	.	S exe(5)=" Q 0"
	.	S zexpr=expr Q  			; SQL syntax error
	S newvar=$$CONV(.exe)				; Replace vsql(n)
	S kw="S vsql=",zsqlerr=0
	F i=1:1 Q:'$D(exe(i))  D			; Scan for keyword 'vsql="
	.	D SQLCHK
	.	S exe(i)=" "_exe(i)
	.	I exe(i)["v0=100" S exe(i)=" ;"
	.	I exe(i)'[kw Q
	.	S code=$p(exe(i),kw,1)			; Procedural code
	.	S flag=$P(exe(i),kw,2)			; Brach or quit condition
	.	I flag=-1 S exe(i)=code_"Q """"" Q      ; Change 'vsql=-1' to 'Q v'
	.	S exe(i)=code_"G "_tag_(flag+1)		; Change 'vsql=n" to ' G vsqln'
	.	S exe(flag+.5)=tag_(flag+1)_" ;"	; Insert line tag
	S exe(i+1)=" S vc=1 Q vd"
	S exe(i+2)=line
	S exe(0.5)=line
	S exe(0.6)=tag_"(vc) ; Select "_$E(expr,1,512)
	S exe(0.7)=line
	S exe(0.8)=" N vd,"_newvar
	S exe(0.9)=" S vc=0"
	I zsqlerr D SQLERR 		; *** debug mode
	Q 
SQLCHK	; 
	I exe(i)["$O(^XCLS" S zsqlerr=1 Q                                               ; $O(^XCLS( or $O(^XCLS 
	I exe(i)["$O(^ACN(" I $P($P(exe(i),"$O(^ACN(",2),",1)",1)'["," S zsqlerr=1 Q    ; $O(^ACN(key)) 
	Q 
SQLERR	; 
	I expr[":VZ" Q 
	W !,"*** Please review following SQL statement ***" 
	W !!," SELECT ",expr,!! 
	N i S i="" F  S i=$O(exe(i)) Q:i=""  W !,?5,exe(i) 
	W !! 
	Q 
	;----------------------------------------------------------------------
OPEN(expr,tag,exe,noopt)	; 
	;----------------------------------------------------------------------
	;
	; ARGUMENTS:
	;	. expr		SQL SELECT expression	/TYP=T/MECH=VAL
	;	. tag		Routine Tag name	/TYP=N/DFT=1/MECH=VAL
	;	. exe		Procedural code		/TYP=T/MECH=REFNAM:W
	;       . noopt         suppress optimization	/TYP=L/NOREQ/DEF=0
	;
	; RETURNS:
	;	. ER		Error indicator
	;	. RM            Error message
	;
	; EXAMPLE:
	;	D $$OPEN("CID,BAL FROM DEP WHERE CID>:CID",5,.exe)
	;----------------------------------------------------------------------
	;
	; Returns: exe()
	;
	N (%LIBS,ER,RM,expr,tag,exe,noopt,expr1,zexpr)
	S par("DQMODE")=1				; Define DQMODE
	S par("CACHE")=0				; Disable CACHE option
	I $G(noopt) S par("OPTIMIZE")=0			; No optimization
	S $P(line,"-",70)="",line=" ;"_line
	K exe 
	I expr["..." S expr1=$G(expr1)_$P(expr,"...",1) Q  ; Long expression
	I $D(expr1) S expr=expr1_expr k expr1
	D HOSTVAR(expr) Q:ER				; Invalid host variable
	D SELECT^SQL(expr,.par,,,,,,-1)			; Parse expression
	;
	S tago="vo"_$E(1000+tag,2,4)			; Base routine tag
	S tagf="vf"_$E(tago,3,5),tagc="vc"_$E(tago,3,5) ; Fetch & Close tags
	I ER D  Q					; OPEN syntax error
	.	K exe
	.	S exe(1)=line		
	.	S exe(2)=tago_"() ; Open cursor "_tag_" as Select "_$E(expr,1,180)
	.	S exe(3)=line
	.	S exe(4)=" ; "_RM
	.	S exe(5)=" Q 0"
	.	S zexpr=expr Q				; SQL syntax error
	;
	S kw="S vsql="
	S ptr=$G(vsql("P"))
	S bkey=tago_vsql(0)
	S exe(vsql(0)-.5)=bkey_" ;"			; Last collating key
	S exe(ptr+.5)=" S vsql(0)=1 Q"			; Quit for next fetch
	S exe(ptr+.6)=tagf_"(vd) ; ----- Fetch entry point"
	S exe(ptr+.7)=" I '$G(vsql(0)) S vd="""" Q 0"	; End of table
	S single=0,killtmp=""
	F i=1:1 Q:'$D(exe(i))  D			; Scan for keyword 'vsql="
	.	S exe(i)=" "_exe(i)			; Add space for tag
	.	;					; 10/06/98 BC
	.	I exe(i)["^DBTMP(%TOKEN," D 		; Replace %TOKEN,sqlcur with $J,@sqlcur
	..		S exe(i)=$P(exe(i),"%TOKEN,sqlcur",1)_"$J,"_tag_$P(exe(i),"%TOKEN,sqlcur",2,99)
	..		S killtmp=" K ^DBTMP($J,"_tag_")"
	.	I exe(i)["vsql(0)=100" S exe(i)=" ;",single=1 Q
	.	I exe(i)'[kw Q				; vsql= keyword
	.	S code=$p(exe(i),kw,1)			; Procedural code
	.	S flag=$P(exe(i),kw,2)			; Brach or quit condition
	.	I flag=-1 S exe(i)=code_"D "_tagc_" Q" Q  ; Change 'vsql=-1'
	. 	I flag'=+flag,$e(flag,1,2)="$S" do  quit
	.. 		set flag=$p(flag,"(",2,99)
	.. 		set flag=$e(flag,1,$l(flag)-1)
	.. 		N expr,ptr,snippet,tok
	.. 		S expr=$$TOKEN^%ZS(flag,.tok)
	.. 		S ptr=0
	.. 		for  S snippet=$$ATOM^%ZS(flag,.ptr,",:",.tok)  D  Q:ptr=0
	... 			I snippet="," S code=code_"  " Q
	... 			I snippet=":" Q
	... 			;default
	... 			I snippet=1,$$ATOM^%ZS(flag,ptr,",:",.tok)=":"  s code=code_"D "_tagc_" Q",ptr=0 Q
	... 			I snippet=+snippet S code=code_tago_(snippet+1),exe(snippet+.5)=tago_(snippet+1) Q
	... 			S code=code_"G:"_snippet_" "
	.. 		S exe(i)=code
	.	S exe(i)=code_"G "_tago_(flag+1)	; Change 'vsql=n" to ' G vsqln'
	.	S exe(flag+.5)=tago_(flag+1)_" ;"	; Insert line tag
	;
	S newvar=$$CONV(.exe,"v"_$E(1000+tag,2,4))	; vsql(n) -> vtagn
	;
	S exe(0.1)=line					; OPEN cursor
	S exe(0.12)=tago_"() ; Open cursor "_tag_" as Select "_$E(expr,1,180)
	S exe(0.13)=line
	S exe(0.14)=" ;"
	I killtmp'="" S exe(0.141)=killtmp		; Delete temporary table 10/06/98 BC
	S exe(0.15)=" D "_tagc_","_tago_"a"_" Q $G(v"_$E(1000+tag,2,4)_"0)"
	S exe(0.16)=tago_"a ;"
	I 'single S exe(i+1)=" D "_bkey_" Q 1"		; Get next access keys
	E  S exe(i+1)=" D "_tagc_" Q 1"
	S exe(i+2)=tagc_" ; Close cursor"
	S exe(i+3)=" K "_newvar_" Q"			; Close cursor
	S exe(i+9)=line
	Q
	;----------------------------------------------------------------------
CONV(in,var)	; Convert vsql(n) array references into single variable name 
	;----------------------------------------------------------------------
	; ARGUMENTS:
	;	. in	Input array		/TYP=T/REQ/METH=REFARRY:RW
	;	. var   Substitution varibale	/TYP=T/DEF="v"/METH=VAR
	;
	; RETURNS:
	;
	;	. $$	Variable list
	;
	; EXAMPLES:
	;
	;    S exe(1)="vsql(1)+vsql(2)>2"
	;    S exe(2)="vsql(1)>vsql(3)"
	;
	;    S varlist=$$CONV(.exe,"v")
	;
	;    Returns: 
	;
	;	varliat="v1,v2,v3"
	;       exe(1)="v1+v2>2"
	;       exe(2)="v1>v3"
	;----------------------------------------------------------------------
	N kw,kw1,n,new,seq,sql1,x
	S kw="vsql("
	I $G(var)="" S var="v"                          ; Sustitution variable
	S n="" F  S n=$O(in(n)) Q:n=""  D
	.	S x=in(n)
	.	F  Q:x'[kw  D
	..		S seq=$P(x,kw,2)                ; Array sequence
	..		S seq=$P(seq,")",1)
	..		S seq1=$$QSUB^%ZS(seq)          ; pc 11/5/02
	..		S kw1=kw_seq_")"                ; New name
	..		S x=$P(x,kw1,1)_var_seq1_$P(x,kw1,2,9999)       ; Replace
	..		S new(seq1)=""                  ; Save variable name
	.	S in(n)=x
	S new="",n="" F  S n=$o(new(n)) Q:n=""  S new=new_","_var_n
	Q $E(new,2,9999)
	;----------------------------------------------------------------------
HOSTVAR(expr)	; Validate host variable syntax (uppercase name only)
	;----------------------------------------------------------------------
	;
	; ARGUMENTS:
	;
	;	.expr	SQL SELECT/OPEN expression	/TYP=T/REQ/MECH=VAL
	;
	; RETURNS:
	;
	;	. ER	Error indicator
	;	. RM	Error message
	;
	;----------------------------------------------------------------------
	N i,v
	S ER=0
	I expr'[":" Q
	F i=1:1:$L(expr,":")-1 D  Q:ER
	.	S v=$P(expr,":",i+1),v=$P(v," ",1)	; expr...:var
	.	I v="" Q
	.	I (v?1U.U)!(v?1U.UN) Q			; All uppercase
	.	I v?1"%".U Q
	.	S ER=1,RM=$$^MSG(8592,v)		; Invalid host variable
	Q
	;----------------------------------------------------------------------
INSERT(expr,tag,exe,cache)	; table (list) values (list) 
	;----------------------------------------------------------------------
	;
	; ARGUMENTS:
	;	. expr		SQL INSERT expression	/TYP=T/MECH=VAL
	;	. tag		Routine Tag name	/TYP=N/DFT=1/MECH=VAL
	;	. exe		Procedural code		/TYP=T/MECH=REFNAM:W
	;       . cache         Store parsed code in cache
	;                       called by $$^SQL routine
	;						/TYP=L/NOREQ/MECH=VAL
	; RETURNS:
	;	. ER		Error indicator
	;	. RM            Error message
	;
	; EXAMPLE:
	;	D $$INSERT("UTBLBRCD (BRCD,DESC) VALUES (:XBRCD,:XDESC)",5,.exe)
	;
	;----------------------------------------------------------------------
	N akey,cnt,dec,di,dinam,fid,fmtflg,fsn,hostvar,i,key,keyflg,len,lenchk,line
	N list,mdbx,newvar,nod,nul,nods,obj,ov,pat,pos,pslcol,q,isRdb,relcol,reltbl,rectyp
	N reqdi,rtn,rdbx,rdbvx,seq,tblDes,typ,v,value,var,vdd,vkw,vmap,vnextval,vpos,vsub,x
	N wlist,z,zexe,znxval
	;
	K exe,RM 
	S $P(line,"-",70)="",line=" ;"_line,q="""",ER=0,RM=""
	S newvar="",lenchk="",znxval=0,par=$G(par)
	; define keywords (filer and PSL) 04/28/99
	;
	do getKeyword(.vkw)
	;
	S ER=0  						; Parameter
	I expr[$C(9) S par=$P(expr,$C(9),2),expr=$P(expr,$C(9),1) 
	S par=$$initPar^UCUTILN(par)
	;
	I expr["..." S expr1=$G(expr1)_$P(expr,"...",1) Q  	; Long expression
	I $D(expr1) S expr=expr1_expr k expr1
	;
	S fid=$P(expr," ",1)
	I fid["INTO" S fid=$P(expr," ",2)		; Remove INTO keyword
	I fid["(" S fid=$P(fid,"(",1)			; Permit table_name(column_list)
	;
	S isRdb=$$rdb^UCDB(fid)				; Non-M database
	S list=$P($P(expr,"(",2),")",1)			; Column list
	I list="" S ER=1,RM=$$^MSG(1286,"INSERT "_expr) Q  ; Missing columns
	S value=$P(expr,"(",3,999)			; Column values	
        S value=$P(value,")",1,$L(value,")")-1)
	S seq=10
	;
	D fsn^SQLDD(.fsn,fid) Q:ER			; File attributes
	S rectyp=$P(fsn(fid),"|",4)			; Record type
	S key=$P(fsn(fid),"|",3)			; Access key
	I key'="" F i=1:1:$L(key,",") S akey($P(key,",",i))=""
	;
	S obj=$$OBJNAME(fid)				; Object name
	;
	S exe(seq)=" N vobj,"_obj,seq=seq+1
	S tblDes=$$getPslTbl^UCXDD(fid,1)
	S exe(seq)=$$getNewCode^UCXDD(tblDes,obj,0,0)
	S seq=seq+1
	;
	S cnt=$L(list,",")				; column count
	F i=1:1:cnt D  I ER Q
	.	I i=cnt S v=$$TRIM^%ZS($P(value,",",i,999999)) ; Last column value (blob logic)
	.       E  S v=$$TRIM^%ZS($P(value,",",i))      ; Column value 
	.	I v[$C(0) S v=$$UNTOK^%ZS(v,.tok)
        .       I $L(v)<50,v'="",$D(vkw(v)) S v=":"_vkw(v)       ; System keyword
	.	I v="''" Q				; Null value
	.	I v="NULL" Q
 	.	S fmtflg=0
	.	;
	.	S di=$$TRIM^%ZS($P(list,",",i)) 	; Column name
	.	I di[$C(0) S di=$$UNTOK^%ZS(di,.tok)	; Quoted column name
	.	I v="" Q
	.       S dinam=fid_"."_di	                ; table.column format
	.	S typ=$$TYP^SQLDD(dinam,,.vdd)  	; Format type
        .       I v[".NEXTVAL"!(v[".CURRVAL")
	.	E  I $L(v)<50,v'="",'$d(vkw(v)) I (typ="T"&($E(v)'="'")&($E(v)'=":"))!(v["_")!(v?1A.ANP) S v="'"_v_"'"	; Default as string data  6/9/00 mas
	.       I $E(v)=":" D 
	..		S v=$$KWMAP($E(v,2,99),.vkw)	; Host variable
	..              I '((v?1A.AN)!(v?1"%".AN)!(v?1A.AN1"("1N.E1")")) Q      ; Not simple variable name
	..              S exe(seq)=" I '$D("_v_") S ER=1,RM=$$^MSG(8592,"_q_v_q_") Q",seq=seq+1 
	.	;
	.       I " "'[$$CMP^SQLDD(dinam,,.vdd) S ER=1,RM=$$^MSG(7914)_" "_dinam Q	; Computed data item
	.	I ER Q					; Invalid data item
        .       I "BM"'[typ,v["|" S ER=1,RM=$$^MSG(1350,dinam) Q
	.	I $E(v)="'" D				; convert string data
	..		S v=$$QSUB^%ZS(v,"'")		; Remove one layer of '
	..		I typ'="B" S v=$$QADD^%ZS(v)	; Add one layer of "
	..		;; I "MB"[typ S v=$$CNTRL^SQL(v)	; Deal with control characters
	.	S dec=$$DEC^SQLDD(dinam,,.vdd)		; Decimal precision
	.	S nod=$$NOD^SQLDD(dinam,,.vdd)          ; Node number
	.	S pos=$$POS^SQLDD(dinam,,.vdd)		; Position
 	.       S pat=$$PTN^SQLDD(dinam,,.vdd)          ; Pattern check
	.	S len=$$LEN^SQLDD(dinam,,.vdd)		; Length
        .       I isRdb do
        ..              if nod'["*" s nod=""
        ..              S typ=$$TYP^SQLDD(dinam,,.vdd)          ; Data type
        ..              S nul=$$NUL^SQLDD(dinam,,.vdd)          ; nulls allowed
	..		I $$wide^DBSDBASE(fid),nod'["*" do
	...			S nod=$$nod^DBMAP(%DB,fid,di)
	...			I nod="ORACLE" set nod=""
	...			Q:nod=""
	...			S exe(seq)=" S:'$D(vobj("_obj_","_nod_")) vobj("_obj_","_nod_")=""""",seq=seq+1
        ..              S reltbl=fid,relcol=di                  ; DBMAP
        ..              D MAP^DBMAP(%DB,.reltbl,.relcol,.pos)   ; check
        ..              ;S exe(seq)=" set vobj("_obj_",-150,"""_relcol_""")="""_pos_"|"_nod_"|"_typ_"|"_nul_"|"_reltbl_"|"_di_"""" set seq=seq+1
        ..              ;I $$wide^DBSDBASE(fid),nod["*" do
        ...			;;S wlist=fid
        ...			;;D MAP^DBMAP(%DB,.wlist)
        ...                     ;;for j=1:1:$L(wlist,",") do
        ....                            ;;;S reltbl=$p(wlist,",",j)
        ....				;;;Q:reltbl="ACN"
        ....                            ;;;S exe(seq)=" set vobj("_obj_",-150,"""_reltbl_"."_relcol_""")="""_pos_"|"_nod_"|"_typ_"|"_nul_"|"_reltbl_"|"_di_"""" set seq=seq+1
	.	I pat["," S pat=""			; Bad syntax
	.       S keyflg=0 I nod?1N1"*" S key(di)="",keyflg=1		; Access key
	.	E  I nod'="" S nods(nod)=""		; Regular data item
	.       I v'="",$D(vkw(v)) S v=vkw(v),var(v)="" ; Keyword
	.	I $E(v)=":" S v=$$KWMAP($E(v,2,99),.vkw)	; Host variable
	.	S zexe=""
	.	S hostvar(di)=0
	.	I (v?1A.AN)!(v?1"%".AN) S hostvar(di)=1	; Host variable syntax
	.	E  D  Q:ER
	..		I "N$"[typ,$G(dec) D
	...			S v=$$QSUB^%ZS(v)
	...			I $L(+$P(v,".",2))>dec S ER=1,RM=dinam_" "_$$^MSG("774",$TR($J("",+dec)," ","N")) Q
	...			S v=+v
	..		I v=+v,$L(v)>len S ER=1,RM=$$^MSG(3037,dinam,len) Q
        ..		I v=+v,pat'="" S X=v X ("I ("_pat_")") E  S ER=1,RM=$$^MSG(1421,dinam) Q
	..		I $E(v)="""",$L($$QSUB^%ZS(v))>len S ER=1,RM=$$^MSG(3037,dinam,len) Q
	.       D  I ER Q 
        ..		I v?1a.an!(v?1"%".an) D
        ...                     I pat="" S exe(seq)=" I $L("_v_")>"_len_" S ER=1,RM=$$^MSG(3037,"_q_dinam_q_","_len_") Q",seq=seq+1
	...			I pat'="",v'["X" S exe(seq)=" I '("_$$PATCHK(v,pat)_") S ER=1,RM=$$^MSG(1421,"_q_dinam_q_") Q",seq=seq+1
	..		S var(v)="",ov=v
	..		I v'[".NEXTVAL",v'[".CURRVAL",v'?1A.AN1"."1A.AN D CONVFMT
	.       I $E(v)="'" S v=$TR(v,"'","""")   ; "string"
	.	;
        .       I v?1E.E1".CURRVAL" D  S znxval=1 Q
        ..              I isRdb D
        ...                     S v=$$KEYVAL($P(v,".",1),"C",di)
        ...			S pslcol=$$getPslCln^UCXDD(fid,di)
	...			S rdbx=$$getUpdCode^UCXDD(pslcol,obj,v,0,1),seq=seq+1
	...			S rdbvx=" S sqldta="_v
        ..              E  D
        ...			S v=$$KEYVAL($P(v,".",1),"C",di),fmtflg=1 I $G(ER) Q  ; fid.KEYVALCURRVAL
        ...			;S x=$$PARSE^SQLDD(dinam,,,.fsn,,.vdd,,.vsub)
	...			S pslcol=$$getPslCln^UCXDD(fid,di)
	...			S vnextval=$$getUpdCode^UCXDD(pslcol,obj,v,0,1)
        ...			;S vnextval=" S "_x_"="_v
        ...			S mdbx=" S sqldta="_v
        ..		K akey(di)
        .       I v?1E.E1".NEXTVAL" D  S znxval=1 Q				 ; fid.NEXTVAL (next key value)
        ..              S v=$$KEYVAL($P(v,".",1),"N",di),fmtflg=1
        ..              I $G(ER) Q
        ..              I isRdb d
        ...			S pslcol=$$getPslCln^UCXDD(fid,di)
	...			S rdbx=$$getUpdCode^UCXDD(pslcol,obj,v,0,1),seq=seq+1
	...			S rdbvx=" S sqldta="_v
        ..              E  D
        ...                     ;S x=$$PARSE^SQLDD(dinam,,,.fsn,,.vdd,,.vsub)
	...			S pslcol=$$getPslCln^UCXDD(fid,di)
	...			S vnextval=$$getUpdCode^UCXDD(pslcol,obj,v,0,1)
        ...			S mdbx=" S sqldta="_v
        ..              K akey(di)
	.	I v?1A.AN1"."1A.AN S ER=1,RM="Invalid column value syntax "_v_" in INSERT "_expr Q  ; fid.di syntax
	.	;
	.	I "M,B"[typ D  Q					; Memo field
	..		N lvpm
	..		S lvpm(-161)="vobj("_obj_",-161,"
	..		S lvpm(-162)="vobj("_obj_",-162,"
	..		S vmemo=$E(v,2,$L(v)-1)
	..		I v'?1A.AN S v="vmemo"			; save memo data		
	..		S pslcol=$$getPslCln^UCXDD(fid,di)
	..		S exe(seq)=$$getLodCode^UCXDD(tblDes,obj,"*"_di,0,0,.lvpm),seq=seq+1
	..		S exe(seq)=$$getUpdCode^UCXDD(pslcol,obj,v,0,1),seq=seq+1
	.	;
	.	K fsn D
	..		N fidnq,dinq
	..		S fidnq=$$QSUB^%ZS(fid)
	..		S dinq=$$QSUB^%ZS(di)
	..		S x=$$rtCurExpr^UCXDD(fidnq,dinq,$$OBJNAME(fidnq),0)
	.	S pslcol=$$getPslCln^UCXDD(fid,di)
	.	S exe(seq)=$$getUpdCode^UCXDD(pslcol,obj,v,0,1),seq=seq+1
	.       I ER K code Q 
	.       I x=ov Q 		                ; Access key
	.	I pos S vpos(pos)=v			; Field position
	.	;
	.	I zexe'="" S exe(seq)=zexe,seq=seq+1
	.       ; 
	.	K akey(di)				; Remove it from the list
	;	
	I ER Q
	;
	I '$G(cache) D					; Compiled version
	.	S exe(1)=line
	.	S exe(2)="vi"_$E(1000+tag,2,4)_" ; INSERT "_$E(expr,1,512)_" "_par
	.	S exe(3)=line
	I ER S exe(4)=" ;"_RM,exe(5)=" Q" Q		; Error
	;
	I $O(akey(""))'="" S ER=1,RM=$$^MSG(48,$O(akey("")))_" -> INSERT "_expr Q
	I znxval D
	.	if isRdb do
	..		S exe(seq)=rdbx,seq=seq+1
	..		S exe(seq)=rdbvx,seq=seq+1
	.	else  do
	..		S exe(seq)=vnextval,seq=seq+1   ; Define fid.NEXTVAL value
	..		S exe(seq)=mdbx,seq=seq+1	;Return NEXTVAL.
	I $G(psl) S newvar=newvar_",vfkey"		; Foreign key information (not used by Db insert method)
	I '$G(cache),newvar'="" S exe(4)=" N "_$E(newvar,2,9999)	; Protect variables
	;
	S x=$$RTN(fid,obj,par,.nods,"I")  		; Filer name
	S exe(seq)=x,seq=seq+1
	I $G(cache) Q
	S exe(seq)=" Q"				; Return error status
	Q
CONVFMT ; Convert external format
        ;----------------------------------------------------------------------
        I "TFBM"[typ Q                  ; Data conversion not required
        I "N$"[typ D  Q                 ; Numeric or currency
        .       I v?1a.an!(v?1"%".an) D  Q
        ..              I dec S exe(seq)=" I '(("_v_"?.""-"".N)!("_v_"?.""-"".N1"".""1N.N))"
        ..              E  S exe(seq)=" I "_v_"'?.""-"".N"
        ..              S exe(seq)=exe(seq)_" S ER=1,RM=$$^MSG(1350,"_q_dinam_q_") Q",seq=seq+1
	..		I dec D  Q
	...			S exe(seq)=" I $L(+$P("_v_",""."",2))>"_dec_" S ER=1,RM="""_dinam_" ""_$$^MSG(""774"",$TR($J("""","_(+dec)_"),"" "",""N"")) Q",seq=seq+1
	...			S exe(seq)=" S v=+v",seq=seq+1
        .       I $E(v)=q S v=$E(v,2,$L(v)-1)   ; Constant
        .       I '((v?."-".N)!(v?."-".N1"."1N.N)) S ER=1,RM=$$^MSG(3076,dinam) Q
        ;
        I "DC"[typ D  Q
        .       I v[" " S ER=1,RM=$$^MSG(1350,dinam) Q
        .       I $E(v)=q S v=$E(v,2,$L(v)-1),v=$$INT^%ZM(v,typ) Q	; Internal format
        .       I typ="C",v[":" S v=$$INT^%ZM(v,"C") Q                  ; Convert hh:mm:ss
        .       I v=+v Q
        .       I v'?1a.an Q                    ; not pure variable syntax
	.	S v="$S("_v_"?0.5N:"_v_",1:$$INT^%ZM("_v_","_q_typ_q_"))" Q  ; 0 to 5
        I typ="L" D  Q
        .       I $E(v)=q S v=$E(v,2,$L(v)-1),v=$$INT^%ZM(v,"L","NYFT01") Q
        .       I v?.n,v>0 S v=1 Q
        .       I v?.n,v<1 S v=0 Q
   	.       S v="$$INT^%ZM("_v_","_q_"L"_q_","_q_"NYFT01"_q_")"
 	I typ="U" D
        .       I $E(v)=q,$P(v,q,2)?.UP Q
        .       S v="$S("_v_"?.U:"_v_",1:$$INT^%ZM("_v_","_q_typ_q_"))" Q  ; convert to uppercase
        Q
	;---------------------------------------------------------------------- 
MAPOPT()	;
	I rectyp=1 Q 1					; remap for type 1 file
	I v?1A.AN!(v?1A.AN1"+".e)!(v?1A.AN1"-".e)!(v?1N.N)!(v?.N1".".N),keyflg Q 1
	Q 0
	;----------------------------------------------------------------------
KEYVAL(fid,opt,di)	; Get current key value or next key value 
	;----------------------------------------------------------------------
	; ARGUMENTS:
	;
	;	fid	File name			/REQ/TYP=T/REF=VAL
	;       opt     Option C=current N=next		/REQ/TYP=T/REF=VAL
	;	di      Column name			/REQ/TYP=T/REF=VAL
	;
	; RETURN:
	;
	;	$$	Procedural code
	;
	; EXAMPLE:
	;
	;       $$KEYVAL("DEP","N")   returns  $O(^ACN(""),-1)+1
	;       $$KEYVAL("HIST","C")  returns  $O(^HIST(CID,""),-1)
	;----------------------------------------------------------------------
	N gbl,i,keys,q,rfid,rdi,sql1,whr
	I "NDC$"'[typ S ER=1,RM=$$^MSG(8564,v)_" - "_$$^MSG(1350) Q ""	; Invalid type
	S q=""""
	I '$D(fsn(fid)) D fsn^SQLDD(.fsn,fid)  		; File attribute
	S keys=$P(fsn(fid),"|",3)			; Access keys
	I $P(keys,",",$L(keys,","))'=di S ER=1,RM=$$^MSG(8564,v) Q ""	; Not the bottom key
        S rdb=$$rdb^UCDB(fid)
        I rdb D
        .       set (rfid,rlfid)=fid,rdi=di,whr="",z=""
        .       d MAP^DBMAP(%DB,.rfid,.rdi)
        .       for i=1:1:$l(keys,",") d
        ..              set rkey=$p(keys,",",i)
        ..              Q:rkey=di
        ..              set typ=$$TYP^SQLDD(fid_"."_rkey)
        ..              D MAP^DBMAP(%DB,.rlfid,.rkey)
        ..		set z=z_rkey_"="_$S("TU"[typ:"'""_vobj("_obj_",-"_(i+2)_")_""'",1:"""_vobj("_obj_",-"_(i+2)_")_""")_" AND "
        .       I opt="C" D
        ..              I z'="" set sql1="SELECT MAX("_rdi_") FROM "_rfid_" WHERE "_$E(z,1,$l(z)-4)
        ..              E  S sql1="SELECT MAX("_rdi_") FROM "_rfid
        .       E  D
        ..              I z'="" S sql1="SELECT MAX("_rdi_")+1 FROM "_rfid_" WHERE "_$E(z,1,$l(z)-4)
        ..              E  S sql1="SELECT MAX("_rdi_")+1 FROM "_rfid
        .	S gbl="$$RkeyVal^SQLCMP("""_sql1_""","""_opt_""")"
        I rdb Q gbl
	S gbl=$P(fsn(fid),"|",2)  			; Global reference
	I gbl'["," S gbl=$p(gbl,"(",1)_"("_q_q_")"  	; Single level key
	E  S gbl=$P(gbl,",",1,$L(gbl,",")-1)_","_q_q_")" ; Multiple level
	S gbl=gbl_",-1)"	  			; Current value
	I opt="N" S gbl=gbl_"+1"  			; Next value
	S gbl="$O("_$$KEYOBJ(gbl,obj)
	Q gbl
	;----------------------------------------------------------------------
RkeyVal(expr,option)	; returns current or next highest key vale in an rdb.
	;----------------------------------------------------------------------
	N vEr,vRm,vd
	S vEr=$$SELECT^%DBAPI(0,expr,$C(124),"",.vd,.vRm)
	I option="N",vd="" set vd=1
	Q vd
	;----------------------------------------------------------------------
UPDATE(expr,tag,exe,cache,onerow)	; 
	;----------------------------------------------------------------------
	; ARGUMENTS:
	;	. expr		SQL UPDATE expression	/TYP=T/MECH=VAL
	;	. tag		Routine Tag name	/TYP=N/DFT=1/MECH=VAL
	;	. exe		Procedural code		/TYP=T/MECH=REFNAM:W
	;       . cache         Store parsed code in cache /TYP=L/NOREQ/MECH=VAL
	;	. onerow	Single row update	 /TYP=L/NOREQ/MECH=VAL
	;						
	; RETURNS:
	;	. ER		Error indicator
	;	. RM            Error message
	;
	; EXAMPLE:
	;	D UPDATE("UTBLBRCD SET DESC=:ZDESC WHERE BRCD=:ZBRCD",5,.exe)
	;
	;----------------------------------------------------------------------
	;
	N audit,dec,defnod,del,di,dinam,fid,filer,fmtable,fsn,gbl,gblref,hostvar,i,j
	N isRdb,input,keys,lcnt,len,line,list,new,newlist,nod,nodes,nseq,nul,nv,obj,parent,pos,pslcln,q,req,Rkeys,rtn
	N seq,set,sfd,tbl,tbldesc,typ,v,v1,v2,val,vdd,vdi,vfid,vinp,vkw,vout,vsub,whr,wide,xpos
	N z,z1,z2,zexe,zexpr,zdel,zpos,zval,cmp
	N FILES,KILFLG,LOOP,MPLCT,Z
	S onerow=$G(onerow)	;pc 5/10/02
	;
	K exe,RM
	S seq=1,q="""",new="",ER=0
	S $P(line,"-",70)="",line=" ;"_line
	S audit=$g(cache) I $G(par("SYSTEM")) S audit=""	; System/User generated flag
	;
	; *** define keyword table
	do getKeyword(.vkw)
	;
	S ER=0,par=""  						; Parameters
	I expr[$C(9) S par=$P(expr,$C(9),2),expr=$P(expr,$C(9),1)
	;
	I expr["..." S expr1=$G(expr1)_$P(expr,"...",1) Q  ; Long expression
	I $D(expr1) S expr=expr1_expr k expr1
	S zexpr="UPDATE "_expr
	S fid=$P(expr," ",1)				; Table name
	I fid="" S ER=1,RM=$$^MSG(1477,expr) Q  	; Invalid syntax
	D fsn^SQLDD(.fsn,fid) I ER Q  			; Invalid table name
	S isRdb=$$rdb^UCDBRT(fid)			; Non M databases
	S gbl=$P(fsn(fid),"|",2)			; Global reference
	S filer="vSave^Record"_fid                      ; Filer name 
	S keys=$P(fsn(fid),"|",3)			; Access keys
	S defnod=$P(fsn(fid),"|",12)			; Record Exists node
	S list=$P($P(expr," SET ",2,99)," WHERE ",1)  	; Update list
	I $E(list)="(" S list=$E(list,2,$L(list)-1)	; Remove parens
	S whr=$P(expr," WHERE ",2)			; Query
	S wide=$$wide^DBSDBASE(fid)			; rdb Wide table
	S tbldesc=$$getPslTbl^UCXDD(fid,0)
	S obj=$$OBJNAME(fid)
	S parent=$P(^DBTBL("SYSDEV",1,fid,10),"|",4)
	;
	I whr'="" S whr=$$WHERE(fid,whr,.newlist)	; Check access keys
	I ER Q
	;
	F lcnt=1:1:$L(list,",") D  I ER Q
	.	S set=$P(list,",",lcnt)			; column = value
	.	S di=$$TRIM^%ZS($P(set,"=",1))		; column name
        .       I di[$C(0) S di=$$UNTOK^%ZS(di,.tok)
        .       I $E(di)="""" S di=$P(di,"""",2)        ; Remove quotes
	.	I di?1a.an1"."1a.an S di=$P(di,".",2)	; Remove table name
	.	I di="" S ER=1,RM=$$^MSG(1477) Q	; Missing?
	.	I '$$VER^SQLDD(fid,di) S ER=1,RM=$$^MSG(1300,di) Q  ; Invalid name for this table
	.	S dinam=fid_"."_di
	.	S vinp(dinam)=""  			; Save fid,di list
	.	S nv=$$PARSE^SQLDD(dinam,,,.fsn,,.vdd,,.vsub) I ER Q	; Internal vobj() format
	.	S typ=$$TYP^SQLDD(dinam,,.vdd)		; Format type
	.	I " "'[$$CMP^SQLDD(dinam) S ER=1,RM=$$^MSG(7914)_" "_dinam Q  ; Computed data item
	.	S dec=$$DEC^SQLDD(dinam,,.vdd)		; Decimal precision
	.	S len=$$LEN^SQLDD(dinam,,.vdd)		; length
	.	S nod=$$NOD^SQLDD(dinam,,.vdd)		; node
	.	S pos=$$POS^SQLDD(dinam,,.vdd)		; position
	.	S tbl=$$TBL^SQLDD(dinam,,.vdd)		; lookup table
	.	S sfd=$$SFD^SQLDD(dinam,,.vdd)		; Sub-field
	.	S nul=$$NUL^SQLDD(dinam,,.vdd)		; null indicator
	.	S val=$$TRIM^%ZS($p(set,"=",2))		; column value
	.	S req=$$REQ^SQLDD(dinam,,.vdd)		; Required column
	.	S pslcln=$$getPslCln^UCXDD(fid,di)	; PSL column descriptor
	.	I val[$c(0) S val=$$UNTOK^%ZS(val,.tok)
	.	I $L(val)<50,val'="",$D(vkw(val)) S val=":"_vkw(val)	; System keyword
        .       I "BM"'[typ,val["|" S ER=1,RM=$$^MSG(1350,dinam) Q
        .       I val="" S ER=1,RM=$$^MSG(8564,expr) Q  ; PFW bug SET col=
	.	I ",$,N,L,"[(","_typ_","),($E(val)="'"),(val'="''") S val=$$QSUB^%ZS(val,"'")  ;remove single quotes if numeric field
        .       I val=+val,$L(val)>len S ER=1,RM=$$^MSG(3037,dinam,len) Q       ; Length error
	.	I val=+val,dec,$L(+$P(val,".",2))>dec S ER=1,RM=dinam_" "_$$^MSG("774",$TR($J("",+dec))," ","N") Q
	.	I val="NULL" S val="''"			; Convert NULL keyword to ''
	.	I val="''",req S ER=1,RM=$$^MSG(7531,dinam) Q
	.	S zval=val
	.	I $E(val)="'" D				; convert string data
	..		S val=$$QSUB^%ZS(val,"'")	; Remove one layer of '
	..		I typ="B"!(typ="M") S vmemo=val Q	; use raw data
	..		S val=$$QADD^%ZS(val)		; Add one layer of "
	.	E  S val=$$VALUE(fid,val,.vinp)         ; Convert to internal
	.	I ER Q
        .       I $E(val)="""",$L($$QSUB^%ZS(val))>len S ER=1,RM=$$^MSG(3037,dinam,len) Q  ; Length error
	.	D
	..		I "BM"[typ D  Q
	...			N gblref,i,j,lvpm,v,z
	...			I isRdb D
	....				S lvpm(-161)="vobj("_obj_",-161,"
	....				S lvpm(-162)="vobj("_obj_",-162,"
        ...			S v="vmemo" I $E(zval)=":" S v=$E(zval,2,99)
	...			s exe(500+seq)=$$getLodCode^UCXDD(tbldesc,obj,"*"_di,1,0,.lvpm),seq=seq+1
        ...			S exe(500+seq)=$$getUpdCode^UCXDD(pslcln,obj,v,1,1),seq=seq+1
	..		S v1=val,v2=""
	..		D	
	...			I val="""""" Q
	...			I "DC"[typ,val?0.5N Q	; internal format
	...			I typ="L","01"[val Q	; conversion not required
	...			I "TF"[typ,$e(val)="""",$L(val)'>len Q
	...			I typ="U",val'?1A.AN,val?.U Q
	...			I "ULDC"[typ S v1="v",v2=" S "_v1_"="_val,new="ER,v"	; map to v1,v2,...
	...			S val=v1
	..		;
	..		S zexe=""
	..		I "TFN$"[typ,v1?1A.AN S zexe=v2_" I $L("_v1_")>"_len_" S ER=1,RM=$$^MSG(3037,"_""""_dinam_""""_","_len_") Q" Q
	..              I "DC"[typ,v2'="" S zexe=v2_" S "_v1_"=$S("_v1_"?0.5N:"_v1_",1:$$INT^%ZM("_v1_","_""""_typ_""""_")) I $G(ER) Q" Q 
	..              I typ="L",v2'="" S zexe=v2_" S "_v1_"=$S(""01""["_v1_":"_v1_",1:$$INT^%ZM("_v1_","_""""_typ_""""_")) I $G(ER) Q" Q 
	..              I typ="U",v2'="" S zexe=v2_" I "_v1_"'?.U S "_v1_"=$$INT^%ZM("_v1_","_q_typ_q_")" Q
	.	S vfid=""""_fid_"""",vdi=""""_di_"""",del=""""_"|"
	.	;
	.	I nod'=+nod S nod=""""_nod_""""
	.	I "BM"[typ Q
        .       I nod["*" S xpos="000"
        .       E  S xpos=$e(1000+pos,2,4)
	.	I nv'["$$GET" D
	..		S nod=$$rtOldNode^UCXDD(fid,di,1)
	..		if isRdb D
	...			
	...			; Need to define level -150 for columns that changed values on RDB
	...			N rdi,rnod,reltbl
	...			I nod["*",nod'="0*" S Rkeys(di)=""	;track keys that have changed
	...			S reltbl=fid,rdi=di
	...			D MAP^DBMAP(%DB,.reltbl,.rdi,.pos)
	...			S (wnod,rnod)=$$nod^DBMAP(%DB,fid,di)
	...			I wide D
	....				I wnod="" S wnod=parent
	....				S winp(wnod)=""
	..		S fmtable=$$FMTABLE^DBSMACRO(dinam)
	.	;
	.	E  D  Q		; Sub-field structure type_pos_data|audit|sub field delimiter_position
	..		S nod=$$rtOldNode^UCXDD(fid,di,1)
	..		S nv=$$getCurExpr^UCXDD(pslcln,obj,0)
	..		S exe(1000+seq-0.1)=" "_$$getUpdCode^UCXDD(pslcln,obj,val,1,0)
	..		S seq=seq+1
	.	I zexe'="" S exe(999.5+seq)=zexe
	.	; Track nodes we need to load
	.	S nod=$$rtOldNode^UCXDD(fid,di,1)
	.	I $E(nod,$L(nod))'="*" S nodes(nod)=""		; Ignore top and keys
	.	S exe(1000+seq)=$$getUpdCode^UCXDD(pslcln,obj,val,2,1)
	.	S seq=seq+1
	;
	I '$G(cache) D
	.	S exe(1)=line 
	.	S exe(2)="vu"_$E(1000+tag,2,4)_" ; UPDATE "_$E(expr,1,512)_" "_par 
	.	S exe(3)=line 
	I ER S exe(4)=" ; "_RM,exe(5)=" Q" Q  ; Error condition	; MJZ - 40944
	S exe(4)=" N vobj,"_obj
	I 'onerow S exe(4)=exe(4)_",vfkey"
	S i="" F j=2:1 S i=$O(hostvar(i)) Q:i=""  S exe(j/100+4)=" I $G("_i_")="_""""""_" S ER=1,RM=$$^MSG(8592,"_""""_i_""""_") Q" 
	; 
	I whr="",keys'="" D MULSEL(fid,tag,1,10)
	;
	S seq=$O(exe(400),-1)\1+1 			; Next sequence
	S v="" F  S v=$O(var(v)) Q:v=""  I $D(key(v)) K key(v) 
	I whr="",keys'="" S exe(seq)="vu"_$E(1000+tag,2,4)_"M ;",seq=seq+1
	;
	I whr="",keys'="" D
	.	S exe(seq+2)=" S vobj("_obj_",-2)=1"
	.	I isRdb S exe(seq+2)=exe(seq+2)_" D reset161^SQLCMP("_obj_")"
	E  S exe(seq+2)=$$getNewCode^UCXDD($$getPslTbl^UCXDD(fid,1),obj,1,0)
	S nseq=seq+3
	;
        I isRdb do
	.	N wnod,rwnod S (rwnod,wnod)=""
	.	F  S wnod=$O(winp(wnod)) Q:wnod=""  D
	..		set rwnod=rwnod_","_wnod
	.	set rwnod=$e(rwnod,2,99)
        .	I whr'="" S exe(nseq)=whr,nseq=nseq+1
	.	I wide D
	..		s exe(nseq)=" S winp="""_rwnod_"""",nseq=nseq+1
	..		S exe(nseq)=" if $D(winp) D RLodWide^SQLCMP(."_obj_",winp,"""_parent_""")",nseq=nseq+1
	.	S exe(nseq)=$$rtUpdKey^UCXDD(fid,obj),nseq=nseq+1
	S seq=3000
	S exe(seq)=$$RTN(fid,obj,par),seq=seq+1  	; Filer name
	I '$g(cache) S exe(seq+1)=" Q"               	; Return error status 
	;
	S %LIBS="SYSDEV",FILES=fid
	I 'isRdb,defnod'="" S nodes(defnod)=""			; Be sure to load exists node
	; Need to get class information
	I fid="ACN" D
	.	S nod=$$rtOldNode^UCXDD("ACN","CLS",0)
	.	I $E(nod,$L(nod))'="*" S nodes(nod)=""		; Ignore top and keys
	S seq=nseq
	I whr'="",'isRdb S exe(seq)=whr,seq=seq+1
	;
	I (isRdb&('wide)) D
	.	N lvpm
	.	S lvpm(-161)="vobj("_obj_",-161,"
	.	S lvpm(-162)="vobj("_obj_",-162,"
	.	S exe(seq)=$$getLodCode^UCXDD(tbldesc,obj,"0*",1,0,.lvpm),seq=seq+1
	;
	I ('isRdb&(keys'="")) D
	.	S gblref=$P(gbl,"(",2,99),gbl=$P(gbl,"(",1),j=1
	.	S v="" F i=1:1:$L(gblref,",") D
	..		S z=$P(gblref,",",i)			; access key
	..		I $E(z)=""""!($E(z)?1N) S v=v_","_z	; dummy key
	..		E  S v=v_",vobj("_obj_",-"_(j+2)_")",j=j+1
	.	S exe(seq)=" I '$D("_gbl_"("_$E(v,2,999)_")) "
	.	;; I $G(cache) S exe(seq)=exe(seq)_"S sqlcnt=0 Q"		;5/12/2000 MAS  Interactive
	.	;; E  S exe(seq)=exe(seq)_"Q"  ; not a fatal error ; MJZ - 40944
	.	S exe(seq)=exe(seq)_"S sqlcnt=-1 Q"  ;No data found. not a fatal error ; MJZ 
	.	S seq=seq+1
	; Load top node for type 1 and 11 tables
	I 'isRdb&($P(fsn(fid),"|",4)#2) S exe(seq)=" S vobj("_obj_")=$G("_gbl_"("_$E(v,2,999)_"))",seq=seq+1
	; Load required nodes
	S v=""
	F  S v=$O(nodes(v)) Q:v=""  D
	.	N nod
	.	S nod=v
	.	if nod?.E1"*""" set nod=$$QSUB^%ZS(nod)	; """0*""" issue.
	.	quit:$E(nod,$L(nod))="*"			; Key
	.	set exe(seq)=$$getDataNode^UCXDD(tbldesc,obj,nod,1)
	.	set seq=seq+1
	Q 
	;----------------------------------------------------------------------
VALUE(fid,val,vinp)	; Convert complex expression 
	;----------------------------------------------------------------------
	N complex,fidnq,fsn,ns,prt,str,v
	S fidnq=$$QSUB^%ZS(fid)
	S str="",complex=0
	S ptr=0 F  S v=$$ATOM^%ZS(val,.ptr,"+-/*#","") D  Q:ER  Q:'ptr
	.	I v'="",$D(vkw(v)) S v=":"_vkw(v)	; Keyword
	.	I v?1A.AN1"."1A.AN S v=$$rtCurExpr^UCXDD($P(v,".",1),$P(v,".",2),$$OBJNAME($P(v,".",1)),0)	; fid.di syntax
	.	I $E(v)=":" S str=str_$e(v,2,99) Q
	.	I $E(v)="'" S str=str_$TR(v,"'","""") Q   ; change ' to "
	.	I $E(v,$L(v))="'" S str=str_$TR(v,"'","""") Q
	.	I '((v?1A.AN)!(v?1"%".AN)) S str=str_v Q  ; value data item?
	.	S vinp(fidnq_"."_v)=""
	.	S ns=$$rtCurExpr^UCXDD(fidnq,$$QSUB^%ZS(v),$$OBJNAME(fid),0) I ER Q    ; Internal format
	.	S str=str_ns,complex=1
	I ER Q val  					; Return original value
	I complex Q "("_str_")"				; complex expression
	Q str						; New value
	;----------------------------------------------------------------------
WHERE(fid,expr,newv,fastdel)	; Convert key1=val and key2=val to procedure code 
	;----------------------------------------------------------------------
	;
	; ARGUMENTS:
	;	. fid   Table name			/TYP=T/REQ/MECH=VAL
	;	. expr	SQL where expression		/TYP=T/REQ/MECH=VAL
	;	. newv  List of protected variables	/TYP=T/REQ/MECH=REFVAL:W
	;       . fastdel Fast delete flag
	;
	; EXAMPLE:
	;
	; S X=$$WHERE("DEP","CID=:XCID AND TSEQ=:ZTSEQ",.list)
	; 
	; returns:  X="CID=XCID,TSEQ=ZTSEQ"
	;           list="CID,TSEQ"
	;	    hostvar(var)=""
	;----------------------------------------------------------------------
	N dec,dinam,fsn,i,key,keys,nonkey,typ,obj,q,seq,v,val,vkey
	S obj=$$OBJNAME(fid)
	D fsn^SQLDD(.fsn,fid)
	;
	S nonkey=0,q="""",seq=1
	S keys=$P(fsn(fid),"|",3)  			; Access keys
	I keys'="" F i=1:1:$L(keys,",") S vkey($P(keys,",",i))=i
	;
	S keys="",newv=""
	F i=1:1:$L(expr," AND ") D  I ER!nonkey Q
	.	S v=$P(expr," AND ",i)  		; key=:val
	.	I v[$C(0) S v=$$UNTOK^%ZS(v,.tok)
	.	I v["<="!(v[">=") S nonkey=1 Q		; Multiple rows *** 11/06/97
	.	I v'["=" S nonkey=1 Q			; Invalid syntax/mult-row
	.	I $L(v,":")>2!($L(v,"=")>2) s ER=1,RM=$$^MSG(1477,v) Q  ; Invalid SQL syntax
	.	S key=$$TRIM^%ZS($P(v,"=",1))  		; Key column name
	.	I $E(key)="""" S key=$P(key,"""",2)	; Remove quotes
	.	I '$$VER^SQLDD(fid,key) S ER=1,RM=$$^MSG(1300,key) Q
	.	S val=$$TRIM^%ZS($P(v,"=",2))		; Key value
	.	I key=""!(val="") S ER=1,RM=$$^MSG(1477,v) Q  ; Invalid syntax
	.	I $E(val)=":" S val=$E(val,2,99),hostvar(val)=""
	.	I $E(val)="'" S val=$TR(val,"'","""")   ; String data
	.	I '$D(vkey(key)) S nonkey=1  		; non-key query
	.	E  I key'=val,key'="%LIBS" S newv=newv_","_key,subkey(val)=key ; Protected variable *** 09/23/97
        .       S v=val,dinam=fid_"."_key
        .       S typ=$$TYP^SQLDD(dinam,,.vdd)          ; format type
        .       S dec=$$DEC^SQLDD(dinam,,.vdd)          ; Decimal precision
        .       D CONVFMT I $G(ER) Q                    ; Format error
	.	S v=$P(v," I $G(ER)",1)
	.	K exe
        .       S val=v                                 ; format logic
        .       I "DCLN$"[typ,val?1A.AN S val="+"_val
	.	;
	.	I $G(fastdel)'=0,$D(vkey(key)) S keys=keys_",vobj("_obj_",-"_(vkey(key)+2)_")="_val
	.	K vkey(key)                             ; Key referenced
	.	;
	.	I $G(fastdel)'=0 Q
	.	I key=val Q				; Same variable
	.	S keys=keys_","_key_"="_val
	I nonkey Q ""					; Need to select first
	;                                               ; Missing access key def
	I newv'="" S newv=$E(newv,2,999)		; Remove ,
	S key=$O(vkey("")) I key'="" Q ""
	I keys="" Q " ;"
	Q " S "_$E(keys,2,999)  ; S k1=val,k2=val
	;
	;----------------------------------------------------------------------
DELETE(expr,tag,exe,cache)	; 
	;----------------------------------------------------------------------
	; ARGUMENTS:
	;	. expr		SQL DELETE expression	/TYP=T/MECH=VAL
	;	. tag		Routine Tag name	/TYP=N/DFT=1/MECH=VAL
	;	. exe		Procedural code		/TYP=T/MECH=REFNAM:W
	;       . cache         Store parsed code in cache
	;						/TYP=L/NOREQ/MECH=VAL
	;----------------------------------------------------------------------
	N fid,filer,fsn,gbl,global,hostvar,i,index,j,keys,line,loadArr,newlist,obj
	N q,rtn,seq,statusd,tbldesc,vexec,vtag,whr,zexpr,zwhr
	S par("DQMODE")=1				; Define DQMODE
	K RM,exe					; 04/19/99
	S newlist="",ER=0,par=$G(par),ER=0,q=""""
	S $P(line,"-",70)="",line=" ;"_line
	;
	I expr[$C(9) S par=$P(expr,$C(9),2),expr=$P(expr,$C(9),1) 
	I expr["..." S expr1=$G(expr1)_$P(expr,"...",1) Q  ; Long expression
	I $D(expr1) S expr=expr1_expr k expr1
	;
	I $E(expr,1,5)="FROM " S expr=$e(expr,6,$L(expr))	; Remove keyword
	S zexpr="DELETE "_expr
	S fid=$P(expr," ",1)
	I fid="" Q
	S isRdb=$$rdb^UCDBRT(fid)			; db type
	S obj=$$OBJNAME(fid)				; object name
	S statusd=$$statusd(fid)			; Fast delete status
	S tbldesc=$$getPslTbl^UCXDD(fid,0)
	I isRdb set statusd=1
	D fsn^SQLDD(.fsn,fid) I ER Q  			; File attributes
	S filer="vSave^Record"_fid			; Filer name
	S defnod=$P(fsn(fid),"|",12)                    ; Record Exists node
	I fid["_" S statusd=1				; Use filer logic
	S gbl=$P(fsn(fid),"|",2),global=$P(gbl,"(",1) ; Global name
	S whr=$P(expr," WHERE ",2),zwhr=whr
	I whr'="" S whr=$$WHERE(fid,whr,.newlist,statusd)  	; Parse query logic
	;
	I fid["_" S newlist=obj
	S vtag=$E(1000+tag,2,4)
	I '$G(cache) D
	.	S exe(1)=line 
	.	S exe(2)="vd"_vtag_" ; DELETE "_$E(expr,1,512)_" "_par 
	.	S exe(3)=line 
	I ER S exe(4)=" ; "_RM,exe(5)=" Q" Q			; MJZ - 40944
	;
	I $g(newlist)'="" D
	.	I $E(newlist)="," S newlist=$E(newlist,2,999)
	.	S exe(4)=" N "_newlist
	;
	S i="" F j=2:1 S i=$O(hostvar(i)) Q:i=""  S exe(j/100+4)=" I $G("_i_")="_""""""_" S ER=1,RM=$$^MSG(8592,"_""""_i_""""_") Q" 
	;
	do getValues^SQLDD(fid,"TYP",.index)
	;
	I whr="" D MULSEL(fid,tag,3,10) Q  		; Select multiple rows
	;
	I zwhr'="",filer="",'statusd D  Q
	.	S exe(5)=whr
	.	I $P(fsn(fid),"|",4)=1,'$D(index(fid,"TYP","M")),'$D(index(fid,"TYP","B")) S exe(6)=" ZWI "_$P(fsn(fid),"|",2)_")"
	.	E  S exe(6)=" K "_$P(fsn(fid),"|",2)_")"		; Simple M kill command
	.	S exe(7)=" Q"				;
	;
	I zwhr="" D  Q    				; Entire global
	.	S exe(10)=" K "_$P($P(fsn(fid),"|",2),"(",1)
	.	S exe(11)=" Q"					; MJZ - 40944
	; Single row DELETE
	S seq=10
	;						; *** 04/25/99
	I 'statusd D  Q
	.	I whr'="" S exe(seq)=whr,seq=seq+1      ; Access keys
	.       S exe(seq)=" I '$D("_gbl_")) "
	.	;;I $G(cache) S exe(seq)=exe(seq)_"S sqlcnt=0 Q"	;5/12/2000 MAS
	.	;;E  S exe(seq)=exe(seq)_"S sqlcnt=-1 Q"  ; not a fatal error	; MJZ - 40944
	.	S exe(seq)=exe(seq)_"S sqlcnt=-1 Q"  ; not a fatal error	; MJZ - 40944
	.	S seq=seq+1 
	.	S exe(seq)="E  S sqlcnt=sqlcnt+1"    ; For single record delete increment delete sqlcnt.
	.	S seq=seq+1 
	.	D fastdel Q				; Fast delete - global format
	;						; copy keys into obj structure
	S keys=$p(fsn(fid),"|",3)
	;-------------------------------------------
	S gbl=$$OBJGBL(gbl,obj)			; ***
	S exe(seq)=$S($G(cache):"",1:" N "_obj)_" S "_obj_"=$O(vobj(""""),-1)+1",seq=seq+1
	I whr'="" S exe(seq)=whr,seq=seq+1              ; Access keys
	S exe(seq)=" S vobj("_obj_",-1)="_q_"Record"_fid_q_",vobj("_obj_",-2)=3"
	S seq=seq+1
	I isRdb S exe(seq)=$$getUpdKey^UCXDD(tbldesc,obj),seq=seq+1
	D getRecCode^UCDB(tbldesc,obj,,.loadArr)
	F i=1:1 Q:'$D(loadArr(i))  S exe(seq)=loadArr(i),seq=seq+1
	S exe(seq)=" I $T S ER=1,RM=$$^MSG(7932)",seq=seq+1	; Record Not Defined
	I $D(index(fid,"TYP","B"))!$D(index(fid,"TYP","M")) D
	.	N di s di=""
	.	F  S di=$O(^DBTBL("SYSDEV",1,fid,9,di)) Q:di=""  D
	..		I $E(di)=""""!(di?.N) Q		;literal column
	..		I "BM"'[$$TYP^SQLDD(fid_"."_di) Q
	..		N lvpm
	..		I isRdb D
	...			S lvpm(-161)="vobj("_obj_",-161,"
	...			S lvpm(-162)="vobj("_obj_",-162,"
	..		S exe(seq)=$$getLodCode^UCXDD(tbldesc,obj,"*"_di,1,0,.lvpm),seq=seq+1
	S exe(seq)=$$RTN(fid,obj,par)  			; run-time routine
	S seq=seq+1,exe(seq)=" K vobj("_obj_")"
	;-------------------------------------------
	I $G(cache) Q
	S exe(seq+1)=" Q"				; PSL exit
	Q
fastdel	;
	I $P(fsn(fid),"|",7) S exe(seq)=" D ^DBSLOG("_""""_fid_""""_",3)",seq=seq+1     ; log file 
	I $P(fsn(fid),"|",4)=1,'$D(index(fid,"TYP","M")),'$D(index(fid,"TYP","B")) S exe(seq)=" ZWI "_$P(fsn(fid),"|",2)_")"
	E  S exe(seq)=" K "_$P(fsn(fid),"|",2)_")"		; Simple M kill command
	I $g(cache) Q
	S exe(seq+1)=" Q"
	Q
	;----------------------------------------------------------------------
MULSEL(fid,tag,mode,seq)	;
	;----------------------------------------------------------------------
	N vexpr,ztag,i,j,key,z,zgbl
	S ztag=500+tag				; Open/fetch tag *** 01/19/98
	S key=$P(fsn(fid),"|",3) 		; Access keys
	S vexpr=key_" from "_$P(expr," ",1)_" WHERE "_$P(expr,"WHERE",2,99)
	D OPEN(vexpr,ztag,.vexec)
	I mode=1 S exe(seq)=" ; ***** Update multiple rows *****"
	E  S exe(seq)=" ; ***** Delete multiple rows *****"
	;
	S exe(seq+2)=" N "_obj_" S vc=0,ER=0 "_$$getNewCode^UCXDD($$getPslTbl^UCXDD(fid,1),obj,mode,0)_" ; Assign object"
	S exe(seq+3)=" I '$$vo"_ztag_"() Q" 	; Open cursur
	D
	.	S z=" F  Q:'$$vf"_ztag_"(.v)  "
	.	I $$rdb^UCDB(fid) S z=z_"D reset161^SQLCMP("_obj_") "
	.	S z=z_"S vc=vc+1,vobj("_obj_",-2)="_mode
	.	S j=$L(key,",")
	.	F i=1:1:j S z=z_",vobj("_obj_",-"_(2+i)_")=$P(v,$c(9),"_i_")"	; Assign access keys
	I mode=3,$P(fsn(fid),"|",4)=1 S zgbl=$$OBJGBL(gbl,obj),z=z_",vobj("_obj_")=$G("_zgbl_"))"	; Load type 1 data
	I mode=1 S exe(seq+5)=z_" D vu"_$E(1000+tag,2,4)_"M Q:ER"
	E  S exe(seq+5)=z_$$RTN(fid,obj,par)
	S exe(seq+6)=" K vobj("_obj_") Q" 			; Fetch access keys
	S z="",seq=seq+6
	F  S z=$O(vexec(z)) Q:z=""  S seq=seq+1,exe(seq)=vexec(z)
	Q
	;
	;----------------------------------------------------------------------
reset161(obj)	; Reset -161/-162 nodes for MULSEL
	;----------------------------------------------------------------------
	; Because MULSEL uses the same object reference over and over, it is
	; necessary to reset nodes -161 and -162 for RDB each time.  These
	; nodes carry info used to build the insert/update statements.
	N nod
	S nod=""
	F  S nod=$O(vobj(obj,-161,nod)) Q:nod=""  S vobj(obj,-161,nod)=1,vobj(obj,-162,nod)=$P(vobj(obj,-162,nod),",",1)
	Q
	;
	;----------------------------------------------------------------------
RTN(fid,obj,par,nods,pmode)	; Return run-time save call 
	;----------------------------------------------------------------------
	; vauditlogseq will be defined by SQL (in INSERT, UPDATE, and DELETE
	; sections.  Use $G() in case it becomes undefined for some reason.
	; Worst case is that we audit log the individual statements on a
	; multi-row UPDATE or DELETE.
	N gbl,q,keys,rectyp,rtn,sn
	S q=""""
	I '$D(fsn(fid)) D fsn^SQLDD(.fsn,fid)
	S sn=$P($P(fsn(fid),"|",1),"(",1)		; File short name
	S gbl=$P(fsn(fid),"|",2)			; Global reference
	S keys=$P(fsn(fid),"|",3)			; Access keys
	S rectyp=$P(fsn(fid),"|",4)			; Record type 1,10,11
	S rtn="vSave^Record"_fid
	;;I $G(pmode)="I" Q " D "_rtn_"("_obj_","_q_par_q_",$G(vauditlogseq))"	; Insert Mode
	;
	Q " D "_rtn_"("_obj_","_q_par_q_",$G(vauditlogseq)) S sqlcnt=sqlcnt+1"
	;
	;----------------------------------------------------------------------
KEYOBJ(gblref,obj)	; 
	;----------------------------------------------------------------------
	N gbl,i,j,key,keys
	S gbl=$P(gblref,"(",1),keys=$P(gblref,"(",2,99)
	S j=1,expr=""
	F i=1:1:$L(keys,",") D
	.	S key=$P(keys,",",i)
	.	I key?1A.AN!(key?1"%".AN)!(key?1A.AN1"_"1A.AN) S key="vobj("_obj_","_-(2+j)_")",j=j+1
	.	S expr=expr_","_key
	Q gbl_"("_$E(expr,2,9999)
	;----------------------------------------------------------------------
statusd(fid,par)	; Check index definition, delete triggers, journals in delete mode 
	;----------------------------------------------------------------------
	; Also check access rights and audit logging
	;
	I '$$rdb^UCDBRT(fid) I $D(^DBTBL("SYSDEV",8,fid)) Q 1		; Index definition
	I $D(^DBINDX("SYSDEV","FKPTR",fid)) Q 1         ; Foreign Key definition
	N on,z
	S on=0,z=""					; Insert trigger definition
	F  S z=$O(^DBTBL("SYSDEV",7,fid,z)) Q:z=""  D  Q:on
	.	I $P(^(z),"|",4) S on=1 Q		; Before delete trigger
	.	I $P(^(z),"|",7) S on=1 Q		; After delete trigger
	I on Q 1					; Return status
	;  Journal definition in delete mode
	S z=""						;
	F  S z=$O(^DBTBL("SYSDEV",9,fid,z)) Q:z=""  D  Q:on
	.	I $P(^(z),"|",5)["D" S on=1 Q		; Insert journal flag
	I on Q 1					; Return status
	;
	; Access rights and audit loggingon DELETE
	N td
	S td=$$getPslTbl^UCXDD(fid,0)
	I $$checkAccessRights^UCXDD(td,0)["delete" Q 1
	I $$getLogging^UCXDD(td,0)["delete" Q 1
	Q 0
	;----------------------------------------------------------------------
COMPILE(vsqltag,code,psl,msgopt)	; Convert &&SQL macro statement into procedural code 
	;---------------------------------------------------------------------- 
	; ARGUMENTS: 
	; 
	; . vsqltag     SQL macro statement             /REQ/MECH=REFARRY:R 
	;               vsqltag(type,seq)=SQL statement 
	;                       I  Insert 
	;                       U  Update 
	;                       D  Delete 
	;                       O  Open cursor 
	;                       S  Select 
	; . code        Compiled M procedure code       /MECH=REFARRY:W 
	; 
	; . psl			Input source		/TYP=L/DEF=0/MECH=VAL
	;                         0 = Filer code
	;			  1 = PSL code
	;			  2 = SQL Procedure
	;
	; . msgopt      Error message display option    /NOREQ/DEF=0/MECH=VAL 
	;               0 - Display error message on the screen 
	;               1 - Do not display error message, pass ER (error flag) 
	;                   and RM (Error message to the calling routine) back 
	;                   to the calling routine 
	; Example: 
	; 
	; S vsqltag("I",1)="UTBLBRCD (BRCD,DESC) VALUES (:A,:B)" 
	; D COMPILE(.vsqltag,.code) 
	;----------------------------------------------------------------------
	N exe,i,j,line,v
	S $P(line,"-",80)=""
	S msgopt=$G(msgopt)                     ; Message display option 
	;
	S ER=0
	S i="" F  S i=$O(vsqltag("O",i)) Q:i=""  D 
	.	I vsqltag("O",i)="" Q
	.       D OPEN(vsqltag("O",i),i,.exe) I ER,'msgopt D errmsg 
	.       S j="" F  S j=$O(exe(j)) Q:j=""  D add(exe(j)) 
	S i="" F  S i=$O(vsqltag("S",i)) Q:i=""  D 
	.	I vsqltag("S",i)="" Q
	.       D SELECT(vsqltag("S",i),i,.exe) I ER,'msgopt D errmsg 
	.       S j="" F  S j=$O(exe(j)) Q:j=""  D add(exe(j)) 
	S i="" F  S i=$O(vsqltag("I",i)) Q:i=""  D 
	.	I vsqltag("I",i)="" Q
	.       D INSERT(vsqltag("I",i),i,.exe) I ER,'msgopt D errmsg  ; Continue
	.       S j="" F  S j=$O(exe(j)) Q:j=""  D add(exe(j)) 
	S i="" F  S i=$O(vsqltag("U",i)) Q:i=""  D 
	.	I vsqltag("U",i)="" Q
	.       D UPDATE(vsqltag("U",i),i,.exe) I ER,'msgopt D errmsg  ; Continue
	.       S j="" F  S j=$O(exe(j)) Q:j=""  D add(exe(j)) 
	S i="" F  S i=$O(vsqltag("D",i)) Q:i=""  D 
	.	I vsqltag("D",i)="" Q
	.       D DELETE(vsqltag("D",i),i,.exe) I ER,'msgopt D errmsg  ; Continue
	.       S j="" F  S j=$O(exe(j)) Q:j=""  D add(exe(j)) 
	Q
	;----------------------------------------------------------------------
errmsg	; Display error message 
	;----------------------------------------------------------------------
	U 0 W !,"------------------------------------------------------",!
	W !," ",$G(zexpr),!!,RM,!
	K zexpr
	S ER=0,RM=""
	W !,"------------------------------------------------------",!
	H 1
	Q
	;----------------------------------------------------------------------
errmsg1(mode)	; 
	I mode=1 S RM="-I- Update multiple rows"
	E  S RM="-I- Delete multiple rows"
	D errmsg
	Q
	;---------------------------------------------------------------------- 
add(data)	; Insert procedural code into buffer 
	;---------------------------------------------------------------------- 
	S v=$O(code(""),-1)+1 
	S code(v)=data 
	Q 
	;----------------------------------------------------------------------
SQLCACHE(expr,opt)	; Save/execute SQL INSERT/UPDATE/DELETE statement from cache 
	;----------------------------------------------------------------------
	N exe,i,j,vc,vcurval,vsql,vtp,zexe 
	;
	I opt>0,expr'[" WHERE " S ER=1,RM="Invalid CACHE option for this SQL statement" Q
	I '$$FLT^SQLCACHE(expr,.tok,.par) D  I ER Q 	; Cache entry?
	. I opt=0 D INSERT(expr,1,.zexe,1) I ER Q		; Parse it
	. I opt=1 D UPDATE(expr,1,.zexe,1) I ER Q		; Parse it
	. I opt=3 D DELETE(expr,1,.zexe,1) I ER Q		; Parse it
	. S i=3 F j=1:1 S i=$O(zexe(i)) Q:i=""  S exe(j)=zexe(i)
	. D SAV^SQLCACHE($$UNTOK^%ZS(expr,.tok),.par)	; Save it
	;
	; Execute SQL statement
	;
	N @$P(exe(1),"N ",2)				; Save variables
	;
	S vtp=$Tlevel                                   ; TP already on? 
	;                                               ;  
	;I 'vtp,$P($G(^CUVAR("DBS")),"|",7) S vtp=1      ; Trun off TP logic 
	; 
	I 'vtp TStart *                                 ; Start TP 
	; 
	F i=2:1 Q:'$D(exe(i))  X exe(i) Q:ER		; Process
	I vtp Q                                         ; Let calling routine t^ 
	I '$Tlevel S ER=1 Q                             ; TP restart error? 
	I $G(ER) TRollback  Q                           ; Rollback transaction 
	TCommit:$Tlevel 				; Commit transaction
	Q
	;
	;----------------------------------------------------------------------
KEYMAP(gblref,vmap)	;  Replace global key references with host variables 
	;----------------------------------------------------------------------
	; ARGUMENTS:
	;
	; . gblref	Global reference	/TYP=T/REQ/MECH=VAL
	; . vmap	Access key map		/TYP=T/REQ/MECH=REFARRY:R
	;
	; RETURNS:
	;
	; $$		New global reference
	;
	; EXAMPLE:
	;
	; gblref = ^UTBL("BRCD",BRCD
	; vamp("BRCD")="XBR"
	;
	; W $$KEYMAP(gblref,.vmap) returns  ^UTBL("BRCD",XBR
	;
	;----------------------------------------------------------------------
	N gbl,i,key,keys,ref,tok,z
	S gbl=$P(gblref,"(",1)				; Global name
	S ref=$P(gblref,"(",2,99)
	I ref["""" S ref=$$TOKEN^%ZS(ref,.tok)		; Store in token format
	S keys=""
	F i=1:1:$L(ref,",") D
	.	S key=$P(ref,",",i)			; access key
	.	I $D(vmap(key)) S key=vmap(key)		; replace it
	.	S keys=keys_","_key
	S z=gbl_"("_$E(keys,2,999)
	I $D(tok) S z=$$UNTOK^%ZS(z,.tok)		; Convert back
	Q z
	;----------------------------------------------------------------------
POSMAP(pos)	; Map column position into string
	;----------------------------------------------------------------------
	; ARGUMENTS:
	;
	; .pos		Field position array	/TYP=T/REQ/MECH=REFARRY:R
	;               
	;		vpos(n)=data
	;
	; RETURNS:
	;
	; $$		String data
	;
	; EXAMPLE:
	;
	;  pos(2)=A,pos(5)=B,pos(7)=C
	;
	;  W $$POSMAP(.pos) returns: "|"_A_"||||"_B_"||"_C
	;
	;----------------------------------------------------------------------
	N del,n,p,str
	S n=1,str="",$P(del,"|",500)=""
	S p="" F  S p=$O(pos(p)) Q:p=""  D
	.	S v=pos(p)
	.	I n=p S str=str_v Q			; Next position
	.	I str'="" S str=str_"_"				; Add _ character
	.	S str=str_""""_$E(del,1,p-n)_""""_"_"_v	; Insert field delimiter
	.	S n=p						; Reset pointer
	Q str
	;----------------------------------------------------------------------
KWMAP(str,vkw)	; Substitute keywords
	;----------------------------------------------------------------------
	;
	; ARGUMENTS:
	;
	; . str		Input string		/TYP=T/REQ/MECH=VAL
	; . vkw		Keyword table		/TYP=T/REQ/MECH=REFARRY:R
	;
	; RETURNS:
	;
	; $$		Converted string
	;
	; EXAMPLE:
	;
	; W $$KWMAP("%SystemDate+1",.vkw)  returns: TJD+1
	;----------------------------------------------------------------------
	N nstr,ptr,v 
	S ptr=0,nstr="" 
	F  S v=$$ATOM^%ZS(str,.ptr,"(+-*/_#)") Q:'ptr  D ; parse string
	.       I $D(vkw(v)) S v=vkw(v) 			; replace keyword
	.       S nstr=nstr_v 
	I $D(vkw(v)) S v=vkw(v)					
	Q nstr_v 
	;
	;----------------------------------------------------------------------
REQDI(fid)	;  Validate required data items 
	;----------------------------------------------------------------------
	N di,dinam,i,list,x
	S list=$G(^DBTBL("SYSDEV",1,fid,102))
	I list="" Q
	F i=1:1:$L(list,",") D
	.	S di=$P(list,",",i)
	.	I $D(hostvar(di)) Q
	.	; no need to check null condition
	.	S x=" I "_$$ADDGET($$rtCurExpr^UCXDD(fid,di,$$OBJNAME(fid),0))_"="_""""""_" S ER=1,RM=$$^MSG(1767,"_""""_fid_"."_di_""""_") Q"
	.	S exe(seq)=x,seq=seq+1
	Q
	;----------------------------------------------------------------------
DEFAULT(fid)	; Apply default values 
	;----------------------------------------------------------------------
	N di,dinam,expr,node,i,list,typ,x
	S list=$G(^DBTBL("SYSDEV",1,fid,101))
	I list="" Q
	F i=1:1:$L(list,",") D
	.	S di=$P(list,",",i)
	.	I $G(hostvar(di))=0 Q		; no need to consider default value
	.	S dinam=fid_"."_di
	.	S expr=$$rtCurExpr^UCXDD(fid,di,$$OBJNAME(fid),0)	; if dinam="" set dinam=default value
	.       S node=$$NOD^SQLDD(dinam) 
	.       I node'="" S nods(node)="" 
	.	S dft=$$DFT^SQLDD(dinam,,.vdd)	; Default value
	.	I dft="" Q
	.	I dft?1"["1a.an1"]"1a.an Q	; Bad syntax
	.	I dft[",%," S dft=$p(dft,",%,",1)_",""|"","_$P(dft,",%,",2,99)
	.	S typ=$$TYP^SQLDD(dinam,,.vdd)	; Format
        .       D
        ..              I dft=+dft Q
        ..              I typ="L",dft?1A S dft=$S(dft="Y":1,1:0) Q
	..		if '$d(vkw) do getKeyword(.vkw)
        ..              I $D(vkw(dft)) S dft=vkw(dft) Q     ; System keyword
        ..              I dft="T" S dft="TJD" Q                         ; system date
        ..              I $E(dft,1,2)="<<" S dft=$P($P(dft,"<<",2),">>",1) Q    ; <<var>>
        ..              S dft=""""_dft_""""
	.	S x=" I "_$$ADDGET(expr)_"="_""""""_" S "_expr_"="_dft
	.	S exe(seq)=x,seq=seq+1
	.	S hostvar(di)=2
	Q
	;----------------------------------------------------------------------
ADDGET(expr)	; Add $G function to vobj() array
	;----------------------------------------------------------------------
	N node,p1,p2,p3
	S p1=$P(expr,"vobj(",1)
	S p2=$P($P(expr,"vobj(",2),")",1)
	S node=$p(p2,",",2)+0			; Add new field to the list
	I node'<0 S nods(node)=""
	S p3=$P($P(expr,"vobj(",2,99),")",2,99)
	Q p1_"$G(vobj("_p2_"))"_p3
        ;----------------------------------------------------------------------
PATCHK(var,par) ;
        ;----------------------------------------------------------------------
        F  q:par'["X"  S par=$P(par,"X",1)_var_$P(par,"X",2,999)
        Q par
        ;----------------------------------------------------------------------
getKeyword(vkw) ;
	; call procedure to access db to get stbl data from STBLJRNFUNCS and 
	; syskeywords.
        ;----------------------------------------------------------------------
	D JRNFUNCS^UCDTAUTL(.vkw)
	D keywords^UCDTAUTL(.vkw)
	S i=""
	F  S i=$O(vkw(i)) q:i=""  set vkw(i)=$P(vkw(i),"|",1)  
	Q
QA      ;
        D ^SCAIO
        U IO
        S fid="" F  S fid=$O(^DBTBL("SYSDEV",1,fid)) Q:fid=""  D
        .       I $G(^(fid,101))="" Q
        .       W !,fid,! D DEFAULT(fid)
        C IO
        Q
	;-------------------------------------------------------
RLodWide(obj,winp,parent)	;Called from exe array during SQL execution
	; load top level ACN data if it not already loaded.
	; Note:  This function will only be called for wide tables.
	;---------------------------------------------------------
        N i,nod,vTable,vWhere
	;
	S vTable=$E(vobj(obj,-1),7,99999)
	I winp'[parent set winp=parent_","_winp
	F i=1:1:$L(winp,",") d
	.	N vSQL,vData,vRTable,vER,vRM
	.	S nod=$P(winp,",",i)
	.	I nod=parent set vRTable=parent
	.	E  S vRTable="W_"_vTable_"_"_nod
	.	I $G(vobj(obj,-4)) S vSQL="SELECT * FROM "_vRTable,vWhere=" WHERE CID="_$G(vobj(obj,-3))_" AND SEGMENT="_$G(vobj(obj,-4))
	.	E  S vSQL="SELECT * FROM "_vRTable,vWhere=" WHERE CID="_$G(vobj(obj,-3))
	.	S vER=$$SELECT^%DBAPI("",vSQL_vWhere,"|","",.vData,.vRM)
	.	I vER S vData=""
	.	I nod=parent S vobj(obj)=vData
	.	E  S vobj(obj,nod)=vData,vobj(obj,-161,nod)=1,vobj(obj,-162,nod)=vRTable
	Q
	;-----------------------------------------------------------------------
OBJGBL(gbl,obj) ; private String
	;-----------------------------------------------------------------------
	; Convert global reference to vobj format
	;
	; Example:  ^ACN(CID  		-->  ^ACN(vobj(acn,-3)
	;           ^HIST(CID,TSEQ	--> ^HIST(vobj(hist,-3),vobj(hist,-4
	;-----------------------------------------------------------------------
	I gbl["vobj" Q gbl				; Already converted
	N i,j,k,zgbl
	S j=3,zgbl=$P(gbl,"(",1),gbl=$P(gbl,"(",2,99)
	F i=1:1:$L(gbl,",") D
	.	S k=$P(gbl,",",i)			; Access key
	.	I '(($E(k)?1A)!($E(k)="%")) Q		; Dummy key
	.       S $P(gbl,",",i)="vobj("_obj_";-"_j_")"	; Convert to vobj(obj,-
	.	S j=j+1
	Q zgbl_"("_$TR(gbl,";",",")
	;
	;-----------------------------------------------------------------------
OBJNAME(file) ; private String
	;-----------------------------------------------------------------------
	; Assign default object name based on file name
	;
	I file'["_" Q $$LOWER^UCGMR(file) 	; Default to lowercase file name
	;
	Q $$LOWER^UCGMR($TR(file,"_"))		; Remove underscore