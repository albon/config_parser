package bj.albon.arith.config.parser.api.service.impl;

import bj.albon.arith.config.parser.api.service.SingleFieldParser;

/**
 * @author albon
 *         Date : 17-1-19
 *         Time: 上午11:38
 */
public class StringFieldParser extends SingleFieldParser<String> {
    @Override
    public String parse(String value) {
        return value;
    }
}
