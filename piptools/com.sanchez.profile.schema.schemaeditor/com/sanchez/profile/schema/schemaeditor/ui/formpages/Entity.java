package com.sanchez.profile.schema.schemaeditor.ui.formpages;

import com.sanchez.profile.schema.schemaeditor.SchemaEditorPlugin;
import com.sanchez.profile.schema.schemaeditor.ui.EntryBase;
import com.sanchez.profile.schema.schemaeditor.ui.STYPE;
import com.sanchez.profile.schema.schemaeditor.ui.XMLUtil;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Vector;

public class Entity {
   String helpFileName = SchemaEditorPlugin.getResourceString("entity.helpFileName");
   String rootName = SchemaEditorPlugin.getResourceString("entity.rootName");
   String descMainPage = "";
   Vector vMainG1 = new Vector();
   Vector vMainG2 = new Vector();
   String titleMainG1 = " " + SchemaEditorPlugin.getResourceString("entity.titleMainG1") + " ";
   String titleMainG2 = " " + SchemaEditorPlugin.getResourceString("entity.titleMainG2") + " ";
   String descMainG1 = "";
   String descMainG2;
   String descAuditPage;
   Vector vAudit;
   String titleAudit;
   String descAudit;
   String descProfileDBPage;
   Vector vProfileDBG1;
   Vector vProfileDBG2;
   String titleProfileDBG1;
   String titleProfileDBG2;
   String descProfileDBG1;
   String descProfileDBG2;
   String descCharacterScreenPage;
   Vector vCharacterScreenG1;
   Vector vCharacterScreenG2;
   String titleCharacterScreenG1;
   String titleCharacterScreenG2;
   String descCharacterScreenG1;
   String descCharacterScreenG2;
   String descDocumentationPage;
   Vector vDocumentation;
   String titleDocumentation;
   String descDocumentation;
   boolean lookupFILETYPE;
   boolean lookupSYSSN;
   boolean lookupRECTYPE;
   boolean lookupDEL;
   boolean lookupTYP;
   boolean lookupDOM;
   EntryBase entityName;
   EntryBase description;
   EntryBase entityType;
   EntryBase primaryKeys;
   EntryBase publishRoutine;
   EntryBase superEntity;
   EntryBase systemName;
   EntryBase profileDB;
   EntryBase attributeDelimiter;
   EntryBase global;
   EntryBase recordExistsNode;
   EntryBase query;
   EntryBase filer;
   EntryBase recordType;
   EntryBase attributeProtectionRoutine;
   EntryBase networkLocation;
   EntryBase audit;
   EntryBase enableLogging;
   EntryBase createDate;
   EntryBase createTime;
   EntryBase createUser;
   EntryBase modifyDate;
   EntryBase modifyTime;
   EntryBase modifyUser;
   EntryBase characterScreen;
   EntryBase localArrayName;
   EntryBase lookupLineOne;
   EntryBase lookupLineTwo;
   EntryBase deletionRestricted;
   EntryBase defaultHeading;
   EntryBase descendingOrder;
   EntryBase dataEntryPreProcessor;
   EntryBase restrictEntityMaintenance;
   EntryBase dataEntryScreen;
   EntryBase documentation;
   EntryBase emptyEntry;

