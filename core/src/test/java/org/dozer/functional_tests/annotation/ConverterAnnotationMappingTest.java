/**
 * Copyright 2005-2014 Dozer Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dozer.functional_tests.annotation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dozer.Converter;
import org.dozer.DozerConverter;
import org.dozer.Mapping;
import org.dozer.functional_tests.AbstractFunctionalTest;
import org.junit.Test;

/**
 * 
 * @author Andrzej Goławski
 * 
 */
public class ConverterAnnotationMappingTest extends AbstractFunctionalTest {

	@Test
	public void should_map_Integer_to_String_using_given_converter() {

		// given
		SomeNumbers someNumbers = new SomeNumbers();
		someNumbers.setFirstInteger(10);
		someNumbers.setSecondInteger(15);

		// when
		SomeTexts someTexts = mapper.map(someNumbers, SomeTexts.class);

		// then
		assertThat(someTexts.getFirstText(), equalTo("LowInteger[10]"));
		assertThat(someTexts.getSecondText(), equalTo("LowInteger[15]"));
	}

	@Test
	public void should_map_String_to_Integer_using_given_converter() {

		// given
		SomeTexts someTexts = new SomeTexts();
		someTexts.setFirstText("LowInteger[20]");
		someTexts.setSecondText("LowInteger[25]");

		// when
		SomeNumbers someText = mapper.map(someTexts, SomeNumbers.class);

		// then
		assertThat(someText.getFirstInteger(), equalTo(20));
		assertThat(someText.getSecondInteger(), equalTo(25));
	}

	public static class SomeNumbers {

		private Integer firstInteger;

		private Integer secondInteger;

		public Integer getFirstInteger() {
			return firstInteger;
		}

		public void setFirstInteger(Integer firstInteger) {
			this.firstInteger = firstInteger;
		}

		public Integer getSecondInteger() {
			return secondInteger;
		}

		public void setSecondInteger(Integer secondInteger) {
			this.secondInteger = secondInteger;
		}

	}

	public static class SomeTexts {

		@Mapping("firstInteger")
		@Converter(IntegerToStringConverter.class)
		private String firstText;

		private String secondText;

		public String getFirstText() {
			return firstText;
		}

		public void setFirstText(String firstText) {
			this.firstText = firstText;
		}

		@Mapping("secondInteger")
		@Converter(IntegerToStringConverter.class)
		public String getSecondText() {
			return secondText;
		}

		public void setSecondText(String secondText) {
			this.secondText = secondText;
		}
	}

	public static class IntegerToStringConverter extends DozerConverter<Integer, String> {

		public IntegerToStringConverter() {
			super(Integer.class, String.class);

		}

		@Override
		public String convertTo(Integer source, String destination) {
			return "LowInteger[" + source + "]";
		}

		@Override
		public Integer convertFrom(String source, Integer destination) {
			return getNumberBetweenBrackets(source);
		}
	}

	private static Integer getNumberBetweenBrackets(String number) {
		Matcher matcher = Pattern.compile("\\[(.*?)\\]").matcher(number);
		matcher.find();
		return new Integer(matcher.group(1));
	}

}
