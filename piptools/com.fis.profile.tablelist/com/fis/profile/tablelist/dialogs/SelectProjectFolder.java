package com.fis.profile.tablelist.dialogs;

import com.fis.profile.tablelist.TableList;
import java.io.File;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;

public class SelectProjectFolder extends Dialog {
   private Tree tree;
   private OpenPSLEditor openEditor;
   private String selectedText = "";

   public SelectProjectFolder(Shell parentShell, OpenPSLEditor openEditor) {
      super(parentShell);
      this.openEditor = openEditor;
   }

   protected void configureShell(Shell shell) {
      super.configureShell(shell);
      shell.setText("Select a folder");
   }

   protected Control createDialogArea(Composite composite) {
      Composite parent = new Composite(composite, 0);
      GridLayout gridLayout = new GridLayout();
      gridLayout.numColumns = 1;
      parent.setLayout(gridLayout);
      GridData gridData = new GridData(768);
      gridData.heightHint = 300;
      gridData.widthHint = 300;
      parent.setLayoutData(gridData);
      parent.setBackground(new Color((Device)null, 255, 255, 255));
      this.tree = new Tree(parent, 4);
      gridData = new GridData(1808);
      this.tree.setLayoutData(gridData);
      this.tree.addSelectionListener(new SelectionListener() {
         public void widgetDefaultSelected(SelectionEvent e) {
         }

         public void widgetSelected(SelectionEvent e) {
            SelectProjectFolder.this.selectedText = "";
            TreeItem item = SelectProjectFolder.this.tree.getSelection()[0];

            for(TreeItem parentItem = item.getParentItem(); parentItem != null; parentItem = parentItem.getParentItem()) {
               SelectProjectFolder.this.selectedText = parentItem.getText() + "/" + SelectProjectFolder.this.selectedText;
            }

            SelectProjectFolder.this.selectedText = "/" + SelectProjectFolder.this.selectedText + item.getText();
         }
      });

      try {
         this.getProjectList();
      } catch (CoreException var6) {
         var6.printStackTrace();
      }

      return parent;
   }

   private void getProjectList() throws CoreException {
      IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();

      for(int i = 0; i < projects.length; ++i) {
         String projEnv = projects[i].getPersistentProperty(new QualifiedName("com.fnf.profile.ide", "com.fnf.profile.connection.environment"));
         if (projEnv != null && projEnv.equalsIgnoreCase(TableList.getEnvName())) {
            this.addProjectToTree(projects[i].getName(), projects[i].getLocation().toString());
         }
      }

   }

   private void addProjectToTree(String projectName, String projLocation) {
      TreeItem projItem = new TreeItem(this.tree, 0);
      projItem.setText(projectName);
      projItem.setImage(PlatformUI.getWorkbench().getSharedImages().getImage("IMG_OBJ_FOLDER"));
      this.loopThroughProject(projItem, projLocation);
   }

   private void loopThroughProject(TreeItem treeItem, String projLocation) {
      File proj = new File(projLocation);
      File[] files = proj.listFiles();

      for(int i = 0; i < files.length; ++i) {
         if (files[i].isDirectory()) {
            TreeItem item = new TreeItem(treeItem, 0);
            item.setText(files[i].getName());
            item.setImage(PlatformUI.getWorkbench().getSharedImages().getImage("IMG_OBJ_FOLDER"));
            this.loopThroughProject(item, files[i].getPath());
         }
      }

   }

   protected void okPressed() {
      this.openEditor.targetFolder.setText(this.selectedText);
      super.okPressed();
   }
}
