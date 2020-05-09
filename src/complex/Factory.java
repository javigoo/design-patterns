package complex;

import common.DependencyException;

public interface Factory<E> {
    E create(Object... parameters) throws DependencyException;
}
