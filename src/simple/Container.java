package simple;

import java.util.HashMap;

import common.DependencyException;

public class Container implements Injector{

    
    private HashMap<String,Object> registeredObjects;
    private HashMap<String,simple.Factory> FactoriesMap;
    private HashMap<String,String[]> dependencesMap;
    private boolean DEBUG = true;

    public Container(){
        this.registeredObjects = new HashMap<>();
        this.FactoriesMap = new HashMap<>();
        this.dependencesMap = new HashMap<>();
    }

    public void registerConstant(String name, Object value) throws DependencyException {
        // Associa el nom al valor, de manera que quan es demani getObject donat el nom,
        // es retornarà aquest valor
        if (this.registeredObjects.containsKey(name) || this.FactoriesMap.containsKey(name)){  // Comprueba si el nombre se encuentra en algon HashMap
            if (DEBUG) System.err.println("ERROR: '" + name + "' constant is already registered.");
            throw new DependencyException(name + " constant is already registered.");
        } else{
            this.registeredObjects.put(name, value);
            if (DEBUG) System.out.println("Key '" + name + "' registered with value '" + value + "'");
        }
    }

    public void registerFactory(String name, Factory creator, String... parameters) throws DependencyException {
        if (this.FactoriesMap.containsKey(name) || this.registeredObjects.containsKey(name)){  // Comprueba si el nombre se encuentra en algon HashMap
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
    }

    public Object getObject(String name) throws DependencyException {
        if (this.registeredObjects.containsKey(name)){
            return this.registeredObjects.get(name);
        }
        else if(this.FactoriesMap.containsKey(name)){
            return this.FactoriesMap.get(name);            
        }
        else{
            throw new DependencyException(name + " has not been registered.");
        }        
    }
}
