package slim3.it.controller.blog;

import org.slim3.controller.JDOController;
import org.slim3.controller.Navigation;
import org.slim3.util.LongUtil;

import slim3.it.model.Blog;

import com.google.appengine.api.datastore.Key;

public class DeleteController extends JDOController {

    @Override
    public Navigation run() {
        Key key =
            key(Blog.class, LongUtil
                .toPrimitiveLong(request.getAttribute("id")));
        Blog blog = pm.getObjectById(Blog.class, key);
        pm.deletePersistent(blog);
        return redirect(basePath);
    }
}