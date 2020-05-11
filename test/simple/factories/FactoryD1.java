package simple.factories;

import common.DependencyException;
import simple.Factory;
import implementations.ImplementationD1;

public class FactoryD1 implements Factory {
    @Override
    public ImplementationD1 create(Object... parameters) throws DependencyException {
        int i;
        try {
            i = (int) parameters[0];
        } catch (ClassCastException | ArrayIndexOutOfBoundsException ex) {
            throw new DependencyException(ex);
        }
        return new ImplementationD1(i);
    }
}
