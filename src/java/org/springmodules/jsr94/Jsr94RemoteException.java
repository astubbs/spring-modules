/**
 * 
 */
package org.springmodules.jsr94;

/**
 * Maps checked RemoteException to unchecked Jsr94RemoteException
 * 
 * @see java.rmi.RemoteException
 * @author janm
 */
public class Jsr94RemoteException extends Jsr94Exception {

	/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 3257571715306762550L;

	/**
	 * @param message
	 * @param cause
	 */
	public Jsr94RemoteException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public Jsr94RemoteException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public Jsr94RemoteException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
