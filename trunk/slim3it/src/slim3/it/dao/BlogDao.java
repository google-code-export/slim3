package slim3.it.dao;

import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import org.slim3.jdo.GenericDao;
import org.slim3.util.BeanMap;

import slim3.it.model.Blog;
import slim3.it.model.BlogMeta;

public class BlogDao extends GenericDao<Blog> {

    @SuppressWarnings("unused")
    private static final BlogMeta m = new BlogMeta();

    @SuppressWarnings("unused")
    private static final Logger logger =
        Logger.getLogger(BlogDao.class.getName());

    public BlogDao(PersistenceManager pm) {
        super(pm, Blog.class);
    }

    public List<BeanMap> findTopTenAsMapList() {
        return toMapList(from().range(0, 10).getResultList());
    }
}