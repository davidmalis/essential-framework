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
