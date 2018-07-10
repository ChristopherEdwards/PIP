function copy(inElement) {

	inElement.value = inElement.value.replace(/%60%/g, ">");
	inElement.value = inElement.value.replace(/%62%/g, "<");
	inElement.value = inElement.value.replace(/%32%/g, " ");
	inElement.value = inElement.value.replace(/%34%/g, "\"");
	
	if (inElement.createTextRange) {
    	var range = inElement.createTextRange();
    	if (range)
      		range.execCommand('Copy');
 	 } else {
	    var flashcopier = 'flashcopier';
	    if(!document.getElementById(flashcopier)) {
	      var divholder = document.createElement('div');
	      divholder.id = flashcopier;
	      document.body.appendChild(divholder);
	    }
	    document.getElementById(flashcopier).innerHTML = '';
	    var divinfo = '<embed src="_clipboard.swf" FlashVars="clipboard='+encodeURIComponent(inElement.value)+'" width="0" height="0" type="application/x-shockwave-flash"></embed>';
	    document.getElementById(flashcopier).innerHTML = divinfo;
  	}
  	alert("Copy to clipboard successful.");
  	
}

