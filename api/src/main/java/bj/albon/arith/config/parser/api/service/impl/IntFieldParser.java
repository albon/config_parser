package bj.albon.arith.config.parser.api.service.impl;

import bj.albon.arith.config.parser.api.service.SingleFieldParser;

/**
 * @author albon
 *         Date : 17-1-19
 *         Time: 上午11:39
 */
public class IntFieldParser extends SingleFieldParser<Integer> {
    @Override
    public Integer parse(String value) {
        return Integer.valueOf(value);
    }
}
