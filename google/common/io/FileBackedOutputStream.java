package com.google.common.io;
import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.io.ByteSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
@Beta
public final class FileBackedOutputStream extends OutputStream {
   private final int fileThreshold;
   private final boolean resetOnFinalize;
   private final ByteSource source;
   private OutputStream out;
   private FileBackedOutputStream.MemoryOutput memory;
   private File file;
   @VisibleForTesting
   synchronized File getFile() {
      return this.file;
   }
   public FileBackedOutputStream(int fileThreshold) {
      this(fileThreshold, false);
   }
   public FileBackedOutputStream(int fileThreshold, boolean resetOnFinalize) {
      this.fileThreshold = fileThreshold;
      this.resetOnFinalize = resetOnFinalize;
      return buf;
   }
   public Pointer[] getPointerArray(long offset) {
      List array = new ArrayList();
      int addOffset = 0;
      for(Pointer p = this.getPointer(offset); p != null; p = this.getPointer(offset + (long)addOffset)) {
         addOffset += SIZE;
      }
      return (Pointer[])((Pointer[])array.toArray(new Pointer[array.size()]));
   }
   public Pointer[] getPointerArray(long offset, int arraySize) {
      this.read(offset, (Pointer[])buf, 0, arraySize);
      return buf;
   }
   public String[] getStringArray(long offset) {
      return this.getStringArray(offset, -1, false);
   }
   public String[] getStringArray(long offset, int length) {
      return this.getStringArray(offset, length, false);
   }
   public String[] getStringArray(long offset, boolean wide) {
      return this.getStringArray(offset, -1, wide);
   }
   public String[] getStringArray(long offset, int length, boolean wide) {
      List strings = new ArrayList();
      int addOffset = 0;
      Pointer p;
      if(length != -1) {
         p = this.getPointer(offset + (long)addOffset);
         int count = 0;
         while(count++ < length) {
            String s = p == null?null:p.getString(0L, wide);
            strings.add(s);
            if(count < length) {
               addOffset += SIZE;
               p = this.getPointer(offset + (long)addOffset);
            }
         }
      } else {
         while((p = this.getPointer(offset + (long)addOffset)) != null) {
            String s = p == null?null:p.getString(0L, wide);
            strings.add(s);
            addOffset += SIZE;
         }
      }
      return (String[])((String[])strings.toArray(new String[strings.size()]));
   }
   void setValue(long offset, Object value, Class type) {
      if(type != Boolean.TYPE && type != Boolean.class) {
         if(type != Byte.TYPE && type != Byte.class) {
            if(type != Short.TYPE && type != Short.class) {
               if(type != Character.TYPE && type != Character.class) {
                  if(type != Integer.TYPE && type != Integer.class) {
                     if(type != Long.TYPE && type != Long.class) {
                        if(type != Float.TYPE && type != Float.class) {
                           if(type != Double.TYPE && type != Double.class) {
                              if(type == Pointer.class) {
                                 this.setPointer(offset, (Pointer)value);
                              } else if(type == String.class) {
                                 this.setPointer(offset, (Pointer)value);
                              } else if(type == WString.class) {
                                 this.setPointer(offset, (Pointer)value);
                              } else if(Structure.class.isAssignableFrom(type)) {
                                 Structure s = (Structure)value;
                                 if(Structure.ByReference.class.isAssignableFrom(type)) {
                                    this.setPointer(offset, s == null?null:s.getPointer());
                                    if(s != null) {
                                       s.autoWrite();
                                    }
                                 } else {
                                    s.useMemory(this, (int)offset);
                                    s.write();
                                 }
                              } else if(Callback.class.isAssignableFrom(type)) {
                                 this.setPointer(offset, CallbackReference.getFunctionPointer((Callback)value));
                              } else if(Platform.HAS_BUFFERS && Buffer.class.isAssignableFrom(type)) {
                                 Pointer p = value == null?null:Native.getDirectBufferPointer((Buffer)value);
                                 this.setPointer(offset, p);
                              } else if(NativeMapped.class.isAssignableFrom(type)) {
                                 NativeMappedConverter tc = NativeMappedConverter.getInstance(type);
                                 Class nativeType = tc.nativeType();
                                 this.setValue(offset, tc.toNative(value, new ToNativeContext()), nativeType);
                              } else {
                                 if(!type.isArray()) {
                                    throw new IllegalArgumentException("Writing " + type + " to memory is not supported");
                                 }
                                 this.setArrayValue(offset, value, type.getComponentType());
                              }
                           } else {
                              this.setDouble(offset, value == null?0.0D:((Double)value).doubleValue());
                           }
                        } else {
                           this.setFloat(offset, value == null?0.0F:((Float)value).floatValue());
                        }
                     } else {
                        this.setLong(offset, value == null?0L:((Long)value).longValue());
                     }
                  } else {
                     this.setInt(offset, value == null?0:((Integer)value).intValue());
                  }
               } else {
                  this.setChar(offset, value == null?'\u0000':((Character)value).charValue());
               }
            } else {
               this.setShort(offset, value == null?0:((Short)value).shortValue());
            }
         } else {
            this.setByte(offset, value == null?0:((Byte)value).byteValue());
         }
      } else {
         this.setInt(offset, Boolean.TRUE.equals(value)?-1:0);
      }
   }
}
}