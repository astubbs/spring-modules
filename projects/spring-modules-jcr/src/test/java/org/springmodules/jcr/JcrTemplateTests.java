/**
 * Created on Sep 12, 2005
 *
 * $Id: JcrTemplateTests.java,v 1.1 2005/12/20 17:38:26 costin Exp $
 * $Revision: 1.1 $
 */
package org.springmodules.jcr;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.jcr.AccessDeniedException;
import javax.jcr.InvalidItemStateException;
import javax.jcr.Item;
import javax.jcr.ItemExistsException;
import javax.jcr.ItemNotFoundException;
import javax.jcr.NamespaceException;
import javax.jcr.NoSuchWorkspaceException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.PropertyIterator;
import javax.jcr.ReferentialIntegrityException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.ValueFactory;
import javax.jcr.ValueFormatException;
import javax.jcr.Workspace;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.query.InvalidQueryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.jcr.version.VersionException;

import junit.framework.TestCase;

import org.easymock.MockControl;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springmodules.jcr.support.ListSessionHolderProviderManager;
import org.xml.sax.ContentHandler;

/**
 * @author Costin Leau
 * 
 */
public class JcrTemplateTests extends TestCase {

	private MockControl sfControl;

	private SessionFactory sf;

	private MockControl sessionControl;
	private MockControl repositoryControl;
	private Repository repository;
	private Session session;
	private SessionHolderProviderManager providerManager;
	private JcrTemplate jt;

	@Override
	protected void setUp() throws RepositoryException {

		sfControl = MockControl.createControl(SessionFactory.class);
		sf = (SessionFactory) sfControl.getMock();
		sessionControl = MockControl.createControl(Session.class);
		session = (Session) sessionControl.getMock();
		repositoryControl = MockControl.createNiceControl(Repository.class);
		repository = (Repository) repositoryControl.getMock();

		repositoryControl.replay();
		sessionControl.expectAndReturn(session.getRepository(), repository, MockControl.ONE_OR_MORE);
		sfControl.expectAndReturn(sf.getSession(), session);

		sfControl.replay();
		sessionControl.replay();

		providerManager = new ListSessionHolderProviderManager();

		jt = new JcrTemplate(sf);
		jt.setAllowCreate(true);

		sfControl.reset();
		sessionControl.reset();

		session.logout();
		sfControl.expectAndReturn(sf.getSession(), session, MockControl.ONE_OR_MORE);

	}

	@Override
	protected void tearDown() {
		try {
			sessionControl.verify();
			sfControl.verify();
			repositoryControl.verify();
		} catch (final IllegalStateException ex) {
			// ignore: test method didn't call replay
		}
	}

	public void testAfterPropertiesSet() {

		try {
			jt.setSessionFactory(null);
			jt.afterPropertiesSet();
			fail("expected exception");
		} catch (final IllegalArgumentException e) {
			// expected
		}

	}

	public void testInvocationHandler() {
		sessionControl.expectAndReturn(session.getAttribute("smth"), null);
		sessionControl.replay();
		sfControl.replay();

		jt.setAllowCreate(true);
		jt.setExposeNativeSession(true);

		jt.execute(new JcrCallback() {
			public Object doInJcr(final Session sess) throws RepositoryException {
				assertFalse(sess.hashCode() == session.hashCode());
				assertEquals(sess, sess);
				assertFalse(sess.equals(null));
				assertFalse(sess.equals(session));
				sess.getAttribute("smth");
				// logout is proxied so it will not reach our mock
				sess.logout();
				return null;
			}
		}, false);

	}

	public void testTemplateExecuteWithNotAllowCreate() {
		jt.setAllowCreate(false);
		try {
			jt.execute(new JcrCallback() {
				public Object doInJcr(final Session session) {
					return null;
				}
			});
			fail("Should have thrown IllegalStateException");
		} catch (final IllegalStateException ex) {
			// expected
		}
	}

	public void testTemplateExecuteWithNotAllowCreateAndThreadBound() {
		sfControl.reset();
		sessionControl.reset();

		sfControl.replay();
		sessionControl.replay();

		jt.setAllowCreate(false);
		TransactionSynchronizationManager.bindResource(sf, new SessionHolder(session));
		final List l = new ArrayList();
		l.add("test");
		final List result = (List) jt.execute(new JcrCallback() {
			public Object doInJcr(final Session session) {
				return l;
			}
		});
		assertTrue("Correct result list", result == l);
		TransactionSynchronizationManager.unbindResource(sf);
	}

