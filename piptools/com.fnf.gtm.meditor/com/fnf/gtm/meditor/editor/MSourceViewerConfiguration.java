package com.fnf.gtm.meditor.editor;

import com.fnf.gtm.meditor.MEditorPlugin;
import com.fnf.gtm.meditor.editor.utils.ColorManager;
import com.fnf.gtm.meditor.editor.utils.IMColorConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.DefaultIndentLineAutoEditStrategy;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.reconciler.Reconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.LineNumberRulerColumn;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

public class MSourceViewerConfiguration extends SourceViewerConfiguration {
   private MDoubleClickStrategy doubleClickStrategy;
   private MScanner scanner;
   private ColorManager colorManager;
   private MEditor editor;
   private LineNumberRulerColumn fLineNumberRulerColumn;

   public MSourceViewerConfiguration(MEditor editor, ColorManager colorManager) {
      this.colorManager = colorManager;
      this.editor = editor;
   }

   protected MScanner getGtmScanner() {
      if (this.scanner == null) {
         this.scanner = new MScanner(this.colorManager, this.editor);
         this.scanner.setDefaultReturnToken(new Token(new TextAttribute(this.colorManager.getColor(IMColorConstants.GTM_DEFAULT))));
      }

      return this.scanner;
   }

   public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
      PresentationReconciler reconciler = new PresentationReconciler();
      DefaultDamagerRepairer dr = new DefaultDamagerRepairer(this.getGtmScanner());
      reconciler.setDamager(dr, "__dftl_partition_content_type");
      reconciler.setRepairer(dr, "__dftl_partition_content_type");
      NonRuleBasedDamagerRepairer ndr = new NonRuleBasedDamagerRepairer(new TextAttribute(this.colorManager.getColor(IMColorConstants.GTM_COMMENTS)));
      reconciler.setDamager(ndr, "__gtm_comment");
      reconciler.setRepairer(ndr, "__gtm_comment");
      return reconciler;
   }

   public IAutoEditStrategy getAutoEditStrategy(ISourceViewer sourceViewer, String contentType) {
      return (IAutoEditStrategy)("__dftl_partition_content_type".equals(contentType) ? new MAutoIndentStrategy() : new DefaultIndentLineAutoEditStrategy());
   }

   public int getTabWidth(ISourceViewer sourceViewer) {
      return this.getPreferenceStore().getInt("com.fnf.gtm.meditor.width");
   }

   protected IPreferenceStore getPreferenceStore() {
      return MEditorPlugin.getDefault().getPreferenceStore();
   }

   public IReconciler getReconciler(ISourceViewer sourceViewer) {
      sourceViewer.getDocument();
      Reconciler rec = new Reconciler();
      MReconciler r = new MReconciler(this.editor);
      rec.setReconcilingStrategy(r, "__m_title");
      rec.setReconcilingStrategy(r, "__dftl_partition_content_type");
      rec.setDelay(200);
      rec.setIsIncrementalReconciler(false);
      return rec;
   }
}
