package pt.utl.ist.fenix.tools.predicates;

public interface PredicateContainer<Type> {

	public Predicate<Type> getPredicate();

}
