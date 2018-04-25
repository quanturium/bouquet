package com.quanturium.bouquet.plugin.configure;

import com.android.build.api.transform.Context;
import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.Format;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.api.transform.TransformOutputProvider;
import com.android.build.gradle.BaseExtension;
import com.android.build.gradle.api.BaseVariant;
import com.google.common.base.Joiner;
import com.google.common.collect.Sets;

import org.apache.commons.io.FileUtils;
import org.gradle.api.Project;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AspectJAndroidTransform extends Transform {

	private final static String NAME = "aspectj";
	private final static Set<QualifiedContent.ContentType> INPUT_TYPES = Sets.newHashSet(QualifiedContent.DefaultContentType.CLASSES);
	private static final Set<QualifiedContent.Scope> SCOPES = Sets.immutableEnumSet(QualifiedContent.Scope.PROJECT, QualifiedContent.Scope.SUB_PROJECTS, QualifiedContent.Scope.EXTERNAL_LIBRARIES);
	private static final Set<String> includedJars = Sets.newHashSet(
			"io.reactivex.rxjava2:rxjava",
			"com.quanturium.bouquet:bouquet-runtime-android"
	);

	private final Project project;
	private final BaseExtension androidExtension;
	private final Set<BaseVariant> variants = new HashSet<>();

	public AspectJAndroidTransform(Project project, BaseExtension androidExtension) {
		this.project = project;
		this.androidExtension = androidExtension;
	}

	public void addVariant(BaseVariant baseVariant) {
		variants.add(baseVariant);
	}

	public BaseVariant getVariant(Context context) {
		String variantName = context.getVariantName();
		for (BaseVariant variant : variants) {
			if (variant.getName().equals(variantName)) {
				return variant;
			}
		}

		return null;
	}

	@Override
	public void transform(TransformInvocation transformInvocation) throws IOException {

		Context context = transformInvocation.getContext();
		java.util.Collection<TransformInput> inputs = transformInvocation.getInputs();
		Collection<TransformInput> referencedInputs = transformInvocation.getReferencedInputs();
		TransformOutputProvider outputProvider = transformInvocation.getOutputProvider();

		// Clean up
		outputProvider.deleteAll();

		// Check if we are handling this specific variant
		if (getVariant(context) == null) {

			for (TransformInput input : inputs) {
				for (DirectoryInput directoryInput : input.getDirectoryInputs()) {
					File output = outputProvider.getContentLocation(
							directoryInput.getName(),
							INPUT_TYPES,
							SCOPES,
							Format.DIRECTORY
					);
					FileUtils.copyDirectory(directoryInput.getFile(), output);
				}

				for (JarInput jarInput : input.getJarInputs()) {
					File output = outputProvider.getContentLocation(
							jarInput.getName(),
							INPUT_TYPES,
							SCOPES,
							Format.JAR
					);
					FileUtils.copyFile(jarInput.getFile(), output);
				}
			}

			return;
		}

		List<File> files = new ArrayList<>();
		List<File> classpathFiles = new ArrayList<>();

		for (TransformInput input : referencedInputs) {
			for (DirectoryInput directoryInput : input.getDirectoryInputs())
				classpathFiles.add(directoryInput.getFile());
			for (JarInput jarInput : input.getJarInputs())
				classpathFiles.add(jarInput.getFile());
		}

		for (TransformInput input : inputs) {
			for (DirectoryInput directoryInput : input.getDirectoryInputs()) {
				files.add(directoryInput.getFile());
			}
			for (JarInput jarInput : input.getJarInputs()) {
				if (isJarIncluded(jarInput)) {
					files.add(jarInput.getFile());
				} else {
					classpathFiles.add(jarInput.getFile());
					File output = outputProvider.getContentLocation(
							jarInput.getName(),
							INPUT_TYPES,
							SCOPES,
							Format.JAR
					);
					FileUtils.copyFile(jarInput.getFile(), output);
				}
			}
		}

		File output = outputProvider.getContentLocation(NAME, getOutputTypes(), Sets.immutableEnumSet(QualifiedContent.Scope.PROJECT), Format.DIRECTORY);

		List<String> args = Arrays.asList(
				"-1.5",
				"-showWeaveInfo",
				"-inpath", Joiner.on(File.pathSeparator).join(files),
				"-d", output.getAbsolutePath(),
				"-classpath", Joiner.on(File.pathSeparator).join(classpathFiles),
				"-bootclasspath", Joiner.on(File.pathSeparator).join(androidExtension.getBootClasspath())
		);

		AspectCompiler.compile(project, args);
	}

	private boolean isJarIncluded(JarInput jarInput) {
		for (String includedJar : includedJars) {
			if (jarInput.getName().startsWith(includedJar)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Set<QualifiedContent.ContentType> getInputTypes() {
		return INPUT_TYPES;
	}

	@Override
	public Set<? super QualifiedContent.Scope> getScopes() {
		return SCOPES;
	}

	@Override
	public boolean isIncremental() {
		return false;
	}
}
