package factories;

import common.DependencyException;
import simple.Factory;
import implementations.ImplementationC1;

public class FactoryC1 implements Factory <ImplementationC1> {
    @Override
    public ImplementationC1 create(Object... parameters) throws DependencyException {
        String s;
        try {
            s = (String) parameters[0];
        } catch (ClassCastException | ArrayIndexOutOfBoundsException ex) {
            throw new DependencyException(ex);
        }
        return new ImplementationC1(s);
    }
}
