package com.google.common.primitives;
import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.primitives.UnsignedLongs;
import java.lang.reflect.Field;
import java.nio.ByteOrder;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Comparator;
import sun.misc.Unsafe;
public final class UnsignedBytes {
   public static final byte MAX_POWER_OF_TWO = -128;
   public static final byte MAX_VALUE = -1;
   private static final int UNSIGNED_MASK = 255;
   public static int toInt(byte value) {
      return value & 255;
   }
   public static byte checkedCast(long value) {
      if(value >> 8 != 0L) {
         throw new IllegalArgumentException("Out of range: " + value);
      } else {
         return (byte)((int)value);
      }
   }
   public static byte saturatedCast(long value) {
      return value > (long)toInt((byte)-1)?-1:(value < 0L?0:(byte)((int)value));
   }
   public static int compare(byte a, byte b) {
      return toInt(a) - toInt(b);
   }
   public static byte min(byte... array) {
      Preconditions.checkArgument(array.length > 0);
      int min = toInt(array[0]);
      for(int i = 1; i < array.length; ++i) {
         int next = toInt(array[i]);
         if(next < min) {
            min = next;
         }
      }
      return (byte)min;
   }
   public static byte max(byte... array) {
      Preconditions.checkArgument(array.length > 0);
      int max = toInt(array[0]);
      for(int i = 1; i < array.length; ++i) {
         int next = toInt(array[i]);
         if(next > max) {
            max = next;
         }
      }
      return (byte)max;
   }
   @Beta
   public static String toString(byte x) {
// 
   protected int m_dataLength_;
   protected static final int HEADER_LENGTH_ = 16;
   protected static final int HEADER_OPTIONS_LATIN1_IS_LINEAR_MASK_ = 512;
   protected static final int HEADER_SIGNATURE_ = 1416784229;
   private static final int HEADER_OPTIONS_SHIFT_MASK_ = 15;
   protected static final int HEADER_OPTIONS_INDEX_SHIFT_ = 4;
   protected static final int HEADER_OPTIONS_DATA_IS_32_BIT_ = 256;
   private boolean m_isLatin1Linear_;
   private int m_options_;
   public final boolean isLatin1Linear() {
      return this.m_isLatin1Linear_;
   }
   public boolean equals(Object other) {
      if(other == this) {
         return true;
      } else if(!(other instanceof Trie)) {
         return false;
      } else {
         Trie othertrie = (Trie)other;
         return this.m_isLatin1Linear_ == othertrie.m_isLatin1Linear_ && this.m_options_ == othertrie.m_options_ && this.m_dataLength_ == othertrie.m_dataLength_ && Arrays.equals(this.m_index_, othertrie.m_index_);
      }
   }
   public int hashCode() {
      assert false : "hashCode not designed";
      return 42;
   }
   public int getSerializedDataSize() {
      int result = 16;
      result = result + (this.m_dataOffset_ << 1);
      if(this.isCharTrie()) {
         result += this.m_dataLength_ << 1;
      } else if(this.isIntTrie()) {
         result += this.m_dataLength_ << 2;
      }
      return result;
   }
   protected Trie(InputStream inputStream, Trie.DataManipulate dataManipulate) throws IOException {
      DataInputStream input = new DataInputStream(inputStream);
      int signature = input.readInt();
      this.m_options_ = input.readInt();
      if(!this.checkHeader(signature)) {
         throw new IllegalArgumentException("ICU data file error: Trie header authentication failed, please check if you have the most updated ICU data file");
      } else {
         if(dataManipulate != null) {
            this.m_dataManipulate_ = dataManipulate;
         } else {
            this.m_dataManipulate_ = new Trie.DefaultGetFoldingOffset();
         }
         this.m_isLatin1Linear_ = (this.m_options_ & 512) != 0;
         this.m_dataOffset_ = input.readInt();
         this.m_dataLength_ = input.readInt();
         this.unserialize(inputStream);
      }
   }
   protected Trie(char[] index, int options, Trie.DataManipulate dataManipulate) {
      this.m_options_ = options;
      if(dataManipulate != null) {
         this.m_dataManipulate_ = dataManipulate;
      } else {
         this.m_dataManipulate_ = new Trie.DefaultGetFoldingOffset();
      }
      this.m_isLatin1Linear_ = (this.m_options_ & 512) != 0;
      this.m_index_ = index;
      this.m_dataOffset_ = this.m_index_.length;
   }
   protected abstract int getSurrogateOffset(char var1, char var2);
   protected abstract int getValue(int var1);
   protected abstract int getInitialValue();
   protected final int getRawOffset(int offset, char ch) {
      return (this.m_index_[offset + (ch >> 5)] << 2) + (ch & 31);
   }
   protected final int getBMPOffset(char ch) {
      return ch >= '\ud800' && ch <= '\udbff'?this.getRawOffset(320, ch):this.getRawOffset(0, ch);
   }
   protected final int getLeadOffset(char ch) {
      return this.getRawOffset(0, ch);
   }
   protected final int getCodePointOffset(int ch) {
      return ch < 0?-1:(ch < '\ud800'?this.getRawOffset(0, (char)ch):(ch < 65536?this.getBMPOffset((char)ch):(ch <= 1114111?this.getSurrogateOffset(UTF16.getLeadSurrogate(ch), (char)(ch & 1023)):-1)));
   }
   protected void unserialize(InputStream inputStream) throws IOException {
      this.m_index_ = new char[this.m_dataOffset_];
      DataInputStream input = new DataInputStream(inputStream);
      for(int i = 0; i < this.m_dataOffset_; ++i) {
         this.m_index_[i] = input.readChar();
      }
   }
   protected final boolean isIntTrie() {
      return (this.m_options_ & 256) != 0;
   }
   protected final boolean isCharTrie() {
      return (this.m_options_ & 256) == 0;
   }
   private final boolean checkHeader(int signature) {
      return signature != 1416784229?false:(this.m_options_ & 15) == 5 && (this.m_options_ >> 4 & 15) == 2;
   }
   public interface DataManipulate {
      int getFoldingOffset(int var1);
   }
   private static class DefaultGetFoldingOffset implements Trie.DataManipulate {
      private DefaultGetFoldingOffset() {
      }
      public int getFoldingOffset(int value) {
         return value;
      }
   }
}