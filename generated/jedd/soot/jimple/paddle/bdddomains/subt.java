package soot.jimple.paddle.bdddomains;

import jedd.*;
import soot.*;

public class subt extends Attribute {
    public final TypeDomain domain = (TypeDomain) TypeDomain.v();
    
    public Domain domain() { return domain; }
    
    public static Attribute v() { return instance; }
    
    private static Attribute instance = new subt();
    
    public subt() { super(); }
}
