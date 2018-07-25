package com.fnf.gtm.meditor.editor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;

public class MReconciler implements IReconcilingStrategy {
   private MEditor editor;
   private IDocument doc;
   private String last;

   public MReconciler(MEditor editor) {
      this.editor = editor;
   }

   public void setDocument(IDocument doc) {
      this.doc = doc;
   }

   public void reconcile(DirtyRegion dirty, IRegion region) {
      String text = dirty.getText();
      if (text != this.last) {
         this.last = text;
         text = this.doc.get();
         System.out.println("reconcile(DirtyRegion dirty, IRegion region) called");
      }
   }

   public void reconcile(IRegion region) {
      String text = this.doc.get();
      if (!text.equals(this.last)) {
         this.last = text;
         if (this.editor != null) {
            if (this.editor.getOutlinePage() != null) {
               this.editor.getOutlinePage().reconcile();
            }

         }
      }
   }
}
