package io.standardcore;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface Convertible
{
    TypeCode GetTypeCode();
    Boolean toBoolean(FormatProvider provider);
    Byte toByte(FormatProvider provider);
    Character toChar(FormatProvider provider);
    LocalDateTime toDateTime(FormatProvider provider);
    BigDecimal toDecimal(FormatProvider provider);
    Double toDouble(FormatProvider provider);
    Short toShort(FormatProvider provider);
    Integer toInteger(FormatProvider provider);
    Long toLong(FormatProvider provider);
    Float toFloat(FormatProvider provider);
    CharSequence toString(FormatProvider provider);
    Object toType(Type conversionType, FormatProvider provider);

    default Short toInt16(FormatProvider provider){
        return toShort(provider);
    }
    default Integer toInt32(FormatProvider provider){
        return toInteger(provider);
    }
    default Long toInt64(FormatProvider provider){
        return toLong(provider);
    }
    default Float toSingle(FormatProvider provider){
        return toFloat(provider);
    }
}
