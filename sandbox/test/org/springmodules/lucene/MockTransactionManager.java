/* Copyright 2005 Brian McCallister
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springmodules.lucene;

import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;

/**
 * copied from Spring test framework since it is not in the mocks jar =(
 * @author Juergen Hoeller
 * @since 29.04.2003
 */
public class MockTransactionManager extends AbstractPlatformTransactionManager {

	private static final Object TRANSACTION = "transaction";

	private final boolean existingTransaction;

	private final boolean canCreateTransaction;

	protected boolean begin = false;

	protected boolean commit = false;

	protected boolean rollback = false;

	protected boolean rollbackOnly = false;

	public MockTransactionManager(boolean existingTransaction, boolean canCreateTransaction) {
		this.existingTransaction = existingTransaction;
		this.canCreateTransaction = canCreateTransaction;
		setTransactionSynchronization(SYNCHRONIZATION_ON_ACTUAL_TRANSACTION);
	}

	protected Object doGetTransaction() {
		return TRANSACTION;
	}

	protected boolean isExistingTransaction(Object transaction) {
		return existingTransaction;
	}

	protected void doBegin(Object transaction, TransactionDefinition definition) {
		if (!TRANSACTION.equals(transaction)) {
			throw new IllegalArgumentException("Not the same transaction object");
		}
		if (!this.canCreateTransaction) {
			throw new CannotCreateTransactionException("Cannot create transaction");
		}
		this.begin = true;
	}

	protected Object doSuspend(Object transaction) {
		return null;
	}

	protected void doResume(Object transaction, Object suspendedResources) {
	}

	protected void doCommit(DefaultTransactionStatus status) {
		if (!TRANSACTION.equals(status.getTransaction())) {
			throw new IllegalArgumentException("Not the same transaction object");
		}
		this.commit = true;
	}

	protected void doRollback(DefaultTransactionStatus status) {
		if (!TRANSACTION.equals(status.getTransaction())) {
			throw new IllegalArgumentException("Not the same transaction object");
		}
		this.rollback = true;
	}

	protected void doSetRollbackOnly(DefaultTransactionStatus status) {
		if (!TRANSACTION.equals(status.getTransaction())) {
			throw new IllegalArgumentException("Not the same transaction object");
		}
		this.rollbackOnly = true;
	}

	protected void doCleanupAfterCompletion(Object transaction) {
	}

}
