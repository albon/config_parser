package bj.albon.arith.config.parser.api.util;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import bj.albon.arith.config.parser.api.exception.JsonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class JsonUtil {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    private static ObjectMapper objectMapper = new ObjectMapper();
    private static JsonFactory jsonFactory = objectMapper.getFactory();

    static {
        objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);// 兼容pojo增加字段的版本升级
    }

    public static String toString(Object obj) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            JsonGenerator jsonGenerator = jsonFactory.createGenerator(out, JsonEncoding.UTF8);
            jsonGenerator.writeObject(obj);
            return new String(out.toByteArray(), "UTF-8");
        } catch (Exception e) {
            logger.error("json toString error", e);
            return null;
        }
    }

    public static String toStringEx(Object obj) throws JsonException {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            JsonGenerator jsonGenerator = jsonFactory.createGenerator(out, JsonEncoding.UTF8);
            jsonGenerator.writeObject(obj);
            return new String(out.toByteArray(), "UTF-8");
        } catch (Exception e) {
            logger.error("json toString error", e);
            throw new JsonException("json toString error", e);
        }
    }

    public static <T> T parseObject(String jsonStr, Class<T> clazz) throws IOException {
        return objectMapper.readValue(jsonStr, clazz);
    }
}
