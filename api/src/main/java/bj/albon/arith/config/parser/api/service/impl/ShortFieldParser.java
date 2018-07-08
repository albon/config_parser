package bj.albon.arith.config.parser.api.service.impl;

import bj.albon.arith.config.parser.api.service.SingleFieldParser;

/**
 * @author albon
 *         Date : 17-1-19
 *         Time: 上午11:39
 */
public class ShortFieldParser extends SingleFieldParser<Short> {
    @Override
    public Short parse(String value) {
        return Short.valueOf(value);
    }
}
