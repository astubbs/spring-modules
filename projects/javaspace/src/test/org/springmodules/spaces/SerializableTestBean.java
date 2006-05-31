package org.springmodules.spaces;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.springframework.core.Ordered;
import org.springmodules.beans.TestBean;

public class SerializableTestBean extends TestBean 
	implements Externalizable, Ordered {
    
    public SerializableTestBean() {
        setName("rod");
    }
    
    public int getOrder() {
       return 1;
    }

    // TODO as this is just a quick hack,
    // we need to ensure that superclass name at least
    // is serializable, as it's the only thing that
    // will be serialized 
    public void writeExternal(ObjectOutput oo) throws IOException {
        oo.writeBytes(getName());
    }

    public void readExternal(ObjectInput oi) throws IOException, ClassNotFoundException {
        setName(oi.readLine());
    }
    
}