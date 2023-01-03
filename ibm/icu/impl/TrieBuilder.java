package com.ibm.icu.impl;
import java.util.Arrays;
public class TrieBuilder {
   public static final int DATA_BLOCK_LENGTH = 32;
   protected int[] m_index_ = new int['υ'];
   protected int m_indexLength_;
   protected int m_dataCapacity_;
   protected int m_dataLength_;
   protected boolean m_isLatin1Linear_;
   protected boolean m_isCompacted_;
   protected int[] m_map_;
   protected static final int SHIFT_ = 5;
   protected static final int MAX_INDEX_LENGTH_ = 34816;
   protected static final int BMP_INDEX_LENGTH_ = 2048;
   protected static final int SURROGATE_BLOCK_COUNT_ = 32;
   protected static final int MASK_ = 31;
   protected static final int INDEX_SHIFT_ = 2;
   protected static final int MAX_DATA_LENGTH_ = 262144;
   protected static final int OPTIONS_INDEX_SHIFT_ = 4;
   protected static final int OPTIONS_DATA_IS_32_BIT_ = 256;
   protected static final int OPTIONS_LATIN1_IS_LINEAR_ = 512;
   protected static final int DATA_GRANULARITY_ = 4;
   private static final int MAX_BUILD_TIME_DATA_LENGTH_ = 1115168;
   public boolean isInZeroBlock(int ch) {
      return !this.m_isCompacted_ && ch <= 1114111 && ch >= 0?this.m_index_[ch >> 5] == 0:true;
   }
   protected TrieBuilder() {
      this.m_map_ = new int['�'];
      this.m_isLatin1Linear_ = false;
      this.m_isCompacted_ = false;
      this.m_indexLength_ = 'υ';
   }
   protected TrieBuilder(TrieBuilder table) {
      this.m_indexLength_ = table.m_indexLength_;
      System.arraycopy(table.m_index_, 0, this.m_index_, 0, this.m_indexLength_);
      this.m_dataCapacity_ = table.m_dataCapacity_;
      this.m_dataLength_ = table.m_dataLength_;
      this.m_map_ = new int[table.m_map_.length];
      System.arraycopy(table.m_map_, 0, this.m_map_, 0, this.m_map_.length);
      this.m_isLatin1Linear_ = table.m_isLatin1Linear_;
      this.m_isCompacted_ = table.m_isCompacted_;
   }
   protected static final boolean equal_int(int[] array, int start1, int start2, int length) {
      while(length > 0 && array[start1] == array[start2]) {
         ++start1;
         ++start2;
         --length;
      }
      return length == 0;
   }
   protected void findUnusedBlocks() {
      Arrays.fill(this.m_map_, 255);
      for(int i = 0; i < this.m_indexLength_; ++i) {
         this.m_map_[Math.abs(this.m_index_[i]) >> 5] = 0;
      }
      this.m_map_[0] = 0;
   }
   protected static final int findSameIndexBlock(int[] index, int indexLength, int otherBlock) {
      for(int block = 2048; block < indexLength; block += 32) {
         if(equal_int(index, block, otherBlock, 32)) {
            return block;
         }
      }
      return indexLength;
   }
   public interface DataManipulate {
// 
   public PatternTokenizer setLimit(int limit) {
      this.limit = limit;
      return this;
   }
   public int getStart() {
      return this.start;
   }
   public PatternTokenizer setStart(int start) {
      this.start = start;
      return this;
   }
   public PatternTokenizer setPattern(CharSequence pattern) {
      return this.setPattern(pattern.toString());
   }
   public PatternTokenizer setPattern(String pattern) {
      if(pattern == null) {
         throw new IllegalArgumentException("Inconsistent arguments");
      } else {
         this.start = 0;
         this.limit = pattern.length();
         this.pattern = pattern;
         return this;
      }
   }
   public String quoteLiteral(CharSequence string) {
      return this.quoteLiteral(string.toString());
   }
   public String quoteLiteral(String string) {
      if(this.needingQuoteCharacters == null) {
         this.needingQuoteCharacters = (new UnicodeSet()).addAll(this.syntaxCharacters).addAll(this.ignorableCharacters).addAll(this.extraQuotingCharacters);
         if(this.usingSlash) {
            this.needingQuoteCharacters.add(92);
         }
         if(this.usingQuote) {
            this.needingQuoteCharacters.add(39);
         }
      }
      StringBuffer result = new StringBuffer();
      int quotedChar = NO_QUOTE;
      int cp;
      for(int i = 0; i < string.length(); i += UTF16.getCharCount(cp)) {
         cp = UTF16.charAt(string, i);
         if(this.escapeCharacters.contains(cp)) {
            if(quotedChar == IN_QUOTE) {
               result.append('\'');
               quotedChar = NO_QUOTE;
            }
            this.appendEscaped(result, cp);
         } else if(this.needingQuoteCharacters.contains(cp)) {
            if(quotedChar == IN_QUOTE) {
               UTF16.append(result, cp);
               if(this.usingQuote && cp == 39) {
                  result.append('\'');
               }
            } else if(this.usingSlash) {
               result.append('\\');
               UTF16.append(result, cp);
            } else if(this.usingQuote) {
               if(cp == 39) {
                  result.append('\'');
                  result.append('\'');
               } else {
                  result.append('\'');
                  UTF16.append(result, cp);
                  quotedChar = IN_QUOTE;
               }
            } else {
               this.appendEscaped(result, cp);
            }
         } else {
            if(quotedChar == IN_QUOTE) {
               result.append('\'');
               quotedChar = NO_QUOTE;
            }
            UTF16.append(result, cp);
         }
      }
      if(quotedChar == IN_QUOTE) {
         result.append('\'');
      }
      return result.toString();
   }
   private void appendEscaped(StringBuffer result, int cp) {
      if(cp <= '\uffff') {
         result.append("\\u").append(Utility.hex((long)cp, 4));
      } else {
         result.append("\\U").append(Utility.hex((long)cp, 8));
      }
   }
   public String normalize() {
      int oldStart = this.start;
      StringBuffer result = new StringBuffer();
      StringBuffer buffer = new StringBuffer();
      while(true) {
         buffer.setLength(0);
         int status = this.next(buffer);
         if(status == 0) {
            this.start = oldStart;
            return result.toString();
         }
         if(status != 1) {
            result.append(this.quoteLiteral((CharSequence)buffer));
         } else {
            result.append(buffer);
         }
      }
   }
   public int next(StringBuffer buffer) {
      if(this.start >= this.limit) {
         return 0;
      } else {
         int status = 5;
         int lastQuote = 5;
         int quoteStatus = 0;
         int hexCount = 0;
         int hexValue = 0;
// 
         if(buf.length() > 0) {
            buf.append(", ");
         }
         buf.append("region=");
         buf.append(this._region);
      }
      if(this._variant.length() > 0) {
         if(buf.length() > 0) {
            buf.append(", ");
         }
         buf.append("variant=");
         buf.append(this._variant);
      }
      return buf.toString();
   }
   public int hashCode() {
      int h = this._hash;
      if(h == 0) {
         for(int i = 0; i < this._language.length(); ++i) {
            h = 31 * h + this._language.charAt(i);
         }
         for(int i = 0; i < this._script.length(); ++i) {
            h = 31 * h + this._script.charAt(i);
         }
         for(int i = 0; i < this._region.length(); ++i) {
            h = 31 * h + this._region.charAt(i);
         }
         for(int i = 0; i < this._variant.length(); ++i) {
            h = 31 * h + this._variant.charAt(i);
         }
         this._hash = h;
      }
      return h;
   }
   private static class Cache extends LocaleObjectCache {
      protected BaseLocale.Key normalizeKey(BaseLocale.Key key) {
         return BaseLocale.Key.normalize(key);
      }
      protected BaseLocale createObject(BaseLocale.Key key) {
         return new BaseLocale(key._lang, key._scrt, key._regn, key._vart);
      }
   }
   private static class Key implements Comparable {
      private String _lang = "";
      private String _scrt = "";
      private String _regn = "";
      private String _vart = "";
      private volatile int _hash;
      public Key(String language, String script, String region, String variant) {
         if(language != null) {
            this._lang = language;
         }
         if(script != null) {
            this._scrt = script;
         }
         if(region != null) {
            this._regn = region;
         }
         if(variant != null) {
            this._vart = variant;
         }
      }
      public boolean equals(Object obj) {
         return this == obj || obj instanceof BaseLocale.Key && AsciiUtil.caseIgnoreMatch(((BaseLocale.Key)obj)._lang, this._lang) && AsciiUtil.caseIgnoreMatch(((BaseLocale.Key)obj)._scrt, this._scrt) && AsciiUtil.caseIgnoreMatch(((BaseLocale.Key)obj)._regn, this._regn) && AsciiUtil.caseIgnoreMatch(((BaseLocale.Key)obj)._vart, this._vart);
      }
      public int compareTo(BaseLocale.Key other) {
         int res = AsciiUtil.caseIgnoreCompare(this._lang, other._lang);
         if(res == 0) {
            res = AsciiUtil.caseIgnoreCompare(this._scrt, other._scrt);
            if(res == 0) {
               res = AsciiUtil.caseIgnoreCompare(this._regn, other._regn);
               if(res == 0) {
                  res = AsciiUtil.caseIgnoreCompare(this._vart, other._vart);
               }
            }
         }
         return res;
      }
      public int hashCode() {
         int h = this._hash;
         if(h == 0) {
            for(int i = 0; i < this._lang.length(); ++i) {
               h = 31 * h + AsciiUtil.toLower(this._lang.charAt(i));
            }
            for(int i = 0; i < this._scrt.length(); ++i) {
               h = 31 * h + AsciiUtil.toLower(this._scrt.charAt(i));
            }
            for(int i = 0; i < this._regn.length(); ++i) {
               h = 31 * h + AsciiUtil.toLower(this._regn.charAt(i));
            }
            for(int i = 0; i < this._vart.length(); ++i) {
               h = 31 * h + AsciiUtil.toLower(this._vart.charAt(i));
            }
            this._hash = h;
         }
         return h;
      }
      public static BaseLocale.Key normalize(BaseLocale.Key key) {
         String lang = AsciiUtil.toLowerString(key._lang).intern();
         String scrt = AsciiUtil.toTitleString(key._scrt).intern();
         String regn = AsciiUtil.toUpperString(key._regn).intern();
         String vart = AsciiUtil.toUpperString(key._vart).intern();
         return new BaseLocale.Key(lang, scrt, regn, vart);
      }
   }
}