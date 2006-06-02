package org.springmodules.samples.validation.valang.web;

/**
 *
 * @author Uri Boness
 */
public class PersonData {

    private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String verifyPassword;

	public String getEmail() {
		return email;
	}

    public void setEmail(String email) {
		this.email = email;
	}

    public String getFirstName() {
		return firstName;
	}

    public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

    public String getLastName() {
		return lastName;
	}

    public void setLastName(String lastName) {
		this.lastName = lastName;
	}

    public String getPassword() {
		return password;
	}

    public void setPassword(String password) {
		this.password = password;
	}

    public String getVerifyPassword() {
		return verifyPassword;
	}

    public void setVerifyPassword(String verifyPassword) {
		this.verifyPassword = verifyPassword;
	}

}
