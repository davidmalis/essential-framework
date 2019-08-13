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
package org.essentialframework.web;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.essentialframework.core.utility.Assert;

public class PathVariableResolver {

	private List<String> variableNames;

	private Pattern pattern;
	
	private PathVariableResolver(String declaredUrl, List<String> variableNames, Pattern pattern) {
		this.pattern = Pattern.compile(pattern.toString());
		this.variableNames = variableNames;
	}
	
	public boolean matches(String uri) {
		if (uri == null) {
			return false;
		}
		Matcher matcher = this.pattern.matcher(uri);
		return matcher.matches();
	}
	
	public Map<String, String> resolve(String uri){
		Assert.notNull(uri, "'uri' must not be null");
		Map<String, String> result = new LinkedHashMap<>(this.variableNames.size());
		Matcher matcher = this.pattern.matcher(uri);
		if (matcher.find()) {
			for (int i = 1; i <= matcher.groupCount(); i++) {
				String name = this.variableNames.get(i - 1);
				String value = matcher.group(i);
				result.put(name, value);
			}
		}
		return result;
	}
	
	
	public static PathVariableResolver parse(String url) {
		int level = 0;
		List<String> variableNames = new ArrayList<>();
		StringBuilder pattern = new StringBuilder();
		StringBuilder builder = new StringBuilder();
		for (int i = 0 ; i < url.length(); i++) {
			char c = url.charAt(i);
			if (c == '{') {
				level++;
				if (level == 1) {
					pattern.append(Pattern.quote(builder.toString()));
					builder = new StringBuilder();
					continue;
				}
			}
			else if (c == '}') {
				level--;
				if (level == 0) {
					pattern.append("([^/]*)");
					variableNames.add(builder.toString());
					builder = new StringBuilder();
					continue;
				}
			}
			builder.append(c);
		}
		if (builder.length() > 0) {
			pattern.append(Pattern.quote(builder.toString()));
		}
		
		return new PathVariableResolver(url, variableNames, Pattern.compile(pattern.toString()));
	}

}