   public Entity() {
      this.descMainG2 = this.descMainG1;
      this.descAuditPage = "";
      this.vAudit = new Vector();
      this.titleAudit = " " + SchemaEditorPlugin.getResourceString("entity.titleAudit") + " ";
      this.descAudit = "";
      this.descProfileDBPage = "";
      this.vProfileDBG1 = new Vector();
      this.vProfileDBG2 = new Vector();
      this.titleProfileDBG1 = " " + SchemaEditorPlugin.getResourceString("entity.titleProfileDBG1") + " ";
      this.titleProfileDBG2 = " " + SchemaEditorPlugin.getResourceString("entity.titleProfileDBG2") + " ";
      this.descProfileDBG1 = this.descMainG1;
      this.descProfileDBG2 = this.descMainG1;
      this.descCharacterScreenPage = "";
      this.vCharacterScreenG1 = new Vector();
      this.vCharacterScreenG2 = new Vector();
      this.titleCharacterScreenG1 = " " + SchemaEditorPlugin.getResourceString("entity.titleCharacterScreenG1") + " ";
      this.titleCharacterScreenG2 = " " + SchemaEditorPlugin.getResourceString("entity.titleCharacterScreenG2") + " ";
      this.descCharacterScreenG1 = this.descMainG1;
      this.descCharacterScreenG2 = this.descMainG1;
      this.descDocumentationPage = "";
      this.vDocumentation = new Vector();
      this.titleDocumentation = " " + SchemaEditorPlugin.getResourceString("entity.titleDocumentation") + " ";
      this.descDocumentation = "";
      this.entityName = new EntryBase(true, "Table Name:", "EntityName", "", true, false, "", STYPE.withLink, 2, 11, 1, 256);
      this.description = new EntryBase(true, "Description:", "Description", "", false, false, "", STYPE.withLink, 1, 11, 4, 40);
      this.entityType = new EntryBase(true, "Table Type:", "EntityType", "1", false, true, "", STYPE.withLink, 1, 12, 2, 1);
      this.primaryKeys = new EntryBase(true, "Primary Keys:", "PrimaryKeys", "", false, false, "", STYPE.withLink, 2, 11, 1, 100);
      this.publishRoutine = new EntryBase(false, "Publish Routine:", "PublishRoutine", "", false, false, "", STYPE.withLink, 1, 11, 4, 30);
      this.superEntity = new EntryBase(false, "Super Entity:", "SuperEntity", "", false, false, "", STYPE.withLink, 1, 11, 1, 12);
      this.systemName = new EntryBase(false, "System Name:", "SystemName", "", false, true, "", STYPE.withLink, 2, 12, 1, 3);
      this.profileDB = new EntryBase(false, "Profile DataBase:", "ProfileDB", (String)null, false, false, "", STYPE.withLink, 1, 0, 0, 0);
      this.attributeDelimiter = new EntryBase(false, "Attribute Delimiter:", "AttributeDelimiter", "124", false, true, "ProfileDB", STYPE.withLink, 1, 12, 2, 3);
      this.global = new EntryBase(false, "Global:", "Global", "", false, false, "ProfileDB", STYPE.withLink, 1, 11, 4, 40);
      this.recordExistsNode = new EntryBase(false, "Record Exists Node:", "RecordExistsNode", "", false, false, "ProfileDB", STYPE.withLink, 1, 11, 2, 6);
      this.query = new EntryBase(false, "Query:", "Query", "", false, false, "ProfileDB", STYPE.withLink, 2, 11, 4, 100);
      this.filer = new EntryBase(false, "Filer:", "Filer", "", false, false, "ProfileDB", STYPE.withLink, 1, 11, 1, 8);
      this.recordType = new EntryBase(false, "Record Type:", "RecordType", "1", false, true, "ProfileDB", STYPE.withLink, 1, 12, 2, 2);
      this.attributeProtectionRoutine = new EntryBase(false, "Attribute Protection Routine:", "AttributeProtectionRoutine", "", false, false, "ProfileDB", STYPE.withLink, 1, 11, 1, 4);
      this.networkLocation = new EntryBase(false, "Network Location:", "NetworkLocation", "0", false, true, "ProfileDB", STYPE.withLink, 1, 12, 2, 1);
      this.audit = new EntryBase(false, "Audit:", "Audit", (String)null, false, false, "", STYPE.withLink, 1, 0, 0, 0);
      this.enableLogging = new EntryBase(false, "Enable Logging", "EnableLogging", "0", false, false, "Audit", STYPE.withLink, 1, 14, 5, 1);
      this.createDate = new EntryBase(false, "Create Date:", "CreateDate", "", false, false, "Audit", STYPE.withLink, 1, 11, 1, 12);
      this.createTime = new EntryBase(false, "Create Time:", "CreateTime", "", false, false, "Audit", STYPE.withLink, 1, 11, 1, 12);
      this.createUser = new EntryBase(false, "Create User:", "CreateUser", "", false, false, "Audit", STYPE.withLink, 1, 11, 1, 12);
      this.modifyDate = new EntryBase(false, "Modify Date:", "ModifyDate", "", false, false, "Audit", STYPE.withLink, 1, 11, 1, 12);
      this.modifyTime = new EntryBase(false, "Modify Time:", "ModifyTime", "", false, false, "Audit", STYPE.withLink, 1, 11, 1, 12);
      this.modifyUser = new EntryBase(false, "Modify User:", "ModifyUser", "", false, false, "Audit", STYPE.withLink, 1, 11, 1, 12);
      this.characterScreen = new EntryBase(false, "Character Screen:", "CharacterScreen", (String)null, false, false, "", STYPE.withLink, 1, 0, 0, 0);
      this.localArrayName = new EntryBase(false, "Local Array Name:", "LocalArrayName", "f", false, false, "CharacterScreen", STYPE.withLink, 2, 11, 4, 40);
      this.lookupLineOne = new EntryBase(false, "Lookup Line1:", "LookupLineOne", "", false, false, "CharacterScreen", STYPE.withLink, 2, 11, 4, 200);
      this.lookupLineTwo = new EntryBase(false, "Lookup Line2", "LookupLineTwo", "", false, false, "CharacterScreen", STYPE.withLink, 2, 11, 4, 200);
      this.deletionRestricted = new EntryBase(false, "Deletion Restricted", "DeletionRestricted", "0", false, false, "CharacterScreen", STYPE.withLink, 2, 14, 5, 1);
      this.defaultHeading = new EntryBase(false, "Default Heading:", "DefaultHeading", "", false, false, "CharacterScreen", STYPE.withLink, 2, 11, 4, 78);
      this.descendingOrder = new EntryBase(false, "Descending Order", "DescendingOrder", "0", false, false, "CharacterScreen", STYPE.withLink, 2, 14, 5, 1);
      this.dataEntryPreProcessor = new EntryBase(false, "Data Entry Pre-processor:", "DataEntryPreProcessor", "", false, false, "CharacterScreen", STYPE.withLink, 2, 11, 4, 255);
      this.restrictEntityMaintenance = new EntryBase(false, "Restrict Entity Maintenance", "RestrictEntityMaintenance", "0", false, false, "CharacterScreen", STYPE.withLink, 1, 14, 5, 1);
      this.dataEntryScreen = new EntryBase(false, "Data Entry Screen:", "DataEntryScreen", "", false, false, "CharacterScreen", STYPE.withLink, 1, 11, 1, 12);
      this.documentation = new EntryBase(false, "", "Documentation", "", false, false, "", STYPE.withoutLink, 1, 11, 6, 8000);
      this.emptyEntry = new EntryBase(false, "", "", "", false, false, "", STYPE.withoutLink, 1, 0, 0, 0);
      this.initVectorMain();
      this.initVectorAudit();
      this.initVectorProfileDB();
      this.initVectorCharacterScreen();
      this.initVectorDocumentation();
   }

