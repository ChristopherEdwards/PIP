package com.fis.workbench.console;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

public class FISConsole {
   private static FISConsole fDefault = null;
   private String fTitle = null;
   private MessageConsole fMessageConsole = null;
   public static final int MSG_INFORMATION = 1;
   public static final int MSG_ERROR = 2;
   public static final int MSG_WARNING = 3;

   public FISConsole(String messageTitle) {
      fDefault = this;
      this.fTitle = messageTitle;
   }

   public static FISConsole getDefault() {
      return fDefault;
   }

   public void println(String msg, int msgKind) {
      if (msg != null) {
         if (!this.displayConsoleView()) {
            MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error", msg);
         } else {
            this.getNewMessageConsoleStream(msgKind).println(msg);
         }
      }
   }

   public void clear() {
      IDocument document = this.getMessageConsole().getDocument();
      if (document != null) {
         document.set("");
      }

   }

   private boolean displayConsoleView() {
      try {
         IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
         if (activeWorkbenchWindow != null) {
            IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
            if (activePage != null) {
               activePage.showView("org.eclipse.ui.console.ConsoleView", (String)null, 2);
            }
         }

         return true;
      } catch (PartInitException var3) {
         return false;
      }
   }

   private MessageConsoleStream getNewMessageConsoleStream(int msgKind) {
      int swtColorId = 6;
      switch(msgKind) {
      case 1:
         swtColorId = 6;
         break;
      case 2:
         swtColorId = 12;
         break;
      case 3:
         swtColorId = 10;
      }

      MessageConsoleStream msgConsoleStream = this.getMessageConsole().newMessageStream();
      msgConsoleStream.setColor(Display.getCurrent().getSystemColor(swtColorId));
      return msgConsoleStream;
   }

   private MessageConsole getMessageConsole() {
      this.fMessageConsole = this.findConsole(this.fTitle);
      if (this.fMessageConsole == null) {
         this.createMessageConsoleStream(this.fTitle);
      }

      try {
         IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
         IConsoleView view = (IConsoleView)page.findView("org.eclipse.ui.console.ConsoleView");
         if (view == null) {
            view = (IConsoleView)page.showView("org.eclipse.ui.console.ConsoleView");
         }

         view.display(this.fMessageConsole);
      } catch (PartInitException var3) {
         var3.printStackTrace();
      }

      return this.fMessageConsole;
   }

   private void createMessageConsoleStream(String title) {
      this.fMessageConsole = new MessageConsole(title, (ImageDescriptor)null);
      ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[]{this.fMessageConsole});
   }

   private MessageConsole findConsole(String name) {
      ConsolePlugin plugin = ConsolePlugin.getDefault();
      IConsoleManager conMan = plugin.getConsoleManager();
      IConsole[] existing = conMan.getConsoles();

      for(int i = 0; i < existing.length; ++i) {
         if (name.equals(existing[i].getName())) {
            return (MessageConsole)existing[i];
         }
      }

      MessageConsole myConsole = new MessageConsole(name, (ImageDescriptor)null);
      conMan.addConsoles(new IConsole[]{myConsole});
      myConsole.activate();
      return myConsole;
   }
}
