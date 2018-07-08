package bj.albon.arith.config.parser.api.service.impl;

import com.google.common.collect.Maps;
import bj.albon.arith.config.parser.api.service.MultiFieldParser;

import java.util.Map;

/**
 * 解析多个key，形成一个map
 * key为前缀为prefix的key，除去prefix的部分
 * value为原value
 * 
 * @author albon
 *         Date : 17-1-20
 *         Time: 下午5:33
 */
public class PostFixMapFieldParser extends MultiFieldParser<Map<String, String>> {

    @Override
    public Map<String, String> parse(Map<String, String> configMap, String prefix) {
        Map<String, String> resultMap = Maps.newHashMap();

        int prefixLength = prefix.length();
        for (Map.Entry<String, String> entry : configMap.entrySet()) {
            String key = entry.getKey();
            if (!key.startsWith(prefix)) {
                continue;
            }

            resultMap.put(key.substring(prefixLength), entry.getValue());
        }

        return resultMap;
    }
}
