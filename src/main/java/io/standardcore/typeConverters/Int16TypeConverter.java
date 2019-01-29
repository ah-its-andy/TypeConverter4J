package io.standardcore.typeConverters;

import io.standardcore.FormatProvider;
import io.standardcore.TypeCode;
import io.standardcore.TypeConverter;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Int16TypeConverter implements TypeConverter {

    @Override
    public boolean isConvertible(Object value) {
        return false;
    }

    @Override
    public TypeCode getTypeCode(Object value) {
        return null;
    }

    @Override
    public Boolean toBoolean(Object value, FormatProvider provider) {
        return null;
    }

    @Override
    public Byte toByte(Object value, FormatProvider provider) {
        return null;
    }

    @Override
    public Character toChar(Object value, FormatProvider provider) {
        return null;
    }

    @Override
    public LocalDateTime toDateTime(Object value, FormatProvider provider) {
        return null;
    }

    @Override
    public BigDecimal toDecimal(Object value, FormatProvider provider) {
        return null;
    }

    @Override
    public Double toDouble(Object value, FormatProvider provider) {
        return null;
    }

    @Override
    public Short toShort(Object value, FormatProvider provider) {
        return null;
    }

    @Override
    public Integer toInteger(Object value, FormatProvider provider) {
        return null;
    }

    @Override
    public Long toLong(Object value, FormatProvider provider) {
        return null;
    }

    @Override
    public Float toFloat(Object value, FormatProvider provider) {
        return null;
    }

    @Override
    public CharSequence toString(Object value, FormatProvider provider) {
        return null;
    }

    @Override
    public Object toType(Object value, Type conversionType, FormatProvider provider) {
        return null;
    }
}
