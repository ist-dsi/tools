package pt.utl.ist.fenix.tools.util;

/**
 * @author Joao Carvalho (joao.pedro.carvalho@ist.utl.pt)
 * 
 */
public interface Predicate<T> {

    public boolean evaluate(T element);

}
