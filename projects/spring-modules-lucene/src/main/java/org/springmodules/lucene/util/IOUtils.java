/*
 * Copyright 2002-2005 the original author or authors.
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

package org.springmodules.lucene.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.springframework.core.io.Resource;

/**
 * IO utilities class to manage IO resources.
 * 
 * @author Thierry Templier
 */
public abstract class IOUtils {

	/**
	 * This method closes the InputStream parameter.
	 * 
	 * @param inputStream the InputStream to close
	 */
	public static void closeInputStream(InputStream inputStream) {
		try {
			if( inputStream!=null ) {
				inputStream.close();
			}
		} catch(Exception ex) { }
	}

	public static Properties loadPropertiesFromResource(Resource resource) {
		Properties properties=new Properties();
		InputStream inputStream=null;
		try {
			inputStream=resource.getInputStream();
			properties.load(inputStream);
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			IOUtils.closeInputStream(inputStream);
		}
		return properties;
	}
	
	public static String getContents(InputStream inputStream) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		StringBuffer contents = new StringBuffer();
		String line = null;
		while( (line = reader.readLine())!=null ) {
			contents.append(line);
			contents.append("\n");
		}
		return contents.toString();
	}
}
