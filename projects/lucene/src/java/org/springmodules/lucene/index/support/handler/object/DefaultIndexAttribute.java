package org.springmodules.lucene.index.support.handler.object;

public class DefaultIndexAttribute implements IndexAttribute {
	private String name;
	private String type="TEXT";
	private boolean excluded=false;

	public boolean isExcluded() {
		return excluded;
	}

	public void setExcluded(boolean excluded) {
		this.excluded = excluded;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
