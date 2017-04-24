package cn.java.note;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.ClassFile;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.Compiler;
import org.eclipse.jdt.internal.compiler.DefaultErrorHandlingPolicies;
import org.eclipse.jdt.internal.compiler.ICompilerRequestor;
import org.eclipse.jdt.internal.compiler.IErrorHandlingPolicy;
import org.eclipse.jdt.internal.compiler.IProblemFactory;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileReader;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.env.INameEnvironment;
import org.eclipse.jdt.internal.compiler.env.NameEnvironmentAnswer;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;

public class JdtCompilerTest {

	public static void main(String[] args) {
		try {
			final String sourceFile = "d:/a/test/cn/java/test/HelloClazz.java"; // Java文件
			final String outputDir = "d:/a/test/workdir";
			final String targetClassName = "cn.java.test.HelloClazz"; // 类文件 
			test(sourceFile, outputDir, targetClassName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void test(final String sourceFile, final String outputDir, final String targetClassName) throws Exception {
		//		final String sourceFile = "d:/a/test/cn/java/test/HelloClazz.java"; // Java文件
		//		final String outputDir = "d:/a/test/workdir";
		//		final String targetClassName = "cn.java.test.HelloClazz"; // 类文件 
		final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		String[] fileNames = new String[] { sourceFile };
		String[] classNames = new String[] { targetClassName };

		class CompilationUnit implements ICompilationUnit {

			private final String className;
			private final String sourceFile;

			CompilationUnit(String sourceFile, String className) {
				this.className = className;
				this.sourceFile = sourceFile;
			}

			@Override
			public char[] getFileName() {
				return sourceFile.toCharArray();
			}

			@Override
			public char[] getContents() {
				char[] result = null;
				try (FileInputStream is = new FileInputStream(sourceFile); InputStreamReader isr = new InputStreamReader(is, "Utf-8"); Reader reader = new BufferedReader(isr)) {
					char[] chars = new char[8192];
					StringBuilder buf = new StringBuilder();
					int count;
					while ((count = reader.read(chars, 0, chars.length)) > 0) {
						buf.append(chars, 0, count);
					}
					result = new char[buf.length()];
					buf.getChars(0, result.length, result, 0);
				} catch (IOException e) {
					System.out.println("Compilation error" + e.toString());
				}
				return result;
			}

			@Override
			public char[] getMainTypeName() {
				int dot = className.lastIndexOf('.');
				if (dot > 0) {
					return className.substring(dot + 1).toCharArray();
				}
				return className.toCharArray();
			}

			@Override
			public char[][] getPackageName() {
				StringTokenizer izer = new StringTokenizer(className, ".");
				char[][] result = new char[izer.countTokens() - 1][];
				for (int i = 0; i < result.length; i++) {
					String tok = izer.nextToken();
					result[i] = tok.toCharArray();
				}
				return result;
			}

			@Override
			public boolean ignoreOptionalProblems() {
				return false;
			}
		}

		final INameEnvironment env = new INameEnvironment() {

			@Override
			public NameEnvironmentAnswer findType(char[][] compoundTypeName) {
				StringBuilder result = new StringBuilder();
				for (int i = 0; i < compoundTypeName.length; i++) {
					if (i > 0)
						result.append('.');
					result.append(compoundTypeName[i]);
				}
				return findType(result.toString());
			}

			@Override
			public NameEnvironmentAnswer findType(char[] typeName, char[][] packageName) {
				StringBuilder result = new StringBuilder();
				int i = 0;
				for (; i < packageName.length; i++) {
					if (i > 0)
						result.append('.');
					result.append(packageName[i]);
				}
				if (i > 0)
					result.append('.');
				result.append(typeName);
				return findType(result.toString());
			}

			private NameEnvironmentAnswer findType(String className) {

				if (className.equals(targetClassName)) {
					ICompilationUnit compilationUnit = new CompilationUnit(sourceFile, className);
					return new NameEnvironmentAnswer(compilationUnit, null);
				}

				String resourceName = className.replace('.', '/') + ".class";

				try (InputStream is = classLoader.getResourceAsStream(resourceName)) {
					if (is != null) {
						byte[] classBytes;
						byte[] buf = new byte[8192];
						ByteArrayOutputStream baos = new ByteArrayOutputStream(buf.length);
						int count;
						while ((count = is.read(buf, 0, buf.length)) > 0) {
							baos.write(buf, 0, count);
						}
						baos.flush();
						classBytes = baos.toByteArray();
						char[] fileName = className.toCharArray();
						ClassFileReader classFileReader = new ClassFileReader(classBytes, fileName, true);
						return new NameEnvironmentAnswer(classFileReader, null);
					}
				} catch (IOException exc) {
					System.out.println("Compilation error" + exc.toString());
				} catch (org.eclipse.jdt.internal.compiler.classfmt.ClassFormatException exc) {
					System.out.println("Compilation error" + exc.toString());
				}
				return null;
			}

			private boolean isPackage(String result) {
				if (result.equals(targetClassName)) {
					return false;
				}
				String resourceName = result.replace('.', '/') + ".class";
				try (InputStream is = classLoader.getResourceAsStream(resourceName)) {
					return is == null;
				} catch (IOException e) {
					// we are here, since close on is failed. That means it was not null
					return false;
				}
			}

			@Override
			public boolean isPackage(char[][] parentPackageName, char[] packageName) {
				StringBuilder result = new StringBuilder();
				int i = 0;
				if (parentPackageName != null) {
					for (; i < parentPackageName.length; i++) {
						if (i > 0)
							result.append('.');
						result.append(parentPackageName[i]);
					}
				}

				if (Character.isUpperCase(packageName[0])) {
					if (!isPackage(result.toString())) {
						return false;
					}
				}
				if (i > 0)
					result.append('.');
				result.append(packageName);

				return isPackage(result.toString());
			}

			@Override
			public void cleanup() {
			}

		};

		final IErrorHandlingPolicy policy = DefaultErrorHandlingPolicies.proceedWithAllProblems();

		final Map<String, String> settings = new HashMap<>();
		settings.put(CompilerOptions.OPTION_LineNumberAttribute, CompilerOptions.GENERATE);
		settings.put(CompilerOptions.OPTION_SourceFileAttribute, CompilerOptions.GENERATE);
		settings.put(CompilerOptions.OPTION_ReportDeprecation, CompilerOptions.IGNORE);
		settings.put(CompilerOptions.OPTION_Encoding, "utf-8");
		settings.put(CompilerOptions.OPTION_LocalVariableAttribute, CompilerOptions.GENERATE);

		// Source JVM
		// Default to 1.7
		settings.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_7);

		// Target JVM
		settings.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_7);
		settings.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_7);

		final IProblemFactory problemFactory = new DefaultProblemFactory(Locale.getDefault());

		final ICompilerRequestor requestor = new ICompilerRequestor() {
			@Override
			public void acceptResult(CompilationResult result) {
				try {
					if (result.hasProblems()) {
						IProblem[] problems = result.getProblems();
						for (int i = 0; i < problems.length; i++) {
							IProblem problem = problems[i];
							if (problem.isError()) {
								String name = new String(problems[i].getOriginatingFileName());
								throw new IOException("CompilerRequestor error : " + problem.getMessage());
							}
						}
					}
					ClassFile[] classFiles = result.getClassFiles();
					for (int i = 0; i < classFiles.length; i++) {
						ClassFile classFile = classFiles[i];
						char[][] compoundName = classFile.getCompoundName();
						StringBuilder classFileName = new StringBuilder(outputDir).append('/');
						for (int j = 0; j < compoundName.length; j++) {
							if (j > 0)
								classFileName.append('/');
							classFileName.append(compoundName[j]);
						}
						byte[] bytes = classFile.getBytes();
						classFileName.append(".class");
						try (FileOutputStream fout = new FileOutputStream(classFileName.toString()); BufferedOutputStream bos = new BufferedOutputStream(fout);) {
							bos.write(bytes);
						}
					}
				} catch (IOException exc) {
					System.out.println("Compilation error" + exc.toString());
				}
			}
		};

		ICompilationUnit[] compilationUnits = new ICompilationUnit[classNames.length];
		for (int i = 0; i < compilationUnits.length; i++) {
			String className = classNames[i];
			compilationUnits[i] = new CompilationUnit(fileNames[i], className);
		}
		CompilerOptions cOptions = new CompilerOptions(settings);
		cOptions.parseLiteralExpressionsAsConstants = true;
		Compiler compiler = new Compiler(env, policy, cOptions, requestor, problemFactory);
		compiler.compile(compilationUnits); // 进行编译  Java文件转成class文件

	}

}
