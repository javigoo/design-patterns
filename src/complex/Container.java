package complex;

import common.DependencyException;

import java.util.HashMap;

public class Container implements Injector {

    private HashMap<Class, Object> registeredObjects;
    private HashMap<Class, complex.Factory> factoriesMap;
    private HashMap<Class, Class[]> dependenciesMap;

    public Container() {
        this.registeredObjects = new HashMap<>();
        this.factoriesMap = new HashMap<>();
        this.dependenciesMap = new HashMap<>();

    }

    /**
     * Associa el nom al valor, de manera que quan es demani getObject donat el nom, es retornarà aquest valor
     *
     * @param name  Nom constant
     * @param value Valor constant
     * @throws DependencyException Si ja té una constant enregistrat
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
     * @param name Nom factoria
     * @param creator Objectes creats
     * @param parameters Noms objectes
     *
     * @throws DependencyException Si ja té una factoria enregistrada
     */
    public <E> void registerFactory(Class<E> name, Factory<? extends E> creator, Class<?>... parameters) throws DependencyException {
        if (this.factoriesMap.containsKey(name)) {
            throw new DependencyException(name + " ja té una factoria enregistrada");
        } else {
            this.factoriesMap.put(name, creator);
            this.dependenciesMap.put(name, parameters);
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
     * @param name Nom objecte
     *
     * @throws DependencyException
     */
    public <E> E getObject(Class<E> name) throws DependencyException {
        if (this.registeredObjects.containsKey(name)){
            return (E) this.registeredObjects.get(name);
        }
        else if(this.factoriesMap.containsKey(name)){
            return (E) this.makeFactory(name);
        }
        else{
            throw new DependencyException(name + " no enregistrat");
        }
    }

    private <E> Object makeFactory(Class<E> name) throws DependencyException{
        try{
            complex.Factory creator;
            creator = this.factoriesMap.get(name);
            Object[] str1 = new Object[this.dependenciesMap.get(name).length];
            for (int i=0; i<this.dependenciesMap.get(name).length; i++){
                str1[i] = this.getObject(this.dependenciesMap.get(name)[i]);
            }
            return creator.create(str1);
        }catch(DependencyException ex){
            throw new DependencyException(ex);
        }
    }
}
