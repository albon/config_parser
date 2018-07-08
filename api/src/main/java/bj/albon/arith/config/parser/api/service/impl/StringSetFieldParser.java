package bj.albon.arith.config.parser.api.service.impl;

import com.google.common.collect.Sets;
import bj.albon.arith.config.parser.api.service.SingleFieldParser;
import bj.albon.arith.config.parser.api.util.SplitUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

/**
 * 英文逗号,分割而成的set
 * 
 * @author albon
 *         Date : 17-1-19
 *         Time: 上午11:43
 */
public class StringSetFieldParser extends SingleFieldParser<Set<String>> {
    @Override
    public Set<String> parse(String value) {
        if (StringUtils.isBlank(value)) {
            return Sets.newHashSet();
        }

        return Sets.newHashSet(SplitUtil.getList(SplitUtil.SPLIT_COMMA, value));
    }
}
