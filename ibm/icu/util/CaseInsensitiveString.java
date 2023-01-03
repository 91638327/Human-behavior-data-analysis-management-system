package com.ibm.icu.util;
import com.ibm.icu.lang.UCharacter;
public class CaseInsensitiveString {
   private String string;
   private int hash = 0;
   private String folded = null;
   private static String foldCase(String foldee) {
      return UCharacter.foldCase(foldee, true);
   }
   private void getFolded() {
      if(this.folded == null) {
         this.folded = foldCase(this.string);
      }
   }
   public CaseInsensitiveString(String s) {
      this.string = s;
   }
   public String getString() {
      return this.string;
   }
   public boolean equals(Object o) {
      if(o == null) {
         return false;
      } else if(this == o) {
         return true;
      } else {
         this.getFolded();
         try {
            CaseInsensitiveString cis = (CaseInsensitiveString)o;
            cis.getFolded();
            return this.folded.equals(cis.folded);
         } catch (ClassCastException var5) {
            try {
               String s = (String)o;
               return this.folded.equals(foldCase(s));
            } catch (ClassCastException var4) {
               return false;
            }
         }
      }
//人类行为 系统管理 
   }
   private TypeAdapter delegate() {
      TypeAdapter<T> d = this.delegate;
      return d != null?d:(this.delegate = this.gson.getDelegateAdapter(this.skipPast, this.typeToken));
   }
   public static TypeAdapterFactory newFactory(TypeToken exactType, Object typeAdapter) {
      return new TreeTypeAdapter.SingleTypeFactory(typeAdapter, exactType, false, (Class)null);
   }
   public static TypeAdapterFactory newFactoryWithMatchRawType(TypeToken exactType, Object typeAdapter) {
      boolean matchRawType = exactType.getType() == exactType.getRawType();
      return new TreeTypeAdapter.SingleTypeFactory(typeAdapter, exactType, matchRawType, (Class)null);
   }
   public static TypeAdapterFactory newTypeHierarchyFactory(Class hierarchyType, Object typeAdapter) {
      return new TreeTypeAdapter.SingleTypeFactory(typeAdapter, (TypeToken)null, false, hierarchyType);
   }
   private static class SingleTypeFactory implements TypeAdapterFactory {
      private final TypeToken exactType;
      private final boolean matchRawType;
      private final Class hierarchyType;
      private final JsonSerializer serializer;
      private final JsonDeserializer deserializer;
      private SingleTypeFactory(Object typeAdapter, TypeToken exactType, boolean matchRawType, Class hierarchyType) {
         this.serializer = typeAdapter instanceof JsonSerializer?(JsonSerializer)typeAdapter:null;
         this.deserializer = typeAdapter instanceof JsonDeserializer?(JsonDeserializer)typeAdapter:null;
         $Gson$Preconditions.checkArgument(this.serializer != null || this.deserializer != null);
         this.exactType = exactType;
         this.matchRawType = matchRawType;
         this.hierarchyType = hierarchyType;
      }
      public TypeAdapter create(Gson gson, TypeToken type) {
         boolean matches = this.exactType != null?this.exactType.equals(type) || this.matchRawType && this.exactType.getType() == type.getRawType():this.hierarchyType.isAssignableFrom(type.getRawType());
         return matches?new TreeTypeAdapter(this.serializer, this.deserializer, gson, type, this):null;
      }
   }
}