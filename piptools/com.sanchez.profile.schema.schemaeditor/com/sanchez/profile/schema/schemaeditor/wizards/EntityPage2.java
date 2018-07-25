package com.sanchez.profile.schema.schemaeditor.wizards;

import com.sanchez.profile.schema.schemaeditor.SchemaEditorPlugin;
import com.sanchez.profile.schema.schemaeditor.ui.formpages.Entity;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class EntityPage2 extends WizardPage implements Listener {
   private Entity base;
   private WizardsUtils wizardsUtils;
   final String ATTRIBUTE_TYPE = "attribute";
   final String ENTITY_TYPE = "entity";
   private String wizardType;
   Text documentationText;

   public EntityPage2(String arg0, Entity base, String wizardType) {
      super(arg0);
      this.base = base;
      this.setDescription(SchemaEditorPlugin.getResourceString("wizard.entity.page2.description"));
      this.wizardType = wizardType;
   }

   public void onEnterPage() {
      if (this.wizardType.equals("entity")) {
         this.setTitle(SchemaEditorPlugin.getResourceString("wizard.entity.page.title") + " - " + this.base.getEntityName().getValue() + ".TBL");
      }

      if (this.wizardType.equals("attribute")) {
         this.setTitle(SchemaEditorPlugin.getResourceString("wizard.attribute.page.title"));
      }

   }

   public void createControl(Composite parent) {
      this.wizardsUtils = new WizardsUtils();
      Composite composite = new Composite(parent, 0);
      GridLayout gl = new GridLayout();
      int ncol = 1;
      gl.numColumns = ncol;
      composite.setLayout(gl);
      Group group = new Group(composite, 0);
      group.setLayout(new GridLayout());
      group.setText("Documentation");
      group.setLayoutData(new GridData(1808));
      this.documentationText = new Text(group, 2818);
      this.documentationText.setTextLimit(this.base.getDocumentation().getLimit());
      this.documentationText.setLayoutData(new GridData(1808));
      if (this.wizardType.equals("attribute")) {
         this.documentationText.setText(this.base.getDescription().getValue());
         this.documentationText.setEditable(false);
      }

      this.setControl(composite);
      this.addListeners();
   }

   private void addListeners() {
      this.documentationText.addListener(2, this);
   }

   public boolean canFlipToNextPage() {
      if (this.wizardType.equals("attribute")) {
         return true;
      } else {
         return this.wizardsUtils.isTextNonEmpty(this.documentationText);
      }
   }

   public void handleEvent(Event e) {
      new Status(0, "not_used", 0, "", (Throwable)null);
      this.getWizard().getContainer().updateButtons();
   }

   public IWizardPage getNextPage() {
      EntityPage3 page = null;
      if (this.wizardType.equals("entity")) {
         this.base.getDocumentation().setValue(this.documentationText.getText().trim());
         page = ((NewEntityWizard)this.getWizard()).entityPage3;
      } else if (this.wizardType.equals("attribute")) {
         page = ((NewAttributeWizard)this.getWizard()).entityPage3;
      }

      page.onEnterPage();
      return page;
   }
}
