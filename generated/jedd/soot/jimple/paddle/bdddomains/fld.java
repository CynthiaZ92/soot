package soot.jimple.paddle.bdddomains;

import jedd.*;
import soot.*;

public class fld extends Attribute {
    public final FieldDomain domain = (FieldDomain) FieldDomain.v();
    
    public Domain domain() { return domain; }
    
    public static Attribute v() { return instance; }
    
    private static Attribute instance = new fld();
    
    public fld() { super(); }
}
