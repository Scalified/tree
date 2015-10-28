package com.software.shell.tree.util;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static com.software.shell.tree.util.Validator.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author shell
 * @version 1.0.0
 * @since 1.0.0
 */
public class ValidatorTest {

	@Test
	public void testIsNull() throws Exception {
		assertTrue("The input object was expected to be null, but actually was not", isNull(null));
		assertFalse("The input object was not expected to be null, but actually was", isNull(new Object()));
	}

	@Test
	public void testIsNotNull() throws Exception {
		assertTrue("The input object was expected to be null, but actually was not", isNotNull(new Object()));
		assertFalse("The input object was not expected to be null, but actually was", isNotNull(null));
	}

	@Test
	public void testIsAnyNotNull() throws Exception {
		String errorMessageTrue =
				"The specified collection was expected to have at least one not null element, but actually was not";
		assertTrue(errorMessageTrue, isAnyNotNull(Arrays.asList("foo", null)));
		String errorMessageFalse =
				"The specified collection was not expected to have not null elements, but actually was";
		assertFalse(errorMessageFalse, isAnyNotNull(null));
		assertFalse(errorMessageFalse, isAnyNotNull(Collections.emptyList()));
		assertFalse(errorMessageFalse, isAnyNotNull(Arrays.asList(null, null)));
	}

	@Test
	public void testAreAllNulls() throws Exception {
		String errorMessageTrue =
				"The specified collection was expected to have only null elements, but actually was not";
		assertTrue(errorMessageTrue, areAllNulls(null));
		assertTrue(errorMessageTrue, areAllNulls(Collections.emptyList()));
		assertTrue(errorMessageTrue, areAllNulls(Arrays.asList(null, null)));
		String errorMessageFalse =
				"The specified collection was not expected to have not null elements, but actually was";
		assertFalse(errorMessageFalse, areAllNulls(Arrays.asList("foo", null)));
	}

}
