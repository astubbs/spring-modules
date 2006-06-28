package org.springmodules.validation.util.io;

import java.io.File;
import java.io.FileFilter;
import java.util.Iterator;

import org.springmodules.validation.util.collection.ReadOnlyIterator;
import org.springframework.util.Assert;
import org.apache.commons.collections.iterators.ArrayIterator;

/**
 * @author Uri Boness
 */
public class FileIterator extends ReadOnlyIterator {

    private Iterator fileIterator;

    public FileIterator(String dirName) {
        this(new File(dirName));
    }

    public FileIterator(File dir) {
        Assert.isTrue(dir.isDirectory(), "Given file must be a directory");
        fileIterator = new ArrayIterator(dir.listFiles());
    }

    public FileIterator(String dirName, FileFilter filter) {
        this(new File(dirName), filter);
    }

    public FileIterator(File dir, FileFilter filter) {
        Assert.isTrue(dir.isDirectory(), "Given file must be a directory");
        fileIterator = new ArrayIterator(dir.listFiles(filter));
    }

    public boolean hasNext() {
        return fileIterator.hasNext();
    }

    public Object next() {
        return fileIterator.next();
    }

}
