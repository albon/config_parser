package bj.albon.arith.config.parser.api.util;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author albon
 *         Date : 17-2-3
 *         Time: 下午4:01
 */
public class SplitUtil {
    public static final String SPLIT_UNDERLINE = "_";

    public static final String SPLIT_COMMA = ",";

    public static final String SPLIT_STRIKE = "-";

    public static final String SPLIT_COLON = ":";

    /**
     * @param splitter may not be empty, if empty will return empty result
     * @param configValue if empty will return empty result
     */
    public static List<String> getList(String splitter, String configValue) {
        if (Strings.isNullOrEmpty(configValue) || Strings.isNullOrEmpty(splitter)) {
            return Lists.newArrayList();
        }
        return Splitter.on(splitter).trimResults().omitEmptyStrings().splitToList(configValue);
    }
}
