package bj.albon.arith.config.parser.api.service.impl;

import bj.albon.arith.config.parser.api.service.SingleFieldParser;

import java.math.BigDecimal;

/**
 * @author albon
 *         Date: 17-8-10
 *         Time: 下午5:44
 */
public class BigDecimalFieldParser extends SingleFieldParser<BigDecimal> {
    @Override
    public BigDecimal parse(String value) {
        return new BigDecimal(value);
    }
}
