package com.sanchez.profile.schema.schemaeditor.wizards;

public class EntityTableEntry {
   private String attributeName;
   private String subScriptLiteral;
   private String dataType;
   private String computedExpression;
   private String description;
   private String fieldPosition;
   private String length;
   private String decimalPrecision;
   private boolean required = false;

   public EntityTableEntry() {
   }

   public EntityTableEntry(String attributeName, String subScriptLiteral, String fieldPosition, String dataType, boolean required, String length, String decimalPrecision, String computedExpression, String description) {
      this.attributeName = attributeName;
      this.subScriptLiteral = subScriptLiteral;
      this.fieldPosition = fieldPosition;
      this.dataType = dataType;
      this.required = required;
      this.length = length;
      this.decimalPrecision = decimalPrecision;
      this.computedExpression = computedExpression;
      this.description = description;
   }

   public String getAttributeName() {
      return this.attributeName;
   }

   public String getComputedExpression() {
      return this.computedExpression;
   }

   public String getDataType() {
      return this.dataType;
   }

   public String getDecimalPrecision() {
      return this.decimalPrecision;
   }

   public String getFieldPosition() {
      return this.fieldPosition;
   }

   public String getLength() {
      return this.length;
   }

   public boolean isRequired() {
      return this.required;
   }

   public String getSubScriptLiteral() {
      return this.subScriptLiteral;
   }

   public void setAttributeName(String string) {
      this.attributeName = string;
   }

   public void setComputedExpression(String string) {
      this.computedExpression = string;
   }

   public void setDataType(String string) {
      this.dataType = string;
   }

   public void setDecimalPrecision(String value) {
      this.decimalPrecision = value;
   }

   public void setDescription(String string) {
      this.description = string;
   }

   public void setFieldPosition(String value) {
      this.fieldPosition = value;
   }

   public void setLength(String value) {
      this.length = value;
   }

   public void setRequired(boolean isRequired) {
      this.required = isRequired;
   }

   public void setSubScriptLiteral(String string) {
      this.subScriptLiteral = string;
   }

   public String getDescription() {
      return this.description;
   }
}
