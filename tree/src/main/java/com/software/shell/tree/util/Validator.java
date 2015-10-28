package com.software.shell.tree.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Used for input validation
 *
 * @author shell
 * @version 1.0.0
 * @since 1.0.0
 */
public final class Validator {

	/**
	 * Checks whether the input object is {@code null}
	 * <pre>
	 *     Validator.isNull(null)           = true
	 *     Validator.isNull(new Object())   = false
	 * </pre>
	 *
	 * @param object input object to check
	 * @param <T> type of the input object
	 * @return {@code true} if the input object is {@code null};
	 *         {@code false} otherwise
	 */
	public static <T> boolean isNull(T object) {
		return object == null;
	}

	/**
	 * Checks whether the input object is not {@code null}
	 * <pre>
	 *     Validator.isNotNull(new Object)  = true
	 *     Validator.isNotNull(null)        = false
	 * </pre>
	 *
	 * @param object input object to check
	 * @param <T> type of the input object
	 * @return {@code true} if the input object is not {@code null};
	 *         {@code false} otherwise
	 */
	public static <T> boolean isNotNull(T object) {
		return !isNull(object);
	}

	/**
	 * Checks whether there is at least one not {@code null} element within
	 * the input collection
	 * <pre>
	 *     Validator.isAnyNotNull(Arrays.asList("foo", null))   = true
	 *     Validator.isAnyNotNull(null)                         = false
	 *     Validator.isAnyNotNull(Collections.emptyList())      = false
	 *     Validator.isAnyNotNull(Arrays.asList(null, null))    = false
	 * </pre>
	 *
	 * @param collection input collection to check
	 * @param <T> type of the data, which parametrises collection
	 * @return {@code true} if there is at least one not {@code null} element within
	 *         the input collection; {@code false} otherwise
	 */
	public static <T> boolean isAnyNotNull(Collection<T> collection) {
		if (collection == null || collection.isEmpty()) {
			return false;
		}
		for (T item : collection) {
			if (isNotNull(item)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks whether the specified collection is @{code null}, empty or if
	 * all of its elements are {@code null}
	 * <pre>
	 *     Validator.areAllNulls(null)                          = true
	 *     Validator.areAllNulls(Collections.emptyList())       = true
	 *     Validator.areAllNulls(Arrays.asList(null, null))     = true
	 *     Validator.areAllNulls(Arrays.asList("foo", null))    = false
	 * </pre>
	 *
	 * @param collection input collection to check
	 * @param <T> type of the data, which parametrises collection
	 * @return {@code true} if the specified collection is {@code null}, empty
	 *         or if all of its elements are {@code null}; {@code false} otherwise
	 */
	public static <T> boolean areAllNulls(Collection<T> collection) {
		return !isAnyNotNull(collection);
	}

}
