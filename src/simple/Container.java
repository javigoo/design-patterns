package simple;

import common.DependencyException;

public class Container {

    void registerConstant(String name, Object value){

        // Associa el nom al valor, de manera que quan es demani getObject donat el nom, es retornarà aquest valor
    }

    void registerFactory(String name, Factory creator, String... parameters) throws DependencyException{

        // Associa el nom a la factoria de manera que cada vegada que es demani getObject donat aquest nom,
        // s’usarà la instància enregistrada de factoria (a la que se li passaran com arguments els objectes creats,
        // pel mateix contenidor, amb els noms indicats al vector de paràmetres) per a crear la nova instància.
    }

    void registerSingleton(String name, Factory creator, String... parameters) throws DependencyException{
        // Associa el nom a la factoria de manera que quan es demani per primera vegada getObject donat aquest nom,
        // s’usarà la instància enregistrada de factoria (a la que se li passaran com arguments els objectes creats,
        // pel mateix contenidor, amb els noms indicats al vector de paràmetres) per a crear la nova instància.

        // A partir d’aquest moment, les subseqüents crides a getObject donat aquest nom retornaran la mateixa
        // instància creada.

        // Fixeu-vos que en el moment de fer l’enregistrament no podem crear la instància, doncs podria ser que no
        // totes les dependències estiguin ja enregistrades.
    }

    Object getObject(String name) throws DependencyException{
        // Depenent de si el nom està associat a una constant, a una factoria, a un singleton, es retorna
        // (o es crea, mitjançant el mecanisme explicat anteriorment) l’objecte associat al nom.

        return new Object();
    }
}
