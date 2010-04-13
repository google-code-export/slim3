/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.slim3.datastore;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityTranslator;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.apphosting.api.DatastorePb.PutRequest;
import com.google.storage.onestore.v3.OnestoreEntity.EntityProto;

/**
 * A class for journal.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public class Journal {

    /**
     * The kind of journal entity.
     */
    public static final String KIND = "slim3.Journal";

    /**
     * The globalTransactionKey property name.
     */
    public static final String GLOBAL_TRANSACTION_KEY_PROPERTY =
        "globalTransactionKey";

    /**
     * The content property name.
     */
    public static final String CONTENT_PROPERTY = "content";

    /**
     * The putList property prefix.
     */
    public static final String PUT_LIST_PROPERTY = "putList";

    /**
     * The deleteList property prefix.
     */
    public static final String DELETE_LIST_PROPERTY = "deleteList";

    /**
     * Applies the journals.
     * 
     * @param globalTransactionKey
     *            the global transaction key
     * @throws NullPointerException
     *             if the globalTransactionKey parameter is null
     * 
     */
    public static void apply(Key globalTransactionKey)
            throws NullPointerException {
        if (globalTransactionKey == null) {
            throw new NullPointerException(
                "The globalTransactionKey parameter must not be null.");
        }
        List<Entity> entities =
            Datastore.query(KIND).filter(
                GLOBAL_TRANSACTION_KEY_PROPERTY,
                FilterOperator.EQUAL,
                globalTransactionKey).asList();
        apply(entities);
    }

    /**
     * Applies the journals.
     * 
     * @param entities
     *            the entities
     * @throws NullPointerException
     *             if the entities parameter is null
     * 
     */
    @SuppressWarnings("unchecked")
    public static void apply(List<Entity> entities) throws NullPointerException {
        if (entities == null) {
            throw new NullPointerException(
                "The entities parameter must not be null.");
        }
        for (Entity entity : entities) {
            PutRequest putReq = new PutRequest();
            List<Blob> putList =
                (List<Blob>) entity.getProperty(PUT_LIST_PROPERTY);
            List<Key> deleteList =
                (List<Key>) entity.getProperty(DELETE_LIST_PROPERTY);
            if (putList != null) {
                for (Blob blob : putList) {
                    EntityProto proto = putReq.addEntity();
                    proto.mergeFrom(blob.getBytes());
                }
            }
            if (putReq.entitySize() > 0) {
                DatastoreUtil.put(putReq);
            }
            if (deleteList != null) {
                Datastore.deleteWithoutTx(deleteList);
            }
            Datastore.deleteWithoutTx(entity.getKey());
        }
    }

    /**
     * Applies the journals.
     * 
     * @param tx
     *            the transaction
     * @param journals
     *            the journals
     * @throws NullPointerException
     *             if the tx parameter is null or if the journals parameter is
     *             null
     * 
     */
    public static void apply(Transaction tx, Map<Key, Entity> journals)
            throws NullPointerException {
        if (tx == null) {
            throw new NullPointerException("The tx parameter must not be null.");
        }
        if (journals == null) {
            throw new NullPointerException(
                "The journals parameter must not be null.");
        }
        List<Entity> putList = new ArrayList<Entity>();
        List<Key> deleteList = new ArrayList<Key>();
        for (Key key : journals.keySet()) {
            Entity entity = journals.get(key);
            if (entity != null) {
                putList.add(entity);
            } else {
                deleteList.add(key);
            }
        }
        DatastoreUtil.put(tx, putList);
        DatastoreUtil.delete(tx, deleteList);
    }

    /**
     * Puts the journals to the datastore.
     * 
     * @param globalTransactionKey
     *            the global transaction key
     * @param journalMap
     *            the map of journals
     * @return journal entities
     * @throws NullPointerException
     *             if the globalTransactionKey parameter is null or if the
     *             journalMap parameter is null
     * 
     */
    public static List<Entity> put(Key globalTransactionKey,
            Map<Key, Entity> journalMap) throws NullPointerException {
        if (journalMap == null) {
            throw new NullPointerException(
                "The journalMap parameter must not be null.");
        }
        List<Entity> entities = new ArrayList<Entity>();
        if (journalMap.size() == 0) {
            return entities;
        }
        int totalSize = 0;
        Entity entity = createEntity(globalTransactionKey);
        List<Blob> putList = new ArrayList<Blob>();
        List<Key> deleteList = new ArrayList<Key>();
        for (Key key : journalMap.keySet()) {
            Entity targetEntity = journalMap.get(key);
            boolean put = targetEntity != null;
            EntityProto targetProto =
                put ? EntityTranslator.convertToPb(targetEntity) : null;
            int size = put ? targetProto.encodingSize() : 0;
            if (totalSize != 0
                && totalSize + size + DatastoreUtil.EXTRA_SIZE > DatastoreUtil.MAX_ENTITY_SIZE) {
                entity.setUnindexedProperty(PUT_LIST_PROPERTY, putList);
                entity.setUnindexedProperty(DELETE_LIST_PROPERTY, deleteList);
                Datastore.putWithoutTx(entity);
                entities.add(entity);
                entity = createEntity(globalTransactionKey);
                putList = new ArrayList<Blob>();
                deleteList = new ArrayList<Key>();
                totalSize = 0;
            }
            if (put) {
                byte[] content = new byte[targetProto.encodingSize()];
                targetProto.outputTo(content, 0);
                putList.add(new Blob(content));
            } else {
                deleteList.add(key);
            }
            totalSize += size + DatastoreUtil.EXTRA_SIZE;
        }
        entity.setUnindexedProperty(PUT_LIST_PROPERTY, putList);
        entity.setUnindexedProperty(DELETE_LIST_PROPERTY, deleteList);
        Datastore.putWithoutTx(entity);
        entities.add(entity);
        return entities;
    }

    /**
     * Deletes entities specified by the global transaction key in transaction.
     * 
     * @param globalTransactionKey
     *            the global transaction key
     * @throws NullPointerException
     *             if the globalTransactionKey parameter is null
     */
    public static void deleteInTx(Key globalTransactionKey)
            throws NullPointerException {
        if (globalTransactionKey == null) {
            throw new NullPointerException(
                "The globalTransactionKey parameter must not be null.");
        }
        List<Key> keys = getKeys(globalTransactionKey);
        for (Key key : keys) {
            deleteInTx(globalTransactionKey, key);
        }
    }

    /**
     * Returns the keys specified by the global transaction key.
     * 
     * @param globalTransactionKey
     *            the global transaction key
     * @return a list of keys
     * @throws NullPointerException
     *             if the globalTransactionKey parameter is null
     * 
     */
    protected static List<Key> getKeys(Key globalTransactionKey)
            throws NullPointerException {
        if (globalTransactionKey == null) {
            throw new NullPointerException(
                "The globalTransactionKey parameter must not be null.");
        }
        return Datastore.query(KIND).filter(
            GLOBAL_TRANSACTION_KEY_PROPERTY,
            FilterOperator.EQUAL,
            globalTransactionKey).asKeyList();
    }

    /**
     * Creates an entity.
     * 
     * @param globalTransactionKey
     *            the global transaction key
     * @return an entity
     * @throws NullPointerException
     *             if the globalTransactionKey parameter is null
     */
    protected static Entity createEntity(Key globalTransactionKey)
            throws NullPointerException {
        if (globalTransactionKey == null) {
            throw new NullPointerException(
                "The globalTransactionKey parameter must not be null.");
        }
        Entity entity = new Entity(Datastore.allocateId(KIND));
        entity.setProperty(
            GLOBAL_TRANSACTION_KEY_PROPERTY,
            globalTransactionKey);
        return entity;
    }

    /**
     * Deletes an entity specified by the key in transaction.
     * 
     * @param globalTransactionKey
     *            the global transaction key
     * @param key
     *            the key
     * 
     * @throws NullPointerException
     *             if the globalTransactionKey parameter is null or if the key
     *             parameter is null
     */
    protected static void deleteInTx(Key globalTransactionKey, Key key)
            throws NullPointerException {
        if (globalTransactionKey == null) {
            throw new NullPointerException(
                "The globalTransactionKey parameter must not be null.");
        }
        if (key == null) {
            throw new NullPointerException(
                "The key parameter must not be null.");
        }
        for (int i = 0; i < DatastoreUtil.MAX_RETRY; i++) {
            Transaction tx = Datastore.beginTransaction();
            try {
                Entity entity = Datastore.getOrNull(tx, key);
                if (entity != null
                    && globalTransactionKey.equals(entity
                        .getProperty(GLOBAL_TRANSACTION_KEY_PROPERTY))) {
                    Datastore.delete(tx, key);
                    tx.commit();
                }
                return;
            } catch (ConcurrentModificationException e) {
                continue;
            } finally {
                if (tx.isActive()) {
                    tx.rollback();
                }
            }
        }
    }

    /**
     * Constructor.
     * 
     */
    private Journal() {
    }
}