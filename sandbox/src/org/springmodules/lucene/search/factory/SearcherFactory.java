/*
 * Créé le 12 avr. 05
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package org.springmodules.lucene.search.factory;

import java.io.IOException;

import org.apache.lucene.search.Searcher;

/**
 * @author ttemplier
 *
 * Pour changer le modèle de ce commentaire de type généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public interface SearcherFactory {
	public Searcher getSearcher() throws IOException;
}
