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
import static org.essentialframework.apt.IndexFilePrefix.ANNOTATION_INDEXES;
import static org.essentialframework.apt.IndexFilePrefix.SUPERCLASS_INDEXES;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementScanner8;
import javax.tools.Diagnostic;

/**
 * This processor is registered and started at compile-time through {@code ServiceLoader}. 
 * See {@link #process(Set, RoundEnvironment)} method for details on how it is operating. 
 * In general, it is used to create index files on various component types on the classpath 
 * which can be later used for quick class referencing without runtime scanning etc.
 * 
 * @author David Malis
 * @since 1.0.0
 * @see #process(Set, RoundEnvironment)
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class IndexingProcessor extends AbstractIndexFileProcessor {
	
	protected static final Class<? extends Annotation> AnnotationMARKER = IndexAnnotated.class;
	protected static final Class<? extends Annotation> SuperclassMARKER = IndexSubclass.class;
	
	protected final Map<String, Set<String>> annotationMap = new HashMap<>();
	
	protected final Map<String, Set<String>> subclassMap = new HashMap<>();
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<String> getSupportedAnnotationTypes() {
		return Collections.singleton("*");
	}
	/**
	 * {@inheritDoc}}
	 */
	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.RELEASE_8;
	}
	
	/**
	 * This processor goes through all root elements and process them
	 * while examining their annotations. It is used to create index 
	 * files of types annotated by targeted annotations at compile-time
	 * for later retrieval at runtime.
	 * @param annotations Set which is always empty in this processor, we ignore it
	 * @param roundEnv {@code RoundEnvironment} in which we process types
	 */
	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		
		try {
			for(Element element : roundEnv.getRootElements()) {
				element.accept(new ElementScanner8<Void, Void>(){
					@Override
					public Void visitType(TypeElement type, Void o) {
						processAnnotationsOf(type);
						
						//..
						return super.visitType(type, o);
					}
				}, null);
			}
			
			if (roundEnv.processingOver()) {
				generateIndexFiles(ANNOTATION_INDEXES, annotationMap);
				generateIndexFiles(SUPERCLASS_INDEXES, subclassMap);
				return true;
			}
			
		} catch (Throwable e) {
			super.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, 
				"Error while creating annotation and subclass indexes: " + e.getMessage());
		}
		return false;
	}
	
	/**
	 * Returns a full qualified names for top level or member elements.
	 * For all other returns {@code null}.
	 * @param tE element to get a name from
	 * @return full qualified name as a {@code String}
	 */
	private static String getFullQualifiedName(final TypeElement element) {
		switch (element.getNestingKind()) {
			case TOP_LEVEL:
				return element.getQualifiedName().toString();
			case MEMBER:
				final Element outer = element.getEnclosingElement();
				if (outer instanceof TypeElement) {
					final String outerName = getFullQualifiedName(((TypeElement) outer)).toString();
					if (outerName != null) {
						return outerName.toString() + '$' + element.getSimpleName().toString();
					}
				}
				return null;
			case ANONYMOUS:
			case LOCAL:
			default:
				return null;
		}
	}
	
	/**
	 * This method adds specified element's full qualified names 
	 * in an argument map while taking care of instantiating Set 
	 * or just adding to it.
	 * 
	 * @param map map where to put key-value pairs
	 * @param keyElement 
	 * @param valueElement
	 */
	protected static void putElement(Map<String, Set<String>> map, 
		TypeElement keyElement, TypeElement valueElement) {
		
		Set<String> set = map.get(keyElement.getQualifiedName().toString());
		if(set == null) {
			set = new HashSet<>();
			map.put(keyElement.getQualifiedName().toString(), set);
		}
		set.add(getFullQualifiedName(valueElement));
	}
	
	/**
	 * Checks if type element is annotated with specified annotation.
	 * @param typeElement
	 * @param annotationType 
	 * @return {@code true/false}
	 */
	private static boolean hasAnnotation(final TypeElement typeElement,
			final Class<? extends Annotation> annotationType) {
		return typeElement.getAnnotation(annotationType) != null;
	}
	
	/**
	 * Returns a list of direct supertypes of a specified type element.
	 * @param typeElement
	 * @return list of supertypes
	 */
	private List<? extends TypeMirror> getDirectSupertypes(final TypeElement typeElement) {
		return super.processingEnv
			   .getTypeUtils()
			   .directSupertypes(typeElement.asType());

	}

	/**
	 * This method is looking through annotation mirrors of a type
	 * and while examining each. If annotation is marked for indexing
	 * with {@link IndexAnnotated}, the type is put in a Set under the 
	 * corresponding annotation key in a annotation-to-setoftypes map. Which
	 * is later used to create index files.
	 * @param typeElement element to process annotations for
	 */
	protected void processAnnotationsOf(final TypeElement typeElement) {
		for(AnnotationMirror mirror : typeElement.getAnnotationMirrors()) {
			final TypeElement annotation = 
				(TypeElement) mirror.getAnnotationType().asElement();
			
			//if examained annotation of a type is marked for indexing
			//(annotated with IndexAnnotated), create index for this type
			if(hasAnnotation(annotation, AnnotationMARKER)) {
				putElement(annotationMap, annotation, typeElement);
			}
		}
		for(TypeMirror mirror : getDirectSupertypes(typeElement)){
			if(TypeKind.DECLARED != mirror.getKind()) {
				continue;
			}
			TypeElement superType = (TypeElement)((DeclaredType) mirror).asElement();
			if(hasAnnotation(superType, SuperclassMARKER)) {
				putElement(subclassMap, superType, typeElement);
			}
		}
	}

}

