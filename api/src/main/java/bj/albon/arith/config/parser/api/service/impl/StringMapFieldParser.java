package bj.albon.arith.config.parser.api.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import bj.albon.arith.config.parser.api.service.SingleFieldParser;
import bj.albon.arith.config.parser.api.util.SplitUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author albon
 *         Date : 17-1-20
 *         Time: 下午5:33
 */
public class StringMapFieldParser extends SingleFieldParser<Map<String, String>> {
    @Override
    public Map<String, String> parse(String value) {
        if (StringUtils.isBlank(value)) {
            return Maps.newHashMap();
        }

        return Splitter.on(SplitUtil.SPLIT_COMMA).trimResults().omitEmptyStrings()
                .withKeyValueSeparator(SplitUtil.SPLIT_COLON).split(value);
    }
}
