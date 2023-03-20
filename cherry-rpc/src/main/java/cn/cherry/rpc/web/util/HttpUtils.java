package cn.cherry.rpc.web.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

/**
 * @author: sinbad.cheng
 * @time: 2023/3/20 19:37
 * @description： web工具
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpUtils {

    private static final String UNKNOWN = "unknown";

    private final static String LOCALHOST_IPV4 = "127.0.0.1";

    private final static String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";

    /**
     * IP 标头名称
     */
    private static final String[] IP_HEADER_NAMES = new String[]{
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "X-Real-IP",
            "HTTP_X_FORWARDED_FOR"
    };


    /**
     * 获取真实IP
     *
     * @param request
     * @return
     */
    public static String getRealIp(HttpServletRequest request) {
        if (Objects.isNull(request)) {
            return UNKNOWN;
        }
        String addr = request.getRemoteAddr();

        if (LOCALHOST_IPV4.equals(addr) || LOCALHOST_IPV6.equals(addr)) {
            try {
                InetAddress localHost = InetAddress.getLocalHost();
                addr = localHost.getHostAddress();
            } catch (UnknownHostException e) {
                //
            }
        }
        if (addr != null && addr.length() > 15 && addr.contains(",")) {
            return StringUtils.substringBefore(addr, ",");
        }
        return addr;
    }

}
