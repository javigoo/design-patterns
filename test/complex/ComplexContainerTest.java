package complex;

import common.DependencyException;
import complex.factories.*;
import implementations.*;
import interfaces.*;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ComplexContainerTest {

    private Injector injector;

    @Before
    public void newContainer() {
        injector = new Container();
    }

    @Test
    public void instanceOfFactoryA1() throws DependencyException {
        registerConstants();
        registerFactories();

        InterfaceA a = injector.getObject(InterfaceA.class);
        assertThat(a, is(instanceOf(ImplementationA1.class)));

        ImplementationA1 a1 = (ImplementationA1) a;
        assertThat(a1.b, is(instanceOf(ImplementationB1.class)));
        assertThat(a1.c, is(instanceOf(ImplementationC1.class)));
    }

    @Test
    public void instanceOfFactoryB1() throws DependencyException {
        registerConstants();
        registerFactories();

        InterfaceB b = injector.getObject(InterfaceB.class);
        assertThat(b, is(instanceOf(ImplementationB1.class)));

        ImplementationB1 b1 = (ImplementationB1) b;
        assertThat(b1.d, is(instanceOf(ImplementationD1.class)));
    }

    @Test
    public void instanceOfFactoryC1() throws DependencyException {
        registerConstants();
        registerFactories();

        InterfaceC c = injector.getObject(InterfaceC.class);
        assertThat(c, is(instanceOf(ImplementationC1.class)));

        ImplementationC1 c1 = (ImplementationC1) c;
        assertThat(c1.getS(), is("El sentido de la vida, el universo y todo lo demas"));
    }

    @Test
    public void instanceOfFactoryD1() throws DependencyException {
        registerConstants();
        registerFactories();

        InterfaceD d = injector.getObject(InterfaceD.class);
        assertThat(d, is(instanceOf(ImplementationD1.class)));

        ImplementationD1 d1 = (ImplementationD1) d;
        assertThat(d1.getI(), is(42));
    }

    @Test
    public void instanceOfFactoryA1Singleton() throws DependencyException{
        registerConstants();
        registerSingletons();

        InterfaceA a = injector.getObject(InterfaceA.class);
        assertThat(a, is(instanceOf(ImplementationA1.class)));
        ImplementationA1 a1 = (ImplementationA1) a;
        assertThat(a1.b, is(instanceOf(ImplementationB1.class)));
        assertThat(a1.c, is(instanceOf(ImplementationC1.class)));
    }

    @Test
    public void instanceOfFactoryB1Singleton() throws DependencyException{
        registerConstants();
        registerSingletons();

        InterfaceB b = injector.getObject(InterfaceB.class);
        assertThat(b, is(instanceOf(ImplementationB1.class)));
        ImplementationB1 b1 = (ImplementationB1) b;
        assertThat(b1.d, is(instanceOf(ImplementationD1.class)));
    }

    @Test
    public void instanceOfFactoryC1Singleton() throws DependencyException{
        registerConstants();
        registerSingletons();

        InterfaceC c = injector.getObject(InterfaceC.class);
        assertThat(c, is(instanceOf(ImplementationC1.class)));
        ImplementationC1 c1 = (ImplementationC1) c;
        assertThat(c1.s,is("El sentido de la vida, el universo y todo lo demas"));
    }

    @Test
    public void instanceOfFactoryD1Singleton() throws DependencyException{
        registerConstants();
        registerSingletons();

        InterfaceD d = injector.getObject(InterfaceD.class);
        assertThat(d, is(instanceOf(ImplementationD1.class)));
        ImplementationD1 d1 = (ImplementationD1) d;
        assertThat(d1.i, is(42));
    }

    @Test(expected = DependencyException.class)
    public void alreadyRegisteredConstant() throws DependencyException{
        injector.registerConstant(Integer.class, 0);
        injector.registerConstant(Integer.class, 0);
    }

    @Test(expected = DependencyException.class)
    public void alreadyRegisteredFactory() throws DependencyException{
        injector.registerFactory(InterfaceD.class, new FactoryD1(), Integer.class);
        injector.registerFactory(InterfaceD.class, new FactoryD1(), Integer.class);
    }

    @Test(expected = DependencyException.class)
    public void alreadyRegisteredSingleton() throws DependencyException{
        injector.registerSingleton(InterfaceD.class, new FactoryD1(), Integer.class);
        injector.registerSingleton(InterfaceD.class, new FactoryD1(), Integer.class);
    }

    @Test(expected = DependencyException.class)
    public void getObjectNotRegisteredName() throws DependencyException{
        injector.getObject(InterfaceA.class);
    }

    @Test(expected = DependencyException.class)
    public void getObjectNotRegisteredDependenciesFactory() throws DependencyException{
        injector.registerFactory(InterfaceD.class, new FactoryD1(), Integer.class);

        injector.getObject(InterfaceD.class);
    }

    @Test(expected = DependencyException.class)
    public void getObjectNotRegisteredDependenciesSingleton() throws DependencyException{
        injector.registerSingleton(InterfaceD.class, new FactoryD1(), Integer.class);

        injector.getObject(InterfaceD.class);
    }

    @Test(expected = DependencyException.class)
    public void registerFactoryWithBadArguments() throws DependencyException{
        injector.registerConstant(Integer.class, 0);
        injector.registerFactory(InterfaceD.class, new FactoryD1(), String.class);

        injector.getObject(InterfaceD.class);
    }

    @Test(expected = DependencyException.class)
    public void registerSingletonWithBadArguments() throws DependencyException{
        injector.registerConstant(Integer.class, 0);
        injector.registerSingleton(InterfaceD.class, new FactoryD1(), String.class);

        injector.getObject(InterfaceD.class);
    }

    @Test(expected = DependencyException.class)
    public void getObjectWithDependencyCycle() throws DependencyException{
        injector.registerFactory(InterfaceA.class, new FactoryA1(), InterfaceB.class, InterfaceC.class);
        injector.registerFactory(InterfaceD.class, new FactoryD1(), InterfaceA.class);
        injector.registerSingleton(InterfaceC.class, new FactoryC1(), InterfaceD.class);

        injector.getObject(InterfaceA.class);
    }

    @Test
    public void correctSingleton() throws DependencyException{
        registerConstants();
        registerSingletons();

        InterfaceA a1 = injector.getObject(InterfaceA.class);
        InterfaceA a2 = injector.getObject(InterfaceA.class);
        assertTrue(a1 == a2);
    }

    @Test
    public void FactoryisnotSingleton() throws DependencyException{
        registerConstants();
        registerFactories();

        InterfaceA a1 = injector.getObject(InterfaceA.class);
        InterfaceA a2 = injector.getObject(InterfaceA.class);
        assertFalse(a1 == a2);
    }


    private void registerConstants() throws DependencyException {
        injector.registerConstant(Integer.class, 42);
        injector.registerConstant(String.class, "El sentido de la vida, el universo y todo lo demas");
    }

    private void registerFactories() throws DependencyException {
        injector.registerFactory(InterfaceA.class, new FactoryA1(), InterfaceB.class, InterfaceC.class);
        injector.registerFactory(InterfaceB.class, new FactoryB1(), InterfaceD.class);
        injector.registerFactory(InterfaceC.class, new FactoryC1(), String.class);
        injector.registerFactory(InterfaceD.class, new FactoryD1(), Integer.class);
    }

    private void registerSingletons() throws DependencyException {
        injector.registerSingleton(InterfaceA.class, new FactoryA1(), InterfaceB.class, InterfaceC.class);
        injector.registerSingleton(InterfaceB.class, new FactoryB1(), InterfaceD.class);
        injector.registerSingleton(InterfaceC.class, new FactoryC1(), String.class);
        injector.registerSingleton(InterfaceD.class, new FactoryD1(), Integer.class);
    }

}