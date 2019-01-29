package io.standardcore;

import java.math.BigDecimal;

public interface ValueTypeConverter<TR> {
    TR convert(Object value);
    TR convert(Object value, FormatProvider provider);
    TR convert(Boolean value);
    TR convert(Character value);
    TR convert(Byte value);
    TR convert(Short value);
    TR convert(Integer value);
    TR convert(Long value);
    TR convert(Float value);
    TR convert(Double value);
    TR convert(BigDecimal value);
    TR convert(String value);
}
