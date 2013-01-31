package pt.utl.ist.fenix.tools.predicates;

import java.util.Collection;

public class OrPredicate<T> extends ChainPredicate<T> {

	public OrPredicate(Collection<Predicate<T>> predicates) {
		super(predicates);
	}

	public OrPredicate() {
		super();
	}

	@Override
	public boolean eval(T t) {
		for (Predicate<T> predicate : predicates) {
			if (predicate.eval(t)) {
				return true;
			}
		}
		return false;
	}

}
