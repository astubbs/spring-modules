/*
* Copyright 2006 GigaSpaces, Inc.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.springmodules.javaspaces.gigaspaces.remote.support;

import java.io.Serializable;
import java.rmi.RemoteException;

import org.springframework.aop.framework.Advised;
import org.springmodules.javaspaces.entry.UidFactory;

import org.springmodules.javaspaces.gigaspaces.exception.GigaSpacesException;
import com.j_spaces.core.IJSpace;
import com.j_spaces.core.IRemoteJSpace;
import com.j_spaces.core.client.AbstractSpaceProxy;

/**
 * <p>
 * GigaSpacesUidFactory based on the GigaSpaces UID factory
 * implementation.
 * </p>
 * 
 * Copyright:    Copyright 2006 GigaSpaces Technologies. All rights reserved.
 * Company:      Gigaspaces Technologies
 * @author       Lior Ben Yizhak
 * @version      5.0
 *
 */
public class GigaSpacesUidFactory implements UidFactory {

	private IJSpace space;
	private IJSpace realSpace;

	/*
	 * Instance of remote space for generate the space uid.
	 */
	private IRemoteJSpace remoteJSpace = null;

	/* (non-Javadoc)
	 * @see org.springmodules.javaspaces.entry.UidFactory#generateUid()
	 */
	public Serializable generateUid() {
		String genUid = null;
		try{
			if(remoteJSpace == null){
				remoteJSpace = ((AbstractSpaceProxy) realSpace).getRemoteJSpace();
			}
			genUid = remoteJSpace.getUniqueID();
		}
		catch (RemoteException e)
		{
			throw new GigaSpacesException("Failed to generate uid for remote call.",e);
		}
		return genUid;
	}

	/**
	 * Gets space
	 * @return the space
	 */
	public IJSpace getSpace()
	{
		return space;
	}

	/**
	 * Sets the space
	 *
	 * @param space
	 */
	public void setSpace(IJSpace space)
	{
		this.space = space;
		this.realSpace  = space;
		while (realSpace instanceof Advised)
		{
			try
			{
				realSpace = (IJSpace) ((Advised) space).getTargetSource().getTarget();
			}
			catch (Exception ex)
			{
				throw new IllegalArgumentException(	"error trying to reach innermost space",
				                                   	ex);
			}
		}
	}
}
