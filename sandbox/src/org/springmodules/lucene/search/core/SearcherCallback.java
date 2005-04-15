/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package org.springmodules.lucene.search.core;

import java.io.IOException;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Searcher;

public interface SearcherCallback {

    public Object doWithSearcher(Searcher searcher) throws IOException,ParseException;
}
