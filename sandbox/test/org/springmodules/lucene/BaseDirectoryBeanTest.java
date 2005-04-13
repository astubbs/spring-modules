/* Copyright 2005 Brian McCallister
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springmodules.lucene;

import junit.framework.TestCase;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;
import org.springframework.beans.factory.FactoryBean;

public abstract class BaseDirectoryBeanTest extends TestCase
{

    protected abstract FactoryBean getFactory() throws Exception;

    public void testIsDirectory() throws Exception
    {
        Object d = getFactory().getObject();
        assertTrue("Bean is not a Directory", d instanceof Directory);
    }

    public void testDeclaredAsDirectory() throws Exception
    {
        assertTrue("Not declared to be a Directory",
                   getFactory().getObjectType().isAssignableFrom(Directory.class));
    }

    public void testIsEmpty() throws Exception
    {
        Directory d = (Directory) getFactory().getObject();
        assertFalse(IndexReader.indexExists(d));
    }
}
