package com.fnf.gtm.meditor.editor;

import com.fnf.gtm.meditor.MEditorPlugin;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.MarkerUtilities;

public class AddMMarkerAction extends Action {
   private ITextEditor textEditor;
   private Object[][] customAttributes;
   private String message;
   private static final String MARKER_TYPE = "com.fnf.scaide.marker";

   public AddMMarkerAction(ITextEditor editor, String label, Object[][] attributes, String message) {
      this.textEditor = editor;
      this.setText(label);
      this.customAttributes = attributes;
      this.message = message;
   }

   public void run() {
      Map attributes = new HashMap(11);
      ITextSelection selection = (ITextSelection)this.textEditor.getSelectionProvider().getSelection();
      if (!selection.isEmpty()) {
         int start = selection.getOffset();
         int length = selection.getLength();
         if (length < 0) {
            length = -length;
            start -= length;
         }

         MarkerUtilities.setCharStart(attributes, start);
         MarkerUtilities.setCharEnd(attributes, start + length);
         int line = selection.getStartLine();
         MarkerUtilities.setLineNumber(attributes, line == -1 ? -1 : line + 1);

         for(int i = 0; i < this.customAttributes.length; ++i) {
            attributes.put(this.customAttributes[i][0], this.customAttributes[i][1]);
         }

         MarkerUtilities.setMessage(attributes, this.message);
      }

      try {
         MarkerUtilities.createMarker(this.getResource(), attributes, "com.fnf.scaide.marker");
      } catch (CoreException var7) {
         MEditorPlugin.getDefault().getLog().log(var7.getStatus());
         Shell shell = this.textEditor.getSite().getShell();
         String title = "Error occured!";
         String msg = "Core Exception caught";
         ErrorDialog.openError(shell, title, msg, var7.getStatus());
      }

   }

   protected IResource getResource() {
      IEditorInput input = this.textEditor.getEditorInput();
      return (IResource)input.getAdapter(IResource.class);
   }
}
