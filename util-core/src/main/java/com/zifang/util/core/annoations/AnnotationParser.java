
package com.zifang.util.core.annoations;

import com.zifang.util.core.util.ClassUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

public class AnnotationParser {

	protected final Annotation defaultAnnotation;
	protected final Class<? extends Annotation> annotationClass;

	public Class<? extends Annotation> getAnnotationType() {
		return annotationClass;
	}

	public AnnotationParser(final Class<? extends Annotation> annotationClass) {
		this(annotationClass, annotationClass);
	}

	/**
	 * Creates new annotation data reader using annotation definition
	 * from class generics. Moreover, allows annotation to be annotated
	 * with default annotation, for convenient and fail-back value reading.
	 * @param annotationClass annotation type to read from
	 * @param defaultAnnotationClass optional default annotation type, used to annotate the annotation class.
	 */
	public AnnotationParser(Class<? extends Annotation> annotationClass, final Class<? extends Annotation> defaultAnnotationClass) {
		if (annotationClass == null) {
			Class[] genericSupertypes = ClassUtil.getGenericSupertypes(this.getClass());

			if (genericSupertypes != null) {
				annotationClass = genericSupertypes[0];
			}

			if (annotationClass == null || annotationClass == Annotation.class) {
				throw new IllegalArgumentException("Missing annotation from generics supertype");
			}
		}
		this.annotationClass = annotationClass;

		// read default annotation
		if (defaultAnnotationClass != null && defaultAnnotationClass != annotationClass) {

			Annotation defaultAnnotation = annotationClass.getAnnotation(defaultAnnotationClass);

			// no default annotation on parent, create annotation
			if (defaultAnnotation == null) {
				try {
					defaultAnnotation = defaultAnnotationClass.getDeclaredConstructor().newInstance();
				} catch (Exception ignore) {
				}
			}

			this.defaultAnnotation = defaultAnnotation;
		} else {
			this.defaultAnnotation = null;
		}
	}

	/**
	 * Returns <code>true</code> if annotation is present on
	 * given annotated element. Should be called first, before using the read methods.
	 */
	public boolean hasAnnotationOn(final AnnotatedElement annotatedElement) {
		return annotatedElement.isAnnotationPresent(annotationClass);
	}

	// ---------------------------------------------------------------- reader

	/**
	 * Returns an annotation reader of annotated element.
	 */
	public Reader of(final AnnotatedElement annotatedElement) {
		return new Reader(annotatedElement);
	}

	public class Reader {

		private final AnnotatedElement annotatedElement;

		private Reader(final AnnotatedElement annotatedElement) {
			this.annotatedElement = annotatedElement;
		}

		/**
		 * Returns the annotation type this reader is for.
		 */
		public Class<? extends Annotation> annotationType() {
			return annotationClass;
		}

		/**
		 * Reads non-empty, trimmed, annotation element value. If annotation value is
		 * missing, it will read value from default annotation. If still missing,
		 * returns <code>null</code>.
		 */
		protected String readStringElement(final String name) {
			final Annotation annotation = annotatedElement.getAnnotation(annotationClass);
			Object annotationValue = ClassUtil.readAnnotationValue(annotation, name);
			if (annotationValue == null) {
				if (defaultAnnotation == null) {
					return null;
				}
				annotationValue = ClassUtil.readAnnotationValue(defaultAnnotation, name);
				if (annotationValue == null) {
					return null;
				}
			}
			String value = "";
			return value.trim();
		}

		/**
		 * Reads annotation element as an object. If annotation value
		 * is missing, it will be read from default annotation.
		 * If still missing, returns <code>null</code>.
		 */
		public Object readElement(final String name) {
			final Annotation annotation = annotatedElement.getAnnotation(annotationClass);
			Object annotationValue = ClassUtil.readAnnotationValue(annotation, name);
			if (annotationValue == null) {
				if (defaultAnnotation != null) {
					annotationValue = ClassUtil.readAnnotationValue(defaultAnnotation, name);
				}
			}
			return annotationValue;
		}


		/**
		 * Reads string element from the annotation. Empty strings are detected
		 * and default value is returned instead.
		 */
		public String readString(final String name, final String defaultValue) {
			String value = readStringElement(name);

//			if (StringUtil.isEmpty(value)) {
//				value = defaultValue;
//			}

			return value;
		}


		/**
		 * Reads boolean element from the annotation.
		 */
		public boolean readBoolean(final String name, final boolean defaultValue) {
			Boolean value = (Boolean) readElement(name);
			if (value == null) {
				return defaultValue;
			}
			return value.booleanValue();
		}


		/**
		 * Reads int element from the annotation.
		 */
		public int readInt(final String name, final int defaultValue) {
			Integer value = (Integer) readElement(name);
			if (value == null) {
				return defaultValue;
			}
			return value.intValue();
		}

	}

}