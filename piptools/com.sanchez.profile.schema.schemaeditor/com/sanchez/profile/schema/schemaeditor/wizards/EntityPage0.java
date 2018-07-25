package com.sanchez.profile.schema.schemaeditor.wizards;

import com.sanchez.profile.schema.schemaeditor.SchemaEditorPlugin;
import com.sanchez.profile.schema.schemaeditor.ui.formpages.Entity;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

public class EntityPage0 extends WizardPage implements Listener {
   private String wizardType;
   private Button button;
   private Text entityNameText;
   private Text containerText;
   private Entity base;
   private WizardsUtils wizardsUtils;
   final String ATTRIBUTE_TYPE = "attribute";
   final String ENTITY_TYPE = "entity";
   private IContainer folder;
   Text documentationText;

   public EntityPage0(String arg0, Entity base, String wizardType) {
      super(arg0);
      this.base = base;
      if (wizardType.equals("entity")) {
         this.setTitle(SchemaEditorPlugin.getResourceString("wizard.entity.page.title"));
         this.setDescription(SchemaEditorPlugin.getResourceString("wizard.entity.page0.description"));
         this.updateStatus(SchemaEditorPlugin.getResourceString("wizard.entity.page0.description"));
      } else if (wizardType.equals("attribute")) {
         this.setTitle(SchemaEditorPlugin.getResourceString("wizard.attribute.page.title"));
         this.updateStatus(SchemaEditorPlugin.getResourceString("wizard.attribute.page0.description"));
      }

      this.wizardType = wizardType;
   }

   public void createControl(Composite parent) {
      this.wizardsUtils = new WizardsUtils();
      Composite composite = new Composite(parent, 0);
      GridLayout gl = new GridLayout();
      int ncol = 3;
      gl.numColumns = ncol;
      composite.setLayout(gl);
      Label label = new Label(composite, 131072);
      label.setText("Target folder:");
      this.containerText = new Text(composite, 2052);
      if (this.wizardType.equals("attribute")) {
         this.folder = ((NewAttributeWizard)this.getWizard()).folder;
      } else if (this.wizardType.equals("entity")) {
         this.folder = ((NewEntityWizard)this.getWizard()).folder;
      }

      this.containerText.setText(this.folder.getFullPath().toString());
      this.containerText.setEditable(false);
      GridData gd = new GridData(768);
      this.containerText.setLayoutData(gd);
      if (this.wizardType.equals("entity")) {
         this.button = new Button(composite, 8);
         this.button.setText("Browse...");
      } else {
         new Label(composite, 131072);
      }

      this.wizardsUtils.createLinkLabel(composite, this.base.getEntityName(), this.base);
      this.entityNameText = new Text(composite, 2048);
      this.entityNameText.setTextLimit(this.base.getEntityName().getLimit());
      gd = new GridData(768);
      this.entityNameText.setLayoutData(gd);
      if (this.wizardType.equals("attribute")) {
         this.entityNameText.setText(this.base.getEntityName().getValue());
         this.entityNameText.setEditable(false);
      }

      new Label(composite, 131072);
      if (this.wizardType.equals("entity")) {
         Label spaceLabel = new Label(composite, 131072);
         gd = new GridData(32);
         gd.horizontalSpan = 3;
         spaceLabel.setLayoutData(gd);
      }

      this.setControl(composite);
      if (this.wizardType.equals("entity")) {
         this.addListeners();
      }

   }

   private void addListeners() {
      this.entityNameText.addListener(2, this);
      this.entityNameText.addFocusListener(new FocusAdapter() {
         public void focusLost(FocusEvent e) {
            EntityPage0.this.entityNameText.setText(EntityPage0.this.entityNameText.getText().toUpperCase());
         }
      });
      this.button.addListener(13, this);
   }

   public boolean canFlipToNextPage() {
      return this.wizardsUtils.isTextNonEmpty(this.entityNameText) && this.entityNameText.getText().indexOf("\"") == -1 && this.entityNameText.getText().indexOf("'") == -1;
   }

   public void handleEvent(Event e) {
      if (e.widget == this.entityNameText) {
         this.dialogChanged();
      }

      if (e.widget == this.button) {
         this.handleBrowse();
      }

      new Status(0, "not_used", 0, "", (Throwable)null);
      this.getWizard().getContainer().updateButtons();
   }

   public IWizardPage getNextPage() {
      EntityPage4 page = null;
      if (this.wizardType.equals("attribute")) {
         page = ((NewAttributeWizard)this.getWizard()).entityPage4;
      } else if (this.wizardType.equals("entity")) {
         this.base.getEntityName().setValue(this.entityNameText.getText().trim());
         String container = this.containerText.getText();
         IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
         IResource resource = root.findMember(new Path(container));
         ((NewEntityWizard)this.getWizard()).folder = (IContainer)resource;
         page = ((NewEntityWizard)this.getWizard()).entityPage4;
      }

      page.onEnterPage();
      return page;
   }

   private void handleBrowse() {
      ContainerSelectionDialog dialog = new ContainerSelectionDialog(this.getShell(), this.folder, false, "Select new file container");
      if (dialog.open() == 0) {
         Object[] result = dialog.getResult();
         if (result.length == 1) {
            this.containerText.setText(result[0].toString());
         }
      }

   }

   private void dialogChanged() {
      String fileName = this.entityNameText.getText();
      if (fileName.trim().length() == 0) {
         this.updateStatus(SchemaEditorPlugin.getResourceString("wizard.entity.page0.entityName.missing"));
      } else if (fileName.indexOf("\"") == -1 && fileName.indexOf("'") == -1) {
         this.updateStatus((String)null);
      } else {
         this.updateStatus(SchemaEditorPlugin.getResourceString("wizard.entity.page0.entityName.invalid"));
      }
   }

   private void updateStatus(String message) {
      this.setMessage(message, 3);
      this.setPageComplete(message == null);
   }
}
