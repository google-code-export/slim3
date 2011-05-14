package slim3.demo.controller.gtx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;
import org.slim3.datastore.GlobalTransaction;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Transaction;

public class IndexController extends Controller {

    private static int COUNT = 5;

    @Override
    public Navigation run() throws Exception {
        List<Map<String, Long>> list = new ArrayList<Map<String, Long>>(COUNT);
        for (int entityGroups = 1; entityGroups <= COUNT; entityGroups++) {
            Map<String, Long> map = new HashMap<String, Long>();
            long start = System.currentTimeMillis();
            for (int i = 0; i < entityGroups; i++) {
                Transaction tx = Datastore.beginTransaction();
                Datastore.put(tx, new Entity("Hoge"));
                tx.commit();
            }
            long time = System.currentTimeMillis() - start;
            map.put("tx", time);

            start = System.currentTimeMillis();
            GlobalTransaction gtx = Datastore.beginGlobalTransaction();
            for (int i = 0; i < entityGroups; i++) {
                gtx.put(new Entity("Hoge"));
            }
            gtx.commit();
            time = System.currentTimeMillis() - start;
            map.put("gtx", time);
            list.add(map);
        }
        requestScope("gtxResultList", list);
        Datastore.delete(Datastore.query("Hoge").asKeyList());
        return forward("index.jsp");
    }
}