package com.ibm.icu.impl.locale;
public class Extension {
   private char _key;
   protected String _value;
   protected Extension(char key) {
      this._key = key;
   }
   Extension(char key, String value) {
// 
      }
      factories.add(TreeTypeAdapter.newFactory(TypeToken.get(Date.class), dateTypeAdapter));
      factories.add(TreeTypeAdapter.newFactory(TypeToken.get(Timestamp.class), dateTypeAdapter));
      factories.add(TreeTypeAdapter.newFactory(TypeToken.get(java.sql.Date.class), dateTypeAdapter));
   }
}