	public void testTemplateExecuteWithNewSession() {
		sfControl.replay();
		sessionControl.replay();

		jt.setAllowCreate(true);

		final List l = new ArrayList();
		l.add("test");
		final List result = (List) jt.execute(new JcrCallback() {
			public Object doInJcr(final Session session) {
				return l;
			}
		});
		assertTrue("Correct result list", result == l);
	}

	public void testTemplateExceptions() throws RepositoryException {

		try {
			createTemplate().execute(new JcrCallback() {
				public Object doInJcr(final Session session) throws RepositoryException {
					throw new AccessDeniedException();
				}
			});
			fail("Should have thrown DataRetrievalFailureException");
		} catch (final DataRetrievalFailureException ex) {
			// expected
		}

		try {
			createTemplate().execute(new JcrCallback() {
				public Object doInJcr(final Session session) throws RepositoryException {
					throw new ConstraintViolationException();
				}
			});
			fail("Should have thrown DataIntegrityViolationException");
		} catch (final DataIntegrityViolationException ex) {
			// expected
		}

		try {
			createTemplate().execute(new JcrCallback() {
				public Object doInJcr(final Session session) throws RepositoryException {
					throw new InvalidItemStateException();
				}
			});
			fail("Should have thrown ConcurrencyFailureException");
		} catch (final ConcurrencyFailureException ex) {
			// expected
		}

		try {
			createTemplate().execute(new JcrCallback() {
				public Object doInJcr(final Session session) throws RepositoryException {
					throw new InvalidQueryException();
				}
			});
			fail("Should have thrown DataRetrievalFailureException");
		} catch (final DataRetrievalFailureException ex) {
			// expected
		}

		try {
			createTemplate().execute(new JcrCallback() {
				public Object doInJcr(final Session session) throws RepositoryException {
					throw new ItemExistsException();
				}
			});
			fail("Should have thrown DataIntegrityViolationException");
		} catch (final DataIntegrityViolationException ex) {
			// expected
		}

		try {
			createTemplate().execute(new JcrCallback() {
				public Object doInJcr(final Session session) throws RepositoryException {
					throw new ItemNotFoundException();
				}
			});
			fail("Should have thrown DataRetrievalFailureException");
		} catch (final DataRetrievalFailureException ex) {
			// expected
		}

		try {
			createTemplate().execute(new JcrCallback() {
				public Object doInJcr(final Session session) throws RepositoryException {
					throw new LockException();
				}
			});
			fail("Should have thrown ConcurrencyFailureException");
		} catch (final ConcurrencyFailureException ex) {
			// expected
		}
		try {
			createTemplate().execute(new JcrCallback() {
				public Object doInJcr(final Session session) throws RepositoryException {
					throw new NamespaceException();
				}
			});
			fail("Should have thrown InvalidDataAccessApiUsageException");
		} catch (final InvalidDataAccessApiUsageException ex) {
			// expected
		}
		try {
			createTemplate().execute(new JcrCallback() {
				public Object doInJcr(final Session session) throws RepositoryException {
					throw new NoSuchNodeTypeException();
				}
			});
			fail("Should have thrown InvalidDataAccessApiUsageException");
		} catch (final InvalidDataAccessApiUsageException ex) {
			// expected
		}
		try {
			createTemplate().execute(new JcrCallback() {
				public Object doInJcr(final Session session) throws RepositoryException {
					throw new NoSuchWorkspaceException();
				}
			});
			fail("Should have thrown DataAccessResourceFailureException");
		} catch (final DataAccessResourceFailureException ex) {
			// expected
		}
		try {
			createTemplate().execute(new JcrCallback() {
				public Object doInJcr(final Session session) throws RepositoryException {
					throw new PathNotFoundException();
				}
			});
			fail("Should have thrown DataRetrievalFailureException");
		} catch (final DataRetrievalFailureException ex) {
			// expected
		}
		try {
			createTemplate().execute(new JcrCallback() {
				public Object doInJcr(final Session session) throws RepositoryException {
					throw new ReferentialIntegrityException();
				}
			});
			fail("Should have thrown DataIntegrityViolationException");
		} catch (final DataIntegrityViolationException ex) {
			// expected
		}
		try {
			createTemplate().execute(new JcrCallback() {
				public Object doInJcr(final Session session) throws RepositoryException {
					throw new UnsupportedRepositoryOperationException();
				}
			});
			fail("Should have thrown InvalidDataAccessApiUsageException");
		} catch (final InvalidDataAccessApiUsageException ex) {
			// expected
		}

		try {
			createTemplate().execute(new JcrCallback() {
				public Object doInJcr(final Session session) throws RepositoryException {
					throw new ValueFormatException();
				}
			});
			fail("Should have thrown InvalidDataAccessApiUsageException");
		} catch (final InvalidDataAccessApiUsageException ex) {
			// expected
		}

		try {
			createTemplate().execute(new JcrCallback() {
				public Object doInJcr(final Session session) throws RepositoryException {
					throw new VersionException();
				}
			});
			fail("Should have thrown DataIntegrityViolationException");
		} catch (final DataIntegrityViolationException ex) {
			// expected
		}

		try {
			createTemplate().execute(new JcrCallback() {
				public Object doInJcr(final Session session) throws RepositoryException {
					throw new RepositoryException();
				}
			});
			fail("Should have thrown JcrSystemException");
		} catch (final JcrSystemException ex) {
			// expected
		}

	}

