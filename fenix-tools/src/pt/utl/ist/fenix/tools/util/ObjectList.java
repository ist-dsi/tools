package pt.utl.ist.fenix.tools.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public abstract class ObjectList<E> extends ArrayList<E> implements Serializable {

    public ObjectList(Collection<E> objectCollection) {
        super(objectCollection);
    }

    public ObjectList(E[] objectArray) {
        this(Arrays.asList(objectArray));
    }

    public ObjectList(E string) {
        this(Collections.singleton(string));
    }

}
