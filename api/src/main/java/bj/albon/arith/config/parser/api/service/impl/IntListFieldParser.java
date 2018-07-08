package bj.albon.arith.config.parser.api.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import bj.albon.arith.config.parser.api.service.SingleFieldParser;
import bj.albon.arith.config.parser.api.util.SplitUtil;

import java.util.List;

/**
 * 英文逗号,分割而成的list
 * 
 * @author albon
 *         Date : 17-1-19
 *         Time: 上午11:43
 */
public class IntListFieldParser extends SingleFieldParser<List<Integer>> {
    @Override
    public List<Integer> parse(String value) {
        List<String> stringList = SplitUtil.getList(SplitUtil.SPLIT_COMMA, value);
        return Lists.transform(stringList, new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return Integer.valueOf(input);
            }
        });
    }
}
