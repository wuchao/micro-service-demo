package com.github.wuchao.microservicedemo.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;

@Slf4j
public class JsonXMLUtils {

    static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    static final DateTimeFormatter dateTimeFormatterWithT = DateTimeFormatter.ofPattern("yyyy-MM-dd\'T\'HH:mm:ss");

    private JsonXMLUtils() {

    }

    public static ObjectMapper objectMapper() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        javaTimeModule.addSerializer(LocalDateTime.class, new CustomLocalDateTimeSerializer());
        javaTimeModule.addDeserializer(LocalDateTime.class, new CustomLocalDateTimeDeserializer());
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        javaTimeModule.addSerializer(Instant.class, new CustomInstantSerializer());
        javaTimeModule.addDeserializer(Instant.class, new CustomInstantDeserializer());

        ObjectMapper mapper = new ObjectMapper()
                // .findAndRegisterModules(); 会导致对象中加了@Transient注解的字段无法被序列化
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(javaTimeModule);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        return mapper;
    }

    public static String obj2json(Object obj) {

        try {
            return objectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static <T> T json2obj(String json, Class<T> clazz) {

        try {
            return objectMapper().readValue(json, clazz);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("json={}解析为{}出现异常", json, clazz.getName());
            return null;
        }
    }

    public static class CustomLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

        @Override
        public LocalDateTime deserialize(com.fasterxml.jackson.core.JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, com.fasterxml.jackson.core.JsonProcessingException {
            String dateString = jsonParser.getText().trim();
            if (StringUtils.isNotBlank(dateString)) {
                dateString = dateString.substring(0, 19);
                if (dateString.contains("T")) {
                    return LocalDateTime.parse(dateString, dateTimeFormatterWithT);
                } else {
                    return LocalDateTime.parse(dateString, dateTimeFormatter);
                }
            }
            return null;
        }
    }

    public static class CustomLocalDateTimeSerializer extends StdSerializer<LocalDateTime> {

        CustomLocalDateTimeSerializer(Class<LocalDateTime> t) {
            super(t);
        }

        CustomLocalDateTimeSerializer() {
            this(null);
        }

        @Override
        public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeString(dateTimeFormatter.withZone(ZoneId.systemDefault()).format(value));

        }
    }


    public static class CustomInstantDeserializer extends JsonDeserializer<Instant> {

        @Override
        public Instant deserialize(com.fasterxml.jackson.core.JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, com.fasterxml.jackson.core.JsonProcessingException {
            String dateString = jsonParser.getText().trim();
            if (StringUtils.isNotBlank(dateString)) {
                dateString = dateString.substring(0, 19);
                if (dateString.contains("T")) {
                    return LocalDateTime.parse(dateString, dateTimeFormatterWithT)
                            .atZone(ZoneId.systemDefault()).toInstant();
                } else {
                    return LocalDateTime.parse(dateString, dateTimeFormatter)
                            .atZone(ZoneId.systemDefault()).toInstant();
                }
            }
            return null;
        }
    }

    public static class CustomInstantSerializer extends StdSerializer<Instant> {

        CustomInstantSerializer(Class<Instant> t) {
            super(t);
        }

        CustomInstantSerializer() {
            this(null);
        }

        @Override
        public void serialize(Instant value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeString(dateTimeFormatter.withZone(ZoneId.systemDefault()).format(value));

        }
    }


    /**
     * 测试日期的序列化、反序列化
     *
     * @param args
     * @throws IOException
     */
    /*public static void main(String[] args) throws IOException {
        String json = " {\n" +
            "        \"codeInstallData\": \"2019-08-01 19:26:33\",\n" +
            "        \"checkTime\": \"2019-08-01T19:26:33\",\n" +
            "        \"createdDate\": \"2019-08-29T19:26:33Z\",\n" +
            "        \"createdDate2\": \"2019-08-30T19:26:33\"\n" +
            "    }";
        CodeBaseInfoDTO codeBaseInfoDTO = getObjectMapper().readValue(json, CodeBaseInfoDTO.class);
        System.out.println(codeBaseInfoDTO);

        String json2 = getObjectMapper().writeValueAsString(codeBaseInfoDTO);
        System.out.println(json2);
    }*/


}
