package com.quanturium.bouquet.plugin.configure;

import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.MessageHandler;
import org.aspectj.tools.ajc.Main;
import org.gradle.api.Project;

import java.util.List;

import static org.aspectj.bridge.IMessage.DEBUG;
import static org.aspectj.bridge.IMessage.INFO;
import static org.aspectj.bridge.IMessage.WARNING;

public class AspectCompiler {

	public static void compile(Project project, List<String> args) {

		String[] argsArray = args.toArray(new String[args.size()]);
		final MessageHandler handler = new MessageHandler(true);
		new Main().run(argsArray, handler);
		for (IMessage message : handler.getMessages(null, true)) {
			if (message.getKind().isSameOrLessThan(INFO)) {
				project.getLogger().info(message.getMessage(), message.getThrown());
			} else if (message.getKind().isSameOrLessThan(DEBUG)) {
				project.getLogger().debug(message.getMessage(), message.getThrown());
			} else if (message.getKind().isSameOrLessThan(WARNING)) {
				project.getLogger().warn(message.getMessage(), message.getThrown());
			} else {
				project.getLogger().error(message.getMessage(), message.getThrown());
			}
		}
	}
}
