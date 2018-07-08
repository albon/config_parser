package bj.albon.arith.config.parser.api.exception;

/**
 * @author albon
 *         Date : 17-1-22
 *         Time: 下午4:21
 */
public class JsonException extends Exception {

    public JsonException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonException(String message) {
        super(message);
    }
}
