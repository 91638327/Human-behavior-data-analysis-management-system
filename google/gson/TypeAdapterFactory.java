package com.google.gson;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
public interface TypeAdapterFactory {
   TypeAdapter create(Gson var1, TypeToken var2);
}
package com.google.gson.annotations;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
// 
   private String getTag() {
      if(this.tag == null) {
         this.tag = this.readNextTag();
      }
      return this.tag;
   }
   private void advance() {
      this.tag = null;
   }
   private String readData() {
      StringBuilder sb = new StringBuilder();
      boolean inWhitespace = false;
      while(true) {
         int c = this.readChar();
         if(c == -1 || c == 60) {
            this.atTag = c == 60;
            return sb.toString();
         }
         if(c == 38) {
            c = this.readChar();
            if(c == 35) {
               StringBuilder numBuf = new StringBuilder();
               int radix = 10;
               c = this.readChar();
               if(c == 120) {
                  radix = 16;
                  c = this.readChar();
               }
               while(c != 59 && c != -1) {
                  numBuf.append((char)c);
                  c = this.readChar();
               }
               try {
                  int num = Integer.parseInt(numBuf.toString(), radix);
                  c = (char)num;
               } catch (NumberFormatException var7) {
                  System.err.println("numbuf: " + numBuf.toString() + " radix: " + radix);
                  throw var7;
               }
            } else {
               StringBuilder charBuf;
               for(charBuf = new StringBuilder(); c != 59 && c != -1; c = this.readChar()) {
                  charBuf.append((char)c);
               }
               String charName = charBuf.toString();
               if(charName.equals("lt")) {
                  c = 60;
               } else if(charName.equals("gt")) {
                  c = 62;
               } else if(charName.equals("quot")) {
                  c = 34;
               } else if(charName.equals("apos")) {
                  c = 39;
               } else {
                  if(!charName.equals("amp")) {
                     System.err.println("unrecognized character entity: \'" + charName + "\'");
                     continue;
                  }
                  c = 38;
               }
            }
         }
         if(UCharacter.isWhitespace(c)) {
            if(inWhitespace) {
               continue;
            }
            c = 32;
            inWhitespace = true;
         } else {
            inWhitespace = false;
         }
         sb.append((char)c);
      }
   }
   private String readNextTag() {
      int c = 0;
      while(!this.atTag) {
         c = this.readChar();
         if(c != 60 && c != -1) {
            if(UCharacter.isWhitespace(c)) {
               continue;
            }
            System.err.println("Unexpected non-whitespace character " + Integer.toHexString(c));
            break;
         }
         if(c == 60) {
            this.atTag = true;
         }
         break;
      }
      if(!this.atTag) {
         return null;
      } else {
         this.atTag = false;
         StringBuilder sb = new StringBuilder();
         while(true) {
            c = this.readChar();
            if(c == 62 || c == -1) {
               return sb.toString();
            }
            sb.append((char)c);
         }
      }
   }
   int readChar() {
      try {
         return this.r.read();
      } catch (IOException var2) {
         return -1;
      }
   }
}