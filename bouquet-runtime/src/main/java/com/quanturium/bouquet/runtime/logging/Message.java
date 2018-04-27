package com.quanturium.bouquet.runtime.logging;

import com.quanturium.bouquet.runtime.components.ComponentInfo;
import com.quanturium.bouquet.runtime.components.ComponentType;
import com.quanturium.bouquet.runtime.components.RxEvent;

import java.util.List;

public class Message {

	private static final String LABEL = "Bouquet => ";
	private static final String VALUE_SEPARATOR = " -> ";
	private static final String ITEM_SEPERATOR = " | ";
	private static final String METHOD_SEPARATOR = "#";
	private static final String MESSAGE_TYPE_SOURCE = "[Source] ";
	private static final String MESSAGE_TYPE_EVENT = "[Event] ";
	private static final String MESSAGE_TYPE_SUMMARY = "[Summary] ";

	private final String message;

	public Message(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return message;
	}

	public static class Builder {

		private final ComponentInfo componentInfo;
		private final StringBuilder sb;

		public Builder(ComponentInfo componentInfo) {
			this.componentInfo = componentInfo;
			sb = new StringBuilder(LABEL);
		}

		public Builder source() {
			sb.append(MESSAGE_TYPE_SOURCE);
			sb.append(componentInfo.methodReturnType());
			sb.append(" ");
			sb.append(componentInfo.classSimpleName());
			sb.append(METHOD_SEPARATOR);
			sb.append(componentInfo.methodName());
			sb.append("(");
			List<String> methodParamNames = componentInfo.methodParamNamesList();
			if (methodParamNames != null && !methodParamNames.isEmpty()) {
				for (int i = 0; i < methodParamNames.size(); i++) {
					sb.append(methodParamNames.get(i));
					sb.append("=");
					sb.append("'");
					sb.append(String.valueOf(componentInfo.methodParamValuesList().get(i)));
					sb.append("'");
					if ((i != methodParamNames.size() - 1)) {
						sb.append(", ");
					}
				}
			}
			sb.append(")");
			return this;
		}

		public <T> Builder event(RxEvent rxEvent, T value) {
			sb.append(MESSAGE_TYPE_EVENT);
			sb.append(componentInfo.methodName());
			sb.append(VALUE_SEPARATOR);
			switch (rxEvent) {
				case SUBSCRIBE:
					sb.append("onSubscribe()");
					break;
				case NEXT:
					sb.append("onNext()");
					sb.append(VALUE_SEPARATOR);
					sb.append(String.valueOf(value));
					break;
				case SUCCESS:
					sb.append("onSuccess()");
					sb.append(VALUE_SEPARATOR);
					sb.append(String.valueOf(value));
					break;
				case ERROR:
					sb.append("onError()");
					sb.append(VALUE_SEPARATOR);
					sb.append(String.valueOf(value));
					break;
				case COMPLETE:
					sb.append("onComplete()");
					break;
				case TERMINATE:
					sb.append("onTerminate()");
					break;
				case DISPOSE:
					sb.append("onDispose()");
					break;
				case REQUEST:
					sb.append("onRequest()");
					sb.append(VALUE_SEPARATOR);
					sb.append(String.valueOf(value));
					sb.append(" items");
					break;
				case CANCEL:
					sb.append("onCancel()");
					break;
			}
			return this;
		}

		public Builder summary() {
			sb.append(MESSAGE_TYPE_SUMMARY);
			sb.append(componentInfo.methodName());
			sb.append(VALUE_SEPARATOR);
			if (componentInfo.type() != ComponentType.COMPLETABLE) {
				sb.append("Count: ");
				sb.append(componentInfo.totalEmittedItems());
				if (componentInfo.totalEmittedItems() == 1)
					sb.append(" item");
				else
					sb.append(" items");
				sb.append(ITEM_SEPERATOR);
			}
			sb.append("Time: ");
			sb.append(componentInfo.totalExecutionTime());
			sb.append(" ms");
			sb.append(ITEM_SEPERATOR);
			sb.append("Subscription: ");
			if (componentInfo.isSynchronous()) {
				sb.append("synchronous");
			} else {
				sb.append("asynchronous");
			}
			return this;
		}

		public Builder summaryThread() {
			sb.append(MESSAGE_TYPE_SUMMARY);
			sb.append(componentInfo.methodName());
			sb.append(VALUE_SEPARATOR);
			if (componentInfo.isSynchronous()) {
				sb.append("Calling thread: ");
				sb.append(componentInfo.observeOnThread());
			} else {
				if (componentInfo.subscribeOnScheduler() != null) {
					sb.append("Subscribe: ");
					sb.append(componentInfo.subscribeOnScheduler());
					sb.append(" (");
					sb.append(componentInfo.subscribeOnThread());
					sb.append(")");
				}
				if (componentInfo.observeOnScheduler() != null) {
					sb.append(ITEM_SEPERATOR);
					sb.append("Observe: ");
					sb.append(componentInfo.observeOnScheduler());
					sb.append(" (");
					sb.append(componentInfo.observeOnThread());
					sb.append(")");
				}
			}
			return this;
		}

		public Message build() {
			return new Message(sb.toString());
		}
	}
}
