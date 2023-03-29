package cn.cherry.core.util;

import cn.cherry.core.exception.ServiceErrorCode;
import cn.cherry.core.exception.ServiceException;

import java.util.Arrays;
import java.util.Objects;

import static java.lang.String.format;

/**
 * @author: sinbad.cheng
 * @time: 2023/2/22 20:31
 * @description： 校验工具类
 */
public class CheckUtils {
    private CheckUtils() {
        throw new AssertionError("cn.cherry.core.util.CheckUtils instances for you!");
    }


    public static void main(String[] args) {
        checkArgument("body" != null, "Body parameter %s was null", "body");
    }

    /**
     * 校验参数是否为空
     *
     * @param args
     */
    public static void checkArgsIsEmpty(Object... args) {
        if (Arrays.stream(args).anyMatch(Objects::isNull)) {
            throw new ServiceException(ServiceErrorCode.ARGS_IS_NULL.getErrMsg());
        }
    }

    /**
     * 校验参数
     *
     * @param expression 表达式
     * @param exception  异常
     */
    public static void checkArgument(boolean expression, ServiceException exception) {
        if (expression) {
            throw new ServiceException(exception.getCode(), exception.getMessage());
        }
    }

    /**
     * Copy of {@code com.google.common.base.Preconditions#checkArgument}.
     */
    public static void checkArgument(boolean expression, String errorMessageTemplate, Object... errorMessageArgs) {
        if (expression) {
            throw new IllegalArgumentException(format(errorMessageTemplate, errorMessageArgs));
        }
    }

}
