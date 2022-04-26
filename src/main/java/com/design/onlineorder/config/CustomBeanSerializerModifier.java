package com.design.onlineorder.config;

import com.design.onlineorder.enums.BaseEnum;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;

/**
 * @author DrEAmSs
 */
public class CustomBeanSerializerModifier extends BeanSerializerModifier {
    @Override
    public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> serializer) {
        if (BaseEnum.class.isAssignableFrom(beanDesc.getBeanClass())) {
            return new BaeEnumSerializer((JsonSerializer<Object>) serializer, null);
        } else {
            return serializer;
        }
    }

}

class BaeEnumSerializer extends JsonSerializer<BaseEnum> implements ContextualSerializer {
    private final JsonSerializer<Object> serializer;
    private final BeanProperty property;

    public BaeEnumSerializer(
            final JsonSerializer<Object> jsonSerializer,
            final BeanProperty property) {
        this.serializer = jsonSerializer;
        this.property = property;
    }

    @Override
    public void serialize(final BaseEnum o,
                          final JsonGenerator jsonGenerator,
                          final SerializerProvider serializerProvider)
            throws IOException {
        serializer.serialize(o, jsonGenerator, serializerProvider);
        jsonGenerator.writeStringField(property.getName() + "Desc", o == null ? null : o.getLabel());
    }

    @Override
    public JsonSerializer<?> createContextual(final SerializerProvider prov, final BeanProperty property) {
        if (property != null && property.getType().isTypeOrSubTypeOf(BaseEnum.class)) {
            return new BaeEnumSerializer(serializer, property);
        }
        return serializer;
    }
}