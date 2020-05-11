package complex;

import common.DependencyException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Container implements Injector {

    private HashMap<Class<?>, Object> constants;
    private HashMap<Class<?>, complex.Factory<?>> factories;
    private HashMap<Class<?>, complex.Factory<?>> singletons;
    private HashMap<Class<?>, Class<?>[]> dependencies;

    public Container() {
        this.constants = new HashMap<>();
        this.factories = new HashMap<>();
        this.singletons = new HashMap<>();
        this.dependencies = new HashMap<>();

    }

    /**
     * Associa el nom al valor, de manera que quan es demani getObject donat el nom, es retornarà aquest valor
     *
     * @param name  Nom constant
     * @param value Valor constant
     * @throws DependencyException Si ja té una constant enregistrat
     */
    public <E> void registerConstant(Class<E> name, E value) throws DependencyException {
        if (alreadyRegistered(name)) {
            throw new DependencyException(name + " ja té una constant enregistrada");
        }
        this.constants.put(name, value);
    }

    /**
     * Associa el nom a la factoria de manera que cada vegada que es demani getObject donat aquest nom,
     * s’usarà la instància enregistrada de factoria (a la que se li passaran com arguments els objectes creats,
     * pel mateix contenidor, amb els noms indicats al vector de paràmetres) per a crear la nova instància.
     *
     * @param name       Nom factoria
     * @param creator    Objectes creats
     * @param parameters Noms objectes
     * @throws DependencyException Si ja té una factoria enregistrada
     */
    public <E> void registerFactory(Class<E> name, Factory<? extends E> creator, Class<?>... parameters) throws DependencyException {
        if (alreadyRegistered(name)) {
            throw new DependencyException(name + " ja té una factoria enregistrada");
        } else {
            this.factories.put(name, creator);
            this.dependencies.put(name, parameters);
        }
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
     * @param name       Nom Singleton
     * @param creator    Objectes creats
     * @param parameters Noms objectes
     * @throws DependencyException Si ja té un singleton enregistrat
     */
    public <E> void registerSingleton(Class<E> name, Factory<? extends E> creator, Class<?>... parameters) throws DependencyException {
        if (alreadyRegistered(name)) {
            throw new DependencyException(name + " ja té un singleton enregistrat");
        } else {
            this.singletons.put(name, creator);
            this.dependencies.put(name, parameters);
        }
    }

    /**
     * Dependiendo de si el nombre esta asociado a una constante, a una factoria, o a un singleton, retorna
     * ( o se crea, mediante el mecanismo explicado anteriormente) el objeto asociado a name.
     *
     * @param name Nom objecte
     * @throws DependencyException Si el nom no està té enregistrat
     */
    public <E> E getObject(Class<E> name) throws DependencyException {
        if (existsDependenciesCycle(name)) {
            throw new DependencyException("Attempted to create an object which belongs to a dependency cycle");
        }

        if (this.constants.containsKey(name)) {
            return (E) this.constants.get(name);
        } else if (this.factories.containsKey(name)) {
            return (E) this.getFactory(name);
        } else if (this.singletons.containsKey(name)) {
            return (E) this.getSingleton(name);
        } else {
            throw new DependencyException(name + " no enregistrat");
        }
    }

    /**
     * Comprova si el nom ja està registrat
     *
     * @param name Nom objecte
     */
    private <E> boolean alreadyRegistered(Class<E> name) {
        return this.constants.containsKey(name) || this.factories.containsKey(name) || this.singletons.containsKey(name);
    }


    /**
     * Crea una nova instància i li agrega les dependències associades
     *
     * @param name Nom objecte
     */
    private <E> Object getFactory(Class<E> name) throws DependencyException {
        try {
            complex.Factory<?> creator;
            creator = this.factories.get(name);

            Object[] dependenciesList = getDependencies(this.dependencies.get(name));
            Object factoryInstance = creator.create(dependenciesList);

            return factoryInstance;
        } catch (DependencyException ex) {
            throw new DependencyException(ex);
        }
    }

    /**
     * Crea la instància de factory, l'afegeix a constants i elimina factory de singletons.
     *
     * @param name Nom objecte
     */
    private <E> Object getSingleton(Class<E> name) throws DependencyException {
        try {
            complex.Factory<?> creator;
            creator = this.singletons.get(name);

            Object[] dependenciesList = getDependencies(this.dependencies.get(name));
            Object singletonInstance = creator.create(dependenciesList);

            constants.put(name, singletonInstance);
            singletons.remove(name);

            return singletonInstance;
        } catch (DependencyException ex) {
            throw new DependencyException(ex);
        }
    }

    /**
     * Retorna totes les dependències de l'objecte passat com a paràmetre
     *
     * @param classes Nom objecte
     */
    private Object[] getDependencies(Class<?>[] classes) throws DependencyException {
        Object[] dependenciesList = new Object[classes.length];
        for (int i = 0; i < classes.length; i++) {
            dependenciesList[i] = this.getObject(classes[i]);
        }
        return dependenciesList;
    }

    /**
     * Indica si hi ha un cicle de dependències
     *
     * @param name Nom objecte
     */
    private <E> boolean existsDependenciesCycle(Class<E> name) throws DependencyException {
        return existsDependenciesCycle(name, new ArrayList<>());
    }

    /**
     * Si es troba en constants no hi haurà cicle, si no, es comprova si ja s'ha visitat i
     * si cap de les seves dependències posseeix un cicle de dependències.
     *
     * @param actualName
     * @param visited
     * @param <E>
     * @return true si existe un ciclo de dependencias desde actualName
     * @throws DependencyException
     */
    private <E> boolean existsDependenciesCycle(Class<E> actualName, List<Class<E>> visited) throws DependencyException {
        if (!alreadyRegistered(actualName)) {
            throw new DependencyException("Attempted to create an object which doesn't have all dependencies created");
        } else if (constants.containsKey(actualName)) {
            return false;
        }
        if (visited.contains(actualName)) {
            return true;
        } else {
            visited.add(actualName);
            for (Class<?> dependencyToVisit : dependencies.get(actualName)) {
                if (existsDependenciesCycle((Class<E>) dependencyToVisit, visited)) {
                    return true;
                }
            }
        }
        return false;
    }

}