	private JcrOperations createTemplate() throws RepositoryException {
		sfControl.reset();
		sessionControl.reset();

		// sessionControl.expectAndReturn(session.getRepository(), repository, MockControl.ONE_OR_MORE);
		sfControl.expectAndReturn(sf.getSession(), session);
		// sfControl.expectAndReturn(sf.getSession(), session);
		session.logout();

		sfControl.replay();
		sessionControl.replay();

		final JcrTemplate template = new JcrTemplate(sf);
		template.setAllowCreate(true);
		return template;
	}

	/*
	 * Test method for 'org.springmodules.jcr.JcrTemplate.addLockToken(String)'
	 */
	public void testAddLockToken() {

		final String lock = "some lock";
		session.addLockToken(lock);

		sessionControl.replay();
		sfControl.replay();

		jt.addLockToken(lock);

	}

	/*
	 * Test method for 'org.springmodules.jcr.JcrTemplate.getAttribute(String)'
	 */
	public void testGetAttribute() {
		final String attr = "attribute";
		final Object result = new Object();

		sessionControl.expectAndReturn(session.getAttribute(attr), result);
		sessionControl.replay();
		sfControl.replay();

		assertSame(jt.getAttribute(attr), result);
	}

	/*
	 * Test method for 'org.springmodules.jcr.JcrTemplate.getAttributeNames()'
	 */
	public void testGetAttributeNames() {
		final String result[] = { "some node" };
		sessionControl.expectAndReturn(session.getAttributeNames(), result);
		sessionControl.replay();
		sfControl.replay();

		assertSame(jt.getAttributeNames(), result);
	}

	/*
	 * Test method for 'org.springmodules.jcr.JcrTemplate.getImportContentHandler(String, int)'
	 */
	public void testGetImportContentHandler() throws RepositoryException {
		final String path = "path";
		final MockControl resultMock = MockControl.createControl(ContentHandler.class);
		final ContentHandler result = (ContentHandler) resultMock.getMock();

		sessionControl.expectAndReturn(session.getImportContentHandler(path, 0), result);
		sessionControl.replay();
		sfControl.replay();

		assertSame(jt.getImportContentHandler(path, 0), result);

	}

	/*
	 * Test method for 'org.springmodules.jcr.JcrTemplate.getItem(String)'
	 */
	public void testGetItem() throws RepositoryException {
		final String path = "path";
		final MockControl resultMock = MockControl.createControl(Item.class);
		final Item result = (Item) resultMock.getMock();

		sessionControl.expectAndReturn(session.getItem(path), result);
		sessionControl.replay();
		sfControl.replay();

		assertSame(jt.getItem(path), result);

	}

	/*
	 * Test method for 'org.springmodules.jcr.JcrTemplate.getLockTokens()'
	 */
	public void testGetLockTokens() {
		final String result[] = { "lock1", "lock2" };

		sessionControl.expectAndReturn(session.getLockTokens(), result);
		sessionControl.replay();
		sfControl.replay();

		assertSame(jt.getLockTokens(), result);
	}