   private void initVectorDocumentation() {
      this.vDocumentation.addElement(this.documentation);
   }

   private void initVectorCharacterScreen() {
      this.vCharacterScreenG1.addElement(this.dataEntryPreProcessor);
      this.vCharacterScreenG1.addElement(this.emptyEntry);
      this.vCharacterScreenG1.addElement(this.emptyEntry);
      this.vCharacterScreenG1.addElement(this.deletionRestricted);
      this.vCharacterScreenG1.addElement(this.emptyEntry);
      this.vCharacterScreenG1.addElement(this.restrictEntityMaintenance);
      this.vCharacterScreenG1.addElement(this.emptyEntry);
      this.vCharacterScreenG1.addElement(this.descendingOrder);
      this.vCharacterScreenG1.addElement(this.emptyEntry);
      this.vCharacterScreenG2.addElement(this.localArrayName);
      this.vCharacterScreenG2.addElement(this.lookupLineOne);
      this.vCharacterScreenG2.addElement(this.lookupLineTwo);
      this.vCharacterScreenG2.addElement(this.defaultHeading);
      this.vCharacterScreenG2.addElement(this.dataEntryScreen);
   }

   private void initVectorProfileDB() {
      this.vProfileDBG1.addElement(this.global);
      this.vProfileDBG1.addElement(this.attributeDelimiter);
      this.vProfileDBG1.addElement(this.recordExistsNode);
      this.vProfileDBG1.addElement(this.recordType);
      this.vProfileDBG1.addElement(this.networkLocation);
      this.vProfileDBG2.addElement(this.query);
      this.vProfileDBG2.addElement(this.filer);
      this.vProfileDBG2.addElement(this.emptyEntry);
      this.vProfileDBG2.addElement(this.attributeProtectionRoutine);
      this.vProfileDBG2.addElement(this.emptyEntry);
   }

