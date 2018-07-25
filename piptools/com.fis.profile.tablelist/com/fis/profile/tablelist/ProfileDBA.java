package com.fis.profile.tablelist;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class ProfileDBA implements IPerspectiveFactory {
   public void createInitialLayout(IPageLayout layout) {
      layout.setEditorAreaVisible(false);
   }
}
