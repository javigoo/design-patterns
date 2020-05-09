package complex;

import common.DependencyException;
import java.util.HashMap;

public class Container implements Injector {

    private HashMap<Class, Object> constants;
    private HashMap<Class, complex.Factory> factories;
    private HashMap<Class, complex.Factory> singletons;
    private HashMap<Class, Object> singletonInstances;
    private HashMap<Class, Class[]> dependencies;

    public Container() {
        this.constants = new HashMap<>();
        this.factories = new HashMap<>();
        this.singletons = new HashMap<>();
        this.singletonInstances = new HashMap<>();
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
     *
     * A partir d’aquest moment, les subseqüents crides a getObject donat aquest nom retornaran la mateixa
     * instància creada.
     *
     * Fixeu-vos que en el moment de fer l’enregistrament no podem crear la instància, doncs podria ser que no
     * totes les dependències estiguin ja enregistrades.
     *
     * @param name          Nom Singleton
     * @param creator       Objectes creats
     * @param parameters    Noms objectes
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
     * Depenent de si el nom està associat a una constant, a una factoria, a un singleton, es retorna
     * (o es crea, mitjançant el mecanisme explicat anteriorment) l’objecte associat al nom.
     *
     * @param name Nom objecte
     * @throws DependencyException Si el nom no està té enregistrat
     */
    public <E> E getObject(Class<E> name) throws DependencyException {
        if (this.constants.containsKey(name)) {
            System.out.println("\nCONSTANT\n");
            return (E) this.constants.get(name);
        } else if (this.factories.containsKey(name)) {
            return (E) this.makeFactory(name);
        } else if (this.singletons.containsKey(name)) {
            return (E) this.getSingleton(name);
        } else {
            throw new DependencyException(name + " no enregistrat");
        }
    }

    private <E> boolean alreadyRegistered(Class<E> name) {
        return this.constants.containsKey(name) || this.factories.containsKey(name) || this.singletons.containsKey(name);
    }

    private <E> Object makeFactory(Class<E> name) throws DependencyException {
        try {
            complex.Factory creator;
            creator = this.factories.get(name);
            Object[] str1 = new Object[this.dependencies.get(name).length];
            for (int i = 0; i < this.dependencies.get(name).length; i++) {
                str1[i] = this.getObject(this.dependencies.get(name)[i]);
            }
            return creator.create(str1);
        } catch (DependencyException ex) {
            throw new DependencyException(ex);
        }
    }

    private <E> Object getSingleton(Class<E> name) throws DependencyException {
        try {
            if (singletons.get(name)==null){
                complex.Factory creator;
                creator = this.singletons.get(name);
                Object[] str1 = new Object[this.dependencies.get(name).length];
                for (int i=0; i<this.dependencies.get(name).length; i++){
                    str1[i] = this.getObject(this.dependencies.get(name)[i]);
                }
                singletonInstances.put(name, (Factory) creator.create(str1));
            }
            return singletonInstances.get(name);
        } catch (DependencyException ex) {
            throw new DependencyException(ex);
        }
    }
}
