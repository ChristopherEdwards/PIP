package com.sanchez.profile.schema.schemaeditor.ui.formpages;

import com.sanchez.profile.schema.schemaeditor.SchemaEditorPlugin;
import com.sanchez.profile.schema.schemaeditor.editors.AttributeEditor;
import com.sanchez.profile.schema.schemaeditor.editors.EntityEditor;
import com.sanchez.profile.schema.schemaeditor.ui.FieldFactory;
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

public class EditablePage extends Composite {
   private Composite parent;
   private String headerString = "";
   private String pDesc = "";
   private Vector v1 = new Vector();
   private Vector v2 = new Vector();
   private Vector v3 = new Vector();
   private Vector v4 = new Vector();
   private String gTitle1;
   private String gTitle2;
   private String gTitle3;
   private String gDesc1;
   private String gDesc2;
   private String gDesc3;
   FieldFactory factory = new FieldFactory();
   private EditableGroup group1 = null;
   private EditableGroup group2 = null;
   private EditableGroup group3 = null;
   private boolean dirty = false;
   private boolean valid = true;
   private String helpFile;

   public EditablePage(AttributeEditor editor, Composite parent, String headerString, String pDesc, Vector v1, String gTitle1, String gDesc1, int gCols1, Vector v2, String gTitle2, String gDesc2, int gCols2, Vector v3, String gTitle3, String gDesc3, int gCols3, String helpFile) {
      super(parent, 0);
      this.parent = parent;
      this.headerString = headerString;
      this.pDesc = pDesc;
      this.v1 = v1;
      this.gDesc1 = gDesc1;
      this.gTitle1 = gTitle1;
      this.v2 = v2;
      this.gDesc2 = gDesc2;
      this.gTitle2 = gTitle2;
      this.v3 = v3;
      this.gDesc3 = gDesc3;
      this.gTitle3 = gTitle3;
      this.helpFile = helpFile;
      GridLayout gridLayout = new GridLayout();
      int colNum = 2;
      gridLayout.numColumns = colNum;
      this.setLayout(gridLayout);
      GridData gridData = new GridData(768);
      this.setLayoutData(gridData);
      this.createClient(editor, this, v1, gCols1, v2, gCols2, v3, gCols3);
   }

   public EditablePage(EntityEditor editor, Composite parent, String headerString, String pDesc, Vector v1, String gTitle1, String gDesc1, int gCols1, Vector v2, String gTitle2, String gDesc2, int gCols2, Vector v3, String gTitle3, String gDesc3, int gCols3, String helpFile) {
      super(parent, 0);
      this.parent = parent;
      this.headerString = headerString;
      this.pDesc = pDesc;
      this.v1 = v1;
      this.gDesc1 = gDesc1;
      this.gTitle1 = gTitle1;
      this.v2 = v2;
      this.gDesc2 = gDesc2;
      this.gTitle2 = gTitle2;
      this.v3 = v3;
      this.gDesc3 = gDesc3;
      this.gTitle3 = gTitle3;
      this.helpFile = helpFile;
      GridLayout gridLayout = new GridLayout();
      int colNum = 2;
      gridLayout.numColumns = colNum;
      this.setLayout(gridLayout);
      GridData gridData = new GridData(768);
      this.setLayoutData(gridData);
      this.createClient(editor, this, v1, gCols1, v2, gCols2, v3, gCols3);
   }

   public void createClient(Object obj, Composite parent, Vector v1, int num1, Vector v2, int num2, Vector v3, int num3) {
      this.v1 = v1;
      this.v2 = v2;
      this.v3 = v3;
      Display display = parent.getDisplay();
      final String headerString = this.headerString;
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
      description.setText(this.pDesc);
      gd = new GridData(768);
      gd.horizontalSpan = 2;
      description.setLayoutData(gd);
      byte groupSpan;
      if (v1 != null) {
         if (num1 == 21) {
            groupSpan = 1;
         } else {
            groupSpan = 2;
         }

         this.group1 = new EditableGroup(obj, parent, v1, this.gDesc1, this.gTitle1, groupSpan, this.helpFile);
      }

      if (v2 != null) {
         if (num2 == 21) {
            groupSpan = 1;
         } else {
            groupSpan = 2;
         }

         this.group2 = new EditableGroup(obj, parent, v2, this.gDesc2, this.gTitle2, groupSpan, this.helpFile);
      }

      if (v3 != null) {
         if (num3 == 21) {
            groupSpan = 1;
         } else {
            groupSpan = 2;
         }

         this.group3 = new EditableGroup(obj, parent, v3, this.gDesc3, this.gTitle3, groupSpan, this.helpFile);
      }

   }

   public void updateClient(Object obj, Vector v1, int gCols1, Vector v2, int gCols2, Vector v3, int gCols3) {
      this.v1 = v1;
      this.v2 = v2;
      this.v3 = v3;
      this.layout();
      Control[] children = this.getChildren();

      for(int i = 0; i < children.length; ++i) {
         children[i].dispose();
      }

      this.createClient(obj, this, v1, gCols1, v2, gCols2, v3, gCols3);
      this.layout();
   }

   public boolean isDirty() {
      return this.dirty;
   }

   public void setDirty(boolean newDirty) {
      this.dirty = newDirty;
   }

   public void commitChanges(boolean onSave) {
      if (this.group1 != null) {
         this.group1.commitChanges(onSave);
      }

      if (this.group2 != null) {
         this.group2.commitChanges(onSave);
      }

      if (this.group3 != null) {
         this.group3.commitChanges(onSave);
      }

   }

   public boolean isValid() {
      return this.valid;
   }

   public void setValid(boolean b) {
      this.valid = b;
   }

   public EditableGroup getGroup1() {
      return this.group1;
   }

   public EditableGroup getGroup2() {
      return this.group2;
   }

   public EditableGroup getGroup3() {
      return this.group3;
   }
}
