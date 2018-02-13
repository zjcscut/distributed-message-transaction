package org.throwable.support;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.throwable.common.CommonConstants;
import org.throwable.exception.HandleMessageFailException;
import org.throwable.support.validate.ValidateFailStrategy;
import org.throwable.support.validate.ValidateResult;
import org.throwable.utils.JacksonUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Set;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/4 18:43
 */
public abstract class AbstractMessageListener<T> implements MessageListener {

	private Class<T> targetClass;

	@Autowired
	protected Validator validator;

	@Autowired(required = false)
	private ValidateFailStrategy validateFailStrategy;

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

	/**
	 * 由message转换为字符串
	 *
	 * @param message message
	 * @return String
	 */
	private String getMessageBodyContentAsString(Message message) {
		byte[] body = message.getBody();
		if (body == null) {
			return null;
		}
		try {
			MessageProperties messageProperties = message.getMessageProperties();
			String contentType = (messageProperties != null) ? messageProperties.getContentType() : null;
			if (null == contentType) {
				return new String(body, CommonConstants.CHARSET);
			}
			if (MessageProperties.CONTENT_TYPE_SERIALIZED_OBJECT.equals(contentType)) {
				return SerializationUtils.deserialize(body).toString();
			}
			if (MessageProperties.CONTENT_TYPE_TEXT_PLAIN.equals(contentType)
					|| MessageProperties.CONTENT_TYPE_JSON.equals(contentType)
					|| MessageProperties.CONTENT_TYPE_JSON_ALT.equals(contentType)
					|| MessageProperties.CONTENT_TYPE_XML.equals(contentType)) {
				return new String(body, CommonConstants.CHARSET);
			}
			return new String(body, CommonConstants.CHARSET);
		} catch (Exception e) {
			// ignore
		}
		// Comes out as '[B@....b' (so harmless) -- NOSONAR
		return Arrays.toString(body) + "(byte[" + body.length + "])";
	}

	@Override
	public void onMessage(Message message) {
		String content = getMessageBodyContentAsString(message);
		T target = JacksonUtils.INSTANCE.parse(content, targetClass);
		ValidateResult<T> validateResult = validateMessageEntity(target);
		if (Boolean.TRUE.equals(validateResult.getPass())) {
			try {
				handleMessage(target);
			} catch (Exception e) {
				throw new HandleMessageFailException(e);
			}
		} else if (null != validateFailStrategy) {
			validateFailStrategy.handleValidation(target, validateResult.getConstraintViolations());
		}
	}

	/**
	 * 校验消息实体
	 *
	 * @param t t
	 * @return ValidateResult
	 */
	protected ValidateResult<T> validateMessageEntity(T t) {
		ValidateResult<T> validateResult = new ValidateResult<>();
		validateResult.setTarget(t);
		Set<ConstraintViolation<T>> constraintViolations = validator.validate(t);
		if (constraintViolations.isEmpty()) {
			validateResult.setPass(Boolean.TRUE);
		} else {
			validateResult.setPass(Boolean.FALSE);
			validateResult.setConstraintViolations(constraintViolations);
		}
		return validateResult;
	}

	/**
	 * 处理消息方法,留给子类实现
	 *
	 * @param t t
	 * @throws Exception e
	 */
	protected abstract void handleMessage(T t) throws Exception;
}
