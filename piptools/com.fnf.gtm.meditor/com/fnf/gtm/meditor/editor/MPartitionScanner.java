package com.fnf.gtm.meditor.editor;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;

public class MPartitionScanner extends RuleBasedPartitionScanner {
   public static final String GTM_DEFAULT = "__gtm_default";
   public static final String GTM_COMMENT = "__gtm_comment";

   public MPartitionScanner() {
      List rules = new ArrayList();
      IToken GTMComment = new Token("__gtm_comment");
      rules.add(new SingleLineRule("\"", "\"", Token.UNDEFINED, '\\'));
      rules.add(new MultiLineRule("/*", "*/", GTMComment));
      IPredicateRule[] result = new IPredicateRule[rules.size()];
      rules.toArray(result);
      this.setPredicateRules(result);
   }
}
