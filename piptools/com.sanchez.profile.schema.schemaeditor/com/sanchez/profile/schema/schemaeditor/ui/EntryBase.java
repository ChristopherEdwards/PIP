package com.sanchez.profile.schema.schemaeditor.ui;

public class EntryBase {
   private boolean required;
   private String displayLabel;
   private String tag;
   private String value;
   private boolean readonly;
   private boolean lookupTable;
   private String path;
   private boolean linked;
   private int span;
   private int controlType;
   private int dataType;
   private int limit;
   EntryBase entry;

   public EntryBase(boolean required, String displayLabel, String tag, String value, boolean readonly, boolean lookupTable, String path, boolean linked, int span, int controlType, int dataType, int limit) {
      this.required = required;
      this.displayLabel = displayLabel;
      this.tag = tag;
      this.value = value;
      this.readonly = readonly;
      this.lookupTable = lookupTable;
      this.path = path;
      this.linked = linked;
      this.span = span;
      this.controlType = controlType;
      this.dataType = dataType;
      this.limit = limit;
   }

   public boolean getLookupTable() {
      return this.lookupTable;
   }

   public String getPath() {
      return this.path;
   }

   public boolean isReadonly() {
      return this.readonly;
   }

   public int getSpan() {
      return this.span;
   }

   public String getTag() {
      return this.tag;
   }

   public String getValue() {
      return this.value;
   }

   public void setLookupTable(boolean lookupTable) {
      this.lookupTable = lookupTable;
   }

   public void setPath(String path) {
      this.path = path;
   }

   public void setReadonly(boolean readonly) {
      this.readonly = readonly;
   }

   public void setSpan(int span) {
      this.span = span;
   }

   public void setTag(String tag) {
      this.tag = tag;
   }

   public void setValue(String value) {
      this.value = value;
   }

   public int getControlType() {
      return this.controlType;
   }

   public int getDataType() {
      return this.dataType;
   }

   public int getLimit() {
      return this.limit;
   }

   public void setControlType(int i) {
      this.controlType = i;
   }

   public void setDataType(int i) {
      this.dataType = i;
   }

   public void setLimit(int i) {
      this.limit = i;
   }

   public String getDisplayLabel() {
      return this.displayLabel;
   }

   public void setDisplayLabel(String string) {
      this.displayLabel = string;
   }

   public boolean isRequired() {
      return this.required;
   }

   public void setRequired(boolean required) {
      this.required = required;
   }
}
