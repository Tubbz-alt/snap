package org.snapscript.core.constraint;

public enum ConstraintType {
   INSTANCE(0x0001),
   STATIC(0x0002),
   MODULE(0x0004);
   
   public final int mask;
   
   private ConstraintType(int mask) {
      this.mask = mask;
   }
   
   public static boolean isInstance(int modifier){
      return modifier >= 0 && (INSTANCE.mask & modifier) != 0;
   }
   
   public static boolean isStatic(int modifier){
      return modifier >= 0 && (STATIC.mask & modifier) != 0;
   }   
   
   public static boolean isModule(int modifier){
      return modifier >= 0 && (MODULE.mask & modifier) != 0;
   }
}