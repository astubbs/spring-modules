/*
 * Créé le 21 avr. 05
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package org.springmodules.lucene.index.core;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.springmodules.lucene.index.factory.IndexFactory;

/**
 * @author ttemplier
 *
 * Pour changer le modèle de ce commentaire de type généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class ResourceAwareIndexFactoryProxy implements IndexFactory {

	private IndexFactory target;

	/* (non-Javadoc)
	 * @see org.springmodules.lucene.index.factory.IndexFactory#getIndexReader()
	 */
	public IndexReader getIndexReader() {
		// TODO Raccord de méthode auto-généré
		return null;
	}

	/* (non-Javadoc)
	 * @see org.springmodules.lucene.index.factory.IndexFactory#getIndexWriter()
	 */
	public IndexWriter getIndexWriter() {
		// TODO Raccord de méthode auto-généré
		return null;
	}

	/**
	 * @return
	 */
	public IndexFactory getTargetIndexFactory() {
		return target;
	}

	/**
	 * @param factory
	 */
	public void setTargetIndexFactory(IndexFactory factory) {
		target = factory;
	}

}
