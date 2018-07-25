package com.sanchez.profile.schema.schemaeditor.editors;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;

public class AttributeEditorContributor extends MultiPageEditorActionBarContributor {
   private IEditorPart activeEditorPart;
   private Action sampleAction;

   protected IAction getAction(ITextEditor editor, String actionID) {
      return editor == null ? null : editor.getAction(actionID);
   }

   public void setActivePage(IEditorPart part) {
      if (this.activeEditorPart != part) {
         this.activeEditorPart = part;
         IActionBars actionBars = this.getActionBars();
         if (actionBars != null) {
            ITextEditor editor = part instanceof ITextEditor ? (ITextEditor)part : null;
            actionBars.setGlobalActionHandler("revert", this.getAction(editor, ITextEditorActionConstants.REVERT_TO_SAVED));
            actionBars.setGlobalActionHandler("print", this.getAction(editor, ITextEditorActionConstants.PRINT));
            actionBars.setGlobalActionHandler("copy", this.getAction(editor, ITextEditorActionConstants.COPY));
            actionBars.setGlobalActionHandler("selectAll", this.getAction(editor, ITextEditorActionConstants.SELECT_ALL));
            actionBars.setGlobalActionHandler("find", this.getAction(editor, ITextEditorActionConstants.FIND));
            actionBars.setGlobalActionHandler("bookmark", this.getAction(editor, "bookmark"));
            actionBars.updateActionBars();
         }

      }
   }

   private void createActions() {
      this.sampleAction = new Action() {
         public void run() {
            MessageDialog.openInformation((Shell)null, "SchemaEditor Plug-in", "Sample Action Executed");
         }
      };
      this.sampleAction.setText("Sample Action ha ha ha");
      this.sampleAction.setToolTipText("Sample Action tool tip");
      this.sampleAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor("IMG_OBJS_TASK_TSK"));
   }

   public void contributeToMenu(IMenuManager manager) {
   }

   public void contributeToToolBar(IToolBarManager manager) {
   }
}
