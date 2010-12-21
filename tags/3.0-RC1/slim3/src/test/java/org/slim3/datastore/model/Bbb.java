package org.slim3.datastore.model;

import java.io.Serializable;

import org.slim3.datastore.Model;
import org.slim3.datastore.ModelRef;

/**
 * @author higa
 * 
 */
@Model
public class Bbb extends Aaa implements Serializable {

    private static final long serialVersionUID = 1L;

    private ModelRef<Hoge> hogeRef = new ModelRef<Hoge>(Hoge.class);

    private ModelRef<Hoge> hoge2Ref = new ModelRef<Hoge>(Hoge.class);

    /**
     * @return the hogeRef
     */
    public ModelRef<Hoge> getHogeRef() {
        return hogeRef;
    }

    /**
     * @return the hogeRef
     */
    public ModelRef<Hoge> getHoge2Ref() {
        return hoge2Ref;
    }
}