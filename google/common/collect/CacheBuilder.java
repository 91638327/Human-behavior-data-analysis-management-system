package com.ibm.icu.impl;
import com.ibm.icu.impl.Trie2_16;
import com.ibm.icu.impl.Trie2_32;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
// 
         return 0L;
      }
   };
   private static final Logger logger = Logger.getLogger(CacheBuilder.class.getName());
   static final int UNSET_INT = -1;
   boolean strictParsing = true;
   int initialCapacity = -1;
   int concurrencyLevel = -1;
   long maximumSize = -1L;
   long maximumWeight = -1L;
   Weigher weigher;
   LocalCache.Strength keyStrength;
   LocalCache.Strength valueStrength;
   long expireAfterWriteNanos = -1L;
   long expireAfterAccessNanos = -1L;
   long refreshNanos = -1L;
   Equivalence keyEquivalence;
   Equivalence valueEquivalence;
   RemovalListener removalListener;
   Ticker ticker;
   Supplier statsCounterSupplier;
   CacheBuilder() {
      this.statsCounterSupplier = NULL_STATS_COUNTER;
   }
   public static CacheBuilder newBuilder() {
      return new CacheBuilder();
   }
   @Beta
   @GwtIncompatible("To be supported")
   public static CacheBuilder from(CacheBuilderSpec spec) {
      return spec.toCacheBuilder().lenientParsing();
   }
   @Beta
   @GwtIncompatible("To be supported")
   public static CacheBuilder from(String spec) {
      return from(CacheBuilderSpec.parse(spec));
   }
   @GwtIncompatible("To be supported")
   CacheBuilder lenientParsing() {
      this.strictParsing = false;
      return this;
   }
   @GwtIncompatible("To be supported")
   CacheBuilder keyEquivalence(Equivalence equivalence) {
      Preconditions.checkState(this.keyEquivalence == null, "key equivalence was already set to %s", new Object[]{this.keyEquivalence});
      this.keyEquivalence = (Equivalence)Preconditions.checkNotNull(equivalence);
      return this;
   }
   Equivalence getKeyEquivalence() {
      return (Equivalence)Objects.firstNonNull(this.keyEquivalence, this.getKeyStrength().defaultEquivalence());
   }
   @GwtIncompatible("To be supported")
   CacheBuilder valueEquivalence(Equivalence equivalence) {
      Preconditions.checkState(this.valueEquivalence == null, "value equivalence was already set to %s", new Object[]{this.valueEquivalence});
      this.valueEquivalence = (Equivalence)Preconditions.checkNotNull(equivalence);
      return this;
   }
   Equivalence getValueEquivalence() {
      return (Equivalence)Objects.firstNonNull(this.valueEquivalence, this.getValueStrength().defaultEquivalence());
   }
   public CacheBuilder initialCapacity(int initialCapacity) {
      Preconditions.checkState(this.initialCapacity == -1, "initial capacity was already set to %s", new Object[]{Integer.valueOf(this.initialCapacity)});
      Preconditions.checkArgument(initialCapacity >= 0);
      this.initialCapacity = initialCapacity;
      return this;
   }
   int getInitialCapacity() {
      return this.initialCapacity == -1?16:this.initialCapacity;
   }
   public CacheBuilder concurrencyLevel(int concurrencyLevel) {
      Preconditions.checkState(this.concurrencyLevel == -1, "concurrency level was already set to %s", new Object[]{Integer.valueOf(this.concurrencyLevel)});
      Preconditions.checkArgument(concurrencyLevel > 0);
      this.concurrencyLevel = concurrencyLevel;
      return this;
   }
   int getConcurrencyLevel() {
      return this.concurrencyLevel == -1?4:this.concurrencyLevel;
   }
   public CacheBuilder maximumSize(long size) {
      Preconditions.checkState(this.maximumSize == -1L, "maximum size was already set to %s", new Object[]{Long.valueOf(this.maximumSize)});
      Preconditions.checkState(this.maximumWeight == -1L, "maximum weight was already set to %s", new Object[]{Long.valueOf(this.maximumWeight)});
      Preconditions.checkState(this.weigher == null, "maximum size can not be combined with weigher");
      Preconditions.checkArgument(size >= 0L, "maximum size must not be negative");
      this.maximumSize = size;
      return this;
   }
   @GwtIncompatible("To be supported")
   public CacheBuilder maximumWeight(long weight) {
      Preconditions.checkState(this.maximumWeight == -1L, "maximum weight was already set to %s", new Object[]{Long.valueOf(this.maximumWeight)});
      Preconditions.checkState(this.maximumSize == -1L, "maximum size was already set to %s", new Object[]{Long.valueOf(this.maximumSize)});
      this.maximumWeight = weight;
      Preconditions.checkArgument(weight >= 0L, "maximum weight must not be negative");
      return this;
   }
   @GwtIncompatible("To be supported")
   public CacheBuilder weigher(Weigher weigher) {
      Preconditions.checkState(this.weigher == null);
      if(this.strictParsing) {
         Preconditions.checkState(this.maximumSize == -1L, "weigher can not be combined with maximum size", new Object[]{Long.valueOf(this.maximumSize)});
      }
      this.weigher = (Weigher)Preconditions.checkNotNull(weigher);
      return this;
   }
   long getMaximumWeight() {
      return this.expireAfterWriteNanos != 0L && this.expireAfterAccessNanos != 0L?(this.weigher == null?this.maximumSize:this.maximumWeight):0L;
   }
   Weigher getWeigher() {
// 
               foundRepetition = true;
               break;
            }
            patternRepeated[prevCh - PATTERN_CHAR_BASE] = 1;
            count = 0;
         }
         if(ch == 39) {
            if(i + 1 < intervalPattern.length() && intervalPattern.charAt(i + 1) == 39) {
               ++i;
            } else {
               inQuote = !inQuote;
            }
         } else if(!inQuote && (ch >= 97 && ch <= 122 || ch >= 65 && ch <= 90)) {
            prevCh = ch;
            ++count;
         }
      }
      if(count > 0 && !foundRepetition && patternRepeated[prevCh - PATTERN_CHAR_BASE] == 0) {
         count = 0;
      }
      return i - count;
   }
   public void setIntervalPattern(String skeleton, int lrgDiffCalUnit, String intervalPattern) {
      if(this.frozen) {
         throw new UnsupportedOperationException("no modification is allowed after DII is frozen");
      } else if(lrgDiffCalUnit > 12) {
         throw new IllegalArgumentException("calendar field is larger than MINIMUM_SUPPORTED_CALENDAR_FIELD");
      } else {
         if(this.fIntervalPatternsReadOnly) {
            this.fIntervalPatterns = cloneIntervalPatterns(this.fIntervalPatterns);
            this.fIntervalPatternsReadOnly = false;
         }
         DateIntervalInfo.PatternInfo ptnInfo = this.setIntervalPatternInternally(skeleton, CALENDAR_FIELD_TO_PATTERN_LETTER[lrgDiffCalUnit], intervalPattern);
         if(lrgDiffCalUnit == 11) {
            this.setIntervalPattern(skeleton, CALENDAR_FIELD_TO_PATTERN_LETTER[9], ptnInfo);
            this.setIntervalPattern(skeleton, CALENDAR_FIELD_TO_PATTERN_LETTER[10], ptnInfo);
         } else if(lrgDiffCalUnit == 5 || lrgDiffCalUnit == 7) {
            this.setIntervalPattern(skeleton, CALENDAR_FIELD_TO_PATTERN_LETTER[5], ptnInfo);
         }
      }
   }
   private DateIntervalInfo.PatternInfo setIntervalPatternInternally(String skeleton, String lrgDiffCalUnit, String intervalPattern) {
      Map<String, DateIntervalInfo.PatternInfo> patternsOfOneSkeleton = (Map)this.fIntervalPatterns.get(skeleton);
      boolean emptyHash = false;
      if(patternsOfOneSkeleton == null) {
         patternsOfOneSkeleton = new HashMap();
         emptyHash = true;
      }
      boolean order = this.fFirstDateInPtnIsLaterDate;
      if(intervalPattern.startsWith(LATEST_FIRST_PREFIX)) {
         order = true;
         int prefixLength = LATEST_FIRST_PREFIX.length();
         intervalPattern = intervalPattern.substring(prefixLength, intervalPattern.length());
      } else if(intervalPattern.startsWith(EARLIEST_FIRST_PREFIX)) {
         order = false;
         int earliestFirstLength = EARLIEST_FIRST_PREFIX.length();
         intervalPattern = intervalPattern.substring(earliestFirstLength, intervalPattern.length());
      }
      DateIntervalInfo.PatternInfo itvPtnInfo = genPatternInfo(intervalPattern, order);
      patternsOfOneSkeleton.put(lrgDiffCalUnit, itvPtnInfo);
      if(emptyHash) {
         this.fIntervalPatterns.put(skeleton, patternsOfOneSkeleton);
      }
      return itvPtnInfo;
   }
   private void setIntervalPattern(String skeleton, String lrgDiffCalUnit, DateIntervalInfo.PatternInfo ptnInfo) {
      Map<String, DateIntervalInfo.PatternInfo> patternsOfOneSkeleton = (Map)this.fIntervalPatterns.get(skeleton);
      patternsOfOneSkeleton.put(lrgDiffCalUnit, ptnInfo);
   }
   static DateIntervalInfo.PatternInfo genPatternInfo(String intervalPattern, boolean laterDateFirst) {
      int splitPoint = splitPatternInto2Part(intervalPattern);
      String firstPart = intervalPattern.substring(0, splitPoint);
      String secondPart = null;
      if(splitPoint < intervalPattern.length()) {
         secondPart = intervalPattern.substring(splitPoint, intervalPattern.length());
      }
      return new DateIntervalInfo.PatternInfo(firstPart, secondPart, laterDateFirst);
   }
   public DateIntervalInfo.PatternInfo getIntervalPattern(String skeleton, int field) {
      if(field > 12) {
         throw new IllegalArgumentException("no support for field less than MINUTE");
      } else {
         Map<String, DateIntervalInfo.PatternInfo> patternsOfOneSkeleton = (Map)this.fIntervalPatterns.get(skeleton);
         if(patternsOfOneSkeleton != null) {
            DateIntervalInfo.PatternInfo intervalPattern = (DateIntervalInfo.PatternInfo)patternsOfOneSkeleton.get(CALENDAR_FIELD_TO_PATTERN_LETTER[field]);
            if(intervalPattern != null) {
               return intervalPattern;
            }
         }
         return null;
      }
   }
   public String getFallbackIntervalPattern() {
      return this.fFallbackIntervalPattern;
   }
   public void setFallbackIntervalPattern(String fallbackPattern) {
      if(this.frozen) {
         throw new UnsupportedOperationException("no modification is allowed after DII is frozen");
      } else {
         int firstPatternIndex = fallbackPattern.indexOf("{0}");
         int secondPatternIndex = fallbackPattern.indexOf("{1}");
         if(firstPatternIndex != -1 && secondPatternIndex != -1) {
            if(firstPatternIndex > secondPatternIndex) {
               this.fFirstDateInPtnIsLaterDate = true;
            }
            this.fFallbackIntervalPattern = fallbackPattern;
         } else {
            throw new IllegalArgumentException("no pattern {0} or pattern {1} in fallbackPattern");
         }
      }
   }
   public boolean getDefaultOrder() {
      return this.fFirstDateInPtnIsLaterDate;
//用户管理 
         }
         if(quality < 0) {
            quality = 0;
         }
         return quality;
      }
   }
   static class CharsetRecog_2022CN extends CharsetRecog_2022 {
      private byte[][] escapeSequences = new byte[][]{{(byte)27, (byte)36, (byte)41, (byte)65}, {(byte)27, (byte)36, (byte)41, (byte)71}, {(byte)27, (byte)36, (byte)42, (byte)72}, {(byte)27, (byte)36, (byte)41, (byte)69}, {(byte)27, (byte)36, (byte)43, (byte)73}, {(byte)27, (byte)36, (byte)43, (byte)74}, {(byte)27, (byte)36, (byte)43, (byte)75}, {(byte)27, (byte)36, (byte)43, (byte)76}, {(byte)27, (byte)36, (byte)43, (byte)77}, {(byte)27, (byte)78}, {(byte)27, (byte)79}};
      String getName() {
         return "ISO-2022-CN";
      }
      CharsetMatch match(CharsetDetector det) {
         int confidence = this.match(det.fInputBytes, det.fInputLen, this.escapeSequences);
         return confidence == 0?null:new CharsetMatch(det, this, confidence);
      }
   }
   static class CharsetRecog_2022JP extends CharsetRecog_2022 {
      private byte[][] escapeSequences = new byte[][]{{(byte)27, (byte)36, (byte)40, (byte)67}, {(byte)27, (byte)36, (byte)40, (byte)68}, {(byte)27, (byte)36, (byte)64}, {(byte)27, (byte)36, (byte)65}, {(byte)27, (byte)36, (byte)66}, {(byte)27, (byte)38, (byte)64}, {(byte)27, (byte)40, (byte)66}, {(byte)27, (byte)40, (byte)72}, {(byte)27, (byte)40, (byte)73}, {(byte)27, (byte)40, (byte)74}, {(byte)27, (byte)46, (byte)65}, {(byte)27, (byte)46, (byte)70}};
      String getName() {
         return "ISO-2022-JP";
      }
      CharsetMatch match(CharsetDetector det) {
         int confidence = this.match(det.fInputBytes, det.fInputLen, this.escapeSequences);
         return confidence == 0?null:new CharsetMatch(det, this, confidence);
      }
   }
   static class CharsetRecog_2022KR extends CharsetRecog_2022 {
      private byte[][] escapeSequences = new byte[][]{{(byte)27, (byte)36, (byte)41, (byte)67}};
      String getName() {
         return "ISO-2022-KR";
      }
      CharsetMatch match(CharsetDetector det) {
         int confidence = this.match(det.fInputBytes, det.fInputLen, this.escapeSequences);
         return confidence == 0?null:new CharsetMatch(det, this, confidence);
      }
   }
}