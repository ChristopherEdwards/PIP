package com.fnf;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class FidelityProfilePerspective implements IPerspectiveFactory {
   public void createInitialLayout(IPageLayout layout) {
      layout.addActionSet("org.eclipse.debug.ui.launchActionSet");
      String editorArea = layout.getEditorArea();
      IFolderLayout topLeft = layout.createFolder("topLeft", 1, 0.25F, editorArea);
      topLeft.addView("org.eclipse.ui.views.ResourceNavigator");
      topLeft.addPlaceholder("org.eclipse.ui.views.BookmarkView");
      IFolderLayout bottomLeft = layout.createFolder("bottomLeft", 4, 0.5F, "topLeft");
      bottomLeft.addView("org.eclipse.ui.views.ContentOutline");
      IFolderLayout bottomRight = layout.createFolder("bottomRight", 4, 0.72F, editorArea);
      bottomRight.addView("com.fnf.studio.views.FidelityConsoleView");
      bottomRight.addView("org.eclipse.ui.views.TaskList");
   }
}