   private void initVectorAudit() {
      this.vAudit.addElement(this.enableLogging);
      this.vAudit.addElement(this.emptyEntry);
      this.vAudit.addElement(this.emptyEntry);
      this.vAudit.addElement(this.emptyEntry);
      this.vAudit.addElement(this.createDate);
      this.vAudit.addElement(this.modifyDate);
      this.vAudit.addElement(this.createTime);
      this.vAudit.addElement(this.modifyTime);
      this.vAudit.addElement(this.createUser);
      this.vAudit.addElement(this.modifyUser);
   }

   private void initVectorMain() {
      this.vMainG1.addElement(this.entityName);
      this.vMainG1.addElement(this.description);
      this.vMainG1.addElement(this.emptyEntry);
      this.vMainG1.addElement(this.systemName);
      this.vMainG1.addElement(this.superEntity);
      this.vMainG2.addElement(this.entityType);
      this.vMainG2.addElement(this.emptyEntry);
      this.vMainG2.addElement(this.primaryKeys);
      this.vMainG2.addElement(this.publishRoutine);
   }

   private void handleException(String string, String message) {
      System.out.println(message);
   }

   public String getXMLString() {
      StringBuffer sb = new StringBuffer();
      sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
      sb.append("<" + this.rootName + ">");
      new StringBuffer();
      String closeTag = "";
      String path = "";
      Field[] field4ThisClass = this.getClass().getDeclaredFields();

      for(int ii = 0; ii < field4ThisClass.length; ++ii) {
         String fieldType = field4ThisClass[ii].getType().toString();
         if (fieldType.indexOf("EntryBase") != -1) {
            String fieldName = field4ThisClass[ii].getName();
            if (fieldName.indexOf("_") != -1) {
               fieldName = fieldName.substring(fieldName.indexOf("_") + 1);
            }

            String first = fieldName.substring(0, 1).toUpperCase();
            String tagName = first + fieldName.substring(1);
            String getterMethodName = "get" + tagName;
            if (!getterMethodName.equals("getEmptyEntry")) {
               Method method = null;
               EntryBase entry = null;

               try {
                  method = this.getClass().getMethod(getterMethodName, (Class[])null);
               } catch (Exception var15) {
                  var15.printStackTrace();
                  System.err.println("Exception caught when creating method " + getterMethodName + "for class " + this.getClass().getName());
               }

               if (method != null) {
                  try {
                     entry = (EntryBase)method.invoke(this, (Object[])null);
                  } catch (Exception var14) {
                     this.handleException("Exception Exception caught when invoke a method: ", var14.getMessage());
                  }
               }

               if (entry.getValue() != null && path.equals(entry.getPath())) {
                  sb.append("<" + tagName + ">");
                  sb.append(this.validValueString(entry.getValue(), entry.getControlType()));
                  sb.append("</" + tagName + ">");
               } else {
                  sb.append(closeTag);
                  if (entry.getValue() == null) {
                     sb.append("<" + tagName + ">");
                     closeTag = "</" + tagName + ">";
                     path = tagName;
                  } else {
                     sb.append("<" + tagName + ">");
                     sb.append(this.validValueString(entry.getValue(), entry.getControlType()));
                     sb.append("</" + tagName + ">");
                     closeTag = "";
                     path = "";
                  }
               }
            }
         }
      }

      sb.append(closeTag);
      sb.append("</" + this.rootName + ">");
      return sb.toString();
   }

   private String validValueString(String string, int i) {
      return i == 15 ? "<![CDATA[" + string + "]]>" : XMLUtil.encodeXmlString(string);
   }

   public EntryBase getAttributeDelimiter() {
      return this.attributeDelimiter;
   }

