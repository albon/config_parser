package bj.albon.arith.config.parser.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author albon
 *         Date : 17-1-22
 *         Time: 下午5:13
 */
public class AppInfo {
    private static final Logger logger = LoggerFactory.getLogger(AppInfo.class);
    private static final AppInfo INSTANCE = new AppInfo();

    public static AppInfo getINSTANCE() {
        return INSTANCE;
    }

    private String appName;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
