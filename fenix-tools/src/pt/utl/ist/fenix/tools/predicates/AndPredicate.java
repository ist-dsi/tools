package pt.utl.ist.fenix.tools.predicates;

import java.util.Collection;

public class AndPredicate<T> extends ChainPredicate<T> {

    public AndPredicate(Collection<Predicate<T>> predicates) {
	super(predicates);
    }

    public AndPredicate() {
	super();
    }

    @Override
    public boolean eval(T t) {
	for (Predicate<T> predicate : predicates) {
	    if (!predicate.eval(t)) {
		return false;
	    }
	}
	return true;
    }

}
