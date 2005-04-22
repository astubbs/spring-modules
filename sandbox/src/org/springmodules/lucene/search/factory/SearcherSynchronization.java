/*
 * Créé le 21 avr. 05
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package org.springmodules.lucene.search.factory;

import org.springmodules.resource.support.ResourceSynchronizationAdapter;
import org.springmodules.resource.support.ResourceSynchronizationManager;

/**
 * Callback for resource cleanup at the end of an use of index search.
 */
public class SearcherSynchronization extends ResourceSynchronizationAdapter {

	private final SearcherHolder searcherHolder;

	private final SearcherFactory searcherFactory;

	public SearcherSynchronization(SearcherHolder searcherHolder, SearcherFactory searcherFactory) {
		this.searcherHolder = searcherHolder;
		this.searcherFactory = searcherFactory;
	}

}
