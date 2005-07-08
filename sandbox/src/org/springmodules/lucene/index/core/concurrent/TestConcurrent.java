/*
 * Créé le 7 juil. 05
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package org.springmodules.lucene.index.core.concurrent;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springmodules.lucene.index.core.DocumentCreator;
import org.springmodules.lucene.index.core.LuceneIndexTemplate;
import org.springmodules.lucene.search.core.HitExtractor;
import org.springmodules.lucene.search.core.LuceneSearchTemplate;
import org.springmodules.lucene.search.core.QueryCreator;
import org.springmodules.lucene.search.factory.SearcherFactory;

/**
 * @author ttemplier-a
 *
 * Pour changer le modèle de ce commentaire de type généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class TestConcurrent {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx=null;
		try {
			ctx=new ClassPathXmlApplicationContext("/luceneConcurrent.xml");
			LuceneIndexTemplate template=(LuceneIndexTemplate)ctx.getBean("concurrentTemplate");
			template.addDocument(new DocumentCreator() {
				public Document createDocument() throws IOException {
					Document document=new Document();
					document.add(Field.Text("contents", "un texte"));
					return document;
				}
			});
			Thread.sleep(1000);
			LuceneSearchTemplate template1=new LuceneSearchTemplate((SearcherFactory)ctx.getBean("searcherFactory"),new SimpleAnalyzer());
			System.err.println("avant");
			template1.search(new QueryCreator() {
				public Query createQuery(Analyzer analyzer) throws ParseException {
					return new TermQuery(new Term("contents","texte"));
				}
			},new HitExtractor() {
				public Object mapHit(int id, Document document, float score) {
					System.err.println("resultat");
					return null;
				}
			});
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			if( ctx!=null ) {
				ctx.close();
			}
		}
	}
}
