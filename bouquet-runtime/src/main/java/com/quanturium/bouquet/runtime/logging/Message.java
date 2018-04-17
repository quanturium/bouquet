package com.quanturium.bouquet.runtime.logging;

import com.quanturium.bouquet.runtime.components.RxComponent;
import com.quanturium.bouquet.runtime.components.RxComponentInfo;
import com.quanturium.bouquet.runtime.components.RxEvent;

import java.util.List;

public class Message {

	private static final String LABEL = "Bouquet => ";
	private static final String VALUE_SEPARATOR = " -> ";
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

		private final RxComponentInfo rxComponentInfo;
		private final StringBuilder sb;

		public Builder(RxComponentInfo rxComponentInfo) {
			this.rxComponentInfo = rxComponentInfo;
			sb = new StringBuilder(LABEL);
		}

		public Builder source() {
			sb.append(MESSAGE_TYPE_SOURCE);
			sb.append(rxComponentInfo.methodReturnType());
			sb.append(" ");
			sb.append(rxComponentInfo.classSimpleName());
			sb.append(METHOD_SEPARATOR);
			sb.append(rxComponentInfo.methodName());
			sb.append("(");
			List<String> methodParamNames = rxComponentInfo.methodParamNamesList();
			if (methodParamNames != null && !methodParamNames.isEmpty()) {
				for (int i = 0; i < methodParamNames.size(); i++) {
					sb.append(methodParamNames.get(i));
					sb.append("=");
					sb.append("'");
					sb.append(String.valueOf(rxComponentInfo.methodParamValuesList().get(i)));
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
			sb.append(rxComponentInfo.methodName());
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
			sb.append(rxComponentInfo.methodName());
			sb.append(VALUE_SEPARATOR);
			if (rxComponentInfo.rxComponent() != RxComponent.COMPLETABLE) {
				sb.append("Count: ");
				sb.append(rxComponentInfo.totalEmittedItems());
				if (rxComponentInfo.totalEmittedItems() == 1)
					sb.append(" item");
				else
					sb.append(" items");
				sb.append(" | ");
			}
			sb.append("Time: ");
			sb.append(rxComponentInfo.totalExecutionTime());
			sb.append(" ms");
			if (rxComponentInfo.observeOnThread() != null) {
				sb.append(" | ObservingOn: ");
				sb.append(rxComponentInfo.observeOnThread());
			}
			return this;
		}

		public Message build() {
			return new Message(sb.toString());
		}
	}
}
