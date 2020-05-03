package simple;

import common.DependencyException;

public interface Factory {
    Object create(Object... parameters) throws DependencyException;
}
