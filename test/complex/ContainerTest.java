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
    public void newContainer() throws DependencyException {
        injector = new Container();
    }

    @Test
    public void containerFactoryA1() throws DependencyException {
        injector.registerConstant(Integer.class, 42);
        injector.registerConstant(String.class, "El sentido de la vida, el universo y todo lo demas");

        injector.registerFactory(InterfaceA.class, new FactoryA1(), InterfaceB.class,InterfaceC.class);
        injector.registerFactory(InterfaceB.class, new FactoryB1(), InterfaceD.class);
        injector.registerFactory(InterfaceC.class, new FactoryC1(), String.class);
        injector.registerFactory(InterfaceD.class, new FactoryD1(), Integer.class);

        InterfaceA a = injector.getObject(InterfaceA.class);
        assertThat(a, is(instanceOf(ImplementationA1.class)));

        ImplementationA1 a1 = (ImplementationA1) a;
        assertThat(a1.b, is(instanceOf(ImplementationB1.class)));
        assertThat(a1.c, is(instanceOf(ImplementationC1.class)));
    }

    @Test
    public void containerFactoryB1() throws DependencyException {
        injector.registerConstant(Integer.class, 42);

        injector.registerFactory(InterfaceB.class, new FactoryB1(), InterfaceD.class);
        injector.registerFactory(InterfaceD.class, new FactoryD1(), Integer.class);

        InterfaceB b = injector.getObject(InterfaceB.class);
        assertThat(b, is(instanceOf(ImplementationB1.class)));

        ImplementationB1 b1 = (ImplementationB1) b;
        assertThat(b1.d, is(instanceOf(ImplementationD1.class)));
    }

    @Test
    public void containerFactoryC1() throws DependencyException {
        injector.registerConstant(String.class, "El sentido de la vida, el universo y todo lo demas");

        injector.registerFactory(InterfaceC.class, new FactoryC1(), String.class);

        InterfaceC c = injector.getObject(InterfaceC.class);
        assertThat(c, is(instanceOf(ImplementationC1.class)));

        ImplementationC1 c1 = (ImplementationC1) c;
        assertThat(c1.getS(), is("El sentido de la vida, el universo y todo lo demas"));
    }

    @Test
    public void containerFactoryD1() throws DependencyException {
        injector.registerConstant(Integer.class, 42);

        injector.registerFactory(InterfaceD.class, new FactoryD1(), Integer.class);

        InterfaceD d = injector.getObject(InterfaceD.class);
        assertThat(d, is(instanceOf(ImplementationD1.class)));

        ImplementationD1 d1 = (ImplementationD1) d;
        assertThat(d1.getI(), is(42));
    }


    // Test TODO
    //
    // Test singleton
    // Repetir test para cada factory (ok)
    // DependencyException:
    // • Enregistrar un nom que ja té una constant / factoria / singleton enregistrat (ok)
    // • Intentar crear un objecte sota un nom no enregistrat (ok)
    // • Intentar crear un objecte que no té totes les seves dependències enregistrades
    // • Intentar crear un objecte que forma part d’un cicle de dependències. Per simplificar, la detecció de
    //   cicles es realitzarà en el mètode getObject.
    // • Passar a una factoria un objecte d’un tipus inadequat per a ser utilitzar al constructor de la classe.
    // Comprobar ciclos de dependencies}

}