	/*
	 * Test method for 'org.springmodules.jcr.JcrTemplate.getNamespacePrefix(String)'
	 */
	public void testGetNamespacePrefix() throws RepositoryException {
		final String result = "namespace";
		final String uri = "prefix";

		sessionControl.expectAndReturn(session.getNamespacePrefix(uri), result);
		sessionControl.replay();
		sfControl.replay();

		assertSame(jt.getNamespacePrefix(uri), result);
	}

	/*
	 * Test method for 'org.springmodules.jcr.JcrTemplate.getNamespacePrefixes()'
	 */
	public void testGetNamespacePrefixes() throws RepositoryException {
		final String result[] = { "prefix1", "prefix2" };

		sessionControl.expectAndReturn(session.getNamespacePrefixes(), result);
		sessionControl.replay();
		sfControl.replay();

		assertSame(jt.getNamespacePrefixes(), result);
	}

	/*
	 * Test method for 'org.springmodules.jcr.JcrTemplate.getNamespaceURI(String)'
	 */
	public void testGetNamespaceURI() throws RepositoryException {
		final String result = "namespace";
		final String prefix = "prefix";

		sessionControl.expectAndReturn(session.getNamespaceURI(prefix), result);
		sessionControl.replay();
		sfControl.replay();

		assertSame(jt.getNamespaceURI(prefix), result);
	}

	/*
	 * Test method for 'org.springmodules.jcr.JcrTemplate.getNodeByUUID(String)'
	 */
	public void testGetNodeByUUID() throws RepositoryException {
		final MockControl resultMock = MockControl.createControl(Node.class);
		final Node result = (Node) resultMock.getMock();

		final String uuid = "uuid";

		sessionControl.expectAndReturn(session.getNodeByUUID(uuid), result);
		sessionControl.replay();
		sfControl.replay();

		assertSame(jt.getNodeByUUID(uuid), result);
	}

	/*
	 * Test method for 'org.springmodules.jcr.JcrTemplate.getUserID()'
	 */
	public void testGetUserID() {
		final String result = "userid";

		sessionControl.expectAndReturn(session.getUserID(), result);
		sessionControl.replay();
		sfControl.replay();

		assertSame(jt.getUserID(), result);
	}

	/*
	 * Test method for 'org.springmodules.jcr.JcrTemplate.getValueFactory()'
	 */
	public void testGetValueFactory() throws RepositoryException {
		final MockControl resultMock = MockControl.createControl(ValueFactory.class);
		final ValueFactory result = (ValueFactory) resultMock.getMock();

		sessionControl.expectAndReturn(session.getValueFactory(), result);
		sessionControl.replay();
		sfControl.replay();

		assertSame(jt.getValueFactory(), result);
	}

	/*
	 * Test method for 'org.springmodules.jcr.JcrTemplate.hasPendingChanges()'
	 */
	public void testHasPendingChanges() throws RepositoryException {
		final boolean result = true;

		sessionControl.expectAndReturn(session.hasPendingChanges(), result);
		sessionControl.replay();
		sfControl.replay();

		assertEquals(jt.hasPendingChanges(), result);
	}

	/*
	 * Test method for 'org.springmodules.jcr.JcrTemplate.importXML(String, InputStream, int)'
	 */
	public void testImportXML() throws RepositoryException, IOException {
		final String path = "path";
		final InputStream stream = new ByteArrayInputStream(new byte[0]);
		session.importXML(path, stream, 0);

		sessionControl.replay();
		sfControl.replay();

		jt.importXML(path, stream, 0);
	}

	/*
	 * Test method for 'org.springmodules.jcr.JcrTemplate.refresh(boolean)'
	 */
	public void testRefresh() throws RepositoryException {
		final boolean refreshMode = true;

		session.refresh(refreshMode);

		sessionControl.replay();
		sfControl.replay();

		jt.refresh(refreshMode);
	}

	/*
	 * Test method for 'org.springmodules.jcr.JcrTemplate.removeLockToken(String)'
	 */
	public void testRemoveLockToken() {
		final String lock = "lock";
		session.removeLockToken(lock);

		sessionControl.replay();
		sfControl.replay();

		jt.removeLockToken(lock);
	}

