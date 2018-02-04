package org.throwable.server.support.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.utils.SerializationUtils;
import org.throwable.common.GlobalConstants;
import org.throwable.utils.JacksonUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/4 18:43
 */
public abstract class AbstractMessageListener<T> implements MessageListener {

    private Class<T> targetClass;

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public AbstractMessageListener() {
        Class<?> classOfT = getClass();
        Type type = classOfT.getGenericSuperclass();
        if (classOfT == Object.class) {
            this.targetClass = (Class<T>) classOfT;
            return;
        }
        // 如果Service类被包装代理类的话
        while (!(type instanceof ParameterizedType) && classOfT != Object.class) {
            classOfT = classOfT.getSuperclass();
            type = classOfT.getGenericSuperclass();
        }
        //只支持单个泛型参数
        Type realType = ((ParameterizedType) type).getActualTypeArguments()[0];
        this.targetClass = (Class<T>) realType;
    }

    private String getMessageBodyContentAsString(Message message) {
        byte[] body = message.getBody();
        MessageProperties messageProperties = message.getMessageProperties();
        if (body == null) {
            return null;
        }
        try {
            String contentType = (messageProperties != null) ? messageProperties.getContentType() : null;
            if (null == contentType) {
                return new String(body, GlobalConstants.CHARSET);
            }
            if (MessageProperties.CONTENT_TYPE_SERIALIZED_OBJECT.equals(contentType)) {
                return SerializationUtils.deserialize(body).toString();
            }
            if (MessageProperties.CONTENT_TYPE_TEXT_PLAIN.equals(contentType)
                    || MessageProperties.CONTENT_TYPE_JSON.equals(contentType)
                    || MessageProperties.CONTENT_TYPE_JSON_ALT.equals(contentType)
                    || MessageProperties.CONTENT_TYPE_XML.equals(contentType)) {
                return new String(body, GlobalConstants.CHARSET);
            }
            return new String(body, GlobalConstants.CHARSET);
        } catch (Exception e) {
            // ignore
        }
        // Comes out as '[B@....b' (so harmless) -- NOSONAR
        return Arrays.toString(body) + "(byte[" + body.length + "])";
    }

    @Override
    public void onMessage(Message message) {
        String content = getMessageBodyContentAsString(message);
        handleMessage(JacksonUtils.parse(content, targetClass));
    }

    protected abstract void handleMessage(T t);
}
