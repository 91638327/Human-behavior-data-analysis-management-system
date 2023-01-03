package com.ibm.icu.impl.duration.impl;
import com.ibm.icu.impl.duration.impl.RecordWriter;
import com.ibm.icu.lang.UCharacter;
import java.io.IOException;
import java.io.Writer;
// 
      Serialization.writeMultimap(this, stream);
   }
   @GwtIncompatible("java.io.ObjectOutputStream")
   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
      stream.defaultReadObject();
      this.expectedValuesPerKey = stream.readInt();
      int distinctKeys = Serialization.readCount(stream);
      Map<K, Collection<V>> map = Maps.newHashMapWithExpectedSize(distinctKeys);
      this.setMap(map);
      Serialization.populateMultimap(this, stream, distinctKeys);
   }
}
package com.google.common.collect;
import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractIndexedListIterator;
import com.google.common.collect.AbstractMapEntry;
import com.google.common.collect.AbstractTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;
@Beta
@GwtCompatible(
   emulated = true
)
public final class ArrayTable extends AbstractTable implements Serializable {
   private final ImmutableList rowList;
   private final ImmutableList columnList;
   private final ImmutableMap rowKeyToIndex;
   private final ImmutableMap columnKeyToIndex;
   private final Object[][] array;
   private transient ArrayTable.ColumnMap columnMap;
   private transient ArrayTable.RowMap rowMap;
   private static final long serialVersionUID = 0L;
   public static ArrayTable create(Iterable rowKeys, Iterable columnKeys) {
      return new ArrayTable(rowKeys, columnKeys);
   }
   public static ArrayTable create(Table table) {
      return table instanceof ArrayTable?new ArrayTable((ArrayTable)table):new ArrayTable(table);
   }
   private ArrayTable(Iterable rowKeys, Iterable columnKeys) {
      this.rowList = ImmutableList.copyOf(rowKeys);
      this.columnList = ImmutableList.copyOf(columnKeys);
      Preconditions.checkArgument(!this.rowList.isEmpty());
      Preconditions.checkArgument(!this.columnList.isEmpty());
      this.rowKeyToIndex = index(this.rowList);
      this.columnKeyToIndex = index(this.columnList);
      V[][] tmpArray = (Object[][])(new Object[this.rowList.size()][this.columnList.size()]);
      this.array = tmpArray;
      this.eraseAll();
   }
   private static ImmutableMap index(List list) {
      ImmutableMap.Builder<E, Integer> columnBuilder = ImmutableMap.builder();
      for(int i = 0; i < list.size(); ++i) {
         columnBuilder.put(list.get(i), Integer.valueOf(i));
      }
      return columnBuilder.build();
   }
   private ArrayTable(Table table) {
      this(table.rowKeySet(), table.columnKeySet());
      this.putAll(table);
   }
   private ArrayTable(ArrayTable table) {
      this.rowList = table.rowList;
      this.columnList = table.columnList;
      this.rowKeyToIndex = table.rowKeyToIndex;
      this.columnKeyToIndex = table.columnKeyToIndex;
      V[][] copy = (Object[][])(new Object[this.rowList.size()][this.columnList.size()]);
      this.array = copy;
      this.eraseAll();
      for(int i = 0; i < this.rowList.size(); ++i) {
         System.arraycopy(table.array[i], 0, copy[i], 0, table.array[i].length);
      }
   }
   public ImmutableList rowKeyList() {
      return this.rowList;
   }
   public ImmutableList columnKeyList() {
      return this.columnList;
   }
   public Object at(int rowIndex, int columnIndex) {
      Preconditions.checkElementIndex(rowIndex, this.rowList.size());
      Preconditions.checkElementIndex(columnIndex, this.columnList.size());
      return this.array[rowIndex][columnIndex];
   }
   public Object set(int rowIndex, int columnIndex, @Nullable Object value) {
      Preconditions.checkElementIndex(rowIndex, this.rowList.size());
      Preconditions.checkElementIndex(columnIndex, this.columnList.size());
      V oldValue = this.array[rowIndex][columnIndex];
      this.array[rowIndex][columnIndex] = value;
      return oldValue;
   }
   @GwtIncompatible("reflection")
   public Object[][] toArray(Class valueClass) {
      V[][] copy = (Object[][])((Object[][])Array.newInstance(valueClass, new int[]{this.rowList.size(), this.columnList.size()}));
      for(int i = 0; i < this.rowList.size(); ++i) {
         System.arraycopy(this.array[i], 0, copy[i], 0, this.array[i].length);
      }
      return copy;
   }
// 
      this.growIfNeeded();
      this.heapForIndex(insertIndex).bubbleUp(insertIndex, element);
      return this.size <= this.maximumSize || this.pollLast() != element;
   }
   public Object poll() {
      return this.isEmpty()?null:this.removeAndGet(0);
   }
   Object elementData(int index) {
      return this.queue[index];
   }
   public Object peek() {
      return this.isEmpty()?null:this.elementData(0);
   }
   private int getMaxElementIndex() {
      switch(this.size) {
      case 1:
         return 0;
      case 2:
         return 1;
      default:
         return this.maxHeap.compareElements(1, 2) <= 0?1:2;
      }
   }
   public Object pollFirst() {
      return this.poll();
   }
   public Object removeFirst() {
      return this.remove();
   }
   public Object peekFirst() {
      return this.peek();
   }
   public Object pollLast() {
      return this.isEmpty()?null:this.removeAndGet(this.getMaxElementIndex());
   }
   public Object removeLast() {
      if(this.isEmpty()) {
         throw new NoSuchElementException();
      } else {
         return this.removeAndGet(this.getMaxElementIndex());
      }
   }
   public Object peekLast() {
      return this.isEmpty()?null:this.elementData(this.getMaxElementIndex());
   }
   @VisibleForTesting
   MinMaxPriorityQueue.MoveDesc removeAt(int index) {
      Preconditions.checkPositionIndex(index, this.size);
      ++this.modCount;
      --this.size;
      if(this.size == index) {
         this.queue[this.size] = null;
         return null;
      } else {
         E actualLastElement = this.elementData(this.size);
         int lastElementAt = this.heapForIndex(this.size).getCorrectLastElement(actualLastElement);
         E toTrickle = this.elementData(this.size);
         this.queue[this.size] = null;
         MinMaxPriorityQueue.MoveDesc<E> changes = this.fillHole(index, toTrickle);
         return lastElementAt < index?(changes == null?new MinMaxPriorityQueue.MoveDesc(actualLastElement, toTrickle):new MinMaxPriorityQueue.MoveDesc(actualLastElement, changes.replaced)):changes;
      }
   }
   private MinMaxPriorityQueue.MoveDesc fillHole(int index, Object toTrickle) {
      MinMaxPriorityQueue<E>.Heap heap = this.heapForIndex(index);
      int vacated = heap.fillHoleAt(index);
      int bubbledTo = heap.bubbleUpAlternatingLevels(vacated, toTrickle);
      return bubbledTo == vacated?heap.tryCrossOverAndBubbleUp(index, vacated, toTrickle):(bubbledTo < index?new MinMaxPriorityQueue.MoveDesc(toTrickle, this.elementData(index)):null);
   }
   private Object removeAndGet(int index) {
      E value = this.elementData(index);
      this.removeAt(index);
      return value;
   }
   private MinMaxPriorityQueue.Heap heapForIndex(int i) {
      return isEvenLevel(i)?this.minHeap:this.maxHeap;
   }
   @VisibleForTesting
   static boolean isEvenLevel(int index) {
      int oneBased = index + 1;
      Preconditions.checkState(oneBased > 0, "negative index");
      return (oneBased & 1431655765) > (oneBased & -1431655766);
   }
   @VisibleForTesting
   boolean isIntact() {
      for(int i = 1; i < this.size; ++i) {
         if(!this.heapForIndex(i).verifyIndex(i)) {
            return false;
         }
      }
      return true;
   }
   public Iterator iterator() {
      return new MinMaxPriorityQueue.QueueIterator();
   }
   public void clear() {
      for(int i = 0; i < this.size; ++i) {
         this.queue[i] = null;
      }
      this.size = 0;
   }
   public Object[] toArray() {
      Object[] copyTo = new Object[this.size];
      System.arraycopy(this.queue, 0, copyTo, 0, this.size);
      return copyTo;
   }
   public Comparator comparator() {
      return this.minHeap.ordering;
   }
   @VisibleForTesting
   int capacity() {
      return this.queue.length;
   }
   @VisibleForTesting
   static int initialQueueSize(int configuredExpectedSize, int maximumSize, Iterable initialContents) {
      int result = configuredExpectedSize == -1?11:configuredExpectedSize;
//人类行为 系统配置 
      int block = 0;
      int index2Block = 0;
      label18:
      while(cp < limit) {
         if(cp >= '\ud800' && (cp <= '\udbff' || cp > '\uffff')) {
            if(cp < '\uffff') {
               index2Block = 2048;
               block = this.index[index2Block + (cp - '\ud800' >> 5)] << 2;
            } else {
               if(cp >= this.highStart) {
                  if(value == this.data32[this.highValueIndex]) {
                     cp = limit;
                  }
                  break;
               }
               int ix = 2080 + (cp >> 11);
               index2Block = this.index[ix];
               block = this.index[index2Block + (cp >> 5 & 63)] << 2;
            }
         } else {
            index2Block = 0;
            block = this.index[cp >> 5] << 2;
         }
         if(index2Block == this.index2NullOffset) {
            if(value != this.initialValue) {
               break;
            }
            cp += 2048;
         } else if(block == this.dataNullOffset) {
            if(value != this.initialValue) {
               break;
            }
            cp += 32;
         } else {
            int startIx = block + (cp & 31);
            int limitIx = block + 32;
            for(int ix = startIx; ix < limitIx; ++ix) {
               if(this.data32[ix] != value) {
                  cp += ix - startIx;
                  break label18;
               }
            }
            cp += limitIx - startIx;
         }
      }
      if(cp > limit) {
         cp = limit;
      }
      return cp - 1;
   }
}