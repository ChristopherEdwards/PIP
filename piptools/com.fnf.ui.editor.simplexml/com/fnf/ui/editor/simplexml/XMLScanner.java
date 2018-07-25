package com.fnf.ui.editor.simplexml;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;

public class XMLScanner extends RuleBasedScanner {
   public XMLScanner(ColorManager manager) {
      IToken procInstr = new Token(new TextAttribute(manager.getColor(IXMLColorConstants.PROC_INSTR)));
      IRule[] rules = new IRule[]{new SingleLineRule("<?", "?>", procInstr), new WhitespaceRule(new XMLWhitespaceDetector())};
      this.setRules(rules);
   }
}