   public EntryBase getAttributeProtectionRoutine() {
      return this.attributeProtectionRoutine;
   }

   public EntryBase getCreateDate() {
      return this.createDate;
   }

   public EntryBase getCreateTime() {
      return this.createTime;
   }

   public EntryBase getCreateUser() {
      return this.createUser;
   }

   public EntryBase getDataEntryPreProcessor() {
      return this.dataEntryPreProcessor;
   }

   public EntryBase getDataEntryScreen() {
      return this.dataEntryScreen;
   }

   public EntryBase getDefaultHeading() {
      return this.defaultHeading;
   }

   public EntryBase getDeletionRestricted() {
      return this.deletionRestricted;
   }

   public EntryBase getDescendingOrder() {
      return this.descendingOrder;
   }

   public EntryBase getDescription() {
      return this.description;
   }

   public EntryBase getDocumentation() {
      return this.documentation;
   }

   public EntryBase getEnableLogging() {
      return this.enableLogging;
   }

   public EntryBase getEntityName() {
      return this.entityName;
   }

   public EntryBase getEntityType() {
      return this.entityType;
   }

   public EntryBase getFiler() {
      return this.filer;
   }

   public EntryBase getLocalArrayName() {
      return this.localArrayName;
   }

   public boolean getLookupDEL() {
      return this.lookupDEL;
   }

   public boolean getLookupDOM() {
      return this.lookupDOM;
   }

   public boolean getLookupFILETYPE() {
      return this.lookupFILETYPE;
   }

   public EntryBase getLookupLineOne() {
      return this.lookupLineOne;
   }

   public EntryBase getLookupLineTwo() {
      return this.lookupLineTwo;
   }

   public boolean getLookupRECTYPE() {
      return this.lookupRECTYPE;
   }

   public boolean getLookupSYSSN() {
      return this.lookupSYSSN;
   }

   public boolean getLookupTYP() {
      return this.lookupTYP;
   }

   public EntryBase getGlobal() {
      return this.global;
   }

   public EntryBase getModifyDate() {
      return this.modifyDate;
   }

   public EntryBase getModifyTime() {
      return this.modifyTime;
   }

   public EntryBase getModifyUser() {
      return this.modifyUser;
   }

   public EntryBase getNetworkLocation() {
      return this.networkLocation;
   }

   public EntryBase getPrimaryKeys() {
      return this.primaryKeys;
   }

   public EntryBase getPublishRoutine() {
      return this.publishRoutine;
   }

   public EntryBase getQuery() {
      return this.query;
   }

   public EntryBase getRecordExistsNode() {
      return this.recordExistsNode;
   }

   public EntryBase getRecordType() {
      return this.recordType;
   }

   public EntryBase getRestrictEntityMaintenance() {
      return this.restrictEntityMaintenance;
   }

   public String getRootName() {
      return this.rootName;
   }

   public EntryBase getSuperEntity() {
      return this.superEntity;
   }

   public EntryBase getSystemName() {
      return this.systemName;
   }

   public void setArchiveFileList(EntryBase base) {
   }

   public void setArchiveKeys(EntryBase base) {
   }

   public void setAttributeDelimiter(EntryBase base) {
      this.attributeDelimiter = base;
   }

   public void setAttributeProtectionRoutine(EntryBase base) {
      this.attributeProtectionRoutine = base;
   }

   public void setCreateDate(EntryBase base) {
      this.createDate = base;
   }

   public void setCreateTime(EntryBase base) {
      this.createTime = base;
   }

   public void setCreateUser(EntryBase base) {
      this.createUser = base;
   }

   public void setDataEntryPreProcessor(EntryBase base) {
      this.dataEntryPreProcessor = base;
   }

   public void setDataEntryScreen(EntryBase base) {
      this.dataEntryScreen = base;
   }

   public void setDefaultHeading(EntryBase base) {
      this.defaultHeading = base;
   }

   public void setDeletionRestricted(EntryBase base) {
      this.deletionRestricted = base;
   }

   public void setDescendingOrder(EntryBase base) {
      this.descendingOrder = base;
   }

   public void setDescription(EntryBase base) {
      this.description = base;
   }

   public void setDocumentation(EntryBase base) {
      this.documentation = base;
   }

