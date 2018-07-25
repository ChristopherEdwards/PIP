package com.sanchez.profile.schema.schemaeditor.wizards;

import com.sanchez.profile.schema.schemaeditor.SchemaEditorPlugin;
import com.sanchez.profile.schema.schemaeditor.ui.HelpWindow;
import com.sanchez.profile.schema.schemaeditor.ui.SchemaInfo;
import com.sanchez.profile.schema.schemaeditor.ui.formpages.Attribute;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class EntityPage4 extends WizardPage implements Listener {
   public Button checkBox;
   public Button button;
   public String newColumns;
   private Composite composite;
   private Attribute base = new Attribute();
   public String keys;
   private String columns;
   public SchemaInfo help;
   public EntityAttributeList attributeEntry = new EntityAttributeList();
   public WizardsUtils wizardUtil = new WizardsUtils();
   private Table table;
   public TableViewer tableViewer = null;
   private String attributeName_COLUMN = "Name";
   private String subScriptLiteral_COLUMN = "";
   private String fieldPosition_COLUMN = "Position";
   private String dataType_COLUMN = "Type";
   private String required_COLUMN;
   private String length_COLUMN;
   private String decimalPrecision_COLUMN;
   private String computedExpression_COLUMN;
   private String description_COLUMN;
   final String ATTRIBUTE_TYPE;
   final String ENTITY_TYPE;
   public String wizardType;
   IContainer folder;
   public HashMap map;
   private String[] columnNames;
   Text documentationText;

   public EntityPage4(String arg0, String wizardType) {
      super(arg0);
      this.required_COLUMN = this.base.getRequired().getTag();
      this.length_COLUMN = this.base.getLength().getTag();
      this.decimalPrecision_COLUMN = "Decimal";
      this.computedExpression_COLUMN = "";
      this.description_COLUMN = this.base.getDescription().getTag();
      this.ATTRIBUTE_TYPE = "attribute";
      this.ENTITY_TYPE = "entity";
      this.map = new HashMap();
      this.columnNames = new String[]{this.attributeName_COLUMN, this.subScriptLiteral_COLUMN, this.fieldPosition_COLUMN, this.dataType_COLUMN, this.required_COLUMN, this.length_COLUMN, this.decimalPrecision_COLUMN, this.computedExpression_COLUMN, this.description_COLUMN};
      this.map.put("Name", this.base.getAttributeName().getTag());
      this.map.put("Position", this.base.getFieldPosition().getTag());
      this.map.put("Type", this.base.getDataType().getTag());
      this.map.put("Decimal", this.base.getDecimalPrecision().getTag());
      this.help = new SchemaInfo(this.base.getHelpFileName());
      this.setDescription(SchemaEditorPlugin.getResourceString("wizard.entity.page4.description"));
      this.wizardType = wizardType;
   }

   public void createControl(Composite parent) {
      this.composite = new Composite(parent, 0);
      GridLayout gl = new GridLayout();
      this.composite.setLayout(gl);
      GridData gridData = new GridData(1808);
      this.composite.setLayoutData(gridData);
      this.createTable(this.composite);
      Label spaceLabel = new Label(this.composite, 131072);
      GridData gd = new GridData(32);
      spaceLabel.setLayoutData(gd);
      this.button = new Button(this.composite, 8);
      this.button.setText("Add a new column");
      this.button.addSelectionListener(new SelectionListener() {
         public void widgetDefaultSelected(SelectionEvent e) {
         }

         public void widgetSelected(SelectionEvent e) {
            EntityPage4.this.attributeEntry.addAttribute("", "", Integer.toString(EntityPage4.this.tableViewer.getTable().getItemCount()), "", false, "", "", "", "");
            EntityPage4.this.tableViewer.setInput(EntityPage4.this.attributeEntry);

            for(int i = 0; i < EntityPage4.this.tableViewer.getTable().getItemCount(); ++i) {
               TableItem item = EntityPage4.this.tableViewer.getTable().getItem(i);
               item.setBackground(new Color((Device)null, 255, 255, 206));
            }

            EntityPage4.this.tableViewer.refresh();
         }
      });
      gd = new GridData(768);
      gd.horizontalAlignment = 131072;
      this.button.setLayoutData(gd);
      if (this.wizardType.equals("attribute")) {
         this.folder = ((NewAttributeWizard)this.getWizard()).folder;
      } else if (this.wizardType.equals("entity")) {
         this.folder = ((NewEntityWizard)this.getWizard()).folder;
      }

      if (this.folder != null) {
         this.checkBox = new Button(this.composite, 32);
         this.checkBox.setText(SchemaEditorPlugin.getResourceString("wizard.entity.page4.checkbox.label"));
         gd = new GridData(32);
         this.checkBox.setLayoutData(gd);
         this.checkBox.setSelection(true);
      }

      this.createTableViewer();
      this.tableViewer.setContentProvider(new EntityContentProvider(this.attributeEntry, this.tableViewer));
      this.tableViewer.setLabelProvider(new EntityLabelProvider());
      this.setControl(this.composite);
   }

   private void createTable(Composite parent) {
      int style = 68356;
      this.table = new Table(parent, style);
      GridData gridData = new GridData(1808);
      gridData.grabExcessVerticalSpace = true;
      this.table.setLayoutData(gridData);
      this.table.setLinesVisible(true);
      this.table.setHeaderVisible(true);
      this.table.setVisible(true);

      for(final int i = 0; i < this.columnNames.length; ++i) {
         TableColumn column;
         if (!this.columnNames[i].equals(this.required_COLUMN)) {
            column = new TableColumn(this.table, 16384, i);
         } else {
            column = new TableColumn(this.table, 16777216, i);
            column.setAlignment(16777216);
         }

         column.setText(this.columnNames[i]);
         column.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
               EntityPage4.this.tableViewer.setSorter(new EntryTableSorter(i));
            }
         });
         if (!this.columnNames[i].equals(this.computedExpression_COLUMN) && !this.columnNames[i].equals(this.subScriptLiteral_COLUMN)) {
            if (this.columnNames[i].equals(this.description_COLUMN)) {
               column.setWidth(140);
            } else {
               column.setWidth(70);
            }
         } else {
            column.setWidth(0);
         }
      }

   }

   private void createTableViewer() {
      this.tableViewer = new TableViewer(this.table);
      this.tableViewer.setUseHashlookup(true);
      this.tableViewer.setColumnProperties(this.columnNames);
      CellEditor[] editors = new CellEditor[this.columnNames.length];
      TextCellEditor textEditor = new TextCellEditor(this.table);
      ((Text)textEditor.getControl()).setTextLimit(this.base.getAttributeName().getLimit());
      editors[0] = textEditor;
      textEditor = new TextCellEditor(this.table);
      ((Text)textEditor.getControl()).setTextLimit(this.base.getSubScriptLiteral().getLimit());
      editors[1] = textEditor;
      textEditor = new TextCellEditor(this.table);
      ((Text)textEditor.getControl()).setTextLimit(this.base.getFieldPosition().getLimit());
      editors[2] = textEditor;
      final Text text2 = (Text)textEditor.getControl();
      text2.addVerifyListener(new VerifyListener() {
         public void verifyText(VerifyEvent e) {
            if (text2.isFocusControl()) {
               e.doit = "0123456789".indexOf(e.text) >= 0;
            }

         }
      });
      editors[3] = new ComboBoxCellEditor(this.table, this.wizardUtil.getDataTypes(), 8);
      CCombo combo = (CCombo)editors[3].getControl();
      editors[4] = new CheckboxCellEditor(this.table);
      textEditor = new TextCellEditor(this.table);
      ((Text)textEditor.getControl()).setTextLimit(this.base.getLength().getLimit());
      final Text text5 = (Text)textEditor.getControl();
      text5.addVerifyListener(new VerifyListener() {
         public void verifyText(VerifyEvent e) {
            if (text5.isFocusControl()) {
               e.doit = "0123456789".indexOf(e.text) >= 0;
            }

         }
      });
      editors[5] = textEditor;
      textEditor = new TextCellEditor(this.table);
      ((Text)textEditor.getControl()).setTextLimit(this.base.getDecimalPrecision().getLimit());
      final Text text6 = (Text)textEditor.getControl();
      text6.addVerifyListener(new VerifyListener() {
         public void verifyText(VerifyEvent e) {
            if (text6.isFocusControl()) {
               e.doit = "0123456789".indexOf(e.text) >= 0;
            }

         }
      });
      editors[6] = textEditor;
      textEditor = new TextCellEditor(this.table);
      ((Text)textEditor.getControl()).setTextLimit(this.base.getComputedExpression().getLimit());
      editors[7] = textEditor;
      textEditor = new TextCellEditor(this.table);
      ((Text)textEditor.getControl()).setTextLimit(this.base.getDescription().getLimit());
      editors[8] = textEditor;
      combo.addSelectionListener(new SelectionListener() {
         public void widgetDefaultSelected(SelectionEvent e) {
         }

         public void widgetSelected(SelectionEvent e) {
            EntityPage4.this.table.forceFocus();
         }
      });
      this.tableViewer.setCellEditors(editors);
      this.tableViewer.setCellModifier(new EntityCellModifier(this));
   }

   public boolean canFlipToNextPage() {
      return false;
   }

   public void handleSelection(Event e) {
      if (e.button == 3) {
         System.out.println("right click = " + ((TableColumn)e.widget).getText());
      }

      HelpWindow win = new HelpWindow(this.getShell(), this.help.getHelp(((TableColumn)e.widget).getText()), ((TableColumn)e.widget).getText());
      win.open();
   }

   public void onEnterPage() {
      this.setDescription(SchemaEditorPlugin.getResourceString("wizard.entity.page.column.message"));
      SchemaEditorPlugin.list.clear();
      if (this.tableViewer.getInput() != null) {
         this.tableViewer.remove(this.attributeEntry);
      }

      if (this.wizardType.equals("entity")) {
         this.setTitle(SchemaEditorPlugin.getResourceString("wizard.entity.page.title") + " - " + ((NewEntityWizard)this.getWizard()).entityBase.getEntityName().getValue() + ".TBL");
         this.keys = ((NewEntityWizard)this.getWizard()).entityPage1.keysText.getText();
         this.columns = ((NewEntityWizard)this.getWizard()).entityPage3.columnsText.getText();
         this.attributeEntry.initData(((NewEntityWizard)this.getWizard()).entityBase, this.keys, this.columns);
      } else if (this.wizardType.equals("attribute")) {
         this.setTitle(SchemaEditorPlugin.getResourceString("wizard.attribute.page.title"));
         this.attributeEntry.addAttribute("", "", "0", "", false, "", "", "", "");
      }

      this.tableViewer.setInput(this.attributeEntry);

      for(int i = 0; i < this.tableViewer.getTable().getItemCount(); ++i) {
         TableItem item = this.tableViewer.getTable().getItem(i);
         item.setBackground(new Color((Device)null, 255, 255, 206));
      }

      this.tableViewer.refresh();
   }

   public List getColumnNames() {
      return Arrays.asList(this.columnNames);
   }

   public String[] getChoices(String property) {
      return this.dataType_COLUMN.equals(property) ? this.wizardUtil.getDataTypes() : new String[0];
   }

   public EntityAttributeList getAttributeEntry() {
      return this.attributeEntry;
   }

   public void setAttributeEntry(EntityAttributeList list) {
      this.attributeEntry = list;
   }

   public TableViewer getTableViewer() {
      return this.tableViewer;
   }

   public void handleEvent(Event event) {
      new Status(0, "not_used", 0, "", (Throwable)null);
      this.getWizard().getContainer().updateButtons();
   }

   public boolean isPageComplete() {
      Vector attributeVector = this.attributeEntry.getAttributes();
      Iterator iter = attributeVector.iterator();

      EntityTableEntry element;
      do {
         do {
            do {
               if (!iter.hasNext()) {
                  for(int i = 0; i < this.tableViewer.getTable().getItemCount(); ++i) {
                     TableItem item = this.tableViewer.getTable().getItem(i);
                     EntityTableEntry tableEntry = (EntityTableEntry)item.getData();
                     String col = tableEntry.getAttributeName() + "," + tableEntry.getFieldPosition() + "," + tableEntry.getDataType() + "," + tableEntry.isRequired() + "," + tableEntry.getLength() + "," + tableEntry.getDecimalPrecision() + "," + tableEntry.getDescription();
                     SchemaEditorPlugin.list.add(col);
                  }

                  return true;
               }

               element = (EntityTableEntry)iter.next();
            } while(element.getAttributeName() == null);
         } while(element.getAttributeName().length() == 0);

         int count = 0;

         for(int i = 0; i < this.tableViewer.getTable().getItemCount(); ++i) {
            TableItem loopItem = this.tableViewer.getTable().getItem(i);
            EntityTableEntry entry = (EntityTableEntry)loopItem.getData();
            if (entry.getAttributeName().trim().equalsIgnoreCase(element.getAttributeName())) {
               ++count;
            }

            if (count > 1) {
               MessageDialog.openError(this.getShell(), SchemaEditorPlugin.getResourceString("wizard.entity.page4.description.dialog.title"), MessageFormat.format(SchemaEditorPlugin.getResourceString("wizard.entity.page3.duplicateItem.message"), element.getAttributeName()));
               return false;
            }

            if (this.wizardType.equals("attribute")) {
               for(i = 0; i < SchemaEditorPlugin.colList.size(); ++i) {
                  String colName = SchemaEditorPlugin.colList.get(i).toString().split(",")[0];
                  if (colName.trim().equalsIgnoreCase(entry.getAttributeName().trim())) {
                     MessageDialog.openError(this.getShell(), SchemaEditorPlugin.getResourceString("wizard.entity.page4.description.dialog.title"), MessageFormat.format(SchemaEditorPlugin.getResourceString("wizard.entity.page3.existedItem.message") + ": " + colName.trim(), element.getAttributeName()));
                     return false;
                  }
               }
            }
         }

         if (element.getDataType().equals("")) {
            MessageDialog.openError(this.getShell(), SchemaEditorPlugin.getResourceString("wizard.entity.page4.datatype.dialog.title"), MessageFormat.format(SchemaEditorPlugin.getResourceString("wizard.entity.page4.datatype.dialog.message"), element.getAttributeName()));
            return false;
         }

         if (element.getLength().equals("")) {
            MessageDialog.openError(this.getShell(), SchemaEditorPlugin.getResourceString("wizard.entity.page4.length.dialog.title"), MessageFormat.format(SchemaEditorPlugin.getResourceString("wizard.entity.page4.length.dialog.message"), element.getAttributeName()));
            return false;
         }
      } while(!element.getDescription().equals(""));

      MessageDialog.openError(this.getShell(), SchemaEditorPlugin.getResourceString("wizard.entity.page4.description.dialog.title"), MessageFormat.format(SchemaEditorPlugin.getResourceString("wizard.entity.page4.description.dialog.message"), element.getAttributeName()));
      return false;
   }

   public void updateStatus(String message) {
      this.setMessage(message, 3);
      this.setPageComplete(message == null);
   }
}
