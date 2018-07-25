package com.fnf.ui.editor.simplexml;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;

public class XMLPartitionScanner extends RuleBasedPartitionScanner {
   public static final String XML_COMMENT = "__xml_comment";
   public static final String XML_TAG = "__xml_tag";

   public XMLPartitionScanner() {
      IToken xmlComment = new Token("__xml_comment");
      IToken tag = new Token("__xml_tag");
      IPredicateRule[] rules = new IPredicateRule[]{new MultiLineRule("<!--", "-->", xmlComment), new TagRule(tag)};
      this.setPredicateRules(rules);
   }
}
