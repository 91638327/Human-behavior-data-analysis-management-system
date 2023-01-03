package com.ibm.icu.text;
import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import com.ibm.icu.text.CharsetRecognizer;
abstract class CharsetRecog_2022 extends CharsetRecognizer {
   int match(byte[] text, int textLen, byte[][] escapeSequences) {
      int hits = 0;
      int misses = 0;
      int shifts = 0;
      label18:
      for(int i = 0; i < textLen; ++i) {
         if(text[i] == 27) {
            label37:
            for(int escN = 0; escN < escapeSequences.length; ++escN) {
               byte[] seq = escapeSequences[escN];
               if(textLen - i >= seq.length) {
                  for(int j = 1; j < seq.length; ++j) {
                     if(seq[j] != text[i + j]) {
                        continue label37;
                     }
                  }
                  ++hits;
                  i += seq.length - 1;
                  continue label18;
               }
            }
            ++misses;
         }
         if(text[i] == 14 || text[i] == 15) {
            ++shifts;
         }
      }
      if(hits == 0) {
         return 0;
      } else {
         int quality = (100 * hits - 100 * misses) / (hits + misses);
         if(hits + shifts < 5) {
// 
         throw new UnsupportedOperationException();
      }
      public MapMakerInternalMap.ReferenceEntry getNext() {
         throw new UnsupportedOperationException();
      }
      public int getHash() {
         throw new UnsupportedOperationException();
      }
      public Object getKey() {
         throw new UnsupportedOperationException();
      }
      public long getExpirationTime() {
         throw new UnsupportedOperationException();
      }
      public void setExpirationTime(long time) {
         throw new UnsupportedOperationException();
      }
      public MapMakerInternalMap.ReferenceEntry getNextExpirable() {
         throw new UnsupportedOperationException();
      }
      public void setNextExpirable(MapMakerInternalMap.ReferenceEntry next) {
         throw new UnsupportedOperationException();
      }
      public MapMakerInternalMap.ReferenceEntry getPreviousExpirable() {
         throw new UnsupportedOperationException();
      }
      public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry previous) {
         throw new UnsupportedOperationException();
      }
      public MapMakerInternalMap.ReferenceEntry getNextEvictable() {
         throw new UnsupportedOperationException();
      }
      public void setNextEvictable(MapMakerInternalMap.ReferenceEntry next) {
         throw new UnsupportedOperationException();
      }
      public MapMakerInternalMap.ReferenceEntry getPreviousEvictable() {
         throw new UnsupportedOperationException();
      }
      public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry previous) {
         throw new UnsupportedOperationException();
      }
   }
   abstract static class AbstractSerializationProxy extends ForwardingConcurrentMap implements Serializable {
      private static final long serialVersionUID = 3L;
      final MapMakerInternalMap.Strength keyStrength;
      final MapMakerInternalMap.Strength valueStrength;
      final Equivalence keyEquivalence;
      final Equivalence valueEquivalence;
      final long expireAfterWriteNanos;
      final long expireAfterAccessNanos;
      final int maximumSize;
      final int concurrencyLevel;
      final MapMaker.RemovalListener removalListener;
      transient ConcurrentMap delegate;
      AbstractSerializationProxy(MapMakerInternalMap.Strength keyStrength, MapMakerInternalMap.Strength valueStrength, Equivalence keyEquivalence, Equivalence valueEquivalence, long expireAfterWriteNanos, long expireAfterAccessNanos, int maximumSize, int concurrencyLevel, MapMaker.RemovalListener removalListener, ConcurrentMap delegate) {
         this.keyStrength = keyStrength;
         this.valueStrength = valueStrength;
         this.keyEquivalence = keyEquivalence;
         this.valueEquivalence = valueEquivalence;
         this.expireAfterWriteNanos = expireAfterWriteNanos;
         this.expireAfterAccessNanos = expireAfterAccessNanos;
         this.maximumSize = maximumSize;
         this.concurrencyLevel = concurrencyLevel;
         this.removalListener = removalListener;
         this.delegate = delegate;
      }
      protected ConcurrentMap delegate() {
         return this.delegate;
      }
      void writeMapTo(ObjectOutputStream out) throws IOException {
         out.writeInt(this.delegate.size());
         for(Entry<K, V> entry : this.delegate.entrySet()) {
            out.writeObject(entry.getKey());
            out.writeObject(entry.getValue());
         }
         out.writeObject((Object)null);
      }
      MapMaker readMapMaker(ObjectInputStream in) throws IOException {
         int size = in.readInt();
         MapMaker mapMaker = (new MapMaker()).initialCapacity(size).setKeyStrength(this.keyStrength).setValueStrength(this.valueStrength).keyEquivalence(this.keyEquivalence).concurrencyLevel(this.concurrencyLevel);
         mapMaker.removalListener(this.removalListener);
         if(this.expireAfterWriteNanos > 0L) {
            mapMaker.expireAfterWrite(this.expireAfterWriteNanos, TimeUnit.NANOSECONDS);
         }
         if(this.expireAfterAccessNanos > 0L) {
            mapMaker.expireAfterAccess(this.expireAfterAccessNanos, TimeUnit.NANOSECONDS);
         }
         if(this.maximumSize != -1) {
            mapMaker.maximumSize(this.maximumSize);
         }
         return mapMaker;
      }
      void readEntries(ObjectInputStream in) throws IOException, ClassNotFoundException {
         while(true) {
            K key = in.readObject();
            if(key == null) {
               return;
            }
            V value = in.readObject();
            this.delegate.put(key, value);
         }
      }
   }
   static final class CleanupMapTask implements Runnable {
      final WeakReference mapReference;
      public CleanupMapTask(MapMakerInternalMap map) {
         this.mapReference = new WeakReference(map);
      }
      public void run() {
         MapMakerInternalMap<?, ?> map = (MapMakerInternalMap)this.mapReference.get();
         if(map == null) {
            throw new CancellationException();
// 
smearedHash, rowHead);
         LinkedHashMultimap.succeedsInValueSet(this.lastEntry, newEntry);
         LinkedHashMultimap.succeedsInValueSet(newEntry, this);
         LinkedHashMultimap.succeedsInMultimap(LinkedHashMultimap.this.multimapHeaderEntry.getPredecessorInMultimap(), newEntry);
         LinkedHashMultimap.succeedsInMultimap(newEntry, LinkedHashMultimap.this.multimapHeaderEntry);
         this.hashTable[bucket] = newEntry;
         ++this.size;
         ++this.modCount;
         this.rehashIfNecessary();
         return true;
      }
      private void rehashIfNecessary() {
         if(Hashing.needsResizing(this.size, this.hashTable.length, 1.0D)) {
            LinkedHashMultimap.ValueEntry<K, V>[] hashTable = new LinkedHashMultimap.ValueEntry[this.hashTable.length * 2];
            this.hashTable = hashTable;
            int mask = hashTable.length - 1;
            for(LinkedHashMultimap.ValueSetLink<K, V> entry = this.firstEntry; entry != this; entry = entry.getSuccessorInValueSet()) {
               LinkedHashMultimap.ValueEntry<K, V> valueEntry = (LinkedHashMultimap.ValueEntry)entry;
               int bucket = valueEntry.smearedValueHash & mask;
               valueEntry.nextInValueBucket = hashTable[bucket];
               hashTable[bucket] = valueEntry;
            }
         }
      }
      public boolean remove(@Nullable Object o) {
         int smearedHash = Hashing.smearedHash(o);
         int bucket = smearedHash & this.mask();
         LinkedHashMultimap.ValueEntry<K, V> prev = null;
         for(LinkedHashMultimap.ValueEntry<K, V> entry = this.hashTable[bucket]; entry != null; entry = entry.nextInValueBucket) {
            if(entry.matchesValue(o, smearedHash)) {
               if(prev == null) {
                  this.hashTable[bucket] = entry.nextInValueBucket;
               } else {
                  prev.nextInValueBucket = entry.nextInValueBucket;
               }
               LinkedHashMultimap.deleteFromValueSet(entry);
               LinkedHashMultimap.deleteFromMultimap(entry);
               --this.size;
               ++this.modCount;
               return true;
            }
            prev = entry;
         }
         return false;
      }
      public void clear() {
         Arrays.fill(this.hashTable, (Object)null);
         this.size = 0;
         for(LinkedHashMultimap.ValueSetLink<K, V> entry = this.firstEntry; entry != this; entry = entry.getSuccessorInValueSet()) {
            LinkedHashMultimap.ValueEntry<K, V> valueEntry = (LinkedHashMultimap.ValueEntry)entry;
            LinkedHashMultimap.deleteFromMultimap(valueEntry);
         }
         LinkedHashMultimap.succeedsInValueSet(this, this);
         ++this.modCount;
      }
   }
   private interface ValueSetLink {
      LinkedHashMultimap.ValueSetLink getPredecessorInValueSet();
      LinkedHashMultimap.ValueSetLink getSuccessorInValueSet();
      void setPredecessorInValueSet(LinkedHashMultimap.ValueSetLink var1);
      void setSuccessorInValueSet(LinkedHashMultimap.ValueSetLink var1);
   }
}