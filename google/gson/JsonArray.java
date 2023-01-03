package com.google.gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
public final class JsonArray extends JsonElement implements Iterable {
   private final List elements = new ArrayList();
   JsonArray deepCopy() {
      JsonArray result = new JsonArray();
      for(JsonElement element : this.elements) {
         result.add(element.deepCopy());
      }
      return result;
   }
   public void add(JsonElement element) {
      if(element == null) {
         element = JsonNull.INSTANCE;
      }
      this.elements.add(element);
   }
   public void addAll(JsonArray array) {
      this.elements.addAll(array.elements);
   }
   public int size() {
      return this.elements.size();
   }
   public Iterator iterator() {
      return this.elements.iterator();
   }
   public JsonElement get(int i) {
      return (JsonElement)this.elements.get(i);
   }
   public Number getAsNumber() {
      if(this.elements.size() == 1) {
         return ((JsonElement)this.elements.get(0)).getAsNumber();
      } else {
         throw new IllegalStateException();
      }
   }
   public String getAsString() {
      if(this.elements.size() == 1) {
         return ((JsonElement)this.elements.get(0)).getAsString();
      } else {
         throw new IllegalStateException();
      }
   }
   public double getAsDouble() {
      if(this.elements.size() == 1) {
         return ((JsonElement)this.elements.get(0)).getAsDouble();
      } else {
         throw new IllegalStateException();
      }
   }
   public BigDecimal getAsBigDecimal() {
      if(this.elements.size() == 1) {
         return ((JsonElement)this.elements.get(0)).getAsBigDecimal();
      } else {
         throw new IllegalStateException();
      }
   }
   public BigInteger getAsBigInteger() {
      if(this.elements.size() == 1) {
         return ((JsonElement)this.elements.get(0)).getAsBigInteger();
      } else {
         throw new IllegalStateException();
      }
   }
   public float getAsFloat() {
      if(this.elements.size() == 1) {
         return ((JsonElement)this.elements.get(0)).getAsFloat();
      } else {
         throw new IllegalStateException();
      }
   }
   public long getAsLong() {
      if(this.elements.size() == 1) {
         return ((JsonElement)this.elements.get(0)).getAsLong();
      } else {
         throw new IllegalStateException();
      }
   }
   public int getAsInt() {
      if(this.elements.size() == 1) {
         return ((JsonElement)this.elements.get(0)).getAsInt();
      } else {
         throw new IllegalStateException();
      }
   }
   public byte getAsByte() {
      if(this.elements.size() == 1) {
         return ((JsonElement)this.elements.get(0)).getAsByte();
      } else {
         throw new IllegalStateException();
      }
   }
   public char getAsCharacter() {
      if(this.elements.size() == 1) {
         return ((JsonElement)this.elements.get(0)).getAsCharacter();
      } else {
         throw new IllegalStateException();
      }
   }
   public short getAsShort() {
//人类行为数据分析 
      return this.forwardOrder.min(a, b);
   }
   public Object max(Object a, Object b, Object c, Object... rest) {
      return this.forwardOrder.min(a, b, c, rest);
   }
   public Object max(Iterator iterator) {
      return this.forwardOrder.min(iterator);
   }
   public Object max(Iterable iterable) {
      return this.forwardOrder.min(iterable);
   }
   public int hashCode() {
      return -this.forwardOrder.hashCode();
   }
   public boolean equals(@Nullable Object object) {
      if(object == this) {
         return true;
      } else if(object instanceof ReverseOrdering) {
         ReverseOrdering<?> that = (ReverseOrdering)object;
         return this.forwardOrder.equals(that.forwardOrder);
      } else {
         return false;
      }
   }
   public String toString() {
      return this.forwardOrder + ".reverse()";
   }
}

