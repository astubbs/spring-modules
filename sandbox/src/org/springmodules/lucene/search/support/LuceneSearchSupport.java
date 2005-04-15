/*
 * Créé le 11 avr. 05
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package org.springmodules.lucene.search.support;

import org.apache.lucene.analysis.Analyzer;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springmodules.lucene.search.core.LuceneSearchTemplate;
import org.springmodules.lucene.search.factory.SearcherFactory;

/**
 * @author Thierry Templier
 */
public abstract class LuceneSearchSupport implements InitializingBean {
	private LuceneSearchTemplate template;
	private SearcherFactory searcherFactory;
	private Analyzer analyzer;

	/**
	 * @return
	 */
	public Analyzer getAnalyzer() {
		return analyzer;
	}

	/**
	 * @param analyzer
	 */
	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		if (this.searcherFactory == null)
			throw new BeanInitializationException("directory property required");
		if (this.analyzer == null)
			throw new BeanInitializationException("analyzer property required");        
		this.template=new LuceneSearchTemplate(searcherFactory,analyzer);
	}

	/**
	 * @return
	 */
	public LuceneSearchTemplate getTemplate() {
		return template;
	}

	/**
	 * @param template
	 */
	public void setTemplate(LuceneSearchTemplate template) {
		this.template = template;
	}

	/**
	 * @return
	 */
	public SearcherFactory getSearcherFactory() {
		return searcherFactory;
	}

	/**
	 * @param factory
	 */
	public void setSearcherFactory(SearcherFactory factory) {
		searcherFactory = factory;
	}

}
