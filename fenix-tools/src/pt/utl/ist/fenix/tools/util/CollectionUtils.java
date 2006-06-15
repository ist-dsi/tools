package pt.utl.ist.fenix.tools.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class CollectionUtils {

    public static <T extends Comparable<? super T>> SortedSet<T> constructSortedSet(Collection<T> collection) {
	return new TreeSet<T>(collection);
    }

    public static <T> SortedSet<T> constructSortedSet(Collection<T> collection, Comparator<? super T> c) {
	final SortedSet<T> sortedSet = new TreeSet<T>(c);
	sortedSet.addAll(collection);
	return sortedSet;
    }

}