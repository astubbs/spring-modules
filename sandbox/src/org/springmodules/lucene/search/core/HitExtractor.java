/*
 * Créé le 12 avr. 05
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package org.springmodules.lucene.search.core;

import org.apache.lucene.document.Document;

/**
 * @author ttemplier-a
 *
 * Pour changer le modèle de ce commentaire de type généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public interface HitExtractor {
	public Object mapHit(int id,Document document,float score);
}
