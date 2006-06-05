package org.springmodules.javaspaces.gigaspaces.app;

public interface ISimpleDao {
	public void writeSimple(SimpleBean bean) throws Exception ;
	public SimpleBean takeIfExists(SimpleBean template);
}