	/*
	 * Test method for 'org.springmodules.jcr.JcrTemplate.setNamespacePrefix(String, String)'
	 */
	public void testSetNamespacePrefix() throws RepositoryException {
		final String prefix = "prefix";
		final String uri = "uri";
		session.setNamespacePrefix(prefix, uri);

		sessionControl.replay();
		sfControl.replay();

		jt.setNamespacePrefix(prefix, uri);
	}

	/*
	 * Test method for 'org.springmodules.jcr.JcrTemplate.isLive()'
	 */
	public void testIsLive() {
		final boolean result = true;

		sessionControl.expectAndReturn(session.isLive(), result);
		sessionControl.replay();
		sfControl.replay();

		assertEquals(jt.isLive(), result);
	}

	/*
	 * Test method for 'org.springmodules.jcr.JcrTemplate.itemExists(String)'
	 */
	public void testItemExists() throws RepositoryException {
		final boolean result = true;
		final String path = "path";

		sessionControl.expectAndReturn(session.itemExists(path), result);
		sessionControl.replay();
		sfControl.replay();

		assertEquals(jt.itemExists(path), result);
	}

	/*
	 * Test method for 'org.springmodules.jcr.JcrTemplate.move(String, String)'
	 */
	public void testMove() throws RepositoryException {
		final String src = "src";
		final String dest = "dest";

		session.move(src, dest);
		sessionControl.replay();
		sfControl.replay();

		jt.move(src, dest);
	}

	/*
	 * Test method for 'org.springmodules.jcr.JcrTemplate.save()'
	 */
	public void testSave() throws RepositoryException {
		session.save();
		sessionControl.replay();
		sfControl.replay();

		jt.save();
	}

	/*
	 * Test method for 'org.springmodules.jcr.JcrTemplate.dump(Node)'
	 */
	public void testDumpNode() throws RepositoryException {

		final MockControl nodeCtrl = MockControl.createNiceControl(Node.class);
		final Node node = (Node) nodeCtrl.getMock();

		final MockControl iteratorCtrl = MockControl.createControl(PropertyIterator.class);
		final PropertyIterator iterator = (PropertyIterator) iteratorCtrl.getMock();

		final MockControl iterCtrl = MockControl.createControl(NodeIterator.class);
		final NodeIterator iter = (NodeIterator) iterCtrl.getMock();

		nodeCtrl.expectAndReturn(node.getPath(), "path");
		nodeCtrl.expectAndReturn(node.getProperties(), iterator);
		iteratorCtrl.expectAndReturn(iterator.hasNext(), false);
		nodeCtrl.expectAndReturn(node.getNodes(), iter);
		iterCtrl.expectAndReturn(iter.hasNext(), false);

		sessionControl.expectAndReturn(session.getRootNode(), node);

		sessionControl.replay();
		sfControl.replay();
		nodeCtrl.replay();

		jt.dump(null);

		nodeCtrl.verify();
	}

	public void testQueryNode() throws RepositoryException {
		try {
			jt.query((Node) null);
			fail("should have thrown exception");
		} catch (final IllegalArgumentException e) {
			// it's okay
		}

		final MockControl ndCtrl = MockControl.createControl(Node.class);
		final Node nd = (Node) ndCtrl.getMock();

		final MockControl wsCtrl = MockControl.createControl(Workspace.class);
		final Workspace ws = (Workspace) wsCtrl.getMock();

		final MockControl qmCtrl = MockControl.createControl(QueryManager.class);
		final QueryManager qm = (QueryManager) qmCtrl.getMock();

		final MockControl queryCtrl = MockControl.createControl(Query.class);
		final Query query = (Query) queryCtrl.getMock();

		final MockControl resultCtrl = MockControl.createControl(QueryResult.class);
		final QueryResult result = (QueryResult) resultCtrl.getMock();

		sessionControl.expectAndReturn(session.getWorkspace(), ws);
		wsCtrl.expectAndReturn(ws.getQueryManager(), qm);
		qmCtrl.expectAndReturn(qm.getQuery(nd), query);
		queryCtrl.expectAndReturn(query.execute(), result);

		sfControl.replay();
		sessionControl.replay();
		wsCtrl.replay();
		qmCtrl.replay();
		queryCtrl.replay();
		resultCtrl.replay();

		assertSame(result, jt.query(nd));

		sfControl.verify();
		sessionControl.verify();
		wsCtrl.verify();
		qmCtrl.verify();
		queryCtrl.verify();
		resultCtrl.verify();
	}

