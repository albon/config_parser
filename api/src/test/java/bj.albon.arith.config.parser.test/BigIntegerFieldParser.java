package bj.albon.arith.config.parser.test;

import bj.albon.arith.config.parser.api.service.SingleFieldParser;

import java.math.BigInteger;

/**
 * Author: pengyu.song
 * Date: 2019/7/13
 */
public class BigIntegerFieldParser extends SingleFieldParser<BigInteger> {
    @Override
    public BigInteger parse(String value) {
        return new BigInteger(value);
    }
}
