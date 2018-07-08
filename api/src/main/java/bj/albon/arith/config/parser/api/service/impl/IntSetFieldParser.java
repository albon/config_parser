package bj.albon.arith.config.parser.api.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import bj.albon.arith.config.parser.api.service.SingleFieldParser;
import bj.albon.arith.config.parser.api.util.SplitUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Set;

/**
 * 英文逗号,分割而成的set
 * 
 * @author albon
 *         Date : 17-1-19
 *         Time: 上午11:43
 */
public class IntSetFieldParser extends SingleFieldParser<Set<Integer>> {
    @Override
    public Set<Integer> parse(String value) {
        if (StringUtils.isBlank(value)) {
            return Sets.newHashSet();
        }

        List<String> stringList = SplitUtil.getList(SplitUtil.SPLIT_COMMA, value);
        return Sets.newHashSet(Lists.transform(stringList, new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return Integer.valueOf(input);
            }
        }));
    }
}
