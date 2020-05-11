package simple.factories;

import common.DependencyException;
import simple.Factory;
import implementations.ImplementationB1;
import implementations.ImplementationD1;
import interfaces.InterfaceD;

public class FactoryB1 implements Factory{
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
