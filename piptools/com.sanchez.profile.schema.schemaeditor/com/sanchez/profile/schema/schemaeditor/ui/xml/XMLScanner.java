package com.sanchez.profile.schema.schemaeditor.ui.xml;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;

public class XMLScanner extends RuleBasedScanner {
   public XMLScanner(ColorManager manager) {
      List rules = new ArrayList();
      IToken procInstr = new Token(new TextAttribute(manager.getColor(IXMLColorConstants.PROC_INSTR)));
      rules.add(new SingleLineRule("<?", "?>", procInstr));
      rules.add(new WhitespaceRule(new XMLWhitespaceDetector()));
      IRule[] result = new IRule[rules.size()];
      rules.toArray(result);
      this.setRules(result);
   }
}
