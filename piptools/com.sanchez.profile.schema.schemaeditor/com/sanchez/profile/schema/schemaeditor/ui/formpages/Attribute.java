package com.sanchez.profile.schema.schemaeditor.ui.formpages;

import com.sanchez.profile.schema.schemaeditor.SchemaEditorPlugin;
import com.sanchez.profile.schema.schemaeditor.ui.EntryBase;
import com.sanchez.profile.schema.schemaeditor.ui.STYPE;
import com.sanchez.profile.schema.schemaeditor.ui.XMLUtil;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Vector;

public class Attribute {
   String helpFileName = SchemaEditorPlugin.getResourceString("attribute.helpFileName");
   String rootName = SchemaEditorPlugin.getResourceString("attribute.hrootName");
   String columnName = "";
   String descMainPage = "";
   Vector vMainG1 = new Vector();
   Vector vMainG2 = new Vector();
   Vector vMainG3 = new Vector();
   String titleMainG1 = " " + SchemaEditorPlugin.getResourceString("attribute.titleMainG1") + " ";
   String titleMainG2 = " " + SchemaEditorPlugin.getResourceString("attribute.titleMainG2") + " ";
   String titleMainG3 = " " + SchemaEditorPlugin.getResourceString("attribute.titleMainG3") + " ";
   String descMainG1 = "";
   String descMainG2;
   String descMainG3;
   String descCharacterPage;
   Vector vCharacterScreen;
   String titleCharacterScreen;
   String descCharacterScreen;
   String descProfilePage;
   Vector vProfileDB;
   Vector vProfileDBG1;
   Vector vProfileDBG2;
   Vector vProfileDBG3;
   String titleProfileDBG1;
   String titleProfileDBG2;
   String titleProfileDBG3;
   String descProfileDBG1;
   String descProfileDBG2;
   String descProfileDBG3;
   String descDocumentationPage;
   Vector vDocumentation;
   String titleDocumentation;
   String descDocumentation;
   Vector vPSLCode;
   boolean lookupFILETYPE;
   boolean lookupSYSSN;
   boolean lookupRECTYPE;
   boolean lookupDEL;
   boolean lookupTYP;
   boolean lookupDOM;
   EntryBase entityName;
   EntryBase attributeName;
   EntryBase description;
   EntryBase required;
   EntryBase length;
   EntryBase decimalPrecision;
   EntryBase dataType;
   EntryBase maximumValue;
   EntryBase minimumValue;
   EntryBase defaultValue;
   EntryBase computedExpression;
   EntryBase isSerialValue;
   EntryBase lookupEntity;
   EntryBase nullIndicator;
   EntryBase masterAttribute;
   EntryBase domain;
   EntryBase profileDB;
   EntryBase patternMatch;
   EntryBase fieldPosition;
   EntryBase subScriptLiteral;
   EntryBase subFieldPrefix;
   EntryBase subFieldSuffix;
   EntryBase subFieldPosition;
   EntryBase subFieldTag;
   EntryBase characterScreen;
   EntryBase postProcessor;
   EntryBase preProcessor;
   EntryBase dataEntryPostProcessor;
   EntryBase dataEntryPreProcessor;
   EntryBase displaySize;
   EntryBase reportHeader;
   EntryBase documentation;
   EntryBase pSLCode;
   EntryBase emptyEntry;

