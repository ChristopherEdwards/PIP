package com.fnf.gtm.meditor.editor;

import com.fnf.gtm.meditor.MEditorPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

public class MContentOutlinePage extends ContentOutlinePage {
   private IDocument document;
   protected ITextEditor fTextEditor;
   protected TreeViewer viewer;

   public MContentOutlinePage(IDocument document, ITextEditor editor) {
      this.document = document;
      this.fTextEditor = editor;
   }

   public void createControl(Composite parent) {
      super.createControl(parent);
      this.viewer = this.getTreeViewer();
      this.viewer.setContentProvider(new WorkbenchContentProvider());
      this.viewer.setLabelProvider(new MLabelProvider());
      this.viewer.setInput(this.getContentOutline(this.document));
      this.viewer.addSelectionChangedListener(this);
      this.registerToolbarActions();
   }

   public void updateOutline() {
      this.update();
      this.viewer.refresh();
   }

   private void registerToolbarActions() {
      IToolBarManager toolBarManager = this.getSite().getActionBars().getToolBarManager();
      if (toolBarManager != null) {
         Action action = new MContentOutlinePage.LexicalSortingAction();
         toolBarManager.add(action);
      }

   }

   private IAdaptable getContentOutline(IDocument document) {
      return MModelFactory.getInstance().getContentOutline(document);
   }

   public void selectionChanged(SelectionChangedEvent event) {
      super.selectionChanged(event);
      ISelection selection = event.getSelection();
      if (selection.isEmpty()) {
         this.fTextEditor.resetHighlightRange();
      } else {
         MarkElement element = (MarkElement)((IStructuredSelection)selection).getFirstElement();
         int start = element.getStart();
         int length = element.getLength();

         try {
            this.fTextEditor.setHighlightRange(start, 1, true);
            this.fTextEditor.selectAndReveal(start, length);
         } catch (IllegalArgumentException var6) {
            this.fTextEditor.resetHighlightRange();
         }
      }

   }

   public void update() {
      if (this.viewer != null) {
         Control control = this.viewer.getControl();
         if (control != null && !control.isDisposed()) {
            control.setRedraw(false);
            this.viewer.setInput(this.getContentOutline(this.document));
            this.viewer.expandAll();
            control.setRedraw(true);
         }
      }

   }

   public void reconcile() {
      Display d = this.viewer.getTree().getDisplay();
      d.asyncExec(new Runnable() {
         public void run() {
            MContentOutlinePage.this.viewer.setInput(MContentOutlinePage.this.getContentOutline(MContentOutlinePage.this.document));
            MContentOutlinePage.this.viewer.refresh();
         }
      });
   }

   class LexicalSortingAction extends Action {
      private MElementSorter fSorter = new MElementSorter();

      public LexicalSortingAction() {
         this.setText(MessageUtil.getString("MOutlinePage.Sort.Label"));
         this.setImageDescriptor(MEditorPlugin.getImageDescriptor("alphab_sort.gif"));
         this.setToolTipText(MessageUtil.getString("MOutlinePage.Sort.tooltip"));
         this.setDescription(MessageUtil.getString("MOutlinePage.Sort.description"));
         boolean checked = true;
         this.valueChanged(checked, false);
      }

      public void run() {
         this.valueChanged(this.isChecked(), true);
      }

      private void valueChanged(boolean on, boolean store) {
         this.setChecked(on);
         MContentOutlinePage.this.viewer.setSorter(on ? this.fSorter : null);
      }
   }

   class OutlineAction extends Action {
      private Shell shell;

      public OutlineAction(String label) {
         super(label);
         MContentOutlinePage.this.getTreeViewer().addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
               OutlineAction.this.setEnabled(!event.getSelection().isEmpty());
            }
         });
      }

      public void setShell(Shell shell) {
         this.shell = shell;
      }

      public void run() {
      }
   }
}
