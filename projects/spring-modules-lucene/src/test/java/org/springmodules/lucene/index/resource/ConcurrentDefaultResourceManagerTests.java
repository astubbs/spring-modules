package org.springmodules.lucene.index.resource;

import org.apache.lucene.store.RAMDirectory;
import org.springmodules.lucene.index.factory.IndexFactory;
import org.springmodules.lucene.index.factory.concurrent.LockIndexFactory;

public class ConcurrentDefaultResourceManagerTests extends DefaultResourceManagerTests {

	protected IndexFactory createIndexFactory(RAMDirectory directory) throws Exception {
		IndexFactory targetIndexFactory = super.createIndexFactory(directory);
		LockIndexFactory indexFactory = new LockIndexFactory();
		indexFactory.setTargetIndexFactory(targetIndexFactory);
		indexFactory.afterPropertiesSet();
		return indexFactory;
	}

}
