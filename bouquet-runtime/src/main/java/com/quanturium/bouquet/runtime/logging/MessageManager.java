package com.quanturium.bouquet.runtime.logging;

import com.quanturium.bouquet.annotations.RxLogger;
import com.quanturium.bouquet.runtime.components.RxComponentInfo;
import com.quanturium.bouquet.runtime.components.RxEvent;

public class MessageManager {

	private final Logger logger;

	public MessageManager(Logger logger) {
		this.logger = logger;
	}

	public void printWrongMethodReturnType() {
		logger.log(RxLogger.class.getSimpleName(), "RxLogger annotation on wrong return type");
	}

	public void printSource(RxComponentInfo rxComponentInfo) {
		logger.log(
				rxComponentInfo.classSimpleName(),
				new Message.Builder(rxComponentInfo)
						.source()
						.build()
						.toString()
		);
	}

	public void printEvent(RxComponentInfo rxComponentInfo, RxEvent rxEvent) {
		logger.log(
				rxComponentInfo.classSimpleName(),
				new Message.Builder(rxComponentInfo)
						.event(rxEvent, null)
						.build()
						.toString()
		);
	}

	public <T> void printEvent(RxComponentInfo rxComponentInfo, RxEvent rxEvent, T value) {
		logger.log(
				rxComponentInfo.classSimpleName(),
				new Message.Builder(rxComponentInfo)
						.event(rxEvent, value)
						.build()
						.toString()
		);
	}

	public void printSummary(RxComponentInfo rxComponentInfo) {
		logger.log(
				rxComponentInfo.classSimpleName(),
				new Message.Builder(rxComponentInfo)
						.summary()
						.build()
						.toString()
		);
	}
}
