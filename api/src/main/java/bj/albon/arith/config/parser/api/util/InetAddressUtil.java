package bj.albon.arith.config.parser.api.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;

/**
 * @author albon
 *         Date : 17-2-3
 *         Time: 下午2:56
 */
public class InetAddressUtil {
    private static final Logger logger = LoggerFactory.getLogger(InetAddressUtil.class);

    public static String queryHostName() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            return inetAddress.getHostName();
        } catch (Exception e) {
            logger.error("query host name error", e);
            return null;
        }
    }

    public static String queryIp() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            return inetAddress.getHostAddress();
        } catch (Exception e) {
            logger.error("query host address error", e);
            return null;
        }
    }

}