	public void testExecuteQuery() throws RepositoryException {
		try {
			jt.query(null, null);
			fail("should have thrown exception");
		} catch (final IllegalArgumentException e) {
			// it's okay
		}

		final MockControl ndCtrl = MockControl.createControl(Node.class);
		final Node nd = (Node) ndCtrl.getMock();

		final MockControl wsCtrl = MockControl.createControl(Workspace.class);
		final Workspace ws = (Workspace) wsCtrl.getMock();

		final MockControl qmCtrl = MockControl.createControl(QueryManager.class);
		final QueryManager qm = (QueryManager) qmCtrl.getMock();

		final MockControl queryCtrl = MockControl.createControl(Query.class);
		final Query query = (Query) queryCtrl.getMock();

		final MockControl resultCtrl = MockControl.createControl(QueryResult.class);
		final QueryResult result = (QueryResult) resultCtrl.getMock();

		final String stmt = "//*/@bogus:title";
		final String language = Query.XPATH;

		sessionControl.expectAndReturn(session.getWorkspace(), ws);
		wsCtrl.expectAndReturn(ws.getQueryManager(), qm);
		qmCtrl.expectAndReturn(qm.createQuery(stmt, language), query);
		queryCtrl.expectAndReturn(query.execute(), result);

		sfControl.replay();
		sessionControl.replay();
		wsCtrl.replay();
		qmCtrl.replay();
		queryCtrl.replay();
		resultCtrl.replay();

		assertSame(result, jt.query(stmt, null));

		sfControl.verify();
		sessionControl.verify();
		wsCtrl.verify();
		qmCtrl.verify();
		queryCtrl.verify();
		resultCtrl.verify();
	}

	public void testExecuteQuerySimple() throws RepositoryException {
		try {
			jt.query((String) null);
			fail("should have thrown exception");
		} catch (final IllegalArgumentException e) {
			// it's okay
		}
		// the rest of the test is covered by testExecuteQuery

	}

	public void testGetTree() throws RepositoryException {
		try {
			jt.query((List) null);
			fail("should have thown IllegalArgumentException");
		} catch (final IllegalArgumentException e) {
			// it's okay
		}

		final List list = new ArrayList();
		final String stmt1 = "//*/@bogus:title";
		final String stmt2 = "//*";

		list.add(stmt1);
		list.add(stmt2);
		final boolean silent = false;

		final String language = Query.XPATH;

		final MockControl ndCtrl = MockControl.createControl(Node.class);
		final Node nd = (Node) ndCtrl.getMock();

		final MockControl wsCtrl = MockControl.createControl(Workspace.class);
		final Workspace ws = (Workspace) wsCtrl.getMock();

		final MockControl qmCtrl = MockControl.createControl(QueryManager.class);
		final QueryManager qm = (QueryManager) qmCtrl.getMock();

		final MockControl queryCtrl = MockControl.createControl(Query.class);
		final Query query = (Query) queryCtrl.getMock();

		final MockControl resultCtrl = MockControl.createControl(QueryResult.class);
		final QueryResult result = (QueryResult) resultCtrl.getMock();

		sessionControl.expectAndReturn(session.getWorkspace(), ws);
		wsCtrl.expectAndReturn(ws.getQueryManager(), qm);
		qmCtrl.expectAndReturn(qm.createQuery(stmt1, language), query);
		qmCtrl.expectAndReturn(qm.createQuery(stmt2, language), query);
		queryCtrl.expectAndReturn(query.execute(), result);
		queryCtrl.expectAndReturn(query.execute(), result);

		sfControl.replay();
		sessionControl.replay();
		wsCtrl.replay();
		qmCtrl.replay();
		queryCtrl.replay();
		resultCtrl.replay();

		final Map tree = jt.query(list, null, silent);

		assertSame("Results are not the same", result, tree.get("//*"));
		assertSame("Results are not the same", result, tree.get("//*/@bogus:title"));

		sfControl.verify();
		sessionControl.verify();
		wsCtrl.verify();
		qmCtrl.verify();
		queryCtrl.verify();
		resultCtrl.verify();
	}
}
