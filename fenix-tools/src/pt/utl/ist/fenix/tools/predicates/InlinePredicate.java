package pt.utl.ist.fenix.tools.predicates;

public abstract class InlinePredicate<T, K> extends Predicate<T> {

    private K value;

    public InlinePredicate(K k) {
	this.value = k;
    }

    protected K getValue() {
	return value;
    }

}
