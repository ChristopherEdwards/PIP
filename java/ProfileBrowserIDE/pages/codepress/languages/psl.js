/*
 * CodePress regular expressions for PSL syntax highlighting
 */
 
// Java
Language.syntax = [
	{ input : /\"(.*?)(\"|<br>|<\/P>)/g, output : '<s>"$1$2</s>'}, // strings double quote
	{ input : /\b(catch|do|else|for|if|set|throw|type|while|write|quit|literal|local|private|public|protected)\b/g, output : '<b>$1</b>'}, // statements and keywords
	{ input : /\b(kill|halt|lock)\b/g, output : '<big>$1</big>'}, // important keywords
	{ input : /\b(Blob|Boolean|ByteString|Cache|Class|Column|Date|Db|DbSet|Error|HTML|IO|List|Memo|Number|Object|Primitive|Record|RecordACN|RecordCIF|RecordDEP|RecordLN|Reference|ResultSet|Row|Runtime|Schema|SchemaTable|String|Time|TranSet)\b/g, output : '<u>$1</u>' }, // PSL Classes
	{ input : /([^:]|^)\/\/(.*?)(<br|<\/P)/g, output : '$1<i>//$2</i>$3'}, // comments //	
	{ input : /\/\*(.*?)\*\//g, output : '<i>/*$1*/</i>' }// comments /* */
]

Language.snippets = []

Language.complete = [
	{ input : '\'',output : '\'$0\'' },
	{ input : '"', output : '"$0"' },
	{ input : '(', output : '\($0\)' },
	{ input : '[', output : '\[$0\]' },
	{ input : '{', output : '{\n\t$0\n}' }		
]

Language.shortcuts = []
