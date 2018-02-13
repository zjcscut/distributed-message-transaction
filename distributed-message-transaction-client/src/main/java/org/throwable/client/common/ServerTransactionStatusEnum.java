package org.throwable.client.common;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/11 18:48
 */
public enum ServerTransactionStatusEnum {

	/**
	 * server事务成功
	 */
	SUCCESS(LocalTransactionStatusEnum.SUCCESS, 2),

	/**
	 * server事务回滚
	 */
	ROLLBACK(LocalTransactionStatusEnum.ROLLBACK, 3);

	private Integer value;
	private LocalTransactionStatusEnum localTransactionStatusEnum;

	ServerTransactionStatusEnum(LocalTransactionStatusEnum localTransactionStatusEnum, Integer value) {
		this.localTransactionStatusEnum = localTransactionStatusEnum;
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}

	public LocalTransactionStatusEnum getLocalTransactionStatusEnum() {
		return localTransactionStatusEnum;
	}

	public static Integer fromLocalTransactionStatus(LocalTransactionStatusEnum localTransactionStatusEnum) {
		for (ServerTransactionStatusEnum serverTransactionStatusEnum : ServerTransactionStatusEnum.values()) {
			if (serverTransactionStatusEnum.getLocalTransactionStatusEnum().equals(localTransactionStatusEnum)) {
				return serverTransactionStatusEnum.getValue();
			}
		}
		throw new IllegalArgumentException("Illegal localTransactionStatusEnum,value = " + localTransactionStatusEnum);
	}
}