   public Attribute() {
      this.descMainG2 = this.descMainG1;
      this.descMainG3 = this.descMainG1;
      this.descCharacterPage = this.descMainPage;
      this.vCharacterScreen = new Vector();
      this.titleCharacterScreen = " " + SchemaEditorPlugin.getResourceString("attribute.titleCharacterScreen") + " ";
      this.descCharacterScreen = "";
      this.descProfilePage = this.descMainPage;
      this.vProfileDB = new Vector();
      this.vProfileDBG1 = new Vector();
      this.vProfileDBG2 = new Vector();
      this.vProfileDBG3 = new Vector();
      this.titleProfileDBG1 = " " + SchemaEditorPlugin.getResourceString("attribute.titleProfileDBG1") + " ";
      this.titleProfileDBG2 = " " + SchemaEditorPlugin.getResourceString("attribute.titleProfileDBG2") + " ";
      this.titleProfileDBG3 = " " + SchemaEditorPlugin.getResourceString("attribute.titleProfileDBG3") + " ";
      this.descProfileDBG1 = this.descMainG1;
      this.descProfileDBG2 = this.descMainG1;
      this.descProfileDBG3 = this.descMainG1;
      this.descDocumentationPage = this.descMainPage;
      this.vDocumentation = new Vector();
      this.titleDocumentation = " " + SchemaEditorPlugin.getResourceString("attribute.titleDocumentation") + " ";
      this.descDocumentation = "";
      this.vPSLCode = new Vector();
      this.entityName = new EntryBase(true, "Table Name:", "TableName", "", true, false, "", STYPE.withLink, 1, 11, 1, 256);
      this.attributeName = new EntryBase(true, "Column Name:", "ColumnName", "", true, false, "", STYPE.withLink, 1, 11, 1, 256);
      this.description = new EntryBase(true, "Description:", "Description", "", false, false, "", STYPE.withLink, 1, 11, 4, 40);
      this.required = new EntryBase(false, "Required", "Required", "", false, false, "", STYPE.withLink, 1, 14, 5, 1);
      this.length = new EntryBase(true, "Length:", "Length", "", false, false, "", STYPE.withLink, 1, 11, 2, 5);
      this.decimalPrecision = new EntryBase(false, "Decimal Precision:", "DecimalPrecision", "", false, false, "", STYPE.withLink, 1, 11, 2, 2);
      this.dataType = new EntryBase(true, "Data Type:", "DataType", "", false, true, "", STYPE.withLink, 1, 12, 1, 1);
      this.maximumValue = new EntryBase(false, "Maximum Value:", "MaximumValue", "", false, false, "", STYPE.withLink, 1, 11, 4, 25);
      this.minimumValue = new EntryBase(false, "Minimum Value:", "MinimumValue", "", false, false, "", STYPE.withLink, 1, 11, 4, 25);
      this.defaultValue = new EntryBase(false, "Default Value:", "DefaultValue", "", false, false, "", STYPE.withLink, 2, 11, 4, 58);
      this.computedExpression = new EntryBase(false, "Computed Expression:", "ComputedExpression", "", false, false, "", STYPE.withLink, 2, 11, 4, 255);
      this.isSerialValue = new EntryBase(false, "Serial Value?", "isSerialValue", "", false, false, "", STYPE.withLink, 1, 14, 5, 1);
      this.lookupEntity = new EntryBase(false, "Lookup Table:", "LookupTable", "", false, false, "", STYPE.withLink, 2, 11, 4, 255);
      this.nullIndicator = new EntryBase(false, "Treat Null As Blank", "NullIndicator", "", false, false, "", STYPE.withLink, 1, 14, 5, 1);
      this.masterAttribute = new EntryBase(false, "Master Attribute:", "MasterAttribute", "", false, false, "", STYPE.withLink, 1, 11, 1, 12);
      this.domain = new EntryBase(false, "Domain:", "Domain", "", false, true, "", STYPE.withLink, 1, 12, 1, 20);
      this.profileDB = new EntryBase(false, "Profile DataBase:", "ProfileDB", (String)null, false, false, "", STYPE.withLink, 1, 0, 0, 0);
      this.patternMatch = new EntryBase(false, "M Pattern Match:", "MPatternMatch", "", false, false, "ProfileDB", STYPE.withLink, 2, 11, 4, 60);
      this.fieldPosition = new EntryBase(false, "Field Position:", "FieldPosition", "", false, false, "ProfileDB", STYPE.withLink, 1, 11, 2, 2);
      this.subScriptLiteral = new EntryBase(false, "Sub-script Literal:", "SubScriptLiteral", "", false, false, "ProfileDB", STYPE.withLink, 1, 11, 4, 26);
      this.subFieldPrefix = new EntryBase(false, "Sub-field Prefix:", "SubFieldPrefix", "", false, true, "ProfileDB", STYPE.withLink, 1, 12, 2, 3);
      this.subFieldSuffix = new EntryBase(false, "Sub-field Suffix:", "SubFieldSuffix", "", false, true, "ProfileDB", STYPE.withLink, 1, 12, 2, 3);
      this.subFieldPosition = new EntryBase(false, "Sub-field Position:", "SubFieldPosition", "", false, false, "ProfileDB", STYPE.withLink, 1, 11, 2, 2);
      this.subFieldTag = new EntryBase(false, "Sub-field Tag:", "SubFieldTag", "", false, false, "ProfileDB", STYPE.withLink, 1, 11, 1, 12);
      this.characterScreen = new EntryBase(false, "Character Screen:", "CharacterScreen", (String)null, false, false, "", STYPE.withLink, 1, 0, 0, 0);
      this.postProcessor = new EntryBase(false, "Post-processor:", "PostProcessor", "", false, false, "CharacterScreen", STYPE.withLink, 2, 11, 4, 58);
      this.preProcessor = new EntryBase(false, "Pre-processor:", "PreProcessor", "", false, false, "CharacterScreen", STYPE.withLink, 2, 11, 4, 58);
      this.dataEntryPostProcessor = new EntryBase(false, "Data Entry Post-processor:", "DataEntryPostProcessor", "", false, false, "CharacterScreen", STYPE.withLink, 2, 11, 4, 255);
      this.dataEntryPreProcessor = new EntryBase(false, "Data Entry Pre-processor:", "DataEntryPreProcessor", "", false, false, "CharacterScreen", STYPE.withLink, 2, 11, 4, 255);
      this.displaySize = new EntryBase(false, "Display Size:", "DisplaySize", "", false, false, "CharacterScreen", STYPE.withLink, 2, 11, 2, 3);
      this.reportHeader = new EntryBase(false, "Report Header:", "ReportHeader", "", false, false, "CharacterScreen", STYPE.withLink, 1, 11, 4, 40);
      this.documentation = new EntryBase(false, "", "Documentation", "", false, false, "", STYPE.withoutLink, 1, 11, 6, 8000);
      this.pSLCode = new EntryBase(false, "", "PSLCode", "", false, false, "", STYPE.withoutLink, 1, 15, 4, 8000);
      this.emptyEntry = new EntryBase(false, "", "", "", false, false, "", STYPE.withLink, 1, 0, 0, 0);
      this.initVectorMain();
      this.initVectorCharacterScreen();
      this.initVectorProfileDB();
      this.initVectorDocumentation();
      this.initVectorPSLCode();
   }

