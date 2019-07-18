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
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.StandardLocation;

/**
 * Abstract base class for the annotation based indexing of classes.
 * Concrete implementation of this class should be registered via 
 * {@link ServiceLoader} mechanism.
 *  
 * @author David Malis
 * @since 1.0.0
 * @see AbstractProcessor
 */
abstract class AbstractIndexFileProcessor 
	extends AbstractProcessor {
	
	static final Charset UTF_8 = Charset.forName("UTF-8");

	/**
	 * Opens an {@link OutputStream} on the specified path
	 * using {@link Filer} 
	 * @param path
	 * @return {@link OutputStream}
	 * @throws IOException - if something goes wrong
	 */
	private OutputStream createFile(final String path) 
		throws IOException{
		return super.processingEnv
			   .getFiler()
			   .createResource(StandardLocation.CLASS_OUTPUT, "", path)
			   .openOutputStream();
	}
	
	/**
	 * Opens an InputStream on the specified path
	 * using {@link Filer}.
	 * @param path
	 * @return {@link InputStream}
	 * @throws IOException - if something goes wrong
	 */
	private InputStream openFile(final String path) 
		throws IOException{
		return super.processingEnv
			   .getFiler()
			   .getResource(StandardLocation.CLASS_OUTPUT, "", path)
			   .openInputStream();
	}
	
	/**
	 * Generates index files based on specified index map entries. Each
	 * key is a separate file with {@link Set} of values as lines in that file.
	 * Name of files are equal to key values. If a file already exists, this 
	 * method attempts to read it and add additional non-duplicate lines to it.
	 * @param prefix - location prefix
	 * @param indexMap - index files and contents map
	 * @throws IOException - if something goes wrong
	 */
	protected void generateIndexFiles(final IndexFilePrefix prefix, 
		final Map<String, Set<String>> indexMap) throws IOException {
		
		for(String key : indexMap.keySet()) {
			
			String location = IndexFilePrefix.locationFor(prefix, key);
			Set<String> all = new HashSet<>();
			
			try( InputStream is = openFile(location) ){
				all.addAll(read(is));
	        } catch (Exception e) {
	        	//we skip reading old index file
	        	//(if it existed at all)
	        	
	        	super.processingEnv.getMessager().printMessage(
					Diagnostic.Kind.NOTE, "[org.essentialframework.apt.AbstractIndexProcessor] Cannot read old index file.");
	        }
			
			Set<String> entries = new HashSet<>(indexMap.get(key));
			if (all.containsAll(entries)) {
				return;
		    }
			all.addAll(entries);

			try( OutputStream os = createFile(location) ){
				write(os, all);
			} catch (IOException e) {
				super.processingEnv.getMessager().printMessage(
					Diagnostic.Kind.ERROR, "[org.essentialframework.apt.AbstractIndexProcessor] Error while generating index files");
				
				throw e;
			}
		}
	}
	
	/**
	 * Writes lines from a specified {@link Collection} to an opened
	 * {@link OutputStream}.
	 * @param os - {@link OutputStream} to be written at
	 * @param lines - {@link Collection} of lines
	 * @throws IOException - if something goes wrong
	 */
	private static void write(OutputStream os, 
		Collection<String> lines) throws IOException {
		
		BufferedWriter w = new BufferedWriter(
				new OutputStreamWriter(os, UTF_8));
		
		for(String line : lines) {
			w.write(line);
			w.newLine();
		}
		w.flush();
	}
	
	/**
	 * Reads lines from an opened {@link InputStream}.
	 * @param is - {@link InputStream} to be read
	 * @return {@link Set}<{@link String}> - lines read
	 * @throws IOException - if something goes wrong
	 */
	private static Set<String> read(InputStream is) 
		throws IOException{
		Set<String> lines = new HashSet<>();
		
		try(BufferedReader r = new BufferedReader(
				new InputStreamReader(is, UTF_8))) {
			
			for(String l;(l=r.readLine())!= null;) {
				l.trim();
				if(!l.isEmpty()) {
					lines.add(l);
				}
			}
			return lines;
		}
	}
	
	/**
	 * @see AbstractProcessor#process(Set, RoundEnvironment)
	 */
	public abstract boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv);

}
