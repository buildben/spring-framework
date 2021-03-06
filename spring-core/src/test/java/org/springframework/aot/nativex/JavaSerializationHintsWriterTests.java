/*
 * Copyright 2002-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.aot.nativex;

import java.io.StringWriter;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import org.springframework.aot.hint.JavaSerializationHints;
import org.springframework.aot.hint.TypeReference;
import org.springframework.core.env.Environment;

/**
 * Tests for {@link JavaSerializationHintsWriter}.
 *
 * @author Sebastien Deleuze
 */
public class JavaSerializationHintsWriterTests {

	@Test
	void empty() throws JSONException {
		JavaSerializationHints hints = new JavaSerializationHints();
		assertEquals("[]", hints);
	}

	@Test
	void one() throws JSONException {
		JavaSerializationHints hints = new JavaSerializationHints().registerType(TypeReference.of(String.class));
		assertEquals("""
				[
					{ "name": "java.lang.String" }
				]""", hints);
	}

	@Test
	void two() throws JSONException {
		JavaSerializationHints hints = new JavaSerializationHints()
				.registerType(TypeReference.of(String.class))
				.registerType(TypeReference.of(Environment.class));
		assertEquals("""
				[
					{ "name": "java.lang.String" },
					{ "name": "org.springframework.core.env.Environment" }
				]""", hints);
	}

	private void assertEquals(String expectedString, JavaSerializationHints hints) throws JSONException {
		StringWriter out = new StringWriter();
		BasicJsonWriter writer = new BasicJsonWriter(out, "\t");
		JavaSerializationHintsWriter.INSTANCE.write(writer, hints);
		JSONAssert.assertEquals(expectedString, out.toString(), JSONCompareMode.NON_EXTENSIBLE);
	}

}
