package com.fnf.gtm.meditor.editor;

import com.fnf.gtm.meditor.editor.utils.ColorManager;
import com.fnf.gtm.meditor.editor.utils.IMColorConstants;
import com.fnf.gtm.meditor.editor.utils.MColorConstConfig;
import com.fnf.gtm.meditor.editor.utils.MWhitespaceDetector;
import com.fnf.gtm.meditor.editor.utils.MWordDetector;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;

public class MScanner extends RuleBasedScanner {
   private MEditor editor;
   MColorConstConfig gtmConst;

   public MScanner(ColorManager manager, MEditor editor) {
      this.gtmConst = new MColorConstConfig(editor);
      IToken keywords = new Token(new TextAttribute(manager.getColor(IMColorConstants.GTM_KEYWORDS)));
      IToken operators = new Token(new TextAttribute(manager.getColor(IMColorConstants.GTM_OPERATORS)));
      IToken functions = new Token(new TextAttribute(manager.getColor(IMColorConstants.GTM_FUNCTIONS)));
      IToken variables = new Token(new TextAttribute(manager.getColor(IMColorConstants.GTM_VARIABLES)));
      IToken badComments = new Token(new TextAttribute(manager.getColor(IMColorConstants.GTM_BADCOMMANDS)));
      IToken strings = new Token(new TextAttribute(manager.getColor(IMColorConstants.GTM_STRINGS)));
      IToken comments = new Token(new TextAttribute(manager.getColor(IMColorConstants.GTM_COMMENTS)));
      IToken other = new Token(new TextAttribute(manager.getColor(IMColorConstants.GTM_DEFAULT)));
      List rules = new ArrayList();
      rules.add(new EndOfLineRule(";", comments));
      rules.add(new SingleLineRule("\"", "\"", strings, '\\'));
      rules.add(new WhitespaceRule(new MWhitespaceDetector()));
      WordRule wordRule = new WordRule(new MWordDetector(), other);

      int i;
      for(i = 0; i < this.gtmConst.fgOperators.length; ++i) {
         wordRule.addWord(this.gtmConst.fgOperators[i], operators);
      }

      for(i = 0; i < this.gtmConst.fgKeywords.length; ++i) {
         wordRule.addWord(this.gtmConst.fgKeywords[i], keywords);
      }

      for(i = 0; i < this.gtmConst.fgFunctions.length; ++i) {
         wordRule.addWord(this.gtmConst.fgFunctions[i], functions);
      }

      for(i = 0; i < this.gtmConst.fgVariables.length; ++i) {
         wordRule.addWord(this.gtmConst.fgVariables[i], variables);
      }

      for(i = 0; i < this.gtmConst.fgBadComments.length; ++i) {
         wordRule.addWord(this.gtmConst.fgBadComments[i], badComments);
      }

      rules.add(wordRule);
      IRule[] result = new IRule[rules.size()];
      rules.toArray(result);
      this.setRules(result);
   }
}
