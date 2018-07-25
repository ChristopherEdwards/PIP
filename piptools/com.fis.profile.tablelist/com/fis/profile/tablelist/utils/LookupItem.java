package com.fis.profile.tablelist.utils;

public class LookupItem {
   private String defaultLength;
   private String value;
   private String displayValue;

   public LookupItem(String value, String displayValue) {
      this.value = value;
      this.displayValue = displayValue;
   }

   public LookupItem(String value, String displayValue, String defaultLength) {
      this.value = value;
      this.displayValue = displayValue;
      this.defaultLength = defaultLength;
   }

   public String getDisplayValue() {
      return this.displayValue;
   }

   public String getValue() {
      return this.value;
   }

   public void setDisplayValue(String displayValue) {
      this.displayValue = displayValue;
   }

   public void setValue(String value) {
      this.value = value;
   }

   public String getDefaultLength() {
      return this.defaultLength;
   }

   public void setDefaultLength(String string) {
      this.defaultLength = string;
   }
}
