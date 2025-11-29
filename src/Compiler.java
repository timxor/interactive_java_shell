/**
 * =============================================================================
 * Project:      =        tcsiwula-repl
 * Package:      =        cs345.repl
 * Created:      =        2/6/16
 * Author:       =        Tim Siwula <tcsiwula@usfca.edu>
 * University:   =        University of San Francisco
 * Class:        =        Computer Science 345: Programming Languages
 * Liscense:     =        GPLv2
 * Version:      =        0.001
 * ==============================================================================
 */

import com.sun.source.util.JavacTask;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Compiler
{
	JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
	DiagnosticCollector diagnostics = new DiagnosticCollector();
	StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);	// use for compiling disk files.;
	String classFileLocation;

	public String getClassFileLocation()
	{
		return classFileLocation;
	}

	public Compiler()
	{
	}

	public boolean compileDiskFile(ArrayList<File> files, String path) throws IOException
	{
		File newJavaFile = files.get((files.size() - 1));
		File f = new File(newJavaFile.toString());
		String classpath = System.getProperty("java.class.path");
		Iterable<String> compileOptions = Arrays.asList("-d", path,"-cp", (path+File.pathSeparator+classpath));
		Iterable<JavaFileObject> sources = (Iterable<JavaFileObject>) fileManager.getJavaFileObjectsFromFiles(Collections.singletonList(f));
		JavacTask task2 = (JavacTask) compiler.getTask(null, fileManager, diagnostics, compileOptions, null, sources);
		boolean ok = task2.call();
//		Iterable<? extends JavaFileObject> fileObjects = task1.generate();
//		for (JavaFileObject aFile : fileObjects)
//		{
//			classFileLocation = aFile.toUri().getPath().toString();
//		}

		//printErrors();
		DiagnosticCollector<JavaFileObject> collector = new DiagnosticCollector<>();
		compiler.getTask(null, fileManager,
				collector, null, null, sources).call();
		for (Diagnostic<? extends JavaFileObject> d : collector.getDiagnostics()) {
			System.out.print("line " + d.getLineNumber() + ": " + d.getMessage(Locale.US));
			System.out.println();
		}
		return diagnostics.getDiagnostics().size() == 0;
	}

	public boolean isDeclaration(ArrayList<File> files, String path) throws IOException
	{
		File f = new File(files.get((files.size() - 1)).toString());
		String classpath = System.getProperty("java.class.path");
		Iterable<String> compileOptions = Arrays.asList("-d", path,"-cp", (path+File.pathSeparator+classpath));
		Iterable<JavaFileObject> sources = (Iterable<JavaFileObject>) fileManager.getJavaFileObjectsFromFiles(Collections.singletonList(f));
		JavacTask task1 = (JavacTask) compiler.getTask(null, fileManager, diagnostics, compileOptions, null, sources);


		//Iterable<? extends JavaFileObject> fileObjects = task2.generate();

//		for (JavaFileObject aFile : fileObjects)
//		{
//			classFileLocation = aFile.toUri().getPath().toString();
//		}
		task1.parse();
		//boolean ok = task1.call();
		//printErrors();
//		DiagnosticCollector<JavaFileObject> collector = new DiagnosticCollector<>();
//		compiler.getTask(null, fileManager,
//				collector, null, null, sources).call();
//		for (Diagnostic<? extends JavaFileObject> d : collector.getDiagnostics()) {
//			System.out.print("line " + d.getLineNumber() + ": " + d.getMessage(Locale.US));
//			System.out.println();
//		}
		return diagnostics.getDiagnostics().size() == 0;
	}

	public void printErrors(String errorType)
	{
		List<Diagnostic> errors = new ArrayList<>();
		errors.addAll(diagnostics.getDiagnostics());

		for (Diagnostic compilerErrorObject : errors)
		{
			System.out.print("line " + compilerErrorObject.getLineNumber() + ": " + compilerErrorObject.getMessage(Locale.US));
			System.out.println();
		}
	}
}
