package bj.albon.arith.config.parser.api.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author albon
 *         Date: 17-2-6
 *         Time: 上午11:11
 */
public class ErrorUtil {

    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        try {
            throwable.printStackTrace(pw);
            return sw.toString();
        } finally {
            pw.close();
        }
    }
}
