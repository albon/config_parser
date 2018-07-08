package bj.albon.arith.config.parser.api.service.impl;

import bj.albon.arith.config.parser.api.service.SingleFieldParser;

/**
 * @author albon
 *         Date : 17-1-19
 *         Time: 上午11:38
 */
public class DoubleFieldParser extends SingleFieldParser<Double> {
    @Override
    public Double parse(String value) {
        return Double.valueOf(value);
    }
}
