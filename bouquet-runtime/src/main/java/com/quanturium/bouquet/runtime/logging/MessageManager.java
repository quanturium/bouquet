package com.quanturium.bouquet.runtime.logging;

import com.quanturium.bouquet.annotations.RxLogger;
import com.quanturium.bouquet.runtime.components.ComponentInfo;
import com.quanturium.bouquet.runtime.components.RxEvent;

public class MessageManager {

	private final Logger logger;

	public MessageManager(Logger logger) {
		this.logger = logger;
	}

	public void printWrongMethodReturnType() {
		logger.log(RxLogger.class.getSimpleName(), "RxLogger annotation on wrong return type");
	}

	public void printSource(ComponentInfo componentInfo) {
		logger.log(
				componentInfo.classSimpleName(),
				new Message.Builder(componentInfo)
						.source()
						.build()
						.toString()
		);
	}

	public void printEvent(ComponentInfo componentInfo, RxEvent rxEvent) {
		logger.log(
				componentInfo.classSimpleName(),
				new Message.Builder(componentInfo)
						.event(rxEvent, null)
						.build()
						.toString()
		);
	}

	public <T> void printEvent(ComponentInfo componentInfo, RxEvent rxEvent, T value) {
		logger.log(
				componentInfo.classSimpleName(),
				new Message.Builder(componentInfo)
						.event(rxEvent, value)
						.build()
						.toString()
		);
	}

	public void printSummary(ComponentInfo componentInfo) {
		logger.log(
				componentInfo.classSimpleName(),
				new Message.Builder(componentInfo)
						.summary()
						.build()
						.toString()
		);
		logger.log(
				componentInfo.classSimpleName(),
				new Message.Builder(componentInfo)
						.summaryThread()
						.build()
						.toString()
		);
	}
}
