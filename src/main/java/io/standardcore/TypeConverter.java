package io.standardcore;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface TypeConverter {
    boolean isConvertible(Object value);
    TypeCode getTypeCode(Object value);
    Boolean toBoolean(Object value, FormatProvider provider);
    Byte toByte(Object value, FormatProvider provider);
    Character toChar(Object value, FormatProvider provider);
    LocalDateTime toDateTime(Object value, FormatProvider provider);
    BigDecimal toDecimal(Object value, FormatProvider provider);
    Double toDouble(Object value, FormatProvider provider);
    Short toShort(Object value, FormatProvider provider);
    Integer toInteger(Object value, FormatProvider provider);
    Long toLong(Object value, FormatProvider provider);
    Float toFloat(Object value, FormatProvider provider);
    CharSequence toString(Object value, FormatProvider provider);
    Object toType(Object value, Type conversionType, FormatProvider provider);

    default Short toInt16(Object value, FormatProvider provider) {
        return toShort(value, provider);
    }
    default Integer toInt32(Object value, FormatProvider provider){
        return toInteger(value, provider);
    }
    default Long toInt64(Object value, FormatProvider provider){
        return toLong(value, provider);
    }
    default Float toSingle(Object value, FormatProvider provider){
        return toFloat(value, provider);
    }
}
