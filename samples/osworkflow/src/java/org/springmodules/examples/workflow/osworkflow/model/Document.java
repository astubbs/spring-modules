
package org.springmodules.examples.workflow.osworkflow.model;

import java.util.Date;

/**
 * @author robh
 */
public class Document {

	private String title;

	private String content;

	private String author;

	private Date creationDate = new Date();

	private Date lastModifiedDate = new Date();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

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

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}
}
