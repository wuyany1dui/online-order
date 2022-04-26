package com.design.onlineorder.config;

import com.design.onlineorder.enums.BaseEnum;
import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author DrEAmSs
 * 给前端返回值时给需要翻译的字典编码翻译成带desc的译文
 */
@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1000)
public class ApiBaseEnumDescriptionPlugin implements ModelPropertyBuilderPlugin, ParameterBuilderPlugin {

    private static final Logger LOG = LoggerFactory.getLogger(ApiBaseEnumDescriptionPlugin.class);

    @Override
    public boolean supports(DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }

    @Override
    public void apply(ModelPropertyContext context) {
        try {
            Optional<BeanPropertyDefinition> beanDef = context.getBeanPropertyDefinition();
            if (beanDef.isPresent()) {
                AnnotatedField aField = beanDef.get().getField();
                if (aField != null) {
                    Field field = aField.getAnnotated();
                    if (field != null) {
                        ApiParam param = field.getAnnotation(ApiParam.class);
                        ApiModelProperty property = field.getAnnotation(ApiModelProperty.class);
                        String description = "";
                        if (param != null && param.value() != null) {
                            description = param.value();
                        }
                        if (property != null && property.value() != null) {
                            description = property.value();
                        }

                        if (BaseEnum.class.isAssignableFrom(field.getType())) {
                            buildDescription(context, description, field.getType());
                        } else if (List.class.isAssignableFrom(field.getType())) {
                            ParameterizedType integerListType = (ParameterizedType) field.getGenericType();
                            Type type = integerListType.getActualTypeArguments()[0];
                            if (type instanceof Class<?> itemClass) {
                                if (BaseEnum.class.isAssignableFrom(itemClass)) {
                                    buildDescription(context, description, itemClass);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Throwable t) {
            // The exception will be logged, because Springfox will not.
            LOG.warn("Cannot process ApiModelProperty. Will throw new RuntimeException now.", t);
            throw new RuntimeException(t);
        }
    }

    /**
     *
     * If dataType can convert an Enum Class Type, or field type is an Enum
     * Class Type. Create markdown description.
     *
     */
    private void buildDescription(ModelPropertyContext context, String description, Class<?> clazz) {
        if (clazz.isEnum()) {
            @SuppressWarnings("unchecked")
            String markdown = createMarkdownDescription((Class<? extends BaseEnum<?>>) clazz);
            description += "\n" + markdown;
            context.getSpecificationBuilder().description(description);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see springfox.documentation.spi.service.ParameterBuilderPlugin#apply(
     * springfox.documentation.spi.service.contexts.ParameterContext)
     */
    @Override
    public void apply(ParameterContext context) {
        try {
            ResolvedMethodParameter param = context.resolvedMethodParameter();

            if (param != null) {
                ResolvedType resType = param.getParameterType();
                if (resType != null) {
                    Class<?> clazz = resType.getErasedType();

                    Class<?> itemClass = clazz;
                    if (List.class.isAssignableFrom(clazz)) {
                        List<ResolvedType> resolvedTypes = resType.getTypeBindings().getTypeParameters();
                        if (!resolvedTypes.isEmpty()) {
                            itemClass = resolvedTypes.get(0).getErasedType();
                        }
                    }

                    if (!BaseEnum.class.isAssignableFrom(itemClass) || !itemClass.isEnum()) {
                        return;
                    }

                    Optional<ApiParam> annotation = param.findAnnotation(ApiParam.class);
                    String description = annotation.isPresent() ? annotation.get().value() : "";
                    description += "\n" + createMarkdownDescription((Class<? extends BaseEnum<?>>) itemClass);
                    context.requestParameterBuilder().description(description);
                }
            }
        } catch (Throwable t) {
            // The exception will be logged, because Springfox will not.
            LOG.warn("Cannot process ApiParameter. Will throw new RuntimeException now.", t);
            throw new RuntimeException(t);
        }
    }

    static String createMarkdownDescription(Class<? extends BaseEnum<?>> clazz) {
        List<String> lines = new ArrayList<>();

        for (BaseEnum<?> enumVal : clazz.getEnumConstants()) {
            String desc = enumVal.getLabel();
            String enumName = enumVal.getValue().toString();

            String line = "* " + enumName + ": " + desc;
            lines.add(line);
        }

        return String.join("\n", lines);
    }
}
