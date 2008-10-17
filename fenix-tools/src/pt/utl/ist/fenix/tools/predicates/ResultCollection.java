package pt.utl.ist.fenix.tools.predicates;

import java.util.ArrayList;
import java.util.Collection;

public class ResultCollection<T> {

    private Predicate<T> predicate;
    private Collection<T> result;

    public ResultCollection(final Collection<T> result, final Predicate<T> predicate) {
	this.result = result;
	this.predicate = predicate;
    }

    public ResultCollection(final Predicate<T> predicate) {
	this(new ArrayList<T>(), predicate);
    }

    public Collection<T> getResult() {
	return result;
    }

    private boolean add(T t) {
	return this.result.add(t);
    }

    public void condicionalAdd(T t) {
	if (predicate.eval(t)) {
	    add(t);
	}
    }

}
