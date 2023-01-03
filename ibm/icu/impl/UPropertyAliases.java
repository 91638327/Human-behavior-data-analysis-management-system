package com.ibm.icu.impl;
import com.ibm.icu.impl.ICUBinary;
import com.ibm.icu.impl.ICUData;
import com.ibm.icu.impl.IllegalIcuArgumentException;
import com.ibm.icu.util.BytesTrie;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.MissingResourceException;
public final class UPropertyAliases {
   private static final int IX_VALUE_MAPS_OFFSET = 0;
   private static final int IX_BYTE_TRIES_OFFSET = 1;
   private static final int IX_NAME_GROUPS_OFFSET = 2;
   private static final int IX_RESERVED3_OFFSET = 3;
   private int[] valueMaps;
   private byte[] bytesTries;
   private String nameGroups;
   private static final UPropertyAliases.IsAcceptable IS_ACCEPTABLE = new UPropertyAliases.IsAcceptable();
   private static final byte[] DATA_FORMAT = new byte[]{(byte)112, (byte)110, (byte)97, (byte)109};
   public static final UPropertyAliases INSTANCE;
   private void load(InputStream data) throws IOException {
      BufferedInputStream bis = new BufferedInputStream(data);
      ICUBinary.readHeader(bis, DATA_FORMAT, IS_ACCEPTABLE);
      DataInputStream ds = new DataInputStream(bis);
      int indexesLength = ds.readInt() / 4;
      if(indexesLength < 8) {
         throw new IOException("pnames.icu: not enough indexes");
      } else {
         int[] inIndexes = new int[indexesLength];
         inIndexes[0] = indexesLength * 4;
         for(int i = 1; i < indexesLength; ++i) {
            inIndexes[i] = ds.readInt();
         }
         int offset = inIndexes[0];
         int nextOffset = inIndexes[1];
         int numInts = (nextOffset - offset) / 4;
         this.valueMaps = new int[numInts];
         for(int i = 0; i < numInts; ++i) {
            this.valueMaps[i] = ds.readInt();
         }
         nextOffset = inIndexes[2];
         int numBytes = nextOffset - nextOffset;
         this.bytesTries = new byte[numBytes];
         ds.readFully(this.bytesTries);
         nextOffset = inIndexes[3];
         numBytes = nextOffset - nextOffset;
         StringBuilder sb = new StringBuilder(numBytes);
         for(int i = 0; i < numBytes; ++i) {
            sb.append((char)ds.readByte());
         }
         this.nameGroups = sb.toString();
         data.close();
      }
   }
   private UPropertyAliases() throws IOException {
      this.load(ICUData.getRequiredStream("data/icudt51b/pnames.icu"));
   }
   private int findProperty(int property) {
      int i = 1;
      for(int numRanges = this.valueMaps[0]; numRanges > 0; --numRanges) {
         int start = this.valueMaps[i];
         int limit = this.valueMaps[i + 1];
         i = i + 2;
         if(property < start) {
            break;
//人类行为报告 
         throw new IllegalArgumentException("Out of range: " + value);
      } else {
         return result;
      }
   }
   public static byte saturatedCast(long value) {
      return value > 127L?127:(value < -128L?-128:(byte)((int)value));
   }
   public static int compare(byte a, byte b) {
      return a - b;
   }
   public static byte min(byte... array) {
      Preconditions.checkArgument(array.length > 0);
      byte min = array[0];
      for(int i = 1; i < array.length; ++i) {
         if(array[i] < min) {
            min = array[i];
         }
      }
      return min;
   }
   public static byte max(byte... array) {
      Preconditions.checkArgument(array.length > 0);
      byte max = array[0];
      for(int i = 1; i < array.length; ++i) {
         if(array[i] > max) {
            max = array[i];
         }
      }
      return max;
   }
   public static String join(String separator, byte... array) {
      Preconditions.checkNotNull(separator);
      if(array.length == 0) {
         return "";
      } else {
         StringBuilder builder = new StringBuilder(array.length * 5);
         builder.append(array[0]);
         for(int i = 1; i < array.length; ++i) {
            builder.append(separator).append(array[i]);
         }
         return builder.toString();
      }
   }
   public static Comparator lexicographicalComparator() {
      return SignedBytes.LexicographicalComparator.INSTANCE;
   }
   private static enum LexicographicalComparator implements Comparator {
      INSTANCE;
      public int compare(byte[] left, byte[] right) {
         int minLength = Math.min(left.length, right.length);
         for(int i = 0; i < minLength; ++i) {
            int result = SignedBytes.compare(left[i], right[i]);
            if(result != 0) {
               return result;
            }
         }
         return left.length - right.length;
      }
   }
}