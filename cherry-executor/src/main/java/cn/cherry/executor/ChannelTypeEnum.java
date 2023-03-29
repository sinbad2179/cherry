package cn.cherry.executor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.swing.text.html.parser.ContentModel;

/**
 * @author: sinbad.cheng
 * @time: 2023/3/29 17:56
 * @description： 渠道类型
 */
@Getter
@ToString
@AllArgsConstructor
public enum ChannelTypeEnum {


    TEST_A(10000, "测试业务A", "00001"),


    ;


    /**
     * 编码值
     */
    private final Integer code;

    /**
     * 描述
     */
    private final String description;

    /**
     * 线程池唯一id
     */
    private final String uniqueId;


}
