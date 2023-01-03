package com.google.common.net;
import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
@GwtCompatible
public final class HttpHeaders {
   public static final String CACHE_CONTROL = "Cache-Control";
   public static final String CONTENT_LENGTH = "Content-Length";
   public static final String CONTENT_TYPE = "Content-Type";
   public static final String DATE = "Date";
   public static final String PRAGMA = "Pragma";
   public static final String VIA = "Via";
   public static final String WARNING = "Warning";
   public static final String ACCEPT = "Accept";
// 
   }
   @Beta
   protected List standardSubList(int fromIndex, int toIndex) {
      return Lists.subListImpl(this, fromIndex, toIndex);
   }
   @Beta
   protected boolean standardEquals(@Nullable Object object) {
      return Lists.equalsImpl(this, object);
   }
   @Beta
   protected int standardHashCode() {
      return Lists.hashCodeImpl(this);
   }
}
package com.google.common.collect;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ForwardingIterator;
import java.util.ListIterator;
@GwtCompatible
public abstract class ForwardingListIterator extends ForwardingIterator implements ListIterator {
   protected abstract ListIterator delegate();
   public void add(Object element) {
      this.delegate().add(element);
   }
   public boolean hasPrevious() {
      return this.delegate().hasPrevious();
   }
   public int nextIndex() {
      return this.delegate().nextIndex();
   }
   public Object previous() {
      return this.delegate().previous();
   }
   public int previousIndex() {
      return this.delegate().previousIndex();
   }
   public void set(Object element) {
      this.delegate().set(element);
   }
}