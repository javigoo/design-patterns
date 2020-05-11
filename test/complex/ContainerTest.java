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

public class ContainerTest {

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
        injector.registerConstant(String.class, "El sentido de la vida, el universo y todo lo demas");
        injector.registerFactory(InterfaceA.class, new FactoryA1(), InterfaceB.class, InterfaceC.class);
        injector.getObject(InterfaceA.class);
    }

    @Test(expected = DependencyException.class)
    public void registerFactoryWithBadArguments() throws DependencyException{
        injector.registerConstant(Integer.class, 0);
        injector.registerFactory(InterfaceD.class, new FactoryD1(), String.class);
        injector.getObject(InterfaceD.class);
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


    // TODO
    // Repetir test para cada factory (ok)
    // DependencyException:
    // • Enregistrar un nom que ja té una constant / factoria / singleton enregistrat (ok)
    // • Intentar crear un objecte sota un nom no enregistrat (ok)
    // • Intentar crear un objecte que no té totes les seves dependències enregistrades
    // • Intentar crear un objecte que forma part d’un cicle de dependències. Per simplificar, la detecció de
    //   cicles es realitzarà en el mètode getObject.
    // • Passar a una factoria un objecte d’un tipus inadequat per a ser utilitzar al constructor de la classe.
    // Comprobar ciclos de dependencies}
    // Test singleton

}