   public void setEnableLogging(EntryBase base) {
      this.enableLogging = base;
   }

   public void setEntityName(EntryBase base) {
      this.entityName = base;
   }

   public void setEntityType(EntryBase base) {
      this.entityType = base;
   }

   public void setFiler(EntryBase base) {
      this.filer = base;
   }

   public void setLocalArrayName(EntryBase base) {
      this.localArrayName = base;
   }

   public void setLookupLineOne(EntryBase base) {
      this.lookupLineOne = base;
   }

   public void setLookupLineTwo(EntryBase base) {
      this.lookupLineTwo = base;
   }

   public void setGlobal(EntryBase base) {
      this.global = base;
   }

   public void setModifyDate(EntryBase base) {
      this.modifyDate = base;
   }

   public void setModifyTime(EntryBase base) {
      this.modifyTime = base;
   }

   public void setModifyUser(EntryBase base) {
      this.modifyUser = base;
   }

   public void setNetworkLocation(EntryBase base) {
      this.networkLocation = base;
   }

   public void setPrimaryKeys(EntryBase base) {
      this.primaryKeys = base;
   }

   public void setPublishRoutine(EntryBase base) {
      this.publishRoutine = base;
   }

   public void setQuery(EntryBase base) {
      this.query = base;
   }

   public void setRecordExistsNode(EntryBase base) {
      this.recordExistsNode = base;
   }

   public void setRecordType(EntryBase base) {
      this.recordType = base;
   }

   public void setRestrictEntityMaintenance(EntryBase base) {
      this.restrictEntityMaintenance = base;
   }

   public void setRootName(String string) {
      this.rootName = string;
   }

   public void setSuperEntity(EntryBase base) {
      this.superEntity = base;
   }

   public void setSystemName(EntryBase base) {
      this.systemName = base;
   }

   public void setEntityNameValue(String value) {
      this.getEntityName().setValue(value);
   }

   public void setDescriptionValue(String value) {
      this.getDescription().setValue(value);
   }

   public void setEntityTypeValue(String value) {
      this.getEntityType().setValue(value);
   }

   public void setPrimaryKeysValue(String value) {
      this.getPrimaryKeys().setValue(value);
   }

   public void setPublishRoutineValue(String value) {
      this.getPublishRoutine().setValue(value);
   }

   public void setEnableLoggingValue(String value) {
      this.getEnableLogging().setValue(value);
   }

   public void setSuperEntityValue(String value) {
      this.getSuperEntity().setValue(value);
   }

   public void setSystemNameValue(String value) {
      this.getSystemName().setValue(value);
   }

   public void setCreateDateValue(String value) {
      this.getCreateDate().setValue(value);
   }

   public void setCreateTimeValue(String value) {
      this.getCreateTime().setValue(value);
   }

   public void setCreateUserValue(String value) {
      this.getCreateUser().setValue(value);
   }

   public void setModifyDateValue(String value) {
      this.getModifyDate().setValue(value);
   }

   public void setModifyTimeValue(String value) {
      this.getModifyTime().setValue(value);
   }

   public void setModifyUserValue(String value) {
      this.getModifyUser().setValue(value);
   }

   public void setAttributeDelimiterValue(String value) {
      this.getAttributeDelimiter().setValue(value);
   }

   public void setGlobalValue(String value) {
      this.getGlobal().setValue(value);
   }

   public void setRecordExistsNodeValue(String value) {
      this.getRecordExistsNode().setValue(value);
   }

   public void setQueryValue(String value) {
      this.getQuery().setValue(value);
   }

   public void setFilerValue(String value) {
      this.getFiler().setValue(value);
   }

   public void setRecordTypeValue(String value) {
      this.getRecordType().setValue(value);
   }

   public void setAttributeProtectionRoutineValue(String value) {
      this.getAttributeProtectionRoutine().setValue(value);
   }

   public void setLocalArrayNameValue(String value) {
      this.getLocalArrayName().setValue(value);
   }

   public void setNetworkLocationValue(String value) {
      this.getNetworkLocation().setValue(value);
   }

   public void setArchiveFileListValue(String value) {
   }

   public void setArchiveKeysValue(String value) {
   }

