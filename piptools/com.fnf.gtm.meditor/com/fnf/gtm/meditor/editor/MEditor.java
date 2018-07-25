package com.fnf.gtm.meditor.editor;

import com.fnf.gtm.meditor.MEditorPlugin;
import com.fnf.gtm.meditor.actions.ActionFTPSendToHost;
import com.fnf.gtm.meditor.editor.utils.ColorManager;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.AnnotationRulerColumn;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.IVerticalRulerColumn;
import org.eclipse.jface.text.source.LineNumberRulerColumn;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

public class MEditor extends TextEditor {
   private MDocumentProvider mDocumentProvider;
   private IDocument document;
   protected MContentOutlinePage page;
   private ColorManager colorManager = new ColorManager();
   LineNumberRulerColumn fLineNumberRulerColumn;

   public MEditor() {
      this.setSourceViewerConfiguration(new MSourceViewerConfiguration(this, this.colorManager));
      this.mDocumentProvider = new MDocumentProvider();
      this.setDocumentProvider(this.mDocumentProvider);
      this.setPreferenceStore(MEditorPlugin.getDefault().getPreferenceStore());
   }

   protected IVerticalRuler createVerticalRuler() {
      CompositeRuler ruler = new CompositeRuler();
      ruler.addDecorator(0, new AnnotationRulerColumn(12));
      if (this.isLineNumberRulerVisible()) {
         ruler.addDecorator(1, this.createLineNumberRulerColumn());
      }

      return ruler;
   }

   public void showLineNumberRuler() {
      IVerticalRuler v = this.getVerticalRuler();
      if (v instanceof CompositeRuler) {
         CompositeRuler c = (CompositeRuler)v;
         c.addDecorator(1, this.createLineNumberRulerColumn());
      }

   }

   protected IVerticalRulerColumn createLineNumberRulerColumn() {
      this.fLineNumberRulerColumn = new LineNumberRulerColumn();
      this.initializeLineNumberRulerColumn(this.fLineNumberRulerColumn);
      return this.fLineNumberRulerColumn;
   }

   protected boolean isLineNumberRulerVisible() {
      IPreferenceStore store = this.getPreferenceStore();
      return store.getBoolean("com.fnf.gtm.meditor.showLineNumber");
   }

   private void hideLineNumberRuler() {
      IVerticalRuler v = this.getVerticalRuler();
      if (v instanceof CompositeRuler) {
         CompositeRuler c = (CompositeRuler)v;
         c.removeDecorator(1);
      }

   }

   protected void handlePreferenceStoreChanged(PropertyChangeEvent event) {
      try {
         ISourceViewer sourceViewer = this.getSourceViewer();
         if (sourceViewer == null) {
            return;
         }

         String property = event.getProperty();
         if ("com.fnf.gtm.meditor.width".equals(property)) {
            Object value = event.getNewValue();
            if (value instanceof Integer) {
               sourceViewer.getTextWidget().setTabs((Integer)value);
            } else if (value instanceof String) {
               sourceViewer.getTextWidget().setTabs(Integer.parseInt((String)value));
               return;
            }

            return;
         }

         if ("com.fnf.gtm.meditor.showLineNumber".equals(property)) {
            if (this.isLineNumberRulerVisible()) {
               this.showLineNumberRuler();
            } else {
               this.hideLineNumberRuler();
            }

            return;
         }
      } finally {
         super.handlePreferenceStoreChanged(event);
      }

   }

   protected void initializeLineNumberRulerColumn(LineNumberRulerColumn rulerColumn) {
      rulerColumn.setForeground(this.colorManager.getColor(new RGB(50, 50, 50)));
      rulerColumn.setBackground(this.colorManager.getColor(new RGB(155, 155, 155)));
   }

   public void doSave(IProgressMonitor monitor) {
      super.doSave(monitor);
      if (this.page != null) {
         this.page.update();
      }

   }

   public Object getAdapter(Class key) {
      this.document = this.mDocumentProvider.document;
      if (key.equals(IContentOutlinePage.class)) {
         IEditorInput input = this.getEditorInput();
         if (input instanceof IFileEditorInput) {
            this.page = new MContentOutlinePage(this.document, this);
            return this.page;
         }
      }

      return super.getAdapter(key);
   }

   public void dispose() {
      this.colorManager.dispose();
      super.dispose();
   }

   protected void createActions() {
      super.createActions();
   }

   public void editorContextMenuAboutToShow(MenuManager menu) {
      super.editorContextMenuAboutToShow(menu);
      this.addAction(menu, "AssistProposal");
      this.addAction(menu, "ContentAssistTip");
   }

   protected void editorContextMenuAboutToShow(IMenuManager parentMenu) {
      super.editorContextMenuAboutToShow(parentMenu);
      parentMenu.add(new Separator());
      parentMenu.add(new ActionFTPSendToHost(this, "Send to Host"));
   }

   public void doRevertToSaved() {
      super.doRevertToSaved();
      if (this.page != null) {
         this.page.update();
      }

   }

   public void doSaveAs() {
      super.doSaveAs();
      if (this.page != null) {
         this.page.update();
      }

   }

   public void doSetInput(IEditorInput input) throws CoreException {
      super.doSetInput(input);
   }

   public MContentOutlinePage getOutlinePage() {
      return this.page;
   }
}
