package org.slim3.datastore.server.meta;

import com.google.appengine.api.datastore.Key;

//@javax.annotation.Generated(value = { "slim3-gen", "null" }, date = "2009-11-09 15:30:15")
/**
 * @author higa
 * 
 */
public final class CccMeta extends
        org.slim3.datastore.ModelMeta<org.slim3.datastore.shared.model.Ccc> {

    /**
     * 
     */
    public CccMeta() {
        super("Aaa", org.slim3.datastore.shared.model.Ccc.class);
    }

    /**
     * 
     */
    public org.slim3.datastore.CoreAttributeMeta<org.slim3.datastore.shared.model.Ccc, com.google.appengine.api.datastore.Key> key =
        new org.slim3.datastore.CoreAttributeMeta<org.slim3.datastore.shared.model.Ccc, com.google.appengine.api.datastore.Key>(
            this,
            "__key__",
            "key",
            com.google.appengine.api.datastore.Key.class);

    /**
     * 
     */
    public org.slim3.datastore.CoreAttributeMeta<org.slim3.datastore.shared.model.Ccc, java.lang.Integer> schemaVersion =
        new org.slim3.datastore.CoreAttributeMeta<org.slim3.datastore.shared.model.Ccc, java.lang.Integer>(
            this,
            "schemaVersion",
            "schemaVersion",
            java.lang.Integer.class);

    /**
     * 
     */
    public org.slim3.datastore.CoreAttributeMeta<org.slim3.datastore.shared.model.Ccc, java.lang.Long> version =
        new org.slim3.datastore.CoreAttributeMeta<org.slim3.datastore.shared.model.Ccc, java.lang.Long>(
            this,
            "version",
            "version",
            java.lang.Long.class);

    @Override
    protected Key getKey(Object model) {
        org.slim3.datastore.shared.model.Ccc m =
            (org.slim3.datastore.shared.model.Ccc) model;
        return m.getKey();
    }

    @Override
    protected void setKey(Object model,
            com.google.appengine.api.datastore.Key key) {
        org.slim3.datastore.shared.model.Ccc m =
            (org.slim3.datastore.shared.model.Ccc) model;
        m.setKey(key);
    }

    @Override
    protected long getVersion(Object model) {
        org.slim3.datastore.shared.model.Ccc m =
            (org.slim3.datastore.shared.model.Ccc) model;
        return m.getVersion() != null ? m.getVersion().longValue() : 0L;
    }

    @Override
    protected void incrementVersion(Object model) {
        org.slim3.datastore.shared.model.Ccc m =
            (org.slim3.datastore.shared.model.Ccc) model;
        long version = m.getVersion() != null ? m.getVersion().longValue() : 0L;
        m.setVersion(Long.valueOf(version + 1L));
    }

    @Override
    public org.slim3.datastore.shared.model.Ccc entityToModel(
            com.google.appengine.api.datastore.Entity entity) {
        org.slim3.datastore.shared.model.Ccc model =
            new org.slim3.datastore.shared.model.Ccc();
        model.setKey(entity.getKey());
        model.setSchemaVersion(longToInteger((java.lang.Long) entity
            .getProperty("schemaVersion")));
        model.setVersion((java.lang.Long) entity.getProperty("version"));
        return model;
    }

    @Override
    public com.google.appengine.api.datastore.Entity modelToEntity(
            java.lang.Object model) {
        org.slim3.datastore.shared.model.Ccc m =
            (org.slim3.datastore.shared.model.Ccc) model;
        com.google.appengine.api.datastore.Entity entity = null;
        if (m.getKey() != null) {
            entity = new com.google.appengine.api.datastore.Entity(m.getKey());
        } else {
            entity = new com.google.appengine.api.datastore.Entity(kind);
        }
        entity.setProperty("schemaVersion", m.getSchemaVersion());
        entity.setProperty("version", m.getVersion());
        return entity;
    }
}