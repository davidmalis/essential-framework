/* MIT License
*
* Copyright (c) 2019 David Mališ
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
package org.essentialframework.core.utility;

import java.lang.annotation.Annotation;
import java.util.List;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

/**
 * Helper class ClassGraph API operations.
 * 
 * @author dmalis
 * @version 1.0
 */
public class ClassGraphUtils {
	
	/**
	 * TODO
	 * @param annotation
	 * @param scanPackages
	 * @return
	 */
	 public static List<Class<?>> getAnnotated(final Class<? extends Annotation> annotation, String... scanPackages) {
		 Assert.notNull(annotation, "Annotation must not be null");
		 final ClassGraph classGraph = new ClassGraph()
				.enableAnnotationInfo()
				.enableClassInfo()
				.whitelistPackages(scanPackages);
		 
		 try( ScanResult scan = classGraph.scan() ){
			 
			 ClassInfoList classInfo = scan.getClassesWithAnnotation(annotation.getName());
			 return classInfo.loadClasses();
		 }
	 }
	 
	 /**
	  * TODO
	  * @param iface
	  * @param scanPackages
	  * @return
	  */
	 public static List<Class<?>> getSubclasses(final Class<?> iface, String... scanPackages){
		 Assert.notNull(iface, "Parent class must not be null");
		 final ClassGraph classGraph = new ClassGraph()
				.enableAnnotationInfo()
				.enableClassInfo()
				.whitelistPackages(scanPackages);
		 
		 try( ScanResult scan = classGraph.scan() ){
			 
			 ClassInfoList classInfo = scan.getClassesImplementing(iface.getName());
			 return classInfo.loadClasses();
			 
		 }
			
	 }
	
	/**
	 * No instantiation.
	 */
	private ClassGraphUtils() {}

}
