package cn.cherry.excel.enums;

/**
 * @author: sinbad.cheng
 * @time: 2023/4/10 22:40
 * @description：
 */

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.checkerframework.checker.units.qual.A;

@AllArgsConstructor
@Getter
public enum TaskStatusEnum {

    FAIL(-1, "失败"),
    QUEUE(0, "排队中"),
    SUCCESS(1, "成功"),
    DOING(2, "正在进行"),

    ;

    /**
     * 类型
     */
    private Integer code;


    /**
     * 描述
     */
    private String desc;
}
