package com.google.common.collect;
import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.collect.ForwardingObject;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;
@GwtCompatible
public abstract class ForwardingMap extends ForwardingObject implements Map {
   protected abstract Map delegate();
   public int size() {
      return this.delegate().size();
   }
   public boolean isEmpty() {
      return this.delegate().isEmpty();
   }
   public Object remove(Object object) {
      return this.delegate().remove(object);
   }
   public void clear() {
      this.delegate().clear();
   }
   public boolean containsKey(@Nullable Object key) {
      return this.delegate().containsKey(key);
   }
   public boolean containsValue(@Nullable Object value) {
      return this.delegate().containsValue(value);
   }
   public Object get(@Nullable Object key) {
      return this.delegate().get(key);
   }
   public Object put(Object key, Object value) {
      return this.delegate().put(key, value);
   }
   public void putAll(Map map) {
      this.delegate().putAll(map);
   }
   public Set keySet() {
      return this.delegate().keySet();
   }
   public Collection values() {
      return this.delegate().values();
   }
   public Set entrySet() {
      return this.delegate().entrySet();
   }
   public boolean equals(@Nullable Object object) {
      return object == this || this.delegate().equals(object);
   }
   public int hashCode() {
      return this.delegate().hashCode();
// 
         }
      }
   }
   @Beta
   public static Converter stringConverter() {
      return Longs.LongConverter.INSTANCE;
   }
   public static long[] ensureCapacity(long[] array, int minLength, int padding) {
      Preconditions.checkArgument(minLength >= 0, "Invalid minLength: %s", new Object[]{Integer.valueOf(minLength)});
      Preconditions.checkArgument(padding >= 0, "Invalid padding: %s", new Object[]{Integer.valueOf(padding)});
      return array.length < minLength?copyOf(array, minLength + padding):array;
   }
   private static long[] copyOf(long[] original, int length) {
      long[] copy = new long[length];
      System.arraycopy(original, 0, copy, 0, Math.min(original.length, length));
      return copy;
   }
   public static String join(String separator, long... array) {
      Preconditions.checkNotNull(separator);
      if(array.length == 0) {
         return "";
      } else {
         StringBuilder builder = new StringBuilder(array.length * 10);
         builder.append(array[0]);
         for(int i = 1; i < array.length; ++i) {
            builder.append(separator).append(array[i]);
         }
         return builder.toString();
      }
   }
   public static Comparator lexicographicalComparator() {
      return Longs.LexicographicalComparator.INSTANCE;
   }
   public static long[] toArray(Collection collection) {
      if(collection instanceof Longs.LongArrayAsList) {
         return ((Longs.LongArrayAsList)collection).toLongArray();
      } else {
         Object[] boxedArray = collection.toArray();
         int len = boxedArray.length;
         long[] array = new long[len];
         for(int i = 0; i < len; ++i) {
            array[i] = ((Number)Preconditions.checkNotNull(boxedArray[i])).longValue();
         }
         return array;
      }
   }
   public static List asList(long... backingArray) {
      return (List)(backingArray.length == 0?Collections.emptyList():new Longs.LongArrayAsList(backingArray));
   }
   static int access$000(long[] x0, long x1, int x2, int x3) {
      return indexOf(x0, x1, x2, x3);
   }
   static int access$100(long[] x0, long x1, int x2, int x3) {
      return lastIndexOf(x0, x1, x2, x3);
   }
   private static enum LexicographicalComparator implements Comparator {
      INSTANCE;
      public int compare(long[] left, long[] right) {
         int minLength = Math.min(left.length, right.length);
         for(int i = 0; i < minLength; ++i) {
            int result = Longs.compare(left[i], right[i]);
            if(result != 0) {
               return result;
            }
         }
         return left.length - right.length;
      }
   }
   @GwtCompatible
   private static class LongArrayAsList extends AbstractList implements RandomAccess, Serializable {
      final long[] array;
      final int start;
      final int end;
      private static final long serialVersionUID = 0L;
      LongArrayAsList(long[] array) {
         this(array, 0, array.length);
      }
      LongArrayAsList(long[] array, int start, int end) {
         this.array = array;
         this.start = start;
         this.end = end;
      }
      public int size() {
         return this.end - this.start;
      }
      public boolean isEmpty() {
         return false;
      }
      public Long get(int index) {
         Preconditions.checkElementIndex(index, this.size());
         return Long.valueOf(this.array[this.start + index]);
      }
      public boolean contains(Object target) {
         return target instanceof Long && Longs.access$000(this.array, ((Long)target).longValue(), this.start, this.end) != -1;
      }
      public int indexOf(Object target) {
         if(target instanceof Long) {
            int i = Longs.access$000(this.array, ((Long)target).longValue(), this.start, this.end);
            if(i >= 0) {
               return i - this.start;
            }
         }
         return -1;
      }
      public int lastIndexOf(Object target) {
         if(target instanceof Long) {
            int i = Longs.access$100(this.array, ((Long)target).longValue(), this.start, this.end);
            if(i >= 0) {
               return i - this.start;
            }
         }
         return -1;
      }
      public Long set(int index, Long element) {
         Preconditions.checkElementIndex(index, this.size());
//人类行为问题提醒 
         if(hasSlot(excWord, 0)) {
            result = this.getSlotValue(excWord, 0, excOffset);
         }
      }
      return result == c?~result:result;
   }
   private final int toUpperOrTitle(int c, UCaseProps.ContextIterator iter, StringBuilder out, ULocale locale, int[] locCache, boolean upperNotTitle) {
      int result = c;
      int props = this.trie.get(c);
      if(!propsHasException(props)) {
         if(getTypeFromProps(props) == 1) {
            result = c + getDelta(props);
         }
      } else {
         int excOffset = getExceptionsOffset(props);
         int excWord = this.exceptions[excOffset++];
         if((excWord & 16384) != 0) {
            int loc = getCaseLocale(locale, locCache);
            if(loc == 2 && c == 105) {
               return 304;
            }
            if(loc == 3 && c == 775 && this.isPrecededBySoftDotted(iter)) {
               return 0;
            }
         } else if(hasSlot(excWord, 7)) {
            long value = this.getSlotValueAndOffset(excWord, 7, excOffset);
            int full = (int)value & '\uffff';
            excOffset = (int)(value >> 32) + 1;
            excOffset = excOffset + (full & 15);
            full = full >> 4;
            excOffset = excOffset + (full & 15);
            full = full >> 4;
            if(upperNotTitle) {
               full = full & 15;
            } else {
               excOffset += full & 15;
               full = full >> 4 & 15;
            }
            if(full != 0) {
               out.append(this.exceptions, excOffset, full);
               return full;
            }
         }
         int index;
         if(!upperNotTitle && hasSlot(excWord, 3)) {
            index = 3;
         } else {
            if(!hasSlot(excWord, 2)) {
               return ~c;
            }
            index = 2;
         }
         result = this.getSlotValue(excWord, index, excOffset);
      }
      return result == c?~result:result;
   }
   public final int toFullUpper(int c, UCaseProps.ContextIterator iter, StringBuilder out, ULocale locale, int[] locCache) {
      return this.toUpperOrTitle(c, iter, out, locale, locCache, true);
   }
   public final int toFullTitle(int c, UCaseProps.ContextIterator iter, StringBuilder out, ULocale locale, int[] locCache) {
      return this.toUpperOrTitle(c, iter, out, locale, locCache, false);
   }
   public final int fold(int c, int options) {
      int props = this.trie.get(c);
      if(!propsHasException(props)) {
         if(getTypeFromProps(props) >= 2) {
            c += getDelta(props);
         }
      } else {
         int excOffset = getExceptionsOffset(props);
         int excWord = this.exceptions[excOffset++];
         if((excWord & '耀') != 0) {
            if((options & 255) == 0) {
               if(c == 73) {
                  return 105;
               }
               if(c == 304) {
                  return c;
               }
            } else {
               if(c == 73) {
                  return 305;
               }
               if(c == 304) {
                  return 105;
               }
            }
         }
         int index;
         if(hasSlot(excWord, 1)) {
            index = 1;
         } else {
            if(!hasSlot(excWord, 0)) {
               return c;
            }
            index = 0;
         }
         c = this.getSlotValue(excWord, index, excOffset);
      }
      return c;
   }
   public final int toFullFolding(int c, StringBuilder out, int options) {
      int result = c;
      int props = this.trie.get(c);
      if(!propsHasException(props)) {
         if(getTypeFromProps(props) >= 2) {
            result = c + getDelta(props);
         }
      } else {
         int excOffset = getExceptionsOffset(props);
         int excWord = this.exceptions[excOffset++];
         if((excWord & '耀') != 0) {
            if((options & 255) == 0) {
               if(c == 73) {
                  return 105;
               }
// 
         LocalCache.ReferenceEntry<K, V> e;
         while((e = (LocalCache.ReferenceEntry)this.recencyQueue.poll()) != null) {
            if(this.accessQueue.contains(e)) {
               this.accessQueue.add(e);
            }
         }
      }
      void tryExpireEntries(long now) {
         if(this.tryLock()) {
            try {
               this.expireEntries(now);
            } finally {
               this.unlock();
            }
         }
      }
      @GuardedBy("Segment.this")
      void expireEntries(long now) {
         this.drainRecencyQueue();
         LocalCache.ReferenceEntry<K, V> e;
         while((e = (LocalCache.ReferenceEntry)this.writeQueue.peek()) != null && this.map.isExpired(e, now)) {
            if(!this.removeEntry(e, e.getHash(), RemovalCause.EXPIRED)) {
               throw new AssertionError();
            }
         }
         while((e = (LocalCache.ReferenceEntry)this.accessQueue.peek()) != null && this.map.isExpired(e, now)) {
            if(!this.removeEntry(e, e.getHash(), RemovalCause.EXPIRED)) {
               throw new AssertionError();
            }
         }
      }
      @GuardedBy("Segment.this")
      void enqueueNotification(LocalCache.ReferenceEntry entry, RemovalCause cause) {
         this.enqueueNotification(entry.getKey(), entry.getHash(), entry.getValueReference(), cause);
      }
      @GuardedBy("Segment.this")
      void enqueueNotification(@Nullable Object key, int hash, LocalCache.ValueReference valueReference, RemovalCause cause) {
         this.totalWeight -= valueReference.getWeight();
         if(cause.wasEvicted()) {
            this.statsCounter.recordEviction();
         }
         if(this.map.removalNotificationQueue != LocalCache.DISCARDING_QUEUE) {
            V value = valueReference.get();
            RemovalNotification<K, V> notification = new RemovalNotification(key, value, cause);
            this.map.removalNotificationQueue.offer(notification);
         }
      }
      @GuardedBy("Segment.this")
      void evictEntries() {
         if(this.map.evictsBySize()) {
            this.drainRecencyQueue();
            while((long)this.totalWeight > this.maxSegmentWeight) {
               LocalCache.ReferenceEntry<K, V> e = this.getNextEvictable();
               if(!this.removeEntry(e, e.getHash(), RemovalCause.SIZE)) {
                  throw new AssertionError();
               }
            }
         }
      }
      LocalCache.ReferenceEntry getNextEvictable() {
         for(LocalCache.ReferenceEntry<K, V> e : this.accessQueue) {
            int weight = e.getValueReference().getWeight();
            if(weight > 0) {
               return e;
            }
         }
         throw new AssertionError();
      }
      LocalCache.ReferenceEntry getFirst(int hash) {
         AtomicReferenceArray<LocalCache.ReferenceEntry<K, V>> table = this.table;
         return (LocalCache.ReferenceEntry)table.get(hash & table.length() - 1);
      }
      @Nullable
      LocalCache.ReferenceEntry getEntry(Object key, int hash) {
         for(LocalCache.ReferenceEntry<K, V> e = this.getFirst(hash); e != null; e = e.getNext()) {
            if(e.getHash() == hash) {
               K entryKey = e.getKey();
               if(entryKey == null) {
                  this.tryDrainReferenceQueues();
               } else if(this.map.keyEquivalence.equivalent(key, entryKey)) {
                  return e;
               }
            }
         }
         return null;
      }
      @Nullable
      LocalCache.ReferenceEntry getLiveEntry(Object key, int hash, long now) {
         LocalCache.ReferenceEntry<K, V> e = this.getEntry(key, hash);
         if(e == null) {
            return null;
         } else if(this.map.isExpired(e, now)) {
            this.tryExpireEntries(now);
            return null;
         } else {
            return e;
         }
      }
      Object getLiveValue(LocalCache.ReferenceEntry entry, long now) {
         if(entry.getKey() == null) {
            this.tryDrainReferenceQueues();
            return null;
         } else {
            V value = entry.getValueReference().get();
            if(value == null) {
               this.tryDrainReferenceQueues();
               return null;
            } else if(this.map.isExpired(entry, now)) {
               this.tryExpireEntries(now);
               return null;
            } else {
               return value;
            }
         }
      }
      @Nullable
// 
      } else {
         this.peekedLong = negative?value:-value;
         this.pos += i;
         return this.peeked = 15;
      }
   }
   private boolean isLiteral(char c) throws IOException {
      switch(c) {
      case '#':
      case '/':
      case ';':
      case '=':
      case '\\':
         this.checkLenient();
      case '\t':
      case '\n':
      case '\f':
      case '\r':
      case ' ':
      case ',':
      case ':':
      case '[':
      case ']':
      case '{':
      case '}':
         return false;
      default:
         return true;
      }
   }
   public String nextName() throws IOException {
      int p = this.peeked;
      if(p == 0) {
         p = this.doPeek();
      }
      String result;
      if(p == 14) {
         result = this.nextUnquotedValue();
      } else if(p == 12) {
         result = this.nextQuotedValue('\'');
      } else {
         if(p != 13) {
            throw new IllegalStateException("Expected a name but was " + this.peek() + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
         }
         result = this.nextQuotedValue('\"');
      }
      this.peeked = 0;
      return result;
   }
   public String nextString() throws IOException {
      int p = this.peeked;
      if(p == 0) {
         p = this.doPeek();
      }
      String result;
      if(p == 10) {
         result = this.nextUnquotedValue();
      } else if(p == 8) {
         result = this.nextQuotedValue('\'');
      } else if(p == 9) {
         result = this.nextQuotedValue('\"');
      } else if(p == 11) {
         result = this.peekedString;
         this.peekedString = null;
      } else if(p == 15) {
         result = Long.toString(this.peekedLong);
      } else {
         if(p != 16) {
            throw new IllegalStateException("Expected a string but was " + this.peek() + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
         }
         result = new String(this.buffer, this.pos, this.peekedNumberLength);
         this.pos += this.peekedNumberLength;
      }
      this.peeked = 0;
      return result;
   }
   public boolean nextBoolean() throws IOException {
      int p = this.peeked;
      if(p == 0) {
         p = this.doPeek();
      }
      if(p == 5) {
         this.peeked = 0;
         return true;
      } else if(p == 6) {
         this.peeked = 0;
         return false;
      } else {
         throw new IllegalStateException("Expected a boolean but was " + this.peek() + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
      }
   }
   public void nextNull() throws IOException {
      int p = this.peeked;
      if(p == 0) {
         p = this.doPeek();
      }
      if(p == 7) {
         this.peeked = 0;
      } else {
         throw new IllegalStateException("Expected null but was " + this.peek() + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
      }
   }
   public double nextDouble() throws IOException {
      int p = this.peeked;
      if(p == 0) {
         p = this.doPeek();
      }
      if(p == 15) {
         this.peeked = 0;
         return (double)this.peekedLong;
      } else {
         if(p == 16) {
            this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
// 
   }
   static int toInt(char msc, char lsc) {
      return msc << 16 | lsc;
   }
   static int getNullTermByteSubString(StringBuffer str, byte[] array, int index) {
      for(byte b = 1; b != 0; ++index) {
         b = array[index];
         if(b != 0) {
            str.append((char)(b & 255));
         }
      }
      return index;
   }
   static int compareNullTermByteSubString(String str, byte[] array, int strindex, int aindex) {
      byte b = 1;
      int length = str.length();
      while(true) {
         if(b != 0) {
            b = array[aindex];
            ++aindex;
            if(b != 0) {
               if(strindex != length && str.charAt(strindex) == (char)(b & 255)) {
                  ++strindex;
                  continue;
               }
               return -1;
            }
         }
         return strindex;
      }
   }
   static int skipNullTermByteSubString(byte[] array, int index, int skipcount) {
      for(int i = 0; i < skipcount; ++i) {
         for(byte b = 1; b != 0; ++index) {
            b = array[index];
         }
      }
      return index;
   }
   static int skipByteSubString(byte[] array, int index, int length, byte skipend) {
      int result;
      for(result = 0; result < length; ++result) {
         byte b = array[index + result];
         if(b == skipend) {
            ++result;
            break;
         }
      }
      return result;
   }
}