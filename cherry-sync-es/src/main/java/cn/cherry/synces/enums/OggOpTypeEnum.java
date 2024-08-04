package cn.cherry.synces.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;

@ToString
@Getter
@AllArgsConstructor
public enum OggOpTypeEnum {
    INSERT("I"),
    UPDATE("U"),
    DELETE("D"),
    QUERY("Q"),
    DDL("DDL"),
    ;

    final String value;

    public static OggOpTypeEnum value(String value) {
        return Arrays.stream(OggOpTypeEnum.values()).filter(e -> e.value.equals(value)).findFirst().get();
    }

}