   public void setLookupLineOneValue(String value) {
      this.getLookupLineOne().setValue(value);
   }

   public void setLookupLineTwoValue(String value) {
      this.getLookupLineTwo().setValue(value);
   }

   public void setDeletionRestrictedValue(String value) {
      this.getDeletionRestricted().setValue(value);
   }

   public void setDefaultHeadingValue(String value) {
      this.getDefaultHeading().setValue(value);
   }

   public void setDescendingOrderValue(String value) {
      this.getDescendingOrder().setValue(value);
   }

   public void setDataEntryPreProcessorValue(String value) {
      this.getDataEntryPreProcessor().setValue(value);
   }

   public void setRestrictEntityMaintenanceValue(String value) {
      this.getRestrictEntityMaintenance().setValue(value);
   }

   public void setDataEntryScreenValue(String value) {
      this.getDataEntryScreen().setValue(value);
   }

   public void setDocumentationValue(String value) {
      this.getDocumentation().setValue(value);
   }

   public Vector getVAudit() {
      return this.vAudit;
   }

   public Vector getVDocumentation() {
      return this.vDocumentation;
   }

   public void setVAudit(Vector vector) {
      this.vAudit = vector;
   }

   public void setVDocumentation(Vector vector) {
      this.vDocumentation = vector;
   }

   public EntryBase getAudit() {
      return this.audit;
   }

   public EntryBase getCharacterScreen() {
      return this.characterScreen;
   }

   public EntryBase getEmptyEntry() {
      return this.emptyEntry;
   }

   public EntryBase getProfileDB() {
      return this.profileDB;
   }

   public void setAudit(EntryBase base) {
      this.audit = base;
   }

   public void setCharacterScreen(EntryBase base) {
      this.characterScreen = base;
   }

   public void setEmptyEntry(EntryBase base) {
      this.emptyEntry = base;
   }

   public void setProfileDB(EntryBase base) {
      this.profileDB = base;
   }

   public String getDescMainG1() {
      return this.descMainG1;
   }

   public String getDescMainG2() {
      return this.descMainG2;
   }

   public String getTitleMainG1() {
      return this.titleMainG1;
   }

   public String getTitleMainG2() {
      return this.titleMainG2;
   }

   public Vector getVMainG1() {
      return this.vMainG1;
   }

   public Vector getVMainG2() {
      return this.vMainG2;
   }

   public String getDescAudit() {
      return this.descAudit;
   }

   public String getTitleAudit() {
      return this.titleAudit;
   }

   public String getDescAuditPage() {
      return this.descAuditPage;
   }

   public String getDescMainPage() {
      return this.descMainPage;
   }

   public String getDescProfileDBPage() {
      return this.descProfileDBPage;
   }

   public String getDescProfileDBG1() {
      return this.descProfileDBG1;
   }

   public String getDescProfileDBG2() {
      return this.descProfileDBG2;
   }

   public String getTitleProfileDBG1() {
      return this.titleProfileDBG1;
   }

   public String getTitleProfileDBG2() {
      return this.titleProfileDBG2;
   }

   public Vector getVProfileDBG1() {
      return this.vProfileDBG1;
   }

   public Vector getVProfileDBG2() {
      return this.vProfileDBG2;
   }

   public String getDescCharacterScreenG1() {
      return this.descCharacterScreenG1;
   }

   public String getDescCharacterScreenG2() {
      return this.descCharacterScreenG2;
   }

   public String getDescCharacterScreenPage() {
      return this.descCharacterScreenPage;
   }

   public String getDescDocumentationPage() {
      return this.descDocumentationPage;
   }

   public String getTitleCharacterScreenG1() {
      return this.titleCharacterScreenG1;
   }

   public String getTitleCharacterScreenG2() {
      return this.titleCharacterScreenG2;
   }

   public Vector getVCharacterScreenG1() {
      return this.vCharacterScreenG1;
   }

   public Vector getVCharacterScreenG2() {
      return this.vCharacterScreenG2;
   }

   public String getDescDocumentation() {
      return this.descDocumentation;
   }

   public String getTitleDocumentation() {
      return this.titleDocumentation;
   }

   public String getHelpFileName() {
      return this.helpFileName;
   }
}
