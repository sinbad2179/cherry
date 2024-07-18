package cn.cherry.core.util;

import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * ID工具类
 *
 * @author :  sinbad.cheng
 * @since :  2024-07-18 11:53
 */
public class IdUtil {
    private IdUtil() {
    }

    public static String uuid() {
        return UUID.randomUUID().toString();
    }


    public static String uuid2() {
        String uuid = UUID.randomUUID().toString();
        return StringUtils.replace(uuid, "-", "");
    }

}
