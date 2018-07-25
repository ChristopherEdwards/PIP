package com.sanchez.profile.schema.schemaeditor.ui.formpages;

import com.sanchez.profile.schema.schemaeditor.SchemaEditorPlugin;
import com.sanchez.profile.schema.schemaeditor.editors.EntityEditor;
import com.sanchez.profile.schema.schemaeditor.ui.EntryBase;
import java.util.Iterator;
import java.util.Vector;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class ColumnDetails extends Composite {
   private Composite parent;
   private Vector vector;
   private String helpFile = "";
   private MasterDetailedTree tree1 = null;
   private TableKeysTree tree2 = null;
   private TableKeysTree tree3 = null;
   private TableKeysTree tree4 = null;
   private boolean dirty = false;

   public ColumnDetails(EntityEditor editor, Composite parent, Vector mainVector, String helpFile) {
      super(parent, 0);
      this.parent = parent;
      this.vector = mainVector;
      this.helpFile = helpFile;
      GridLayout gridLayout = new GridLayout();
      int colNum = 2;
      gridLayout.numColumns = colNum;
      this.setLayout(gridLayout);
      GridData gridData = new GridData(768);
      this.setLayoutData(gridData);
      this.createClient(editor, this, mainVector);
   }

   public void createClient(Object obj, Composite parent, Vector vector) {
      String tableName = "";
      String tableDesc = "";
      Iterator ite = vector.iterator();

      while(ite.hasNext()) {
         EntryBase entry = (EntryBase)ite.next();
         if (entry.getTag().equalsIgnoreCase("EntityName")) {
            tableName = entry.getValue().toUpperCase();
         } else if (entry.getTag().equalsIgnoreCase("Description")) {
            tableDesc = entry.getValue();
         }
      }

      Display display = parent.getDisplay();
      final String headerString = tableName + "--" + tableDesc;
      ImageDescriptor descriptor = SchemaEditorPlugin.getImageDescriptor("title.gif");
      final Image image = descriptor.createImage();
      GC gc = new GC(image);
      Canvas headerContainer = new Canvas(parent, 0);
      FontData fd = new FontData();
      fd.setStyle(1);
      fd.setHeight(12);
      Font ft = new Font(display, fd);
      headerContainer.setFont(ft);
      headerContainer.setForeground(display.getSystemColor(1));
      headerContainer.setSize(100, 10);
      headerContainer.addPaintListener(new PaintListener() {
         public void paintControl(PaintEvent e) {
            e.gc.drawImage(image, 0, 0);
            e.gc.drawText(headerString, 1, 3, true);
         }
      });
      gc.dispose();
      GridData gd = new GridData(768);
      gd.horizontalSpan = 2;
      gd.heightHint = 26;
      headerContainer.setLayoutData(gd);
      Label description = new Label(parent, 0);
      description.setText("");
      gd = new GridData(768);
      gd.horizontalSpan = 2;
      description.setLayoutData(gd);
      TabFolder tabFolder = new TabFolder(parent, 0);
      gd = new GridData(768);
      tabFolder.setLayoutData(gd);
      Composite colTab = new Composite(tabFolder, 0);
      GridLayout gl = new GridLayout();
      gl.numColumns = 2;
      colTab.setLayout(gl);
      this.tree1 = new MasterDetailedTree(obj, colTab, 1, tableName, this.helpFile);
      TabItem tabItem1 = new TabItem(tabFolder, 0);
      tabItem1.setText("Column Information");
      tabItem1.setControl(colTab);
      Composite indexTab = new Composite(tabFolder, 0);
      gl = new GridLayout();
      gl.numColumns = 1;
      indexTab.setLayout(gl);
      String[] indexColumnProps = new String[]{"Index Description", "Allow NULL Value", "Order By", "Supertype File Name", "Query Definition #1", "Query Definition #2", "DAYEND Index Flag", "Convert to Uppercase", "Global Name"};
      String[] foreignColumnProps = new String[]{"Primary Keys", "Maximum Occurrences in Referencing Table", "Minimum Occurrences in Referencing Table", "Maximum Occurrences in Foreign Table", "Minimum Occurrences in Foreign Table", "Foreign File Name", "Update Constraint", "Delete Constraint"};
      String[] var10000 = new String[]{"Description", "Journal Table Name", "Effective-Dated Option", "Transaction Type", "Processing Mode", "Exclude Columns", "Include Columns", "Priority (processing order)", "Query Condition", "Query Condition", "Supertype File Name"};
      this.tree2 = new TableKeysTree(obj, indexTab, 1, tableName, "Index Column", "Index Name", indexColumnProps, this.helpFile);
      TabItem tabItem2 = new TabItem(tabFolder, 0);
      tabItem2.setText("Index Column");
      tabItem2.setControl(indexTab);
      Composite foreignTab = new Composite(tabFolder, 0);
      gl = new GridLayout();
      gl.numColumns = 1;
      foreignTab.setLayout(gl);
      this.tree3 = new TableKeysTree(obj, foreignTab, 1, tableName, "Foreign Keys Column", "Foreign Keys", foreignColumnProps, this.helpFile);
      TabItem tabItem3 = new TabItem(tabFolder, 0);
      tabItem3.setText("Foreign Keys Column");
      tabItem3.setControl(foreignTab);
      Composite journalTab = new Composite(tabFolder, 0);
      gl = new GridLayout();
      gl.numColumns = 1;
      journalTab.setLayout(gl);
      this.tree4 = new TableKeysTree(obj, journalTab, 1, tableName, "Journal Column", "Journal Name", foreignColumnProps, this.helpFile);
      TabItem tabItem4 = new TabItem(tabFolder, 0);
      tabItem4.setText("Journal Column");
      tabItem4.setControl(journalTab);
   }

   public void updateClient(Object obj, Vector mainVector) {
      this.layout();
      Control[] children = this.getChildren();

      for(int i = 0; i < children.length; ++i) {
         children[i].dispose();
      }

      this.createClient(obj, this, mainVector);
      this.layout();
   }

   public boolean isDirty() {
      return this.dirty;
   }

   public void setDirty(boolean newDirty) {
      this.dirty = newDirty;
   }
}
