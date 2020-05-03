package complex;

import common.DependencyException;

public interface Factory<E> {
    Object create(Object... parameters) throws DependencyException;
}
