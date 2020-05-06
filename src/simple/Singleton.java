package simple;

import common.DependencyException;

public interface Singleton extends Factory {
    Object factory;
    Object create(Object... parameters) throws DependencyException;
}