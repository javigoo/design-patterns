package common;

public class DependencyException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DependencyException(Exception cause) {
        super(cause);
    }

    public DependencyException(String message) {
        super(message);
    }

    // DependencyException representa les situacions errònies que es poden donar a l’usar el contenidor:
    // • Enregistrar un nom que ja té una constant / factoria / singleton enregistrat
    // • Intentar crear un objecte sota un nom no enregistrat
    // • Intentar crear un objecte que no té totes les seves dependències enregistrades
    // • Intentar crear un objecte que forma part d’un cicle de dependències. Per simplificar, la detecció de
    //   cicles es realitzarà en el mètode getObject.
    // • Passar a una factoria un objecte d’un tipus inadequat per a ser utilitzar al constructor de la classe.
}
