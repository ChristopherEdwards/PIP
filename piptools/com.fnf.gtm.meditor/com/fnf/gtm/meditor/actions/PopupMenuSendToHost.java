package com.fnf.gtm.meditor.actions;

import com.fnf.gtm.meditor.utils.SendMrtn;
import com.fnf.studio.views.FidelityConsoleView;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class PopupMenuSendToHost implements IObjectActionDelegate {
   private IWorkbenchPartSite site;
   private IWorkbenchPart part;
   private IAction action;
   private ISelection selection;
   private String messageToConsole;
   private String crlf = "\r\n";
   private String userID;
   private String password;
   private String host;
   private String directory;

   public void run(IAction action) {
      try {
         this.messageToConsole = "";
         this.printResultInConsole("");
         if (this.selection.isEmpty()) {
            return;
         }

         IStructuredSelection structure = (IStructuredSelection)this.selection;
         IProject prj = ((IResource)structure.getFirstElement()).getProject();
         this.userID = prj.getPersistentProperty(new QualifiedName("", "FTPUSERID"));
         this.password = prj.getPersistentProperty(new QualifiedName("", "FTPPASSWORD"));
         this.host = prj.getPersistentProperty(new QualifiedName("", "FTPHOST"));
         this.directory = prj.getPersistentProperty(new QualifiedName("", "FTPDIRECTORY"));
         String dialogTitle = "FTP Send To " + this.host + ": " + this.directory;
         String dialogMessage = "The FTP connection should be set up on the properties page" + this.crlf + "Change the default directory?";
         String initialValue = this.directory;
         InputDialog dlg = new InputDialog(this.part.getSite().getShell(), dialogTitle, dialogMessage, initialValue, (IInputValidator)null);
         if (dlg.open() != 0) {
            this.messageToConsole = "Canceled!";
         } else {
            if (!dlg.getValue().trim().equalsIgnoreCase(this.directory)) {
               this.directory = dlg.getValue().trim();
               System.out.println(this.directory);
               prj.setPersistentProperty(new QualifiedName("", "FTPDIRECTORY"), this.directory);
            }

            if (structure.size() >= 1) {
               Object[] obj = structure.toArray();

               for(int i = 0; i < structure.size(); ++i) {
                  IResource resource = (IResource)obj[i];
                  String fileLocation = resource.getLocation().toString();
                  String fileName = resource.getName();
                  IEditorPart[] part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getDirtyEditors();

                  for(int j = 0; j < part.length; ++j) {
                     if (fileName.equals(part[j].getEditorInput().getName())) {
                        boolean saveOK = MessageDialog.openQuestion((Shell)null, "Save File?", fileName + " has been modified. Do you want to save it?");
                        if (saveOK) {
                           part[j].doSave((IProgressMonitor)null);
                        }
                     }
                  }

                  resource.getFileExtension();
                  if (fileLocation.lastIndexOf("/") != -1) {
                     String filePath = fileLocation.substring(0, fileLocation.lastIndexOf("/"));
                     this.ftpSend(this.userID, this.password, this.host, this.directory, filePath, fileName);
                  }
               }
            }
         }
      } catch (CoreException var16) {
         System.out.println(var16.getMessage());
      }

      this.printResultInConsole(this.messageToConsole);
   }

   public void selectionChanged(IAction action, ISelection selection) {
      this.selection = selection;
   }

   private void ftpSend(String userID, String password, String host, String directory, String filePath, String fileName) {
      String result = SendMrtn.send(host, userID, password, directory, filePath, fileName);
      this.messageToConsole = this.messageToConsole + "Result from sending " + fileName + ": " + result + this.crlf;
   }

   public void setActivePart(IAction action, IWorkbenchPart targetPart) {
      this.part = targetPart;
      this.action = action;
      this.site = targetPart.getSite();
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
