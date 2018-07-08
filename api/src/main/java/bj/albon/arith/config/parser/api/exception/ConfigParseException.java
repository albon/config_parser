package bj.albon.arith.config.parser.api.exception;

/**
 * @author albon
 *         Date : 17-1-22
 *         Time: 下午4:21
 */
public class ConfigParseException extends Exception {

    public ConfigParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigParseException(String message) {
        super(message);
    }
}
