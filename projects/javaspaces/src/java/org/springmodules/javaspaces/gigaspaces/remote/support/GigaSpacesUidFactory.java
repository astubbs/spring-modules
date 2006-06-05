/*
 * Copyright 2006 GigaSpaces Technologies. All rights reserved.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS," WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED INCLUDING BUT NOT LIMITED TO WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT. GIGASPACES WILL NOT BE LIABLE FOR ANY DAMAGE OR
 * LOSS IN CONNECTION WITH THE SOFTWARE.
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
