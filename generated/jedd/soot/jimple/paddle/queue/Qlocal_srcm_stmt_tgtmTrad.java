package soot.jimple.paddle.queue;

import soot.util.*;
import soot.jimple.paddle.bdddomains.*;
import soot.jimple.paddle.*;
import soot.jimple.toolkits.callgraph.*;
import soot.*;
import soot.util.queue.*;
import jedd.*;
import java.util.*;

public class Qlocal_srcm_stmt_tgtmTrad extends Qlocal_srcm_stmt_tgtm {
    private ChunkedQueue q = new ChunkedQueue();
    
    public void add(Local _local, SootMethod _srcm, Unit _stmt, SootMethod _tgtm) {
        q.add(_local);
        q.add(_srcm);
        q.add(_stmt);
        q.add(_tgtm);
    }
    
    public void add(final jedd.internal.RelationContainer in) {
        Iterator it =
          new jedd.internal.RelationContainer(new Attribute[] { local.v(), tgtm.v(), srcm.v(), stmt.v() },
                                              new PhysicalDomain[] { V1.v(), T2.v(), T1.v(), ST.v() },
                                              ("in.iterator(new jedd.Attribute[...]) at /tmp/soot-trunk/src/" +
                                               "soot/jimple/paddle/queue/Qlocal_srcm_stmt_tgtmTrad.jedd:39,2" +
                                               "2-24"),
                                              in).iterator(new Attribute[] { local.v(), srcm.v(), stmt.v(), tgtm.v() });
        while (it.hasNext()) {
            Object[] tuple = (Object[]) it.next();
            for (int i = 0; i < 4; i++) {
                add((Local) tuple[0], (SootMethod) tuple[1], (Unit) tuple[2], (SootMethod) tuple[3]);
            }
        }
    }
    
    public Rlocal_srcm_stmt_tgtm reader() { return new Rlocal_srcm_stmt_tgtmTrad(q.reader()); }
    
    public Qlocal_srcm_stmt_tgtmTrad() { super(); }
}
