package io.standardcore;

import io.standardcore.typeConverters.Int16TypeConverter;
import io.standardcore.valueTypeConverters.*;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Convert {
    final static List<TypeConverter> typeConverters;

    static {
        typeConverters = new LinkedList<>();
        typeConverters.add(new Int16TypeConverter());
    }

    public static void setTypeConverters(Collection<TypeConverter> typeConverters){
        for(TypeConverter typeConverter : typeConverters){
            if(typeConverters.stream().anyMatch(x-> x.getClass() == typeConverter.getClass())) continue;

            typeConverters.add(typeConverter);
        }
    }

    private static TypeConverter getTypeConverter(Object value){
        return typeConverters.stream()
                .filter(x-> x.isConvertible(value))
                .findFirst().orElse(null);
    }

    // Returns the type code for the given Object. If the argument is null,
    // the result is TypeCode.Empty. If the argument is not a value (i.e. if
    // the Object does not implement IConvertible), the result is TypeCode.Object.
    // Otherwise, the result is the type code of the Object, as determined by
    // the Object's implementation of IConvertible.
    public static TypeCode getTypeCode(Object value) {
        if (value == null) return TypeCode.EMPTY;
        if (value instanceof Convertible)
        {
            Convertible temp = (Convertible)value;
            return temp.GetTypeCode();
        }

        TypeConverter typeConverter = getTypeConverter(value);
        if(typeConverters != null){
            return typeConverter.getTypeCode(value);
        }

        return TypeCode.OBJECT;
    }

    public static Object changeType(Object value, TypeCode typeCode, FormatProvider provider) {
        if (value == null && (typeCode == TypeCode.EMPTY || typeCode == TypeCode.STRING || typeCode == TypeCode.OBJECT)) {
            return null;
        }

        if(value instanceof  Convertible){
            return changeType((Convertible)value, typeCode, provider);
        }

        TypeConverter typeConverter = getTypeConverter(value);
        if(typeConverters != null){
            return changeType(value, typeConverter, typeCode, provider);
        }

        throw new InvalidCastException("InvalidCast_Convertible");
    }

    private static Object changeType(Convertible v, TypeCode typeCode, FormatProvider provider){
        // This line is invalid for things like Enums that return a TypeCode
        // of int, but the Object can't actually be cast to an int.
        //            if (v.GetTypeCode() == typeCode) return value;
        switch (typeCode)
        {
            case BOOLEAN:
                return v.toBoolean(provider);
            case CHAR:
                return v.toChar(provider);
            case BYTE:
                return v.toByte(provider);
            case INT16:
                return v.toShort(provider);
            case INT32:
                return v.toInt32(provider);
            case INT64:
                return v.toInt64(provider);
            case SINGLE:
                return v.toFloat(provider);
            case DOUBLE:
                return v.toDouble(provider);
            case DECIMAL:
                return v.toDecimal(provider);
            case DATETIME:
                return v.toDateTime(provider);
            case STRING:
                return v.toString(provider);
            case OBJECT:
                return v;
            case EMPTY:
                throw new InvalidCastException("InvalidCast_Empty");
            default:
                throw new ArgumentException("Arg_UnknownTypeCode");
        }
    }

    private static Object changeType(Object value, TypeConverter converter, TypeCode typeCode, FormatProvider provider){
        switch (typeCode)
        {
            case BOOLEAN:
                return converter.toBoolean(value, provider);
            case CHAR:
                return converter.toChar(value, provider);
            case BYTE:
                return converter.toByte(value, provider);
            case INT16:
                return converter.toShort(value, provider);
            case INT32:
                return converter.toInteger(value, provider);
            case INT64:
                return converter.toLong(value, provider);
            case SINGLE:
                return converter.toFloat(value, provider);
            case DOUBLE:
                return converter.toDouble(value, provider);
            case DECIMAL:
                return converter.toDecimal(value, provider);
            case DATETIME:
                return converter.toDateTime(value, provider);
            case STRING:
                return converter.toString(value, provider);
            case OBJECT:
                return value;
            case EMPTY:
                throw new InvalidCastException("InvalidCast_Empty");
            default:
                throw new ArgumentException("Arg_UnknownTypeCode");
        }
    }

    public static Object changeType(Object value, Type conversionType, FormatProvider provider){
        if (conversionType == null)
        {
            throw new ArgumentNullException("conversionType");
        }

        if(referenceEquals(value.getClass(), conversionType)){
            return value;
        }

        if(value instanceof Convertible){
            return ((Convertible) value).toType(conversionType, provider);
        }

        TypeConverter typeConverter = getTypeConverter(value);
        if(typeConverters == null){
            throw new InvalidCastException("InvalidCast_IConvertible");
        }

        if(referenceEquals(conversionType, Boolean.class, Boolean.TYPE)){
            return typeConverter.toBoolean(value, provider);
        }

        if(referenceEquals(conversionType, Character.class, Character.TYPE)){
            return typeConverter.toChar(value, provider);
        }

        if(referenceEquals(conversionType, Byte.class, Byte.TYPE)){
            return typeConverter.toByte(value, provider);
        }

        if(referenceEquals(conversionType, Short.class, Short.TYPE)){
            return typeConverter.toShort(value, provider);
        }

        if(referenceEquals(conversionType, Integer.class, Integer.TYPE)){
            return typeConverter.toInteger(value, provider);
        }

        if(referenceEquals(conversionType, Long.class, Long.TYPE)){
            return typeConverter.toLong(value, provider);
        }

        if(referenceEquals(conversionType, Float.class, Float.TYPE)){
            return typeConverter.toFloat(value, provider);
        }

        if(referenceEquals(conversionType, Double.class, Double.TYPE)){
            return typeConverter.toDouble(value, provider);
        }

        if(referenceEquals(conversionType, BigDecimal.class)){
            return typeConverter.toDecimal(value, provider);
        }

        if(referenceEquals(conversionType, LocalDateTime.class, LocalDate.class, LocalTime.class)){
            return typeConverter.toDateTime(value, provider);
        }

        if(referenceEquals(conversionType, String.class, CharSequence.class)){
            return typeConverter.toString(value, provider);
        }

        if(referenceEquals(conversionType, Object.class)){
            return value;
        }

        return typeConverter.toType(value, conversionType, provider);
    }


    private static boolean referenceEquals(Object o1, Object... o2){
        if(o1 == null) throw new ArgumentNullException("o1"):
        if(o2 == null || o2.length == 0)  throw new ArgumentNullException("o2"):
        return Arrays.stream(o2).anyMatch(x-> x == o1);
    }

    private static void throwCharOverflowException() { throw new OverflowException("Overflow_Char"); }

    private static void throwByteOverflowException() { throw new OverflowException("Overflow_Byte"); }

    private static void throwSByteOverflowException() { throw new OverflowException("Overflow_SByte"); }

    private static void throwInt16OverflowException() { throw new OverflowException("Overflow_Int16"); }

    private static void throwUInt16OverflowException() { throw new OverflowException("Overflow_UInt16"); }

    private static void throwInt32OverflowException() { throw new OverflowException("Overflow_Int32"); }

    private static void throwUInt32OverflowException() { throw new OverflowException("Overflow_UInt32"); }

    private static void throwInt64OverflowException() { throw new OverflowException("Overflow_Int64"); }

    private static void throwUInt64OverflowException() { throw new OverflowException("Overflow_UInt64"); }

    private final static ValueTypeConverter<Boolean> BOOLEAN_VALUE_TYPE_CONVERTER = new BooleanValueTypeConverter();
    private final static ValueTypeConverter<Character> CHARACTER_VALUE_TYPE_CONVERTER = new CharacterValueTypeConverter();
    private final static ValueTypeConverter<Byte> BYTE_VALUE_TYPE_CONVERTER = new ByteValueTypeConverter();
    private final static ValueTypeConverter<Short> SHORT_VALUE_TYPE_CONVERTER = new ShortValueTypeConverter();
    private final static ValueTypeConverter<Integer> INTEGER_VALUE_TYPE_CONVERTER = new IntegerValueTypeConverter();
    private final static ValueTypeConverter<Long> LONG_VALUE_TYPE_CONVERTER = new LongValueTypeConverter();
    private final static ValueTypeConverter<Float> FLOAT_VALUE_TYPE_CONVERTER = new FloatValueTypeConverter();
    private final static ValueTypeConverter<Double> DOUBLE_VALUE_TYPE_CONVERTER = new DoubleValueTypeConverter();
    private final static ValueTypeConverter<BigDecimal> BIG_DECIMAL_VALUE_TYPE_CONVERTER = new BigDecimalValueTypeConverter();
    private final static ValueTypeConverter<String> STRING_VALUE_TYPE_CONVERTER = new StringValueTypeConverter();
    private final static ValueTypeConverter<LocalDateTime> LOCAL_DATE_TIME_VALUE_TYPE_CONVERTER = new LocalDateTimeValueTypeConverter();


    // Conversions to Boolean
    public static Boolean toBoolean(Object value) {
        return value == null ? false : BOOLEAN_VALUE_TYPE_CONVERTER.convert(value);
    }

    public static Boolean toBoolean(Object value, FormatProvider provider) {
        return value == null ? false : BOOLEAN_VALUE_TYPE_CONVERTER.convert(value, provider);
    }

    public static Boolean toBoolean(Boolean value) {
        return BOOLEAN_VALUE_TYPE_CONVERTER.convert(value);
    }

    // to be consistent with IConvertible in the base data types else we get different semantics
    // with widening operations. Without this operator this widen succeeds,with this API the widening throws.
    public static Boolean toBoolean(Character value) {
        return BOOLEAN_VALUE_TYPE_CONVERTER.convert(value);
    }

    public static Boolean toBoolean(Byte value) {
        return BOOLEAN_VALUE_TYPE_CONVERTER.convert(value);
    }

    public static Boolean toBoolean(Short value) {
        return BOOLEAN_VALUE_TYPE_CONVERTER.convert(value);
    }

    public static Boolean toBoolean(Integer value) {
        return BOOLEAN_VALUE_TYPE_CONVERTER.convert(value);
    }

    public static Boolean toBoolean(Long value) {
        return BOOLEAN_VALUE_TYPE_CONVERTER.convert(value);
    }

    public static Boolean toBoolean(String value) {
        if (value == null)
            return false;
        return BOOLEAN_VALUE_TYPE_CONVERTER.convert(value);
    }

    public static Boolean toBoolean(String value, FormatProvider provider) {
        if (value == null)
            return false;
        return BOOLEAN_VALUE_TYPE_CONVERTER.convert(value, provider);
    }

    public static Boolean toBoolean(Float value) {
        return BOOLEAN_VALUE_TYPE_CONVERTER.convert(value);
    }

    public static Boolean toBoolean(Double value) {
        return BOOLEAN_VALUE_TYPE_CONVERTER.convert(value);
    }

    public static Boolean toBoolean(BigDecimal value) {
        return BOOLEAN_VALUE_TYPE_CONVERTER.convert(value);
    }

    public static Boolean toBoolean(LocalDateTime value) {
        return BOOLEAN_VALUE_TYPE_CONVERTER.convert(value);
    }

    // Conversions to Char
    public static Character toChar(Object value) {
        return value == null ?  (char)0 : CHARACTER_VALUE_TYPE_CONVERTER.convert(value);
    }

    public static Character toChar(Object value, FormatProvider provider) {
        return value == null ? (char)0 : CHARACTER_VALUE_TYPE_CONVERTER.convert(value, provider);
    }

    public static Character toChar(Boolean value) {
        return CHARACTER_VALUE_TYPE_CONVERTER.convert(value);
    }

    public static Character toChar(Character value) {
        return CHARACTER_VALUE_TYPE_CONVERTER.convert(value);
    }

    public static Character toChar(Byte value) {
        return CHARACTER_VALUE_TYPE_CONVERTER.convert(value);
    }

    public static Character toChar(Short value) {
        if (value < 0) throwCharOverflowException();
        return CHARACTER_VALUE_TYPE_CONVERTER.convert(value);
    }

    public static Character toChar(Integer value) {
        if (value < 0 || value > Character.MAX_VALUE) throwCharOverflowException();
        return CHARACTER_VALUE_TYPE_CONVERTER.convert(value);
    }

    public static Character toChar(Long value) {
        if (value < 0 || value > Character.MAX_VALUE) throwCharOverflowException();
        return CHARACTER_VALUE_TYPE_CONVERTER.convert(value);
    }

    //
    // @VariantSwitch
    // Remove FormatExceptions;
    //
    public static Character toChar(String value) {
        return toChar(value, null);
    }

    public static Character toChar(String value, FormatProvider provider) {
        if (value == null)
            throw new ArgumentNullException("value");

        if (value.length() != 1)
            throw new FormatException("Format_NeedSingleChar");

        return CHARACTER_VALUE_TYPE_CONVERTER.convert(value);
    }

    // to be consistent with IConvertible in the base data types else we get different semantics
    // with widening operations. Without this operator this widen succeeds,with this API the widening throws.
    public static Character toChar(Float value) {
        return CHARACTER_VALUE_TYPE_CONVERTER.convert(value);
    }

    // to be consistent with IConvertible in the base data types else we get different semantics
    // with widening operations. Without this operator this widen succeeds,with this API the widening throws.
    public static Character toChar(Double value) {
        return CHARACTER_VALUE_TYPE_CONVERTER.convert(value);
    }

    // to be consistent with IConvertible in the base data types else we get different semantics
    // with widening operations. Without this operator this widen succeeds,with this API the widening throws.
    public static Character toChar(BigDecimal value) {
        return CHARACTER_VALUE_TYPE_CONVERTER.convert(value);
    }

    public static Character toChar(LocalDateTime value) {
        return CHARACTER_VALUE_TYPE_CONVERTER.convert(value);
    }

    // Conversions to Byte
    public static Byte toByte(Object value) {
        return value == null ? (byte)0 : BYTE_VALUE_TYPE_CONVERTER.convert(value);
    }

    public static Byte toByte(Object value, FormatProvider provider) {
        return value == null ? (byte)0 :  BYTE_VALUE_TYPE_CONVERTER.convert(value);
    }

    public static Byte toByte(Boolean value) {
        return value ?   BYTE_VALUE_TYPE_CONVERTER.convert(value);
    }

    public static Byte toByte(Byte value) {
        return value;
    }

    public static Byte toByte(Character value) {
        if (value > Byte.MAX_VALUE) throwByteOverflowException();
        return  BYTE_VALUE_TYPE_CONVERTER.convert(value);
    }

    public static Byte toByte(Short value) {
        if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) throwByteOverflowException();
        return  BYTE_VALUE_TYPE_CONVERTER.convert(value);
    }

    public static Byte toByte(Integer value) {
        if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) throwByteOverflowException();
        return  BYTE_VALUE_TYPE_CONVERTER.convert(value);
    }

    public static Byte toByte(Long value) {
        if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) throwByteOverflowException();
        return  BYTE_VALUE_TYPE_CONVERTER.convert(value);
    }

    public static Byte toByte(Float value) {
        return  BYTE_VALUE_TYPE_CONVERTER.convert(value);
    }

    public static Byte toByte(Double value) {
        return  BYTE_VALUE_TYPE_CONVERTER.convert(value);
    }

    public static Byte toByte(BigDecimal value) {
        return  BYTE_VALUE_TYPE_CONVERTER.convert(value);
    }

    public static Byte toByte(String value) {
        if (value == null)
            return 0;
        return  BYTE_VALUE_TYPE_CONVERTER.convert(value);
    }

    public static Byte toByte(String value, FormatProvider provider) {
        if (value == null)
            return 0;
        return  BYTE_VALUE_TYPE_CONVERTER.convert(value, provider);
    }

    public static Byte toByte(LocalDateTime value) {
        return  BYTE_VALUE_TYPE_CONVERTER.convert(value);
    }

    // Conversions to Int16

    public static Short toInt16(Object value) {
        return value == null ? (short)0 : SHORT_VALUE_TYPE_CONVERTER.convert(value);
    }

    public static Short toInt16(Object value, FormatProvider provider) {
        return value == null ? (short)0 : SHORT_VALUE_TYPE_CONVERTER.convert(value, provider);
    }

    public static Short toInt16(Boolean value) {
        return  SHORT_VALUE_TYPE_CONVERTER.convert(value);
    }

    public static Short toInt16(Character value) {
        if (value > Short.MAX_VALUE) throwInt16OverflowException();
        return  SHORT_VALUE_TYPE_CONVERTER.convert(value);
    }

    public static Short toInt16(Byte value) {
        return  SHORT_VALUE_TYPE_CONVERTER.convert(value);
    }

    public static Short toInt16(Integer value) {
        if (value < Short.MIN_VALUE || value > Short.MAX_VALUE) throwInt16OverflowException();
        return  SHORT_VALUE_TYPE_CONVERTER.convert(value);
    }

    public static Short toInt16(Short value) {
        return  SHORT_VALUE_TYPE_CONVERTER.convert(value);
    }

    public static Short toInt16(Long value) {
        if (value < Short.MIN_VALUE || value > Short.MAX_VALUE) throwInt16OverflowException();
        return  SHORT_VALUE_TYPE_CONVERTER.convert(value);
    }

    public static Short toInt16(Float value) {
        return  SHORT_VALUE_TYPE_CONVERTER.convert(value);
    }

    public static Short toInt16(Double value) {
        return SHORT_VALUE_TYPE_CONVERTER.convert(value);
    }

    public static Short toInt16(BigDecimal value) {
        return  SHORT_VALUE_TYPE_CONVERTER.convert(value);
    }

    public static Short toInt16(String value) {
        if (value == null)
            return 0;
        return  SHORT_VALUE_TYPE_CONVERTER.convert(value);
    }

    public static Short toInt16(String value, FormatProvider provider) {
        if (value == null)
            return 0;
        return  SHORT_VALUE_TYPE_CONVERTER.convert(value, provider);
    }

    public static Short toInt16(LocalDateTime value) {
        return  SHORT_VALUE_TYPE_CONVERTER.convert(value);
    }

    
    // Conversions to Int32

    public static Integer toInt32(Object value) {
        return value == null ? 0 : ((IConvertible)value).toInt32(null);
    }

    public static Integer toInt32(Object value, FormatProvider provider) {
        return value == null ? 0 : ((IConvertible)value).toInt32(provider);
    }


    public static Integer toInt32(Boolean value) {
        return value ? Boolean.True : Boolean.False;
    }

    public static Integer toInt32(Character value) {
        return value;
    }


    public static Integer toInt32(sByte value) {
        return value;
    }

    public static Integer toInt32(Byte value) {
        return value;
    }

    public static Integer toInt32(Short value) {
        return value;
    }


    public static Integer toInt32(uShort value) {
        return value;
    }


    public static Integer toInt32(uInteger value) {
        if (value > Integer.MaxValue) ThrowInt32OverflowException();
        return (Integer)value;
    }

    public static Integer toInt32(Integer value) {
        return value;
    }

    public static Integer toInt32(Long value) {
        if (value < Integer.MinValue || value > Integer.MaxValue) ThrowInt32OverflowException();
        return (Integer)value;
    }


    public static Integer toInt32(uLong value) {
        if (value > Integer.MaxValue) ThrowInt32OverflowException();
        return (Integer)value;
    }

    public static Integer toInt32(Float value) {
        return toInt32((Double)value);
    }

    public static Integer toInt32(Double value) {
        if (value >= 0)
        {
            if (value < 2147483647.5)
            {
                Integer result = (Integer)value;
                Double dif = value - result;
                if (dif > 0.5 || dif == 0.5 && (result & 1) != 0) result++;
                return result;
            }
        }
        else
        {
            if (value >= -2147483648.5)
            {
                Integer result = (Integer)value;
                Double dif = value - result;
                if (dif < -0.5 || dif == -0.5 && (result & 1) != 0) result--;
                return result;
            }
        }
        throw new OverflowException(SR.Overflow_Int32);
    }

    public static Integer toInt32(BigDecimal value) {
        return BigDecimal.toInt32(BigDecimal.Round(value, 0));
    }

    public static Integer toInt32(String value) {
        if (value == null)
            return 0;
        return Integer.Parse(value, );
    }

    public static Integer toInt32(String value, FormatProvider provider) {
        if (value == null)
            return 0;
        return Integer.Parse(value, NumberStyles.Integer, provider);
    }

    public static Integer toInt32(LocalDateTime value) {
        return ((IConvertible)value).toInt32(null);
    }


    // Disallowed conversions to Int32
    // public static Integer toInt32(TimeSpan value)

    // Conversions to UInt32


    public static uInteger toUInt32(Object value) {
        return value == null ? 0 : ((IConvertible)value).toUInt32(null);
    }


    public static uInteger toUInt32(Object value, FormatProvider provider) {
        return value == null ? 0 : ((IConvertible)value).toUInt32(provider);
    }



    public static uInteger toUInt32(Boolean value) {
        return value ? (uInteger)Boolean.True : (uInteger)Boolean.False;
    }


    public static uInteger toUInt32(Character value) {
        return value;
    }


    public static uInteger toUInt32(sByte value) {
        if (value < 0) ThrowUInt32OverflowException();
        return (uInteger)value;
    }


    public static uInteger toUInt32(Byte value) {
        return value;
    }


    public static uInteger toUInt32(Short value) {
        if (value < 0) ThrowUInt32OverflowException();
        return (uInteger)value;
    }


    public static uInteger toUInt32(uShort value) {
        return value;
    }


    public static uInteger toUInt32(Integer value) {
        if (value < 0) ThrowUInt32OverflowException();
        return (uInteger)value;
    }


    public static uInteger toUInt32(uInteger value) {
        return value;
    }


    public static uInteger toUInt32(Long value) {
        if (value < 0 || value > uInteger.MaxValue) ThrowUInt32OverflowException();
        return (uInteger)value;
    }


    public static uInteger toUInt32(uLong value) {
        if (value > uInteger.MaxValue) ThrowUInt32OverflowException();
        return (uInteger)value;
    }


    public static uInteger toUInt32(Float value) {
        return toUInt32((Double)value);
    }


    public static uInteger toUInt32(Double value) {
        if (value >= -0.5 && value < 4294967295.5)
        {
            uInteger result = (uInteger)value;
            Double dif = value - result;
            if (dif > 0.5 || dif == 0.5 && (result & 1) != 0) result++;
            return result;
        }
        throw new OverflowException(SR.Overflow_UInt32);
    }


    public static uInteger toUInt32(BigDecimal value) {
        return BigDecimal.toUInt32(BigDecimal.Round(value, 0));
    }


    public static uInteger toUInt32(String value) {
        if (value == null)
            return 0;
        return uInteger.Parse(value, );
    }


    public static uInteger toUInt32(String value, FormatProvider provider) {
        if (value == null)
            return 0;
        return uInteger.Parse(value, NumberStyles.Integer, provider);
    }


    public static uInteger toUInt32(LocalDateTime value) {
        return ((IConvertible)value).toUInt32(null);
    }

    // Disallowed conversions to UInt32
    // public static uInteger toUInt32(TimeSpan value)

    // Conversions to Int64

    public static Long toInt64(Object value) {
        return value == null ? 0 : ((IConvertible)value).toInt64(null);
    }

    public static Long toInt64(Object value, FormatProvider provider) {
        return value == null ? 0 : ((IConvertible)value).toInt64(provider);
    }


    public static Long toInt64(Boolean value) {
        return value ? Boolean.True : Boolean.False;
    }

    public static Long toInt64(Character value) {
        return value;
    }


    public static Long toInt64(sByte value) {
        return value;
    }

    public static Long toInt64(Byte value) {
        return value;
    }

    public static Long toInt64(Short value) {
        return value;
    }


    public static Long toInt64(uShort value) {
        return value;
    }

    public static Long toInt64(Integer value) {
        return value;
    }


    public static Long toInt64(uInteger value) {
        return value;
    }


    public static Long toInt64(uLong value) {
        if (value > Long.MaxValue) ThrowInt64OverflowException();
        return (Long)value;
    }

    public static Long toInt64(Long value) {
        return value;
    }


    public static Long toInt64(Float value) {
        return toInt64((Double)value);
    }

    public static Long toInt64(Double value) {
        return checked((Long)Math.Round(value));
    }

    public static Long toInt64(BigDecimal value) {
        return BigDecimal.toInt64(BigDecimal.Round(value, 0));
    }

    public static Long toInt64(String value) {
        if (value == null)
            return 0;
        return Long.Parse(value, );
    }

    public static Long toInt64(String value, FormatProvider provider) {
        if (value == null)
            return 0;
        return Long.Parse(value, NumberStyles.Integer, provider);
    }

    public static Long toInt64(LocalDateTime value) {
        return ((IConvertible)value).toInt64(null);
    }

    // Disallowed conversions to Int64
    // public static Long toInt64(TimeSpan value)

    // Conversions to UInt64


    public static uLong toUInt64(Object value) {
        return value == null ? 0 : ((IConvertible)value).toUInt64(null);
    }


    public static uLong toUInt64(Object value, FormatProvider provider) {
        return value == null ? 0 : ((IConvertible)value).toUInt64(provider);
    }


    public static uLong toUInt64(Boolean value) {
        return value ? (uLong)Boolean.True : (uLong)Boolean.False;
    }


    public static uLong toUInt64(Character value) {
        return value;
    }



    public static uLong toUInt64(sByte value) {
        if (value < 0) ThrowUInt64OverflowException();
        return (uLong)value;
    }


    public static uLong toUInt64(Byte value) {
        return value;
    }


    public static uLong toUInt64(Short value) {
        if (value < 0) ThrowUInt64OverflowException();
        return (uLong)value;
    }


    public static uLong toUInt64(uShort value) {
        return value;
    }


    public static uLong toUInt64(Integer value) {
        if (value < 0) ThrowUInt64OverflowException();
        return (uLong)value;
    }


    public static uLong toUInt64(uInteger value) {
        return value;
    }


    public static uLong toUInt64(Long value) {
        if (value < 0) ThrowUInt64OverflowException();
        return (uLong)value;
    }


    public static uLong toUInt64(uLong value) {
        return value;
    }


    public static uLong toUInt64(Float value) {
        return toUInt64((Double)value);
    }


    public static uLong toUInt64(Double value) {
        return checked((uLong)Math.Round(value));
    }


    public static uLong toUInt64(BigDecimal value) {
        return BigDecimal.toUInt64(BigDecimal.Round(value, 0));
    }


    public static uLong toUInt64(String value) {
        if (value == null)
            return 0;
        return uLong.Parse(value, );
    }


    public static uLong toUInt64(String value, FormatProvider provider) {
        if (value == null)
            return 0;
        return uLong.Parse(value, NumberStyles.Integer, provider);
    }


    public static uLong toUInt64(LocalDateTime value) {
        return ((IConvertible)value).toUInt64(null);
    }

    // Disallowed conversions to UInt64
    // public static uLong toUInt64(TimeSpan value)

    // Conversions to Single

    public static Float toSingle(Object value) {
        return value == null ? 0 : ((IConvertible)value).toSingle(null);
    }

    public static Float toSingle(Object value, FormatProvider provider) {
        return value == null ? 0 : ((IConvertible)value).toSingle(provider);
    }


    public static Float toSingle(sByte value) {
        return value;
    }

    public static Float toSingle(Byte value) {
        return value;
    }

    public static Float toSingle(Character value) {
        return ((IConvertible)value).toSingle(null);
    }

    public static Float toSingle(Short value) {
        return value;
    }


    public static Float toSingle(uShort value) {
        return value;
    }

    public static Float toSingle(Integer value) {
        return value;
    }


    public static Float toSingle(uInteger value) {
        return value;
    }

    public static Float toSingle(Long value) {
        return value;
    }


    public static Float toSingle(uLong value) {
        return value;
    }

    public static Float toSingle(Float value) {
        return value;
    }

    public static Float toSingle(Double value) {
        return (Float)value;
    }

    public static Float toSingle(BigDecimal value) {
        return (Float)value;
    }

    public static Float toSingle(String value) {
        if (value == null)
            return 0;
        return Float.Parse(value, );
    }

    public static Float toSingle(String value, FormatProvider provider) {
        if (value == null)
            return 0;
        return Float.Parse(value, NumberStyles.Float | NumberStyles.AllowThousands, provider);
    }


    public static Float toSingle(Boolean value) {
        return value ? Boolean.True : Boolean.False;
    }

    public static Float toSingle(LocalDateTime value) {
        return ((IConvertible)value).toSingle(null);
    }

    // Disallowed conversions to Single
    // public static Float toSingle(TimeSpan value)

    // Conversions to Double

    public static Double toDouble(Object value) {
        return value == null ? 0 : ((IConvertible)value).toDouble(null);
    }

    public static Double toDouble(Object value, FormatProvider provider) {
        return value == null ? 0 : ((IConvertible)value).toDouble(provider);
    }



    public static Double toDouble(sByte value) {
        return value;
    }

    public static Double toDouble(Byte value) {
        return value;
    }

    public static Double toDouble(Short value) {
        return value;
    }

    public static Double toDouble(Character value) {
        return ((IConvertible)value).toDouble(null);
    }


    public static Double toDouble(uShort value) {
        return value;
    }

    public static Double toDouble(Integer value) {
        return value;
    }


    public static Double toDouble(uInteger value) {
        return value;
    }

    public static Double toDouble(Long value) {
        return value;
    }


    public static Double toDouble(uLong value) {
        return value;
    }

    public static Double toDouble(Float value) {
        return value;
    }

    public static Double toDouble(Double value) {
        return value;
    }

    public static Double toDouble(BigDecimal value) {
        return (Double)value;
    }

    public static Double toDouble(String value) {
        if (value == null)
            return 0;
        return Double.Parse(value, );
    }

    public static Double toDouble(String value, FormatProvider provider) {
        if (value == null)
            return 0;
        return Double.Parse(value, NumberStyles.Float | NumberStyles.AllowThousands, provider);
    }

    public static Double toDouble(Boolean value) {
        return value ? Boolean.True : Boolean.False;
    }

    public static Double toDouble(LocalDateTime value) {
        return ((IConvertible)value).toDouble(null);
    }

    // Disallowed conversions to Double
    // public static Double toDouble(TimeSpan value)

    // Conversions to Decimal

    public static BigDecimal toDecimal(Object value) {
        return value == null ? 0 : ((IConvertible)value).toDecimal(null);
    }

    public static BigDecimal toDecimal(Object value, FormatProvider provider) {
        return value == null ? 0 : ((IConvertible)value).toDecimal(provider);
    }

    public static BigDecimal toDecimal(Byte value) {
        return value;
    }

    public static BigDecimal toDecimal(Character value) {
        return ((IConvertible)value).toDecimal(null);
    }

    public static BigDecimal toDecimal(Short value) {
        return value;
    }


    public static BigDecimal toDecimal(uShort value) {
        return value;
    }

    public static BigDecimal toDecimal(Integer value) {
        return value;
    }

    public static BigDecimal toDecimal(Long value) {
        return value;
    }

    public static BigDecimal toDecimal(Float value) {
        return (BigDecimal)value;
    }

    public static BigDecimal toDecimal(Double value) {
        return (BigDecimal)value;
    }

    public static BigDecimal toDecimal(String value) {
        if (value == null)
            return 0m;
        return BigDecimal.Parse(value, );
    }

    public static BigDecimal toDecimal(String value, FormatProvider provider) {
        if (value == null)
            return 0m;
        return BigDecimal.Parse(value, NumberStyles.Number, provider);
    }

    public static BigDecimal toDecimal(BigDecimal value) {
        return value;
    }

    public static BigDecimal toDecimal(Boolean value) {
        return value ? Boolean.True : Boolean.False;
    }

    public static BigDecimal toDecimal(LocalDateTime value) {
        return ((IConvertible)value).toDecimal(null);
    }

    // Disallowed conversions to Decimal
    // public static BigDecimal toDecimal(TimeSpan value)

    // Conversions to LocalDateTime

    public static LocalDateTime toLocalDateTime(LocalDateTime value) {
        return value;
    }

    public static LocalDateTime toLocalDateTime(Object value) {
        return value == null ? LocalDateTime.MIN_VALUE : ((IConvertible)value).toLocalDateTime(null);
    }

    public static LocalDateTime toLocalDateTime(Object value, FormatProvider provider) {
        return value == null ? LocalDateTime.MIN_VALUE : ((IConvertible)value).toLocalDateTime(provider);
    }

    public static LocalDateTime toLocalDateTime(String value) {
        if (value == null)
            return new LocalDateTime(0);
        return LocalDateTime.Parse(value, );
    }

    public static LocalDateTime toLocalDateTime(String value, FormatProvider provider) {
        if (value == null)
            return new LocalDateTime(0);
        return LocalDateTime.Parse(value, provider);
    }


    public static LocalDateTime toLocalDateTime(sByte value) {
        return ((IConvertible)value).toLocalDateTime(null);
    }

    public static LocalDateTime toLocalDateTime(Byte value) {
        return ((IConvertible)value).toLocalDateTime(null);
    }

    public static LocalDateTime toLocalDateTime(Short value) {
        return ((IConvertible)value).toLocalDateTime(null);
    }


    public static LocalDateTime toLocalDateTime(uShort value) {
        return ((IConvertible)value).toLocalDateTime(null);
    }

    public static LocalDateTime toLocalDateTime(Integer value) {
        return ((IConvertible)value).toLocalDateTime(null);
    }


    public static LocalDateTime toLocalDateTime(uInteger value) {
        return ((IConvertible)value).toLocalDateTime(null);
    }

    public static LocalDateTime toLocalDateTime(Long value) {
        return ((IConvertible)value).toLocalDateTime(null);
    }


    public static LocalDateTime toLocalDateTime(uLong value) {
        return ((IConvertible)value).toLocalDateTime(null);
    }

    public static LocalDateTime toLocalDateTime(Boolean value) {
        return ((IConvertible)value).toLocalDateTime(null);
    }

    public static LocalDateTime toLocalDateTime(Character value) {
        return ((IConvertible)value).toLocalDateTime(null);
    }

    public static LocalDateTime toLocalDateTime(Float value) {
        return ((IConvertible)value).toLocalDateTime(null);
    }

    public static LocalDateTime toLocalDateTime(Double value) {
        return ((IConvertible)value).toLocalDateTime(null);
    }

    public static LocalDateTime toLocalDateTime(BigDecimal value) {
        return ((IConvertible)value).toLocalDateTime(null);
    }

    // Disallowed conversions to LocalDateTime
    // public static LocalDateTime toLocalDateTime(TimeSpan value)

    // Conversions to String

    public static String toString(Object value) {
        return toString(value, null);
    }

    public static String toString(Object value, FormatProvider provider) {
        if (value is IConvertible ic)
        return ic.toString(provider);
        if (value is IFormattable formattable)
        return formattable.toString(null, provider);
        return value == null ? String.Empty : value.toString();
    }

    public static String toString(Boolean value) {
        return value.toString();
    }

    public static String toString(Boolean value, FormatProvider provider) {
        return value.toString();
    }

    public static String toString(Character value) {
        return Character.toString(value);
    }

    public static String toString(Character value, FormatProvider provider) {
        return value.toString();
    }

    public static String toString(Byte value) {
        return value.toString();
    }

    public static String toString(Byte value, FormatProvider provider) {
        return value.toString(provider);
    }

    public static String toString(Short value) {
        return value.toString();
    }

    public static String toString(Short value, FormatProvider provider) {
        return value.toString(provider);
    }

    public static String toString(Integer value) {
        return value.toString();
    }

    public static String toString(Integer value, FormatProvider provider) {
        return value.toString(provider);
    }

    public static String toString(Long value) {
        return value.toString();
    }

    public static String toString(Long value, FormatProvider provider) {
        return value.toString(provider);
    }

    public static String toString(Float value) {
        return value.toString();
    }

    public static String toString(Float value, FormatProvider provider) {
        return value.toString(provider);
    }

    public static String toString(Double value) {
        return value.toString();
    }

    public static String toString(Double value, FormatProvider provider) {
        return value.toString(provider);
    }

    public static String toString(BigDecimal value) {
        return value.toString();
    }

    public static String toString(BigDecimal value, FormatProvider provider) {
        return value.toString(provider);
    }

    public static String toString(LocalDateTime value) {
        return value.toString();
    }

    public static String toString(LocalDateTime value, FormatProvider provider) {
        return value.toString(provider);
    }

    public static String toString(String value) {
        return value;
    }

    public static String toString(String value, FormatProvider provider) {
        return value; // avoid the null check
    }
}
