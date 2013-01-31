package pt.utl.ist.fenix.tools.predicates;

import java.util.ArrayList;
import java.util.Collection;

public abstract class ChainPredicate<T> extends Predicate<T> {

	protected Collection<Predicate<T>> predicates;

	public ChainPredicate(Collection<Predicate<T>> predicates) {
		this.predicates = predicates;
	}

	public ChainPredicate() {
		this(new ArrayList<Predicate<T>>());
	}

	public void add(Predicate<T> predicate) {
		this.predicates.add(predicate);
	}

}
