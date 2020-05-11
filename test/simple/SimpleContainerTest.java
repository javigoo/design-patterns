package simple;

import common.DependencyException;
import implementations.*;
import interfaces.*;
import org.junit.Before;
import org.junit.Test;
import simple.factories.*;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SimpleContainerTest {

    private Injector injector;

    @Before
    public void newContainer() {
        injector = new Container();
    }

    @Test
    public void containerFactoryA1() throws DependencyException {
        registerConstants();
        registerFactories();

        InterfaceA a = (InterfaceA) injector.getObject("InterfaceA");
        assertThat(a, is(instanceOf(ImplementationA1.class)));

        ImplementationA1 a1 = (ImplementationA1) a;
        assertThat(a1.b, is(instanceOf(ImplementationB1.class)));
        assertThat(a1.c, is(instanceOf(ImplementationC1.class)));
    }

    @Test
    public void containerFactoryB1() throws DependencyException {
        registerConstants();
        registerFactories();

        InterfaceB b = (InterfaceB) injector.getObject("InterfaceB");
        assertThat(b, is(instanceOf(ImplementationB1.class)));

        ImplementationB1 b1 = (ImplementationB1) b;
        assertThat(b1.d, is(instanceOf(ImplementationD1.class)));
    }

    @Test
    public void containerFactoryC1() throws DependencyException {
        registerConstants();
        registerFactories();

        InterfaceC c = (InterfaceC) injector.getObject("InterfaceC");
        assertThat(c, is(instanceOf(ImplementationC1.class)));

        ImplementationC1 c1 = (ImplementationC1) c;
        assertThat(c1.getS(), is("El sentido de la vida, el universo y todo lo demas"));
    }

    @Test
    public void containerFactoryD1() throws DependencyException {
        registerConstants();
        registerFactories();

        InterfaceD d = (InterfaceD) injector.getObject("InterfaceD");
        assertThat(d, is(instanceOf(ImplementationD1.class)));

        ImplementationD1 d1 = (ImplementationD1) d;
        assertThat(d1.getI(), is(42));
    }

    @Test
    public void instanceOfFactoryA1Singleton() throws DependencyException{
        registerConstants();
        registerSingletons();

        InterfaceA a = (InterfaceA) injector.getObject("InterfaceA");
        assertThat(a, is(instanceOf(ImplementationA1.class)));
        ImplementationA1 a1 = (ImplementationA1) a;
        assertThat(a1.b, is(instanceOf(ImplementationB1.class)));
        assertThat(a1.c, is(instanceOf(ImplementationC1.class)));
    }

    @Test
    public void instanceOfFactoryB1Singleton() throws DependencyException{
        registerConstants();
        registerSingletons();

        InterfaceB b = (InterfaceB) injector.getObject("InterfaceB");
        assertThat(b, is(instanceOf(ImplementationB1.class)));
        ImplementationB1 b1 = (ImplementationB1) b;
        assertThat(b1.d, is(instanceOf(ImplementationD1.class)));
    }

    @Test
    public void instanceOfFactoryC1Singleton() throws DependencyException{
        registerConstants();
        registerSingletons();

        InterfaceC c = (InterfaceC) injector.getObject("InterfaceC");
        assertThat(c, is(instanceOf(ImplementationC1.class)));
        ImplementationC1 c1 = (ImplementationC1) c;
        assertThat(c1.s,is("El sentido de la vida, el universo y todo lo demas"));
    }

    @Test
    public void instanceOfFactoryD1Singleton() throws DependencyException{
        registerConstants();
        registerSingletons();

        InterfaceD d = (InterfaceD) injector.getObject("InterfaceD");
        assertThat(d, is(instanceOf(ImplementationD1.class)));
        ImplementationD1 d1 = (ImplementationD1) d;
        assertThat(d1.i, is(42));
    }


    @Test(expected = DependencyException.class)
    public void alreadyRegisteredConstant() throws DependencyException{
        injector.registerConstant("Integer", 0);
        injector.registerConstant("Integer", 0);
    }

    @Test(expected = DependencyException.class)
    public void alreadyRegisteredFactory() throws DependencyException{
        injector.registerFactory("InterfaceD", new FactoryD1(), "Integer");
        injector.registerFactory("InterfaceD", new FactoryD1(), "Integer");
    }

    @Test(expected = DependencyException.class)
    public void alreadyRegisteredSingleton() throws DependencyException{
        injector.registerSingleton("InterfaceD", new FactoryD1(), "Integer");
        injector.registerSingleton("InterfaceD", new FactoryD1(), "Integer");
    }

    @Test(expected = DependencyException.class)
    public void getObjectNotRegisteredName() throws DependencyException{
        injector.getObject("Integer");
    }

    @Test(expected = DependencyException.class)
    public void getObjectNotRegisteredDependenciesFactory() throws DependencyException{
        injector.registerFactory("InterfaceD", new simple.factories.FactoryD1(), "Integer");

        injector.getObject("InterfaceD");
    }

    @Test(expected = DependencyException.class)
    public void getObjectNotRegisteredDependenciesSingleton() throws DependencyException{
        injector.registerSingleton("InterfaceD", new simple.factories.FactoryD1(), "Integer");

        injector.getObject("InterfaceD");
    }

    @Test(expected = DependencyException.class)
    public void registerFactoryWithBadArguments() throws DependencyException{
        injector.registerConstant("Integer", 0);
        injector.registerFactory("InterfaceD", new FactoryD1(), "String");
        injector.getObject("InterfaceD");
    }


    @Test(expected = DependencyException.class)
    public void registerSingletonWithBadArguments() throws DependencyException{
        injector.registerConstant("Integer", 0);
        injector.registerSingleton("InterfaceD", new simple.factories.FactoryD1(), "String");

        injector.getObject("InterfaceD");
    }

    @Test(expected = DependencyException.class)
    public void getObjectWithDependencyCycle() throws DependencyException{
        injector.registerFactory("InterfaceA", new simple.factories.FactoryA1(), "InterfaceB", "InterfaceC");
        injector.registerFactory("InterfaceD", new simple.factories.FactoryD1(), "InterfaceA");
        injector.registerSingleton("InterfaceC", new simple.factories.FactoryC1(), "InterfaceD");

        injector.getObject("InterfaceA");
    }


    private void registerConstants() throws DependencyException {
        injector.registerConstant("Integer", 42);
        injector.registerConstant("String", "El sentido de la vida, el universo y todo lo demas");
    }

    private void registerFactories() throws DependencyException {
        injector.registerFactory("InterfaceA", new simple.factories.FactoryA1(), "InterfaceB", "InterfaceC");
        injector.registerFactory("InterfaceB", new simple.factories.FactoryB1(), "InterfaceD");
        injector.registerFactory("InterfaceC", new simple.factories.FactoryC1(), "String");
        injector.registerFactory("InterfaceD", new simple.factories.FactoryD1(), "Integer");
    }

    private void registerSingletons() throws DependencyException {
        injector.registerSingleton("InterfaceA", new simple.factories.FactoryA1(), "InterfaceB", "InterfaceC");
        injector.registerSingleton("InterfaceB", new simple.factories.FactoryB1(), "InterfaceD");
        injector.registerSingleton("InterfaceC", new simple.factories.FactoryC1(), "String");
        injector.registerSingleton("InterfaceD", new simple.factories.FactoryD1(), "Integer");
    }


}