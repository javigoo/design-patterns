package complex.factories;

import common.DependencyException;
import complex.Factory;
import implementations.ImplementationB1;

import implementations.ImplementationD1;
import interfaces.InterfaceD;

public class FactoryB1 implements Factory<ImplementationB1> {
    @Override
    public ImplementationB1 create(Object... parameters) throws DependencyException {
        InterfaceD d;
        try {
            d = (ImplementationD1) parameters[0];
        } catch (ClassCastException | ArrayIndexOutOfBoundsException ex) {
            throw new DependencyException(ex);
        }
        return new ImplementationB1(d);
    }
}
