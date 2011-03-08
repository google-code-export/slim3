package slim3.demo.controller.blobstore;

import java.util.Map;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;

import slim3.demo.model.Blobstore;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.Key;

public class UploadController extends Controller {

    @Override
    public Navigation run() throws Exception {
        BlobstoreService bs = BlobstoreServiceFactory.getBlobstoreService();
        Map<String, BlobKey> blobs = bs.getUploadedBlobs(request);
        BlobKey blobKey = blobs.get("formFile");
        if (blobKey != null) {
            Key key =
                Datastore.createKey(Blobstore.class, blobKey.getKeyString());
            Blobstore blobstore = new Blobstore();
            blobstore.setKey(key);
            Datastore.put(blobstore);
        }
        return redirect(basePath);
    }
}
