package simple;

import common.DependencyException;
import implementations.ImplementationD1;
import interfaces.InterfaceD;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class ContainerTest {

    @Test
    public void FactoryD1() throws DependencyException{
        Injector injector = (Injector) new Container();
        injector.registerConstant("I", 42);
        injector.registerFactory("D", new FactoryD1(), "I");
        InterfaceD d = (InterfaceD) injector.getObject("D");
        assertThat(d, is(instanceOf(ImplementationD1.class)));
        ImplementationD1 d1 = (ImplementationD1) d;
        // assertThat(d1.getI(), is(42));
    }

}