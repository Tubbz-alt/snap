/*
 * ConstraintMatcher.java December 2016
 *
 * Copyright (C) 2016, Niall Gallagher <niallg@users.sf.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package org.snapscript.core.convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.TypeCache;
import org.snapscript.core.TypeCastChecker;
import org.snapscript.core.TypeExtractor;
import org.snapscript.core.TypeLoader;
import org.snapscript.core.TypeVerifier;

public class ConstraintMatcher {
   
   private final TypeCache<ConstraintConverter> converters;
   private final ConstraintConverter converter;
   private final TypeExtractor extractor;
   private final TypeVerifier comparator;
   private final TypeCastChecker checker;
   private final ProxyWrapper wrapper;
   
   public ConstraintMatcher(TypeLoader loader, ProxyWrapper wrapper) {
      this.converters = new TypeCache<ConstraintConverter>();
      this.extractor = new TypeExtractor(loader);
      this.checker = new TypeCastChecker(this, extractor, loader);
      this.comparator = new TypeVerifier(loader, checker);
      this.converter = new NullConverter();
      this.wrapper = wrapper;
   }
   
   public ConstraintConverter match(Type type) throws Exception { // type declared in signature
      if(type != null) {
         ConstraintConverter converter = converters.fetch(type);
         
         if(converter == null) {
            converter = resolve(type);
            converters.cache(type, converter);
         }
         return converter;
      }
      return converter;
   }
   
   private ConstraintConverter resolve(Type type) throws Exception {
      if(comparator.isSame(type, Object.class)) {
         return new AnyConverter(wrapper);
      }
      if(comparator.isSame(type, double.class)) {
         return new DoubleConverter(type);
      }
      if(comparator.isSame(type, float.class)) {
         return new FloatConverter(type);
      }
      if(comparator.isSame(type, int.class)) {
         return new IntegerConverter(type);
      }
      if(comparator.isSame(type, long.class)) {
         return new LongConverter(type);
      }
      if(comparator.isSame(type, short.class)) {
         return new ShortConverter(type);
      }
      if(comparator.isSame(type, byte.class)) {
         return new ByteConverter(type);
      }
      if(comparator.isSame(type, char.class)) {
         return new CharacterConverter(type);
      }
      if(comparator.isSame(type, boolean.class)) {
         return new BooleanConverter(type);
      }
      if(comparator.isSame(type, Number.class)) {
         return new NumberConverter(type);
      }
      if(comparator.isSame(type, Double.class)) {
         return new DoubleConverter(type);
      }
      if(comparator.isSame(type, Float.class)) {
         return new FloatConverter(type);
      }
      if(comparator.isSame(type, Integer.class)) {
         return new IntegerConverter(type);
      }
      if(comparator.isSame(type, Long.class)) {
         return new LongConverter(type);
      }
      if(comparator.isSame(type, Short.class)) {
         return new ShortConverter(type);
      }
      if(comparator.isSame(type, Byte.class)) {
         return new ByteConverter(type);
      }
      if(comparator.isSame(type, Character.class)) {
         return new CharacterConverter(type);
      }
      if(comparator.isSame(type, Boolean.class)) {
         return new BooleanConverter(type);
      }
      if(comparator.isSame(type, BigDecimal.class)) {
         return new BigDecimalConverter(type);
      }
      if(comparator.isSame(type, BigInteger.class)) {
         return new BigIntegerConverter(type);
      }
      if(comparator.isSame(type, AtomicLong.class)) {
         return new AtomicLongConverter(type);
      }
      if(comparator.isSame(type, AtomicInteger.class)) {
         return new AtomicIntegerConverter(type);
      }
      if(comparator.isSame(type, String.class)) {
         return new StringConverter();
      }
      if(comparator.isLike(type, Scope.class)) {
         return new ScopeConverter();
      }
      if(comparator.isLike(type, Enum.class)) {
         return new EnumConverter(type);
      }      
      if(comparator.isArray(type)) {
         return new ArrayConverter(this, wrapper, type);
      }
      return new ObjectConverter(extractor, checker, wrapper, type);
   }
}
