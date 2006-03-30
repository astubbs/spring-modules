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

package org.springmodules.lucene.index.object.directory;

import java.io.File;

/**
 * @author Thierry Templier
 */
public interface FileDocumentIndexingListener {
	public void beforeIndexingDirectory(File file);
	public void afterIndexingDirectory(File file);

	public void beforeIndexingFile(File file);
	public void afterIndexingFile(File file);
	public void onErrorIndexingFile(File file,Exception ex);
	public void onNotAvailableHandler(File file);
}
