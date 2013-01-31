package pt.utl.ist.fenix.tools.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * 
 * @author lepc
 * @author Joao Carvalho (joao.pedro.carvalho@ist.utl.pt)
 * 
 */
public class CollectionUtils {

	public static <T extends Comparable<? super T>> SortedSet<T> constructSortedSet(Collection<T> collection) {
		return new TreeSet<T>(collection);
	}

	public static <T> SortedSet<T> constructSortedSet(Collection<T> collection, Comparator<? super T> c) {
		final SortedSet<T> sortedSet = new TreeSet<T>(c);
		sortedSet.addAll(collection);
		return sortedSet;
	}

	private CollectionUtils() {
		super();
	}

	/**
	 * Selects all elements from input collection which match the given
	 * predicate into an output collection.
	 * 
	 * @param inputCollection
	 *            the collection to get the input from, may not be null
	 * @param predicate
	 *            the predicate to use, may be null
	 * @return the elements matching the predicate (new list)
	 */
	public static <T> List<T> filter(Collection<T> collection, Predicate<T> predicate) {

		final List<T> result = new ArrayList<T>();

		for (final T each : collection) {
			if (predicate.evaluate(each)) {
				result.add(each);
			}
		}

		return result;
	}

	/**
	 * Finds the first element in the given collection which matches the given
	 * predicate.
	 * <p>
	 * If the input collection or predicate is null, or no element of the collection matches the predicate, null is returned.
	 * 
	 */
	public static <T> T find(Collection<T> collection, Predicate<T> predicate) {
		if (collection != null && predicate != null) {
			for (T item : collection) {
				if (predicate.evaluate(item)) {
					return item;
				}
			}
		}
		return null;
	}

	/**
	 * Returns a new Collection consisting of the elements of inputCollection
	 * transformed by the given transformer.
	 * <p>
	 * If the input transformer is null, the result is an empty list.
	 * 
	 * @param inputCollection
	 *            the collection to get the input from, may not be null
	 * @param transformer
	 *            the transformer to use, may be null
	 * @return the transformed result (new list)
	 */
	public static <T, E> List<T> collect(Collection<E> collection, Transformer<E, T> transformer) {

		List<T> list = new ArrayList<T>(collection.size());
		if (transformer != null && collection != null) {
			Iterator<E> iterator = collection.iterator();

			while (iterator.hasNext()) {
				list.add(transformer.transform(iterator.next()));
			}
		}
		return list;

	}

	/**
	 * Executes the given closure on each element in the collection.
	 * <p>
	 * If the input collection or closure is null, there is no change made.
	 * 
	 * @param collection
	 *            the collection to get the input from, may be null
	 * @param closure
	 *            the closure to perform, may be null
	 */
	public static <T> void forAllDo(Collection<T> collection, Closure<T> closure) {
		if (collection != null && closure != null) {
			for (T t : collection) {
				closure.execute(t);
			}
		}
	}

	/**
	 * Filter the collection by applying a Predicate to each element. If the
	 * predicate returns false, remove the element.
	 * <p>
	 * If the input collection or predicate is null, there is no change made.
	 * 
	 * @param collection
	 *            the collection to get the input from, may be null
	 * @param predicate
	 *            the predicate to use as a filter, may be null
	 */
	public static <T> void filterMatching(Collection<T> collection, Predicate<T> predicate) {
		if (collection != null && predicate != null) {
			for (Iterator<T> it = collection.iterator(); it.hasNext();) {
				if (predicate.evaluate(it.next()) == false) {
					it.remove();
				}
			}
		}
	}

	public static <T> boolean anyMatches(Collection<T> collection, Predicate<T> predicate) {
		if (collection != null && predicate != null) {
			for (T t : collection) {
				if (predicate.evaluate(t)) {
					return true;
				}
			}
		}
		return false;
	}

}