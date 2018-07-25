package com.fnf.studio.views;

import com.fnf.studio.StudioPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;

public class FidelityConsoleView extends ViewPart {
   public static final String CONSOLE_ID = "com.fnf.studio.views.FidelityConsoleView";
   private TextViewer viewer = null;
   private Document document = null;
   private Action action1;
   private Action action2;

   public void createPartControl(Composite parent) {
      this.viewer = new TextViewer(parent, 832);
      GridData viewerData = new GridData(1808);
      this.viewer.getControl().setLayoutData(viewerData);
      this.viewer.setEditable(false);
      this.makeActions();
      this.hookContextMenu();
      this.contributeToActionBars();
   }

   private void contributeToActionBars() {
      IActionBars bars = this.getViewSite().getActionBars();
      this.fillLocalToolBar(bars.getToolBarManager());
   }

   private void fillLocalToolBar(IToolBarManager manager) {
      manager.add(this.action1);
      manager.add(this.action2);
   }

   private void hookContextMenu() {
      MenuManager menuMgr = new MenuManager("#PopupMenu");
      menuMgr.setRemoveAllWhenShown(true);
      menuMgr.addMenuListener(new IMenuListener() {
         public void menuAboutToShow(IMenuManager manager) {
            FidelityConsoleView.this.fillContextMenu(manager);
         }
      });
      Menu menu = menuMgr.createContextMenu(this.viewer.getControl());
      this.viewer.getControl().setMenu(menu);
      this.getSite().registerContextMenu(menuMgr, this.viewer);
   }

   private void fillContextMenu(IMenuManager manager) {
      manager.add(this.action1);
      manager.add(this.action2);
      manager.add(new Separator("Additions"));
   }

   private void makeActions() {
      this.action1 = new Action() {
         public void run() {
            FidelityConsoleView.this.setOutputText("");
         }
      };
      this.action1.setText("Clear Fidelity Console");
      this.action1.setToolTipText("Clear Fidelity Console");
      this.action1.setImageDescriptor(StudioPlugin.getImageDescriptor("clear.gif"));
      this.action2 = new Action() {
         public void run() {
            FidelityConsoleView.this.copyAllToClipboard();
         }
      };
      this.action2.setText("Copy All");
      this.action2.setToolTipText("Copy All");
      this.action2.setImageDescriptor(StudioPlugin.getImageDescriptor("copy.gif"));
   }

   private void copyAllToClipboard() {
      Display display = this.viewer.getControl().getDisplay();
      Clipboard clipboard = new Clipboard(display);
      String textData = this.viewer.getTextWidget().getText();
      TextTransfer textTransfer = TextTransfer.getInstance();
      clipboard.setContents(new Object[]{textData}, new Transfer[]{textTransfer});
   }

   public void setFocus() {
   }

   public void setOutputText(String text) {
      this.document = new Document(text);
      this.viewer.setDocument(this.document);
   }
}
