package com.sanchez.profile.schema.schemaeditor.ui.xml;

import java.util.Vector;
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
      Vector rules = new Vector();
      rules.add(new SingleLineRule("\"", "\"", string, '\\'));
      rules.add(new SingleLineRule("'", "'", string, '\\'));
      rules.add(new WhitespaceRule(new XMLWhitespaceDetector()));
      IRule[] result = new IRule[rules.size()];
      rules.copyInto(result);
      this.setRules(result);
   }

   public IToken nextToken() {
      return super.nextToken();
   }
}
