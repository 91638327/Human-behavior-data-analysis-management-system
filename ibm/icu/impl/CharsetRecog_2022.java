package com.ibm.icu.text;
import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import com.ibm.icu.text.CharsetRecognizer;
abstract class CharsetRecog_Unicode extends CharsetRecognizer {
// 
         return ICUResourceBundle.getFullLocaleNameSet(this.bundleName, this.loader());
      }
      public void updateVisibleIDs(Map result) {
         for(String id : ICUResourceBundle.getAvailableLocaleNameSet(this.bundleName, this.loader())) {
            result.put(id, this);
         }
      }
      protected Object handleCreate(ULocale loc, int kind, ICUService service) {
         return ICUResourceBundle.getBundleInstance(this.bundleName, loc, this.loader());
      }
      protected ClassLoader loader() {
         ClassLoader cl = this.getClass().getClassLoader();
         if(cl == null) {
            cl = Utility.getFallbackClassLoader();
         }
         return cl;
      }
      public String toString() {
         return super.toString() + ", bundle: " + this.bundleName;
      }
   }
   public static class LocaleKey extends ICUService.Key {
      private int kind;
      private int varstart;
      private String primaryID;
      private String fallbackID;
      private String currentID;
      public static final int KIND_ANY = -1;
      public static ICULocaleService.LocaleKey createWithCanonicalFallback(String primaryID, String canonicalFallbackID) {
         return createWithCanonicalFallback(primaryID, canonicalFallbackID, -1);
      }
      public static ICULocaleService.LocaleKey createWithCanonicalFallback(String primaryID, String canonicalFallbackID, int kind) {
         if(primaryID == null) {
            return null;
         } else {
            String canonicalPrimaryID = ULocale.getName(primaryID);
            return new ICULocaleService.LocaleKey(primaryID, canonicalPrimaryID, canonicalFallbackID, kind);
         }
      }
      public static ICULocaleService.LocaleKey createWithCanonical(ULocale locale, String canonicalFallbackID, int kind) {
         if(locale == null) {
            return null;
         } else {
            String canonicalPrimaryID = locale.getName();
            return new ICULocaleService.LocaleKey(canonicalPrimaryID, canonicalPrimaryID, canonicalFallbackID, kind);
         }
      }
      protected LocaleKey(String primaryID, String canonicalPrimaryID, String canonicalFallbackID, int kind) {
         super(primaryID);
         this.kind = kind;
         if(canonicalPrimaryID != null && !canonicalPrimaryID.equalsIgnoreCase("root")) {
            int idx = canonicalPrimaryID.indexOf(64);
            if(idx == 4 && canonicalPrimaryID.regionMatches(true, 0, "root", 0, 4)) {
               this.primaryID = canonicalPrimaryID.substring(4);
               this.varstart = 0;
               this.fallbackID = null;
            } else {
               this.primaryID = canonicalPrimaryID;
               this.varstart = idx;
               if(canonicalFallbackID != null && !this.primaryID.equals(canonicalFallbackID)) {
                  this.fallbackID = canonicalFallbackID;
               } else {
                  this.fallbackID = "";
               }
            }
         } else {
            this.primaryID = "";
            this.fallbackID = null;
         }
         this.currentID = this.varstart == -1?this.primaryID:this.primaryID.substring(0, this.varstart);
      }
      public String prefix() {
         return this.kind == -1?null:Integer.toString(this.kind());
      }
      public int kind() {
         return this.kind;
      }
      public String canonicalID() {
         return this.primaryID;
      }
      public String currentID() {
         return this.currentID;
      }
      public String currentDescriptor() {
         String result = this.currentID();
         if(result != null) {
            StringBuilder buf = new StringBuilder();
            if(this.kind != -1) {
               buf.append(this.prefix());
            }
            buf.append('/');
            buf.append(result);
            if(this.varstart != -1) {
               buf.append(this.primaryID.substring(this.varstart, this.primaryID.length()));
            }
            result = buf.toString();
         }
         return result;
      }
      public ULocale canonicalLocale() {
         return new ULocale(this.primaryID);
      }
      public ULocale currentLocale() {
         return this.varstart == -1?new ULocale(this.currentID):new ULocale(this.currentID + this.primaryID.substring(this.varstart));
      }
      public boolean fallback() {
         int x = this.currentID.lastIndexOf(95);
         if(x == -1) {
            if(this.fallbackID != null) {
               this.currentID = this.fallbackID;
               if(this.fallbackID.length() == 0) {
                  this.fallbackID = null;
               } else {
// 
         this.addDelayedShutdownHook(service, terminationTimeout, timeUnit);
         return service;
      }
      final void addDelayedShutdownHook(final ExecutorService service, final long terminationTimeout, final TimeUnit timeUnit) {
         Preconditions.checkNotNull(service);
         Preconditions.checkNotNull(timeUnit);
         this.addShutdownHook(MoreExecutors.newThread("DelayedShutdownHook-for-" + service, new Runnable() {
            public void run() {
               try {
                  service.shutdown();
                  service.awaitTermination(terminationTimeout, timeUnit);
               } catch (InterruptedException var2) {
                  ;
               }
            }
         }));
      }
      final ExecutorService getExitingExecutorService(ThreadPoolExecutor executor) {
         return this.getExitingExecutorService(executor, 120L, TimeUnit.SECONDS);
      }
      final ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor) {
         return this.getExitingScheduledExecutorService(executor, 120L, TimeUnit.SECONDS);
      }
      @VisibleForTesting
      void addShutdownHook(Thread hook) {
         Runtime.getRuntime().addShutdownHook(hook);
      }
   }
   private static class ListeningDecorator extends AbstractListeningExecutorService {
      private final ExecutorService delegate;
      ListeningDecorator(ExecutorService delegate) {
         this.delegate = (ExecutorService)Preconditions.checkNotNull(delegate);
      }
      public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
         return this.delegate.awaitTermination(timeout, unit);
      }
      public boolean isShutdown() {
         return this.delegate.isShutdown();
      }
      public boolean isTerminated() {
         return this.delegate.isTerminated();
      }
      public void shutdown() {
         this.delegate.shutdown();
      }
      public List shutdownNow() {
         return this.delegate.shutdownNow();
      }
      public void execute(Runnable command) {
         this.delegate.execute(command);
      }
   }
   private static class SameThreadExecutorService extends AbstractListeningExecutorService {
      private final Lock lock;
      private final Condition termination;
      private int runningTasks;
      private boolean shutdown;
      private SameThreadExecutorService() {
         this.lock = new ReentrantLock();
         this.termination = this.lock.newCondition();
         this.runningTasks = 0;
         this.shutdown = false;
      }
      public void execute(Runnable command) {
         this.startTask();
         try {
            command.run();
         } finally {
            this.endTask();
         }
      }
      public boolean isShutdown() {
         this.lock.lock();
         boolean var1;
         try {
            var1 = this.shutdown;
         } finally {
            this.lock.unlock();
         }
         return var1;
      }
      public void shutdown() {
         this.lock.lock();
         try {
            this.shutdown = true;
         } finally {
            this.lock.unlock();
         }
      }
      public List shutdownNow() {
         this.shutdown();
         return Collections.emptyList();
      }
      public boolean isTerminated() {
         this.lock.lock();
         boolean var1;
         try {
            var1 = this.shutdown && this.runningTasks == 0;
         } finally {
            this.lock.unlock();
         }
         return var1;
      }
      public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
         long nanos = unit.toNanos(timeout);
         this.lock.lock();
         boolean var10;
         try {
            while(!this.isTerminated()) {
               if(nanos <= 0L) {
                  var10 = false;
                  return var10;
               }
               nanos = this.termination.awaitNanos(nanos);
            }
            var10 = true;
// 
                  ++this.pos;
                  this.skipToEndOfLine();
                  p = this.pos;
                  l = this.limit;
                  break;
               default:
                  return c;
               }
            } else {
               if(c != 35) {
                  this.pos = p;
                  return c;
               }
               this.pos = p;
               this.checkLenient();
               this.skipToEndOfLine();
               p = this.pos;
               l = this.limit;
            }
         }
      }
   }
   private void checkLenient() throws IOException {
      if(!this.lenient) {
         throw this.syntaxError("Use JsonReader.setLenient(true) to accept malformed JSON");
      }
   }
   private void skipToEndOfLine() throws IOException {
      while(this.pos < this.limit || this.fillBuffer(1)) {
         char c = this.buffer[this.pos++];
         if(c == 10) {
            ++this.lineNumber;
            this.lineStart = this.pos;
         } else if(c != 13) {
            continue;
         }
         break;
      }
   }
   private boolean skipTo(String toFind) throws IOException {
      for(; this.pos + toFind.length() <= this.limit || this.fillBuffer(toFind.length()); ++this.pos) {
         if(this.buffer[this.pos] == 10) {
            ++this.lineNumber;
            this.lineStart = this.pos + 1;
         } else {
            int c = 0;
            while(true) {
               if(c >= toFind.length()) {
                  return true;
               }
               if(this.buffer[this.pos + c] != toFind.charAt(c)) {
                  break;
               }
               ++c;
            }
         }
      }
      return false;
   }
   public String toString() {
      return this.getClass().getSimpleName() + " at line " + this.getLineNumber() + " column " + this.getColumnNumber();
   }
   private char readEscapeCharacter() throws IOException {
      if(this.pos == this.limit && !this.fillBuffer(1)) {
         throw this.syntaxError("Unterminated escape sequence");
      } else {
         char escaped = this.buffer[this.pos++];
         switch(escaped) {
         case '\n':
            ++this.lineNumber;
            this.lineStart = this.pos;
         case '\"':
         case '\'':
         case '\\':
         default:
            return escaped;
         case 'b':
            return '\b';
         case 'f':
            return '\f';
         case 'n':
            return '\n';
         case 'r':
            return '\r';
         case 't':
            return '\t';
         case 'u':
            if(this.pos + 4 > this.limit && !this.fillBuffer(4)) {
               throw this.syntaxError("Unterminated escape sequence");
            } else {
               char result = '\u0000';
               int i = this.pos;
               for(int end = i + 4; i < end; ++i) {
                  char c = this.buffer[i];
                  result = (char)(result << 4);
                  if(c >= 48 && c <= 57) {
                     result = (char)(result + (c - 48));
                  } else if(c >= 97 && c <= 102) {
                     result = (char)(result + c - 97 + 10);
                  } else {
                     if(c < 65 || c > 70) {
                        throw new NumberFormatException("\\u" + new String(this.buffer, this.pos, 4));
                     }
                     result = (char)(result + c - 65 + 10);
                  }
               }
               this.pos += 4;
               return result;
            }
         }
      }
   }
   private IOException syntaxError(String message) throws IOException {
      throw new MalformedJsonException(message + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
   }
   private void consumeNonExecutePrefix() throws IOException {
//???????? 
   private InputStream fInputStream = null;
   private String fCharsetName;
   private String fLang;
   public Reader getReader() {
      InputStream inputStream = this.fInputStream;
      if(inputStream == null) {
         inputStream = new ByteArrayInputStream(this.fRawInput, 0, this.fRawLength);
      }
      try {
         inputStream.reset();
         return new InputStreamReader(inputStream, this.getName());
      } catch (IOException var3) {
         return null;
      }
   }
   public String getString() throws IOException {
      return this.getString(-1);
   }
   public String getString(int maxLength) throws IOException {
      String result = null;
      if(this.fInputStream == null) {
         String name = this.getName();
         int startSuffix = name.indexOf("_rtl") < 0?name.indexOf("_ltr"):name.indexOf("_rtl");
         if(startSuffix > 0) {
            name = name.substring(0, startSuffix);
         }
         result = new String(this.fRawInput, name);
         return result;
      } else {
         StringBuilder sb = new StringBuilder();
         char[] buffer = new char[1024];
         Reader reader = this.getReader();
         int max = maxLength < 0?Integer.MAX_VALUE:maxLength;
         int var11;
         for(bytesRead = 0; (var11 = reader.read(buffer, 0, Math.min(max, 1024))) >= 0; max -= var11) {
            sb.append(buffer, 0, var11);
         }
         reader.close();
         return sb.toString();
      }
   }
   public int getConfidence() {
      return this.fConfidence;
   }
   public String getName() {
      return this.fCharsetName;
   }
   public String getLanguage() {
      return this.fLang;
   }
   public int compareTo(CharsetMatch other) {
      int compareResult = 0;
      if(this.fConfidence > other.fConfidence) {
         compareResult = 1;
      } else if(this.fConfidence < other.fConfidence) {
         compareResult = -1;
      }
      return compareResult;
   }
   CharsetMatch(CharsetDetector det, CharsetRecognizer rec, int conf) {
      this.fConfidence = conf;
      if(det.fInputStream == null) {
         this.fRawInput = det.fRawInput;
         this.fRawLength = det.fRawLength;
      }
      this.fInputStream = det.fInputStream;
      this.fCharsetName = rec.getName();
      this.fLang = rec.getLanguage();
   }
   CharsetMatch(CharsetDetector det, CharsetRecognizer rec, int conf, String csName, String lang) {
      this.fConfidence = conf;
      if(det.fInputStream == null) {
         this.fRawInput = det.fRawInput;
         this.fRawLength = det.fRawLength;
      }
      this.fInputStream = det.fInputStream;
      this.fCharsetName = csName;
      this.fLang = lang;
   }
}
