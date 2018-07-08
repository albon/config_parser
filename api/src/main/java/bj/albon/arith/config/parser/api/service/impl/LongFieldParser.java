package bj.albon.arith.config.parser.api.service.impl;

import bj.albon.arith.config.parser.api.service.SingleFieldParser;

/**
 * @author albon
 *         Date : 17-1-22
 *         Time: 下午3:40
 */
public class LongFieldParser extends SingleFieldParser<Long> {
    @Override
    public Long parse(String value) {
        return Long.valueOf(value);
    }
}
