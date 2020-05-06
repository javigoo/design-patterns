package complex;

import common.DependencyException;

import java.util.HashMap;

public class Container implements Injector {

    private HashMap<Class, Object> registeredObjects;

    public Container(){
        this.registeredObjects = new HashMap<>();

    }

    /**
     * Associa el nom al valor, de manera que quan es demani getObject donat el nom, es retornarà aquest valor
     *
     * @param name
     * @param value
     * @param <E>
     * @throws DependencyException
     */
    public <E> void registerConstant(Class<E> name, E value) throws DependencyException {
        if (this.registeredObjects.containsKey(name)) {
            throw new DependencyException(name + " ja té una constant enregistrat");
        }
        this.registeredObjects.put(name, value);

    }

    /**
     * Associa el nom a la factoria de manera que cada vegada que es demani getObject donat aquest nom,
     * s’usarà la instància enregistrada de factoria (a la que se li passaran com arguments els objectes creats,
     * pel mateix contenidor, amb els noms indicats al vector de paràmetres) per a crear la nova instància.
     *
     * @param name
     * @param creator
     * @param parameters
     * @param <E>
     * @throws DependencyException
     */
    public <E> void registerFactory(Class<E> name, Factory<? extends E> creator, Class<?>... parameters) throws DependencyException {

    }

    /**
     * Associa el nom a la factoria de manera que quan es demani per primera vegada getObject donat aquest nom,
     * s’usarà la instància enregistrada de factoria (a la que se li passaran com arguments els objectes creats,
     * pel mateix contenidor, amb els noms indicats al vector de paràmetres) per a crear la nova instància.
     * <p>
     * A partir d’aquest moment, les subseqüents crides a getObject donat aquest nom retornaran la mateixa
     * instància creada.
     * <p>
     * Fixeu-vos que en el moment de fer l’enregistrament no podem crear la instància, doncs podria ser que no
     * totes les dependències estiguin ja enregistrades.
     *
     * @param name
     * @param creator
     * @param parameters
     * @param <E>
     * @throws DependencyException
     */
    public <E> void registerSingleton(Class<E> name, Factory<? extends E> creator, Class<?>... parameters) throws DependencyException {

    }

    /**
     * Depenent de si el nom està associat a una constant, a una factoria, a un singleton, es retorna
     * (o es crea, mitjançant el mecanisme explicat anteriorment) l’objecte associat al nom.
     *
     * @param name
     * @param <E>
     * @return
     * @throws DependencyException
     */
    public <E> E getObject(Class<E> name) throws DependencyException {
        return null;
    }
}
