package io.standardcore;

public enum TypeCode {
    EMPTY(0),
    OBJECT(1),
    BOOLEAN(3),
    CHAR(4),
    BYTE(6),
    INT16(7),
    INT32(9),
    INT64(11),
    SINGLE(13),
    DOUBLE(14),
    DECIMAL(15),
    DATETIME(16),
    STRING(18);
    private final int value;

    TypeCode(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }


}
