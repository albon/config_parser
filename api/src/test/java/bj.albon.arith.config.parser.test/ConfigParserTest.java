package bj.albon.arith.config.parser.test;

import bj.albon.arith.config.parser.api.exception.ConfigParseException;
import bj.albon.arith.config.parser.api.service.ConfigParser;
import bj.albon.arith.config.parser.api.service.FieldParserFactory;
import com.google.common.collect.Maps;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Map;

/**
 * Author: pengyu.song
 * Date: 2018/7/8
 */
public class ConfigParserTest {

    @Test
    public void test() throws ConfigParseException {
        // 添加自定义的解析器
        FieldParserFactory.addFieldParser(new BigIntegerFieldParser());

        Map<String, String> configMap = Maps.newHashMap();
        configMap.put("age", "55");
        configMap.put("is.open", "true");
        configMap.put("person.list", "zhangsan,lisi");
        configMap.put("bint", "99999999999999");

        ConfigParser.parse(configMap, CommonConfig.getINSTANE());

        Assert.assertTrue(CommonConfig.getINSTANE().isOpen());
        Assert.assertEquals(CommonConfig.getINSTANE().getAge(), 55);
        Assert.assertTrue(CommonConfig.getINSTANE().getPersonList().contains("zhangsan")
                && CommonConfig.getINSTANE().getPersonList().contains("lisi"));
        Assert.assertEquals(CommonConfig.getINSTANE().getBint(), new BigInteger("99999999999999"));
    }
}
