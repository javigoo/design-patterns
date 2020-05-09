package complex;

import common.DependencyException;

public interface Injector {

    <E> void registerConstant(Class<E> name,
                          E value)
            throws DependencyException;

    <E> void registerFactory(Class<E> name,
                             Factory<? extends E> creator,
                             Class<?>... parameters)
            throws DependencyException;

    <E> void registerSingleton(Class<E> name,
                           Factory<? extends E> creator,
                           Class<?>... parameters)
            throws DependencyException;

    <E> Object getObject(Class<E> name)
            throws DependencyException;
}
