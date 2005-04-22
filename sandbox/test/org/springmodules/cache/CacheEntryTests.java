/* 
 * Created on Mar 10, 2005
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * Copyright @2005 the original author or authors.
 */
package org.springmodules.cache;

import junit.framework.TestCase;

/**
 * <p>
 * Unit Test for <code>{@link AbstractCacheEntry}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/22 02:37:23 $
 */
public final class CacheEntryTests extends TestCase {

  /**
   * Primary object that is under test.
   */
  private AbstractCacheEntry entry;

  /**
   * Constructor.
   * 
   * @param name
   *          the name of the Test Case.
   */
  public CacheEntryTests(String name) {
    super(name);
  }

  /**
   * Sets up the test fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();

    this.entry = new AbstractCacheEntry() {
      /**
       * Version number of this class.
       * 
       * @see java.io.Serializable
       */
      private static final long serialVersionUID = 3257004375812681776L;
    };
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheEntry#needsRefresh()}</code> returns
   * <code>true</code> if the entry has not been updated yet.
   */
  public void testNeedsRefreshWhenEntryHasNotBeenUpdatedYet() {
    this.entry.setRefreshPeriod(35);

    boolean actualNeedsRefresh = this.entry.needsRefresh();
    assertTrue("The entry needs refresh", actualNeedsRefresh);
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheEntry#needsRefresh()}</code> returns
   * <code>true</code> the time the entry has been in the cache is greater
   * than the specified refresh period.
   */
  public void testNeedsRefreshWhenLiveTimeIsEqualThanRefreshPeriod()
      throws Exception {
    // update the entry.
    this.entry.setContent(new Object());
    this.entry.setRefreshPeriod(2);

    Thread.sleep(2000);

    boolean actualNeedsRefresh = this.entry.needsRefresh();
    assertTrue("The entry needs refresh", actualNeedsRefresh);
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheEntry#needsRefresh()}</code> returns
   * <code>true</code> the time the entry has been in the cache is greater
   * than the specified refresh period.
   */
  public void testNeedsRefreshWhenLiveTimeIsGreaterThanRefreshPeriod()
      throws Exception {
    // update the entry.
    this.entry.setContent(new Object());
    this.entry.setRefreshPeriod(1);

    Thread.sleep(3000);

    boolean actualNeedsRefresh = this.entry.needsRefresh();
    assertTrue("The entry needs refresh", actualNeedsRefresh);
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheEntry#needsRefresh()}</code> returns
   * <code>false</code> the time the entry has been in the cache is less than
   * the specified refresh period.
   */
  public void testNeedsRefreshWhenLiveTimeIsLessThanRefreshPeriod()
      throws Exception {
    // update the entry.
    this.entry.setContent(new Object());
    this.entry.setRefreshPeriod(10);

    Thread.sleep(2000);

    boolean actualNeedsRefresh = this.entry.needsRefresh();
    assertFalse("The entry does not need refresh", actualNeedsRefresh);
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheEntry#needsRefresh()}</code> returns
   * <code>false</code> if the refresh period is equal to
   * <code>{@link AbstractCacheEntry#INDEFINITE_EXPIRY}</code> and the entry
   * has been previously updated.
   */
  public void testNeedsRefreshWithRefreshPeriodEqualToIndefiniteExpiry() {
    // update the entry.
    this.entry.setContent(new Object());
    this.entry.setRefreshPeriod(AbstractCacheEntry.INDEFINITE_EXPIRY);

    boolean actualNeedsRefresh = this.entry.needsRefresh();
    assertFalse("The entry does not need refresh", actualNeedsRefresh);
  }

  /**
   * Verifies that the method
   * <code>{@link AbstractCacheEntry#needsRefresh()}</code> returns
   * <code>true</code> if the refresh period is equal to zero and the entry
   * has been previously updated.
   */
  public void testNeedsRefreshWithRefreshPeriodEqualToZero() {
    // update the entry.
    this.entry.setContent(new Object());
    this.entry.setRefreshPeriod(0);

    boolean actualNeedsRefresh = this.entry.needsRefresh();
    assertTrue("The entry needs refresh", actualNeedsRefresh);
  }
}
