FORMDQ2B(OPT)	; Option
	;
	; **** Routine compiled from DATA-QWIK Procedure FORMDQ2B ****
	;
	; 08/30/2007 13:48 - joynerd
	;
	;
	I (OPT=2) D
	.	;
	.	D DEFINE^FORMDEF("LOAD D LOAD^FORMDQ2($G(SID))")
	.	D DEFINE^FORMDEF("SAVE S SAVE=$$SAVE^FORMDQ2(SID)")
	.	D DEFINE^FORMDEF("EXIT D EXIT^FORMDQ2C(SID)")
	.	D DEFINE^FORMDEF("BACKUP D BACKUP^FORMDQ2C(SID)")
	.	;
	.	D DEFINE^FORMDEF("SCR_DATA D ^FORMVAR()")
	.	D DEFINE^FORMDEF("SCR_OBJ D ^FORMVAR("""",1)")
	.	D DEFINE^FORMDEF("SCR_VAR D ^FORMVAR("""",2)")
	.	D DEFINE^FORMDEF("SCR_EXP D ^FORMVAR("""",3)")
	.	D DEFINE^FORMDEF("SCR_QL D QL^FORMDQ2C()")
	.	D DEFINE^FORMDEF("SCR_DEF D ^FORMDQ2C($G(SID))")
	.	D DEFINE^FORMDEF("SCR_FUNC D FUNC^FORMDQ2C(SID)")
	.	;
	.	D DEFINE^FORMDEF("FRAME D ^FORMFRM($G(RHTMAR))")
	.	;
	.	D key("KPE",0,"SCR_DEF")
	.	D key("KP0",0,"SCR_FUNC")
	.	D key("PF2",0,"SCR_DATA")
	.	D key("KPE",1,"SCR_QL")
	.	;
	.	D MAP
	.	Q 
	E  I (OPT=5) D
	.	;
	.	D DEFINE^FORMDEF("BACKUP D BACKUP^FORMDQ5C")
	.	D DEFINE^FORMDEF("LOAD K M D LOAD^FORMDQ5("""",1)")
	.	D DEFINE^FORMDEF("SAVE S SAVE=$$SAVE^FORMDQ5()")
	.	D DEFINE^FORMDEF("EXIT D EXIT^FORMDQ5C")
	.	;
	.	; Regular data item [fid]di, variable <<VAR>>, functions @FUN
	.	;
	.	D DEFINE^FORMDEF("RW_DATA D ^FORMVAR()")
	.	;
	.	; Other object types: Variable, Function, Marker, Report Header
	.	;
	.	D DEFINE^FORMDEF("RW_OBJ D ^FORMVAR("""",1)")
	.	D DEFINE^FORMDEF("RW_FUN D ^FORMVAR("""",2)")
	.	D DEFINE^FORMDEF("RW_VAR D ^FORMVAR("""",3)")
	.	D DEFINE^FORMDEF("RW_MATH D ^FORMVAR("""",4)")
	.	D DEFINE^FORMDEF("RW_MARK D ^FORMVAR("""",5)")
	.	D DEFINE^FORMDEF("RW_HDR D ^FORMVAR("""",6)")
	.	;
	.	D DEFINE^FORMDEF("RW_QL D QL^FORMDQ5C()","OPTION")
	.	D DEFINE^FORMDEF("RW_DEF D DEF^FORMDQ5C()")
	.	D DEFINE^FORMDEF("RW_FUNC D FUNC^FORMDQ5C()")
	.	D DEFINE^FORMDEF("RW_COM D COMMAND^FORMDQ5D()")
	.	;
	.	D key("KPE",0,"RW_DEF")
	.	D key("KP0",0,"RW_FUNC")
	.	D key("PF2",0,"RW_DATA")
	.	D key("KP0",1,"RW_COM")
	.	D key("KPE",1,"RW_QL")
	.	;
	.	D MAP
	.	Q 
	;
	Q 
	;
MAP	;
	;
	D DEFINE^FORMDEF("EXPORT D EXPORT^FORMEXCH()")
	D DEFINE^FORMDEF("SEL_FORM D SELALL^FORMSEL(3)")
	;
	D key^FORMINST("KP.",1,"SAVE")
	D key^FORMINST("KP.",0,"EXIT")
	D key^FORMINST("F20",0,"COMMAND")
	;
	Q 
	;
key(KEY,GOLD,COMMAND)	;
	;
	I GOLD S key("*"_KEY)=COMMAND
	E  S key(KEY)=COMMAND
	;
	Q 
	;  #OPTION ResultClass ON
vSIG()	;
	Q "60425^3057^Dan Russell^2476" ; Signature - LTD^TIME^USER^SIZE