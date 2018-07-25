package com.sanchez.profile.schema.schemaeditor.ui.xml;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;

public class XMLPartitionScanner extends RuleBasedPartitionScanner {
   public static final String XML_DEFAULT = "__xml_default";
   public static final String XML_COMMENT = "__xml_comment";
   public static final String XML_TAG = "__xml_tag";

   public XMLPartitionScanner() {
      List rules = new ArrayList();
      IToken xmlComment = new Token("__xml_comment");
      IToken tag = new Token("__xml_tag");
      rules.add(new MultiLineRule("<!--", "-->", xmlComment));
      rules.add(new TagRule(tag));
      IPredicateRule[] result = new IPredicateRule[rules.size()];
      rules.toArray(result);
      this.setPredicateRules(result);
   }
}
