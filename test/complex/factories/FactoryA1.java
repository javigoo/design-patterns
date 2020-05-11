package complex.factories;

import common.DependencyException;
import complex.Factory;
import implementations.ImplementationA1;

import implementations.ImplementationB1;
import implementations.ImplementationC1;
import interfaces.InterfaceB;
import interfaces.InterfaceC;

public class FactoryA1 implements Factory<ImplementationA1> {
    @Override
    public ImplementationA1 create(Object... parameters) throws DependencyException {
        InterfaceB b;
        InterfaceC c;
        try {
            b = (ImplementationB1) parameters[0];
            c = (ImplementationC1) parameters[1];
        } catch (ClassCastException | ArrayIndexOutOfBoundsException ex) {
            throw new DependencyException(ex);
        }
        return new ImplementationA1(b, c);
    }
}
