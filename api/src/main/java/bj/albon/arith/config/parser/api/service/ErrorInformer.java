package bj.albon.arith.config.parser.api.service;

/**
 * @author albon
 * Date : 17-1-22
 * Time: 下午4:09
 */
public class ErrorInformer {

    private static AbstractIMService INSTANCE;

    public static void setINSTANCE(AbstractIMService INSTANCE) {
        ErrorInformer.INSTANCE = INSTANCE;
    }

    public static void inform(String content) {
        AbstractIMService instance = INSTANCE;
        if (instance != null) {
            instance.inform(content);
        }
    }
}
