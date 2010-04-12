package org.slim3.meta;

//@javax.annotation.Generated(value = { "slim3-gen", "@VERSION@" }, date = "2010-04-12 13:50:20")
/** */
public final class Slim3ModelMeta extends org.slim3.datastore.ModelMeta<org.slim3.model.Slim3Model> {

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<org.slim3.model.Slim3Model, com.google.appengine.api.datastore.Key> key = new org.slim3.datastore.CoreAttributeMeta<org.slim3.model.Slim3Model, com.google.appengine.api.datastore.Key>(this, "__key__", "key", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.StringAttributeMeta<org.slim3.model.Slim3Model> prop1 = new org.slim3.datastore.StringAttributeMeta<org.slim3.model.Slim3Model>(this, "prop1", "prop1");

    private static final Slim3ModelMeta slim3_singleton = new Slim3ModelMeta();

    /**
     * @return the singleton
     */
    public static Slim3ModelMeta get() {
       return slim3_singleton;
    }

    /** */
    public Slim3ModelMeta() {
        super("Slim3Model", org.slim3.model.Slim3Model.class);
    }

    @Override
    public org.slim3.model.Slim3Model entityToModel(com.google.appengine.api.datastore.Entity entity) {
        org.slim3.model.Slim3Model model = new org.slim3.model.Slim3Model();
        model.setKey(entity.getKey());
        model.setProp1((java.lang.String) entity.getProperty("prop1"));
        return model;
    }

    @Override
    public com.google.appengine.api.datastore.Entity modelToEntity(java.lang.Object model) {
        org.slim3.model.Slim3Model m = (org.slim3.model.Slim3Model) model;
        com.google.appengine.api.datastore.Entity entity = null;
        if (m.getKey() != null) {
            entity = new com.google.appengine.api.datastore.Entity(m.getKey());
        } else {
            entity = new com.google.appengine.api.datastore.Entity(kind);
        }
        entity.setProperty("prop1", m.getProp1());
        return entity;
    }

    @Override
    protected com.google.appengine.api.datastore.Key getKey(Object model) {
        org.slim3.model.Slim3Model m = (org.slim3.model.Slim3Model) model;
        return m.getKey();
    }

    @Override
    protected void setKey(Object model, com.google.appengine.api.datastore.Key key) {
        validateKey(key);
        org.slim3.model.Slim3Model m = (org.slim3.model.Slim3Model) model;
        m.setKey(key);
    }

    @Override
    protected long getVersion(Object model) {
        throw new IllegalStateException("The version property of the model(org.slim3.model.Slim3Model) is not defined.");
    }

    @Override
    protected void incrementVersion(Object model) {
    }

}