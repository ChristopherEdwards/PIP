package com.fnf.gtm.meditor.actions;

import com.fnf.gtm.meditor.utils.SendMrtn;
import com.fnf.studio.views.FidelityConsoleView;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.TextEditor;

public class ActionFTPSendToHost extends Action {
   private TextEditor editor;
   private String userID;
   private String password;
   private String host;
   private String directory;

   public ActionFTPSendToHost(TextEditor editor, String label) {
      this.editor = editor;
      this.setText(label);
   }

   public void run() {
      try {
         this.printResultInConsole("");
         if (this.editor.isDirty()) {
            boolean saveOK = MessageDialog.openQuestion(this.editor.getSite().getShell(), "Save File?", "The current file has been modified. Do you want to save it?");
            if (saveOK) {
               this.editor.doSave((IProgressMonitor)null);
            }
         }

         IProject prj = this.getResource().getProject();
         this.userID = prj.getPersistentProperty(new QualifiedName("", "FTPUSERID"));
         this.password = prj.getPersistentProperty(new QualifiedName("", "FTPPASSWORD"));
         this.host = prj.getPersistentProperty(new QualifiedName("", "FTPHOST"));
         this.directory = prj.getPersistentProperty(new QualifiedName("", "FTPDIRECTORY"));
         String fileName = this.getResource().getName();
         String fileLocation = this.getResource().getLocation().toString();
         String filePath = fileLocation.substring(0, fileLocation.lastIndexOf("/"));
         String dialogTitle = "FTP Send To " + this.host + ": " + this.directory;
         String dialogMessage = "The FTP connection should be set up on the properties page. \r\nChange the default directory?";
         String initialValue = this.directory;
         InputDialog dlg = new InputDialog(this.editor.getSite().getShell(), dialogTitle, dialogMessage, initialValue, (IInputValidator)null);
         if (dlg.open() == 0) {
            if (!dlg.getValue().trim().equalsIgnoreCase(this.directory)) {
               this.directory = dlg.getValue().trim();
               prj.setPersistentProperty(new QualifiedName("", "FTPDIRECTORY"), this.directory);
            }

            this.FTPSend(this.userID, this.password, this.host, this.directory, filePath, fileName);
         } else {
            this.printResultInConsole("Canceled!");
         }
      } catch (CoreException var9) {
         System.out.println(var9.getMessage());
      }

   }

   private void FTPSend(String userID, String password, String host, String directory, String filePath, String fileName) {
      String result = SendMrtn.send(host, userID, password, directory, filePath, fileName);
      this.printResultInConsole("Result from send: " + result);
   }

   protected IResource getResource() {
      IEditorInput input = this.editor.getEditorInput();
      return (IResource)input.getAdapter(IResource.class);
   }

   public void selectionChanged(IAction action, ISelection selection) {
   }

   protected void printResultInConsole(String output) {
      try {
         IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
         FidelityConsoleView console = (FidelityConsoleView)page.findView("com.fnf.studio.views.FidelityConsoleView");
         if (console != null) {
            page.showView("com.fnf.studio.views.FidelityConsoleView");
            console.setOutputText(output);
         } else {
            page.showView("com.fnf.studio.views.FidelityConsoleView");
            console = (FidelityConsoleView)page.findView("com.fnf.studio.views.FidelityConsoleView");
            console.setOutputText(output);
         }
      } catch (PartInitException var4) {
         System.out.println(var4.getMessage());
         var4.printStackTrace();
      }

   }
}