   private void initVectorPSLCode() {
      this.vPSLCode.addElement(this.pSLCode);
   }

   private void initVectorDocumentation() {
      this.vDocumentation.addElement(this.documentation);
   }

   private void initVectorProfileDB() {
      this.vProfileDBG1.addElement(this.subScriptLiteral);
      this.vProfileDBG1.addElement(this.fieldPosition);
      this.vProfileDBG2.addElement(this.subFieldPrefix);
      this.vProfileDBG2.addElement(this.subFieldSuffix);
      this.vProfileDBG2.addElement(this.subFieldPosition);
      this.vProfileDBG2.addElement(this.subFieldTag);
      this.vProfileDBG3.addElement(this.patternMatch);
   }

   private void initVectorCharacterScreen() {
      this.vCharacterScreen.addElement(this.preProcessor);
      this.vCharacterScreen.addElement(this.postProcessor);
      this.vCharacterScreen.addElement(this.dataEntryPreProcessor);
      this.vCharacterScreen.addElement(this.dataEntryPostProcessor);
      this.vCharacterScreen.addElement(this.displaySize);
      this.vCharacterScreen.addElement(this.reportHeader);
   }

   private void initVectorMain() {
      this.vMainG1.addElement(this.entityName);
      this.vMainG1.addElement(this.attributeName);
      this.vMainG1.addElement(this.description);
      this.vMainG1.addElement(this.masterAttribute);
      this.vMainG1.addElement(this.domain);
      this.vMainG1.addElement(this.emptyEntry);
      this.vMainG1.addElement(this.required);
      this.vMainG2.addElement(this.maximumValue);
      this.vMainG2.addElement(this.minimumValue);
      this.vMainG2.addElement(this.length);
      this.vMainG2.addElement(this.decimalPrecision);
      this.vMainG2.addElement(this.emptyEntry);
      this.vMainG2.addElement(this.isSerialValue);
      this.vMainG2.addElement(this.nullIndicator);
      this.vMainG3.addElement(this.dataType);
      this.vMainG3.addElement(this.emptyEntry);
      this.vMainG3.addElement(this.defaultValue);
      this.vMainG3.addElement(this.computedExpression);
      this.vMainG3.addElement(this.lookupEntity);
   }

