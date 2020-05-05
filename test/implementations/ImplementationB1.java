package implementations;

import interfaces.InterfaceB;
import interfaces.InterfaceD;

public class ImplementationB1 implements InterfaceB {
    public InterfaceD d;
    public ImplementationB1(InterfaceD d){
        this.d = d;
    }
    // ....
}
