package bj.albon.arith.config.parser.api.util;

/**
 * @author albon
 *         Date : 17-2-3
 *         Time: 下午3:03
 */
public class MachineUtil {

    public static boolean isProd() {
        String hostName = InetAddressUtil.queryHostName();

        if (hostName == null) {
            return false;
        }

        if (hostName.contains("beta") || hostName.contains("dev")) {
            return false;
        }

        return true;
    }

}