   private void handleException(String string, Exception ex) {
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
                     this.handleException("Exception Exception caught when invoke a method: ", var14);
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

   public EntryBase getAttributeName() {
      return this.attributeName;
   }

   public EntryBase getComputedExpression() {
      return this.computedExpression;
   }

   public EntryBase getDataEntryPostProcessor() {
      return this.dataEntryPostProcessor;
   }

   public EntryBase getDataEntryPreProcessor() {
      return this.dataEntryPreProcessor;
   }

   public EntryBase getDataType() {
      return this.dataType;
   }

   public EntryBase getDecimalPrecision() {
      return this.decimalPrecision;
   }

   public EntryBase getDefaultValue() {
      return this.defaultValue;
   }

   public EntryBase getDescription() {
      return this.description;
   }

   public EntryBase getDisplaySize() {
      return this.displaySize;
   }

   public EntryBase getDocumentation() {
      return this.documentation;
   }

   public EntryBase getDomain() {
      return this.domain;
   }

   public EntryBase getEntityName() {
      return this.entityName;
   }

   public EntryBase getFieldPosition() {
      return this.fieldPosition;
   }

   public EntryBase getIsSerialValue() {
      return this.isSerialValue;
   }

   public EntryBase getLength() {
      return this.length;
   }

   public EntryBase getLookupEntity() {
      return this.lookupEntity;
   }

   public EntryBase getMasterAttribute() {
      return this.masterAttribute;
   }

   public EntryBase getMaximumValue() {
      return this.maximumValue;
   }

   public EntryBase getMinimumValue() {
      return this.minimumValue;
   }

   public EntryBase getPatternMatch() {
      return this.patternMatch;
   }

   public EntryBase getNullIndicator() {
      return this.nullIndicator;
   }

   public EntryBase getPostProcessor() {
      return this.postProcessor;
   }

   public EntryBase getPreProcessor() {
      return this.preProcessor;
   }

   public EntryBase getReportHeader() {
      return this.reportHeader;
   }

   public EntryBase getRequired() {
      return this.required;
   }

   public String getRootName() {
      return this.rootName;
   }

   public EntryBase getSubScriptLiteral() {
      return this.subScriptLiteral;
   }

   public EntryBase getSubFieldPosition() {
      return this.subFieldPosition;
   }

   public EntryBase getSubFieldPrefix() {
      return this.subFieldPrefix;
   }

   public EntryBase getSubFieldSuffix() {
      return this.subFieldSuffix;
   }

   public EntryBase getSubFieldTag() {
      return this.subFieldTag;
   }

   public Vector getVCharacterScreen() {
      return this.vCharacterScreen;
   }

   public Vector getVDocumentation() {
      return this.vDocumentation;
   }

   public Vector getVProfileDB() {
      return this.vProfileDB;
   }

   public Vector getVPSLCode() {
      return this.vPSLCode;
   }

   public void setAttributeName(EntryBase base) {
      this.attributeName = base;
   }

   public void setComputedExpression(EntryBase base) {
      this.computedExpression = base;
   }

   public void setDataEntryPostProcessor(EntryBase base) {
      this.dataEntryPostProcessor = base;
   }

   public void setDataEntryPreProcessor(EntryBase base) {
      this.dataEntryPreProcessor = base;
   }

   public void setDataType(EntryBase base) {
      this.dataType = base;
   }

   public void setDecimalPrecision(EntryBase base) {
      this.decimalPrecision = base;
   }

   public void setDefaultValue(EntryBase base) {
      this.defaultValue = base;
   }

   public void setDescription(EntryBase base) {
      this.description = base;
   }

   public void setDisplaySize(EntryBase base) {
      this.displaySize = base;
   }

   public void setDocumentation(EntryBase base) {
      this.documentation = base;
   }

   public void setDomain(EntryBase base) {
      this.domain = base;
   }

   public void setEntityName(EntryBase base) {
      this.entityName = base;
   }

   public void setFieldPosition(EntryBase base) {
      this.fieldPosition = base;
   }

   public void setIsSerialValue(EntryBase base) {
      this.isSerialValue = base;
   }

   public void setLength(EntryBase base) {
      this.length = base;
   }

   public void setLookupEntity(EntryBase base) {
      this.lookupEntity = base;
   }

   public void setMasterAttribute(EntryBase base) {
      this.masterAttribute = base;
   }

   public void setMaximumValue(EntryBase base) {
      this.maximumValue = base;
   }

   public void setMinimumValue(EntryBase base) {
      this.minimumValue = base;
   }

   public void setPatternMatch(EntryBase base) {
      this.patternMatch = base;
   }

   public void setNullIndicator(EntryBase base) {
      this.nullIndicator = base;
   }

   public void setPostProcessor(EntryBase base) {
      this.postProcessor = base;
   }

   public void setPreProcessor(EntryBase base) {
      this.preProcessor = base;
   }

   public void setReportHeader(EntryBase base) {
      this.reportHeader = base;
   }

   public void setRequired(EntryBase base) {
      this.required = base;
   }

   public void setRootName(String string) {
      this.rootName = string;
   }

   public void setSubScriptLiteral(EntryBase base) {
      this.subScriptLiteral = base;
   }

   public void setSubFieldPosition(EntryBase base) {
      this.subFieldPosition = base;
   }

   public void setSubFieldPrefix(EntryBase base) {
      this.subFieldPrefix = base;
   }

   public void setSubFieldSuffix(EntryBase base) {
      this.subFieldSuffix = base;
   }

   public void setSubFieldTag(EntryBase base) {
      this.subFieldTag = base;
   }

   public void setVCharacterScreen(Vector vector) {
      this.vCharacterScreen = vector;
   }

   public void setVDocumentation(Vector vector) {
      this.vDocumentation = vector;
   }

   public void setVProfileDB(Vector vector) {
      this.vProfileDB = vector;
   }

   public void setVPSLCode(Vector vector) {
      this.vPSLCode = vector;
   }

   public void setEntityNameValue(String value) {
      this.getEntityName().setValue(value);
   }

   public void setAttributeNameValue(String value) {
      this.getAttributeName().setValue(value);
   }

   public void setDescriptionValue(String value) {
      this.getDescription().setValue(value);
   }

   public void setRequiredValue(String value) {
      this.getRequired().setValue(value);
   }

   public void setLengthValue(String value) {
      this.getLength().setValue(value);
   }

   public void setDecimalPrecisionValue(String value) {
      this.getDecimalPrecision().setValue(value);
   }

   public void setDataTypeValue(String value) {
      this.getDataType().setValue(value);
   }

   public void setMaximumValueValue(String value) {
      this.getMaximumValue().setValue(value);
   }

   public void setMinimumValueValue(String value) {
      this.getMinimumValue().setValue(value);
   }

   public void setDefaultValueValue(String value) {
      this.getDefaultValue().setValue(value);
   }

   public void setComputedExpressionValue(String value) {
      this.getComputedExpression().setValue(value);
   }

   public void setReportHeaderValue(String value) {
      this.getReportHeader().setValue(value);
   }

   public void setIsSerialValueValue(String value) {
      this.getIsSerialValue().setValue(value);
   }

   public void setLookupEntityValue(String value) {
      this.getLookupEntity().setValue(value);
   }

   public void setNullIndicatorValue(String value) {
      this.getNullIndicator().setValue(value);
   }

   public void setMasterAttributeValue(String value) {
      this.getMasterAttribute().setValue(value);
   }

   public void setDomainValue(String value) {
      this.getDomain().setValue(value);
   }

   public void setPostProcessorValue(String value) {
      this.getPostProcessor().setValue(value);
   }

   public void setPreProcessorValue(String value) {
      this.getPreProcessor().setValue(value);
   }

   public void setDataEntryPostProcessorValue(String value) {
      this.getDataEntryPostProcessor().setValue(value);
   }

   public void setDataEntryPreProcessorValue(String value) {
      this.getDataEntryPreProcessor().setValue(value);
   }

   public void setDisplaySizeValue(String value) {
      this.getDisplaySize().setValue(value);
   }

   public void setPatternMatchValue(String value) {
      this.getPatternMatch().setValue(value);
   }

   public void setFieldPositionValue(String value) {
      this.getFieldPosition().setValue(value);
   }

   public void setSubScriptLiteralValue(String value) {
      this.getSubScriptLiteral().setValue(value);
   }

   public void setSubFieldPrefixValue(String value) {
      this.getSubFieldPrefix().setValue(value);
   }

   public void setSubFieldSuffixValue(String value) {
      this.getSubFieldSuffix().setValue(value);
   }

   public void setSubFieldPositionValue(String value) {
      this.getSubFieldPosition().setValue(value);
   }

   public void setSubFieldTagValue(String value) {
      this.getSubFieldTag().setValue(value);
   }

   public void setDocumentationValue(String value) {
      this.getDocumentation().setValue(value);
   }

   public void setPSLCodeValue(String value) {
      this.getPSLCode().setValue(value);
   }

   public EntryBase getEmptyEntry() {
      return this.emptyEntry;
   }

   public EntryBase getPslCode() {
      return this.pSLCode;
   }

   public void setEmptyEntry(EntryBase base) {
      this.emptyEntry = base;
   }

   public EntryBase getPSLCode() {
      return this.pSLCode;
   }

   public void setPSLCode(EntryBase base) {
      this.pSLCode = base;
   }

   public EntryBase getCharacterScreen() {
      return this.characterScreen;
   }

   public EntryBase getProfileDB() {
      return this.profileDB;
   }

   public void setCharacterScreen(EntryBase base) {
      this.characterScreen = base;
   }

   public void setProfileDB(EntryBase base) {
      this.profileDB = base;
   }

   public Vector getVMainG1() {
      return this.vMainG1;
   }

   public Vector getVMainG2() {
      return this.vMainG2;
   }

   public Vector getVMainG3() {
      return this.vMainG3;
   }

   public void setVMainG1(Vector vector) {
      this.vMainG1 = vector;
   }

   public void setVMainG2(Vector vector) {
      this.vMainG2 = vector;
   }

   public void setVMainG3(Vector vector) {
      this.vMainG3 = vector;
   }

   public String getDescMainG1() {
      return this.descMainG1;
   }

   public String getDescMainG2() {
      return this.descMainG2;
   }

   public String getDescMainG3() {
      return this.descMainG3;
   }

   public String getTitleMainG1() {
      return this.titleMainG1;
   }

   public String getTitleMainG2() {
      return this.titleMainG2;
   }

   public String getTitleMainG3() {
      return this.titleMainG3;
   }

   public String getDescDocumentation() {
      return this.descDocumentation;
   }

   public String getDescProfileDBG1() {
      return this.descProfileDBG1;
   }

   public String getDescProfileDBG2() {
      return this.descProfileDBG2;
   }

   public String getTitleDocumentation() {
      return this.titleDocumentation;
   }

   public String getTitleProfileDBG1() {
      return this.titleProfileDBG1;
   }

   public String getTitleProfileDBG2() {
      return this.titleProfileDBG2;
   }

   public Vector getVProfileDBG2() {
      return this.vProfileDBG2;
   }

   public String getDescCharacterPage() {
      return this.descCharacterPage;
   }

   public String getDescCharacterScreen() {
      return this.descCharacterScreen;
   }

   public String getDescDocumentationPage() {
      return this.descDocumentationPage;
   }

   public String getDescMainPage() {
      return this.descMainPage;
   }

   public String getDescProfilePage() {
      return this.descProfilePage;
   }

   public String getTitleCharacterScreen() {
      return this.titleCharacterScreen;
   }

   public Vector getVProfileDBG1() {
      return this.vProfileDBG1;
   }

   public String getDescProfileDBG3() {
      return this.descProfileDBG3;
   }

   public String getTitleProfileDBG3() {
      return this.titleProfileDBG3;
   }

   public Vector getVProfileDBG3() {
      return this.vProfileDBG3;
   }

   public String getHelpFileName() {
      return this.helpFileName;
   }
}
