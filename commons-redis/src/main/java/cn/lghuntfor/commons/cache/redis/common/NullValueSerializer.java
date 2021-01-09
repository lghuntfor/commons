package cn.lghuntfor.commons.cache.redis.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.cache.support.NullValue;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * @author liaogang
 * @since 2018/10/18
 */
public class NullValueSerializer extends StdSerializer<NullValue> {
    private static final long serialVersionUID = 1999052150548658808L;
    private final String classIdentifier;

    public NullValueSerializer(String classIdentifier) {
        super(NullValue.class);
        this.classIdentifier = StringUtils.hasText(classIdentifier)?classIdentifier:"@class";
    }

    @Override
    public void serialize(NullValue value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField(this.classIdentifier, NullValue.class.getName());
        jgen.writeEndObject();
    }
}
