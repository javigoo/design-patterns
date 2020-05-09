package complex;

import common.DependencyException;
import complex.factories.FactoryD1;
import implementations.ImplementationD1;
import interfaces.InterfaceD;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContainerTest {

    private Injector injector;

    @Before
    public void ContainerTest() throws DependencyException {
        injector = new Container();
    }

    @Test
    public void FactoryD1() throws DependencyException {
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
    // Repetir test para cada factory
    // DependencyException:
    // • Enregistrar un nom que ja té una constant / factoria / singleton enregistrat (ok)
    // • Intentar crear un objecte sota un nom no enregistrat (ok)
    // • Intentar crear un objecte que no té totes les seves dependències enregistrades
    // • Intentar crear un objecte que forma part d’un cicle de dependències. Per simplificar, la detecció de
    //   cicles es realitzarà en el mètode getObject.
    // • Passar a una factoria un objecte d’un tipus inadequat per a ser utilitzar al constructor de la classe.
    // Comprobar ciclos de dependencies}

}