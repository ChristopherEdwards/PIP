package com.fis.workbench.console;

import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class WorkbenchConsoleStartup implements IStartup {
   public void earlyStartup() {
      final IWorkbench workbench = PlatformUI.getWorkbench();
      workbench.getDisplay().asyncExec(new Runnable() {
         public void run() {
            IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
            if (window != null) {
               new FISConsole(ResourceBoundle.getString("FISWorkbench.Console.id"));
            }

         }
      });
   }
}
