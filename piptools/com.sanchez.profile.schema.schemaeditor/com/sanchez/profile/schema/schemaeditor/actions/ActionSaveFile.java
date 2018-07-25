package com.sanchez.profile.schema.schemaeditor.actions;

import com.sanchez.profile.schema.schemaeditor.ui.xml.XMLEditor;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;

public class ActionSaveFile extends Action {
   private XMLEditor editor;

   public ActionSaveFile(XMLEditor editor, String label) {
      this.editor = editor;
      this.setText(label);
   }

   public void run() {
      try {
         this.editor.doSave((IProgressMonitor)null);
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }
}
