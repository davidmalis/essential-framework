/* MIT License
*
* Copyright (c) 2018 David Mališ
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
* 
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.

* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*/
package org.essentialframework.apt;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * This class can be used to read results of {@link IndexingProcessor} processing.
 * Contains convenient methods to search through index files.
 * 
 * @author David Malis
 * @since 1.0.0 
 * @see IndexingProcessor
 */
public class Index {
	
	/**
	 * No instantiation.
	 */
	private Index(){}
	
	/**
	 * Returns classes that subclass specified type using current thread's context 
	 * {@link ClassLoader} instance to read their names from a dedicated index file 
	 * and load them.
	 * @param superclass 
	 * @return {@link Iterable}<{@link Class}>
	 */
	public static Iterable<Class<?>> getSubclassesOf(final Class<?> superclass){
		return getSubclassesOf(superclass, Thread.currentThread().getContextClassLoader());
	}
	
	/**
	 * Returns classes that subclass specified type using {@link ClassLoader} instance
	 * to read their names from a dedicated index file and load them.
	 * @param superclass
	 * @param classLoader
	 * @return {@link Iterable}<{@link Class}>
	 */
	public static Iterable<Class<?>> getSubclassesOf(final Class<?> superclass, 
		final ClassLoader classLoader){
		
		final Set<Class<?>> classes = new HashSet<>();
		loadClasses(classLoader, classes, 
			getSubclassNames(superclass, classLoader));
		return classes;
		
	}
	
	/**
	 * Returns classes annotated with specified annotation using current thread's context 
	 * {@link ClassLoader} instance to read their names from a dedicated index file and 
	 * load them. 
	 * @param annotation
	 * @return {@link Iterable}<{@link Class}>
	 */
	public static Iterable<Class<?>> getAnnotated(final Class<? extends Annotation> annotation){
		return getAnnotated(annotation, Thread.currentThread().getContextClassLoader());
	}
	
	/**
	 * Returns classes annotated with specified annotation using {@link ClassLoader} instance
	 * to read their names from a dedicated index file and loading them. 
	 * @param annotation
	 * @param classLoader
	 * @return {@link Iterable}<{@link Class}>
	 */
	public static Iterable<Class<?>> getAnnotated(final Class<? extends Annotation> annotation, 
		final ClassLoader classLoader){
		
		final Set<Class<?>> classes = new HashSet<>();
		loadClasses(classLoader, classes, 
			getAnnotatedNames(annotation, classLoader));
		return classes;
	}
	
	/**
	 * Returns names of classes annotated with specified annotation using current thread's context 
	 * {@link ClassLoader} instance to read them from a dedicated index file.
	 * @param annotation
	 * @return {@link Iterable}<{@link String}>
	 */
	public static Iterable<String> getAnnotatedNames(final Class<? extends Annotation> annotation) {
		return getAnnotatedNames(annotation, Thread.currentThread().getContextClassLoader());
	}
	
	/**
	 * Returns names of classes annotated with specified annotation using {@link ClassLoader} instance
	 * to read them from a dedicated index file.
	 * @param annotation
	 * @param classLoader
	 * @return {@link Iterable}<{@link String}>
	 */
	public static Iterable<String> getAnnotatedNames(final Class<? extends Annotation> annotation, 
		final ClassLoader classLoader) {
		
		if(classLoader != null && annotation != null) {
			return read(classLoader, IndexFilePrefix.locationFor(
					IndexFilePrefix.ANNOTATION_INDEXES, annotation.getCanonicalName()));
		}
		throw new IllegalArgumentException("Annotation and classLoader arguments must not be null");
	}
	
	/**
	 * Returns names of subclasses of a specified class using current thread's context 
	 * {@link ClassLoader} instance to read them from a dedicated index file.
	 * @param superClass
	 * @return {@link Iterable}<{@link String}>
	 */
	public static Iterable<String> getSubclassNames(final Class<?> superClass) {
		return getSubclassNames(superClass, Thread.currentThread().getContextClassLoader());
	}
	
	/**
	 * Returns names of subclasses of a specified class using {@link ClassLoader} instance 
	 * to read them from a dedicated index file.
	 * @param superClass
	 * @param classLoader 
	 * @return {@link Iterable}<{@link String}>
	 */
	public static Iterable<String> getSubclassNames(final Class<?> superClass, 
		final ClassLoader classLoader) {
		
		if(classLoader != null && superClass != null) {
			return read(classLoader, IndexFilePrefix.locationFor(
				IndexFilePrefix.SUPERCLASS_INDEXES, superClass.getCanonicalName()));
		}
		throw new IllegalArgumentException("Annotation and classLoader arguments must not be null");
	}
	
	/**
	 * Uses a {@link ClassLoader} instance to read a file, line by line at the specified location.
	 * @param classLoader
	 * @param file
	 * @return {@link Iterable}<{@link String}>
	 * @throws RuntimeException
	 */
	private static Iterable<String> read(final ClassLoader classLoader, final String file) {
		Set<String> entries = new HashSet<>();

		try {
			final Enumeration<URL> urls = classLoader.getResources(file);

			while(urls.hasMoreElements()) {
				try( BufferedReader br = new BufferedReader(new InputStreamReader(
					 urls.nextElement().openStream(), StandardCharsets.UTF_8.name()))) {
					
					for( String l ; (l=br.readLine())!= null ; ) {
						l.trim();
						if(!l.isEmpty()) {
							entries.add(l);
						}
					}
				
				}
			}
		} catch(IOException e) {
			throw new RuntimeException("Cannot read class index file", e);
		}	
		return entries;
	}
	/**
	 * Uses a {@link ClassLoader} instance to load classes by class names in specified 
	 * {@link Iterable}<{@link String}> of names read from an index.
	 * @param classLoader
	 * @param classes
	 * @param entries
	 */
	private static void loadClasses(final ClassLoader classLoader,
		final Set<Class<?>> classes, final Iterable<String> entries) {
		
		for(String entry : entries) {
			try {
				classes.add(classLoader.loadClass(entry));
			} catch(ClassNotFoundException e) {
				continue;
			}
		}
		
	}

}
