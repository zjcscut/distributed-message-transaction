package org.throwable.support;

import org.springframework.boot.actuate.health.HealthAggregator;
import org.springframework.boot.actuate.health.OrderedHealthAggregator;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/11/26 14:33
 */
public enum HealthAggregatorUtils {

	INSTANCE;

	private static final OrderedHealthAggregator AGGREGATOR = new OrderedHealthAggregator();

	public HealthAggregator getDefaultOrderedHealthAggregator() {
		return AGGREGATOR;
	}
}
