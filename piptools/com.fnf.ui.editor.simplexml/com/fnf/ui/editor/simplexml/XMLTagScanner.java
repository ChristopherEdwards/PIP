package com.fnf.ui.editor.simplexml;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;

public class XMLTagScanner extends RuleBasedScanner {
   public XMLTagScanner(ColorManager manager) {
      IToken string = new Token(new TextAttribute(manager.getColor(IXMLColorConstants.STRING)));
      IRule[] rules = new IRule[]{new SingleLineRule("\"", "\"", string, '\\'), new SingleLineRule("'", "'", string, '\\'), new WhitespaceRule(new XMLWhitespaceDetector())};
      this.setRules(rules);
   }
}
