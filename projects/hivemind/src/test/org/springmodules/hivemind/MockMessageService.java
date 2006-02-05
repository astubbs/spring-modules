
package org.springmodules.hivemind;

/**
 * @author Rob Harrop
 */
public class MockMessageService implements MessageService {

	private String message = "Hello World!";

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}
}
