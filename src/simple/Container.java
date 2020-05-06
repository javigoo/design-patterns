package simple;

import java.util.HashMap;

import common.DependencyException;

public class Container implements Injector{

    
    private HashMap<String,Object> registeredObjects;
    private HashMap<String,simple.Factory> FactoriesMap;
    private HashMap<String,simple.Factory> SingletonMap;
    private HashMap<String,Object> SingletonInstanceMap;
    private HashMap<String,String[]> dependencesMap;
    private boolean DEBUG = true;

    public Container(){
        this.registeredObjects = new HashMap<>();
        this.FactoriesMap = new HashMap<>();
        this.SingletonMap = new HashMap<>();
        this.dependencesMap = new HashMap<>();
    }

    public void registerConstant(String name, Object value) throws DependencyException {
        // Associa el nom al valor, de manera que quan es demani getObject donat el nom,
        // es retornarà aquest valor
        if (this.registeredObjects.containsKey(name) || this.FactoriesMap.containsKey(name) || this.SingletonMap.containsKey(name)){  // Comprueba si el nombre se encuentra en algon HashMap
            if (DEBUG) System.err.println("ERROR: '" + name + "' constant is already registered.");
            throw new DependencyException(name + " constant is already registered.");
        } else{
            this.registeredObjects.put(name, value);
            if (DEBUG) System.out.println("Key '" + name + "' registered with value '" + value + "'");
        }
    }

    public void registerFactory(String name, Factory creator, String... parameters) throws DependencyException {
        if (this.FactoriesMap.containsKey(name) || this.registeredObjects.containsKey(name) || this.SingletonMap.containsKey(name)){  // Comprueba si el nombre se encuentra en algon HashMap
            if (DEBUG) System.err.println("ERROR: '" + name + "' factory is already registered.");
            throw new DependencyException(name + " factory is already registered.");
        }else{
            if (DEBUG) System.out.println("Trying to register a factory with name: '" + name + "'");
            this.FactoriesMap.put(name, creator);
            if (DEBUG) System.out.println("Successfull factory register with FactoryName: '" + name + "'");
            if (DEBUG) System.out.println("Trying to register a factory dependences with FactoryName: '" + name + "'");
            this.dependencesMap.put(name, parameters);
            if (DEBUG) System.out.println("Successfull dependences register for Factory: '" + name + "'");
        }
    }

    public void registerSingleton(String name, Factory creator, String... parameters) throws DependencyException {
        // Associa el nom a la factoria de manera que quan es demani per primera vegada
        // getObject donat aquest nom,
        // s’usarà la instància enregistrada de factoria (a la que se li passaran com
        // arguments els objectes creats,
        // pel mateix contenidor, amb els noms indicats al vector de paràmetres) per a
        // crear la nova instància.

        // A partir d’aquest moment, les subseqüents crides a getObject donat aquest nom
        // retornaran la mateixa
        // instància creada.

        // Fixeu-vos que en el moment de fer l’enregistrament no podem crear la
        // instància, doncs podria ser que no
        // totes les dependències estiguin ja enregistrades.

        if (this.FactoriesMap.containsKey(name) || this.registeredObjects.containsKey(name) || this.SingletonMap.containsKey(name)){  // Comprueba si el nombre se encuentra en algon HashMap
            if (DEBUG) System.err.println("ERROR: '" + name + "' factory is already registered.");
            throw new DependencyException(name + " factory is already registered.");
        }else{
            if (DEBUG) System.out.println("Trying to register a factory like Singleton with name: '" + name + "'");
            this.SingletonMap.put(name, creator);
            if (DEBUG) System.out.println("Successfull factory register with FactoryName: '" + name + "'");
            if (DEBUG) System.out.println("Trying to register a factory dependences with FactoryName: '" + name + "'");
            this.dependencesMap.put(name, parameters);
            if (DEBUG) System.out.println("Successfull dependences register for Factory: '" + name + "'");
        }
    }

    public Object getObject(String name) throws DependencyException {
        if (this.registeredObjects.containsKey(name)){
            return this.registeredObjects.get(name);
        }
        else if(this.FactoriesMap.containsKey(name)){
            return this.makeFactory(name);            
        }
        else if(this.SingletonMap.containsKey(name)){
            return this.getSingletonFactory(name);            
        }
        else{
            throw new DependencyException(name + " has not been registered.");
        }        
    }

    private Object getSingletonFactory(String name) throws DependencyException {
        try{
            if (SingletonInstanceMap.get(name)==null){
                Factory creator;
                creator = this.SingletonMap.get(name);     
                Object[] str1 = new Object[this.dependencesMap.get(name).length];
                for (int i=0; i<this.dependencesMap.get(name).length; i++){
                    str1[i] = this.getObject(this.dependencesMap.get(name)[i]);
                }
                SingletonInstanceMap.put(name, creator.create(str1));
            }
            return SingletonInstanceMap.get(name);
        }catch(DependencyException ex){
            if (DEBUG) System.err.println("ERROR: Something whent wrong trying to make '" + name + "' factory");
            throw new DependencyException(ex);
        }
    }

    private Object makeFactory(String name) throws DependencyException { // Cada vez que pedimos ese nombre, nos crea una instancia de Factory.
        try{
            Factory creator;
            creator = this.FactoriesMap.get(name);
            Object[] str1 = new Object[this.dependencesMap.get(name).length];
            for (int i=0; i<this.dependencesMap.get(name).length; i++){
                str1[i] = this.getObject(this.dependencesMap.get(name)[i]);
            }
            return creator.create(str1);    // Nos retorna una nueva instancia de esa Factory con los parámetros asignados.
        }catch(DependencyException ex){
                if (DEBUG) System.err.println("ERROR: Something whent wrong trying to make '" + name + "' factory");
                throw new DependencyException(ex);
        }
    }
}
