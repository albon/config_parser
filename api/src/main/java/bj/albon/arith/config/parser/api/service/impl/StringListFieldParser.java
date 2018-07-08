package bj.albon.arith.config.parser.api.service.impl;

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
public class StringListFieldParser extends SingleFieldParser<List<String>> {
    @Override
    public List<String> parse(String value) {
        return SplitUtil.getList(SplitUtil.SPLIT_COMMA, value);
    }
}
