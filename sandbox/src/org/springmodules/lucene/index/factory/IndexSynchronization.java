/*
 * Créé le 21 avr. 05
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package org.springmodules.lucene.index.factory;

import org.springmodules.resource.support.ResourceSynchronizationAdapter;

/**
 * Callback for resource cleanup at the end of an use of index read / write.
 */
public class IndexSynchronization extends ResourceSynchronizationAdapter {

	private final IndexHolder indexHolder;

	private final IndexFactory indexFactory;

	public IndexSynchronization(IndexHolder indexHolder, IndexFactory indexFactory) {
		this.indexHolder = indexHolder;
		this.indexFactory = indexFactory;
	}

}
