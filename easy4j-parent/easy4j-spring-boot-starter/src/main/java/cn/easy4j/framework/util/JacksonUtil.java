package cn.easy4j.framework.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.TimeZone;

/**
 * Jackson工具类
 *
 * @author yangzongmin
 * @date 2019-07-19
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JacksonUtil {

    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = initObjectMapper(new ObjectMapper());
    }

    /**
     * 转换Json
     *
     * @param object 对象
     * @return json字符串
     */
    public static String toJson(Object object) {
        try {
            return getObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("对象转json失败：{}", e.getMessage());
            return "";
        }
    }

    /**
     * 获取ObjectMapper
     *
     * @return ObjectMapper
     */
    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    /**
     * 初始化 ObjectMapper
     *
     * @param objectMapper 待初始化的实例
     * @return 已设置默认参数的实例
     */
    private static ObjectMapper initObjectMapper(ObjectMapper objectMapper) {
        if (Objects.isNull(objectMapper)) {
            objectMapper = new ObjectMapper();
        }
        return doInitObjectMapper(objectMapper);
    }

    /**
     * 初始化 ObjectMapper 时间方法
     *
     * @param objectMapper 待初始化的实例
     * @return 已设置默认参数的实例
     */
    private static ObjectMapper doInitObjectMapper(ObjectMapper objectMapper) {
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // 不显示为null的字段
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 忽略不能转移的字符
        objectMapper.configure(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER.mappedFeature(), true);
        // 过滤对象的null属性.
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 忽略transient
        objectMapper.configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true);
        // 设置时间格式化
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        // 设置时区
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return registerModule(objectMapper);
    }

    /**
     * 注册模块
     *
     * @param objectMapper 实例
     * @return 已注册模块的实例
     */
    private static ObjectMapper registerModule(ObjectMapper objectMapper) {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        simpleModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        simpleModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(simpleModule);
        return objectMapper;
    }

    /**
     * Json转换为对象 转换失败返回null
     *
     * @param json 字符串
     * @return Object对象
     */
    public static Object parse(String json) {
        Object object = null;
        try {
            object = getObjectMapper().readValue(json, Object.class);
        } catch (Exception e) {
            log.error("字符串转对象失败：{}", e.getMessage());
        }
        return object;
    }

    /**
     * Json转换为对象 转换失败返回null
     *
     * @param json  字符串
     * @param clazz 需要转换的class
     * @param <T>   泛型
     * @return 泛型对象
     */
    public static <T> T parse(String json, Class<T> clazz) {
        T t = null;
        try {
            t = getObjectMapper().readValue(json, clazz);
        } catch (Exception e) {
            log.error("字符串转对象失败：{}", e.getMessage());
        }
        return t;
    }

    /**
     * Json转换为JsonNode 转换失败返回null
     *
     * @param json 字符串
     * @return jsonNode
     */
    public static JsonNode readValue(String json) {
        JsonNode jsonNode = null;
        try {
            jsonNode = getObjectMapper().readTree(json);
        } catch (Exception e) {
            log.error("字符串转JsonNode失败：{}", e.getMessage());
        }
        return jsonNode;
    }

}
