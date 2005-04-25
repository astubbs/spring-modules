
package org.springmodules.examples.workflow.osworkflow.model;

import java.util.Date;

/**
 * 
 * @author robh
 */
public class Comment {

	private String content;
	private String author;
	private Date creationDate = new Date();

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getCreationDate() {
		return creationDate;
	}

}
