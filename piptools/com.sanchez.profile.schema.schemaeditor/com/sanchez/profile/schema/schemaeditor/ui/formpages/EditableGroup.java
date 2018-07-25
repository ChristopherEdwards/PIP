package com.sanchez.profile.schema.schemaeditor.ui.formpages;

import com.sanchez.profile.schema.schemaeditor.editors.AttributeEditor;
import com.sanchez.profile.schema.schemaeditor.editors.EntityEditor;
import com.sanchez.profile.schema.schemaeditor.ui.EntryBase;
import com.sanchez.profile.schema.schemaeditor.ui.FormEntry;
import com.sanchez.profile.schema.schemaeditor.ui.IFormTextListener;
import com.sanchez.profile.schema.schemaeditor.ui.PageModel;
import java.util.Iterator;
import java.util.Vector;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

public class EditableGroup extends PageModel {
   private String title;
   private String desc;
   private Vector formEntries = new Vector();
   private boolean dirty = false;
   private EditablePage page;
   private AttributeEditor aEditor = null;
   private EntityEditor eEditor = null;
   private Composite container;

   public EditableGroup(Object obj, Composite parent, Vector vector, String desc, String title, int groupSpan, String helpFile) {
      super(helpFile);
      if (title == null) {
         title = "";
      }

      if (desc == null) {
         desc = "";
      }

      this.page = (EditablePage)parent;
      if (obj instanceof AttributeEditor) {
         this.aEditor = (AttributeEditor)obj;
      }

      if (obj instanceof EntityEditor) {
         this.eEditor = (EntityEditor)obj;
      }

      Composite composite = new Composite(parent, 0);
      GridLayout gridLayout = new GridLayout();
      composite.setLayout(gridLayout);
      GridData gridData = new GridData(1808);
      gridData.verticalAlignment = 1024;
      gridData.horizontalSpan = groupSpan;
      composite.setLayoutData(gridData);
      Group group = new Group(composite, 0);
      group.setText(title);
      group.setFont(this.factory.getSchemaLabelFont());
      gridLayout = new GridLayout();
      group.setLayout(gridLayout);
      gridData = new GridData(768);
      gridData.horizontalSpan = groupSpan;
      group.setLayoutData(gridData);
      if (!desc.equals("")) {
         Label description = new Label(group, 0);
         description.setText(desc);
         gridData = new GridData(768);
         description.setLayoutData(gridData);
      }

      this.container = new Composite(group, 0);
      gridLayout = new GridLayout();
      gridLayout.numColumns = groupSpan * 2;
      this.container.setLayout(gridLayout);
      gridData = new GridData(1808);
      this.container.setLayoutData(gridData);
      Iterator iter = vector.iterator();

      while(iter.hasNext()) {
         final EntryBase entry = (EntryBase)iter.next();
         if (entry.getValue() != null) {
            if (entry.getTag() == "") {
               this.createSpace(this.container);
            } else {
               FormEntry formText = new FormEntry(obj, this.page, this.container, this.createEntry(this.container, entry, this.factory), entry);
               this.formEntries.add(formText);
               formText.addFormTextListener(new IFormTextListener() {
                  public void textValueChanged(FormEntry formEntry) {
                     entry.setValue(formEntry.getValue());
                  }

                  public void textDirty(FormEntry text) {
                     EditableGroup.this.dirty = true;
                     EditableGroup.this.page.setDirty(true);
                     EditableGroup.this.updateXML();
                  }
               });
            }
         }
      }

   }

   private void updateXML() {
      String xml;
      if (this.aEditor != null) {
         if (!this.aEditor.isDirty()) {
            xml = this.aEditor.getBase().getXMLString();
            this.aEditor.updateXMLEditor(xml);
         }
      } else if (this.eEditor != null && !this.eEditor.isDirty()) {
         xml = this.eEditor.getBase().getXMLString();
         this.eEditor.updateXMLEditor(xml);
      }

   }

   public void commitChanges(boolean newChange) {
      if (newChange) {
         Iterator iter = this.formEntries.iterator();

         while(iter.hasNext()) {
            FormEntry entry = (FormEntry)iter.next();
            entry.commit();
         }
      }

   }

   public boolean isDirty() {
      return this.dirty;
   }

   public Composite getContainer() {
      return this.container;
   }
}
