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

import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.slim3.util.CipherFactory;
import org.slim3.util.DoubleUtil;

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceConfig;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.KeyRange;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.utils.FutureWrapper;

/**
 * A delegate to access datastore service.
 * 
 * @author higa
 * @since 1.0.6
 * 
 */
public class DatastoreDelegate {

    /**
     * The key of deadline.
     */
    public static final String DEADLINE = "slim3.datastoreDeadline";

    /**
     * The deadline(seconds).
     */
    protected Double deadline;

    /**
     * The datastore service.
     */
    protected DatastoreService ds;

    /**
     * The asynchronous datastore service.
     */
    protected AsyncDatastoreService ads;

    /**
     * The datastore service configuration.
     */
    protected DatastoreServiceConfig dsConfig;

    /**
     * Constructor.
     */
    public DatastoreDelegate() {
        this(null);
    }

    /**
     * Constructor.
     * 
     * @param deadline
     *            the deadline
     */
    public DatastoreDelegate(Double deadline) {
        if (deadline == null) {
            deadline = DoubleUtil.toDouble(System.getProperty(DEADLINE));
        }
        this.deadline = deadline;
        setUpDatastoreServices();
    }

    /**
     * Returns the deadline.
     * 
     * @return the deadline
     */
    protected Double getDeadline() {
        return deadline;
    }

    /**
     * Returns the datastore service.
     * 
     * @return the datastore service
     */
    public DatastoreService getDatastoreService() {
        return ds;
    }

    /**
     * Returns the asynchronous datastore service.
     * 
     * @return the asynchronous datastore service
     */
    public AsyncDatastoreService getAsyncDatastoreService() {
        return ads;
    }

    /**
     * Creates datastore services.
     * 
     */
    protected void setUpDatastoreServices() {
        dsConfig = DatastoreServiceConfig.Builder.withDefaults();
        if (deadline != null) {
            dsConfig.deadline(deadline);
        }
        ds = DatastoreServiceFactory.getDatastoreService(dsConfig);
        ads = DatastoreServiceFactory.getAsyncDatastoreService(dsConfig);
    }

    /**
     * Begins a transaction.
     * 
     * @return a begun transaction
     */
    public Transaction beginTransaction() {
        return ds.beginTransaction();
    }

    /**
     * Commits the transaction.
     * 
     * @param tx
     *            the transaction
     * @throws NullPointerException
     *             if the tx parameter is null
     */
    public void commit(Transaction tx) throws NullPointerException {
        if (tx == null) {
            throw new NullPointerException("The tx parameter must not be null.");
        }
        tx.commit();
    }

    /**
     * Rolls back the transaction.
     * 
     * @param tx
     *            the transaction
     * @throws NullPointerException
     *             if the tx parameter is null
     */
    public void rollback(Transaction tx) throws NullPointerException {
        if (tx == null) {
            throw new NullPointerException("The tx parameter must not be null.");
        }
        tx.rollback();
    }

    /**
     * Returns the active transactions.
     * 
     * @return the active transactions
     */
    public Collection<Transaction> getActiveTransactions() {
        return DatastoreServiceFactory
            .getDatastoreService()
            .getActiveTransactions();
    }

    /**
     * Returns the current transaction. Returns null if there is no transaction.
     * 
     * @return the current transaction
     */
    public Transaction getCurrentTransaction() {
        return DatastoreServiceFactory
            .getDatastoreService()
            .getCurrentTransaction(null);
    }

    /**
     * Begins a global transaction.
     * 
     * @return a begun global transaction
     */
    public GlobalTransaction beginGlobalTransaction() {
        GlobalTransaction gtx = new GlobalTransaction(ds);
        gtx.begin();
        return gtx;
    }

    /**
     * Returns the active global transactions.
     * 
     * @return the active global transactions
     */
    public Collection<GlobalTransaction> getActiveGlobalTransactions() {
        return GlobalTransaction.getActiveTransactions();
    }

    /**
     * Returns the current global transaction. Returns null if there is no
     * transaction.
     * 
     * @return the current global transaction
     */
    public GlobalTransaction getCurrentGlobalTransaction() {
        return GlobalTransaction.getCurrentTransaction();
    }

    /**
     * Allocates a key within a namespace defined by the kind.
     * 
     * @param kind
     *            the kind
     * @return a key within a namespace defined by the kind
     * @throws NullPointerException
     *             if the kind parameter is null
     */
    public Key allocateId(String kind) throws NullPointerException {
        return DatastoreUtil.allocateId(ds, kind);
    }

    /**
     * Allocates a key within a namespace defined by the kind asynchronously.
     * 
     * @param kind
     *            the kind
     * @return a future key within a namespace defined by the kind
     * @throws NullPointerException
     *             if the kind parameter is null
     */
    public Future<Key> allocateIdAsync(String kind) throws NullPointerException {
        return DatastoreUtil.allocateIdAsync(ads, kind);
    }

    /**
     * Allocates a key within a namespace defined by the kind of the model.
     * 
     * @param modelClass
     *            the model class
     * @return a key within a namespace defined by the kind of the model
     * @throws NullPointerException
     *             if the modelClass parameter is null
     */
    public Key allocateId(Class<?> modelClass) throws NullPointerException {
        if (modelClass == null) {
            throw new NullPointerException("The modelClass parameter is null.");
        }
        return allocateId(getModelMeta(modelClass));
    }

    /**
     * Allocates a key within a namespace defined by the kind of the model
     * asynchronously.
     * 
     * @param modelClass
     *            the model class
     * @return a future key within a namespace defined by the kind of the model
     * @throws NullPointerException
     *             if the modelClass parameter is null
     */
    public Future<Key> allocateIdAsync(Class<?> modelClass)
            throws NullPointerException {
        if (modelClass == null) {
            throw new NullPointerException("The modelClass parameter is null.");
        }
        return allocateIdAsync(getModelMeta(modelClass));
    }

    /**
     * Allocates a key within a namespace defined by the kind of the model.
     * 
     * @param modelMeta
     *            the meta data of the model
     * @return a key within a namespace defined by the kind of the model
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     */
    public Key allocateId(ModelMeta<?> modelMeta) throws NullPointerException {
        if (modelMeta == null) {
            throw new NullPointerException("The modelMeta parameter is null.");
        }
        return allocateId(modelMeta.getKind());
    }

    /**
     * Allocates a key within a namespace defined by the kind of the model
     * asynchronously.
     * 
     * @param modelMeta
     *            the meta data of the model
     * @return a future key within a namespace defined by the kind of the model
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     */
    public Future<Key> allocateIdAsync(ModelMeta<?> modelMeta)
            throws NullPointerException {
        if (modelMeta == null) {
            throw new NullPointerException("The modelMeta parameter is null.");
        }
        return allocateIdAsync(modelMeta.getKind());
    }

    /**
     * Allocates a key within a namespace defined by the parent key and the
     * kind.
     * 
     * @param parentKey
     *            the parent key
     * @param kind
     *            the kind
     * @return a key within a namespace defined by the parent key and the kind
     * @throws NullPointerException
     *             if the parentKey parameter is null or if the kind parameter
     *             is null
     */
    public Key allocateId(Key parentKey, String kind)
            throws NullPointerException {
        return DatastoreUtil.allocateId(ds, parentKey, kind);
    }

    /**
     * Allocates a key within a namespace defined by the parent key and the kind
     * asynchronously.
     * 
     * @param parentKey
     *            the parent key
     * @param kind
     *            the kind
     * @return a future key within a namespace defined by the parent key and the
     *         kind
     * @throws NullPointerException
     *             if the parentKey parameter is null or if the kind parameter
     *             is null
     */
    public Future<Key> allocateIdAsync(Key parentKey, String kind)
            throws NullPointerException {
        return DatastoreUtil.allocateIdAsync(ads, parentKey, kind);
    }

    /**
     * Allocates a key within a namespace defined by the parent key and the kind
     * of the model.
     * 
     * @param parentKey
     *            the parent key
     * @param modelClass
     *            the model class
     * @return a key within a namespace defined by the parent key and the kind
     *         of the model
     * @throws NullPointerException
     *             if the parentKey parameter is null or if the modelClass
     *             parameter is null
     */
    public Key allocateId(Key parentKey, Class<?> modelClass)
            throws NullPointerException {
        if (modelClass == null) {
            throw new NullPointerException("The modelClass parameter is null.");
        }
        return allocateId(parentKey, getModelMeta(modelClass));
    }

    /**
     * Allocates a key within a namespace defined by the parent key and the kind
     * of the model asynchronously.
     * 
     * @param parentKey
     *            the parent key
     * @param modelClass
     *            the model class
     * @return a future key within a namespace defined by the parent key and the
     *         kind of the model
     * @throws NullPointerException
     *             if the parentKey parameter is null or if the modelClass
     *             parameter is null
     */
    public Future<Key> allocateIdAsync(Key parentKey, Class<?> modelClass)
            throws NullPointerException {
        if (modelClass == null) {
            throw new NullPointerException("The modelClass parameter is null.");
        }
        return allocateIdAsync(parentKey, getModelMeta(modelClass));
    }

    /**
     * Allocates a key within a namespace defined by the parent key and the kind
     * of the model.
     * 
     * @param parentKey
     *            the parent key
     * @param modelMeta
     *            the meta data of the model
     * @return a key within a namespace defined by the parent key and the kind
     *         of the model
     * @throws NullPointerException
     *             if the parentKey parameter is null or if the modelMeta
     *             parameter is null
     */
    public Key allocateId(Key parentKey, ModelMeta<?> modelMeta)
            throws NullPointerException {
        if (modelMeta == null) {
            throw new NullPointerException("The modelMeta parameter is null.");
        }
        return allocateId(parentKey, modelMeta.getKind());
    }

    /**
     * Allocates a key within a namespace defined by the parent key and the kind
     * of the model asynchronously.
     * 
     * @param parentKey
     *            the parent key
     * @param modelMeta
     *            the meta data of the model
     * @return a future key within a namespace defined by the parent key and the
     *         kind of the model
     * @throws NullPointerException
     *             if the parentKey parameter is null or if the modelMeta
     *             parameter is null
     */
    public Future<Key> allocateIdAsync(Key parentKey, ModelMeta<?> modelMeta)
            throws NullPointerException {
        if (modelMeta == null) {
            throw new NullPointerException("The modelMeta parameter is null.");
        }
        return allocateIdAsync(parentKey, modelMeta.getKind());
    }

    /**
     * Allocates keys within a namespace defined by the kind.
     * 
     * @param kind
     *            the kind
     * @param num
     *            the number of allocated keys
     * @return keys within a namespace defined by the kind
     * @throws NullPointerException
     *             if the kind parameter is null
     */
    public KeyRange allocateIds(String kind, long num)
            throws NullPointerException {
        return DatastoreUtil.allocateIds(ds, kind, num);
    }

    /**
     * Allocates keys within a namespace defined by the kind asynchronously.
     * 
     * @param kind
     *            the kind
     * @param num
     *            the number of allocated keys
     * @return future keys within a namespace defined by the kind
     * @throws NullPointerException
     *             if the kind parameter is null
     */
    public Future<KeyRange> allocateIdsAsync(String kind, long num)
            throws NullPointerException {
        return DatastoreUtil.allocateIdsAsync(ads, kind, num);
    }

    /**
     * Allocates keys within a namespace defined by the kind of the model.
     * 
     * @param modelClass
     *            the model class
     * @param num
     *            the number of allocated keys
     * @return keys within a namespace defined by the kind of the model
     * @throws NullPointerException
     *             if the modelClass parameter is null
     */
    public KeyRange allocateIds(Class<?> modelClass, long num)
            throws NullPointerException {
        if (modelClass == null) {
            throw new NullPointerException("The modelClass parameter is null.");
        }
        return allocateIds(getModelMeta(modelClass), num);
    }

    /**
     * Allocates keys within a namespace defined by the kind of the model
     * asynchronously.
     * 
     * @param modelClass
     *            the model class
     * @param num
     *            the number of allocated keys
     * @return future keys within a namespace defined by the kind of the model
     * @throws NullPointerException
     *             if the modelClass parameter is null
     */
    public Future<KeyRange> allocateIdsAsync(Class<?> modelClass, long num)
            throws NullPointerException {
        if (modelClass == null) {
            throw new NullPointerException("The modelClass parameter is null.");
        }
        return allocateIdsAsync(getModelMeta(modelClass), num);
    }

    /**
     * Allocates keys within a namespace defined by the kind of the model.
     * 
     * @param modelMeta
     *            the meta data of the model
     * @param num
     *            the number of allocated keys
     * @return keys within a namespace defined by the kind of the model
     */
    public KeyRange allocateIds(ModelMeta<?> modelMeta, long num) {
        if (modelMeta == null) {
            throw new NullPointerException("The modelMeta parameter is null.");
        }
        return allocateIds(modelMeta.getKind(), num);
    }

    /**
     * Allocates keys within a namespace defined by the kind of the model
     * asynchronously.
     * 
     * @param modelMeta
     *            the meta data of the model
     * @param num
     *            the number of allocated keys
     * @return future keys within a namespace defined by the kind of the model
     */
    public Future<KeyRange> allocateIdsAsync(ModelMeta<?> modelMeta, long num) {
        if (modelMeta == null) {
            throw new NullPointerException("The modelMeta parameter is null.");
        }
        return allocateIdsAsync(modelMeta.getKind(), num);
    }

    /**
     * Allocates keys within a namespace defined by the parent key and the kind.
     * 
     * @param parentKey
     *            the parent key
     * @param kind
     *            the kind
     * @param num
     * @return keys within a namespace defined by the parent key and the kind
     * @throws NullPointerException
     *             if the parentKey parameter is null or if the kind parameter
     *             is null
     */
    public KeyRange allocateIds(Key parentKey, String kind, int num)
            throws NullPointerException {
        return DatastoreUtil.allocateIds(ds, parentKey, kind, num);
    }

    /**
     * Allocates keys within a namespace defined by the parent key and the kind
     * asynchronously.
     * 
     * @param parentKey
     *            the parent key
     * @param kind
     *            the kind
     * @param num
     * @return future keys within a namespace defined by the parent key and the
     *         kind
     * @throws NullPointerException
     *             if the parentKey parameter is null or if the kind parameter
     *             is null
     */
    public Future<KeyRange> allocateIdsAsync(Key parentKey, String kind, int num)
            throws NullPointerException {
        return DatastoreUtil.allocateIdsAsync(ads, parentKey, kind, num);
    }

    /**
     * Allocates keys within a namespace defined by the parent key and the kind
     * of the model.
     * 
     * @param parentKey
     *            the parent key
     * @param modelClass
     *            the model class
     * @param num
     * @return keys within a namespace defined by the parent key and the kind of
     *         the model
     * @throws NullPointerException
     *             if the parentKey parameter is null or if the modelClass
     *             parameter is null
     */
    public KeyRange allocateIds(Key parentKey, Class<?> modelClass, int num)
            throws NullPointerException {
        if (modelClass == null) {
            throw new NullPointerException("The modelClass parameter is null.");
        }
        return allocateIds(parentKey, getModelMeta(modelClass), num);
    }

    /**
     * Allocates keys within a namespace defined by the parent key and the kind
     * of the model asynchronously.
     * 
     * @param parentKey
     *            the parent key
     * @param modelClass
     *            the model class
     * @param num
     * @return future keys within a namespace defined by the parent key and the
     *         kind of the model
     * @throws NullPointerException
     *             if the parentKey parameter is null or if the modelClass
     *             parameter is null
     */
    public Future<KeyRange> allocateIdsAsync(Key parentKey,
            Class<?> modelClass, int num) throws NullPointerException {
        if (modelClass == null) {
            throw new NullPointerException("The modelClass parameter is null.");
        }
        return allocateIdsAsync(parentKey, getModelMeta(modelClass), num);
    }

    /**
     * Allocates keys within a namespace defined by the parent key and the kind
     * of the model.
     * 
     * @param parentKey
     *            the parent key
     * @param modelMeta
     *            the meta data of the model
     * @param num
     * @return keys within a namespace defined by the parent key and the kind of
     *         the model
     * @throws NullPointerException
     *             if the parentKey parameter is null or if the modelMeta
     *             parameter is null
     */
    public KeyRange allocateIds(Key parentKey, ModelMeta<?> modelMeta, int num)
            throws NullPointerException {
        if (modelMeta == null) {
            throw new NullPointerException("The modelMeta parameter is null.");
        }
        return allocateIds(parentKey, modelMeta.getKind(), num);
    }

    /**
     * Allocates keys within a namespace defined by the parent key and the kind
     * of the model asynchronously.
     * 
     * @param parentKey
     *            the parent key
     * @param modelMeta
     *            the meta data of the model
     * @param num
     * @return future keys within a namespace defined by the parent key and the
     *         kind of the model
     * @throws NullPointerException
     *             if the parentKey parameter is null or if the modelMeta
     *             parameter is null
     */
    public Future<KeyRange> allocateIdsAsync(Key parentKey,
            ModelMeta<?> modelMeta, int num) throws NullPointerException {
        if (modelMeta == null) {
            throw new NullPointerException("The modelMeta parameter is null.");
        }
        return allocateIdsAsync(parentKey, modelMeta.getKind(), num);
    }

    /**
     * Creates a key.
     * 
     * @param kind
     *            the kind of entity
     * @param id
     *            the identifier
     * @return a key
     * @throws NullPointerException
     *             if the kind parameter is null
     */
    public Key createKey(String kind, long id) throws NullPointerException {
        if (kind == null) {
            throw new NullPointerException("The kind parameter is null.");
        }
        return KeyFactory.createKey(kind, id);
    }

    /**
     * Creates a key.
     * 
     * @param modelClass
     *            the model class
     * @param id
     *            the identifier
     * @return a key
     * @throws NullPointerException
     *             if the modelClass parameter is null
     */
    public Key createKey(Class<?> modelClass, long id)
            throws NullPointerException {
        if (modelClass == null) {
            throw new NullPointerException("The modelClass parameter is null.");
        }
        return createKey(getModelMeta(modelClass), id);
    }

    /**
     * Creates a key.
     * 
     * @param modelMeta
     *            the meta data of the model
     * @param id
     *            the identifier
     * @return a key
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     */
    public Key createKey(ModelMeta<?> modelMeta, long id)
            throws NullPointerException {
        if (modelMeta == null) {
            throw new NullPointerException("The modelMeta parameter is null.");
        }
        return createKey(modelMeta.getKind(), id);
    }

    /**
     * Creates a key.
     * 
     * @param kind
     *            the kind of entity
     * @param name
     *            the name
     * @return a key
     * @throws NullPointerException
     *             if the kind parameter is null or if the name parameter is
     *             null
     */
    public Key createKey(String kind, String name) throws NullPointerException {
        if (kind == null) {
            throw new NullPointerException("The kind parameter is null.");
        }
        if (name == null) {
            throw new NullPointerException("The name parameter is null.");
        }
        return KeyFactory.createKey(kind, name);
    }

    /**
     * Creates a key.
     * 
     * @param modelClass
     *            the model class
     * @param name
     *            the name
     * @return a key
     * @throws NullPointerException
     *             if the modeClass parameter is null or if the name parameter
     *             is null
     */
    public Key createKey(Class<?> modelClass, String name)
            throws NullPointerException {
        if (modelClass == null) {
            throw new NullPointerException("The modelClass parameter is null.");
        }
        return createKey(getModelMeta(modelClass), name);
    }

    /**
     * Creates a key.
     * 
     * @param modelMeta
     *            the meta data of the model
     * @param name
     *            the name
     * @return a key
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the name parameter
     *             is null
     */
    public Key createKey(ModelMeta<?> modelMeta, String name)
            throws NullPointerException {
        if (modelMeta == null) {
            throw new NullPointerException("The modelMeta parameter is null.");
        }
        return createKey(modelMeta.getKind(), name);
    }

    /**
     * Creates a key.
     * 
     * @param parentKey
     *            the parent key
     * @param kind
     *            the kind of entity
     * @param id
     *            the identifier
     * @return a key
     * @throws NullPointerException
     *             if the parentKey parameter is null or if the kind parameter
     *             is null
     */
    public Key createKey(Key parentKey, String kind, long id)
            throws NullPointerException {
        if (parentKey == null) {
            throw new NullPointerException("The parentKey parameter is null.");
        }
        if (kind == null) {
            throw new NullPointerException("The kind parameter is null.");
        }
        return KeyFactory.createKey(parentKey, kind, id);
    }

    /**
     * Creates a key.
     * 
     * @param parentKey
     *            the parent key
     * @param modelClass
     *            the model class
     * @param id
     *            the identifier
     * @return a key
     * @throws NullPointerException
     *             if the parentKey parameter is null or if the modelClass
     *             parameter is null
     */
    public Key createKey(Key parentKey, Class<?> modelClass, long id)
            throws NullPointerException {
        if (modelClass == null) {
            throw new NullPointerException("The modelClass parameter is null.");
        }
        return createKey(parentKey, getModelMeta(modelClass), id);
    }

    /**
     * Creates a key.
     * 
     * @param parentKey
     *            the parent key
     * @param modelMeta
     *            the meta data of the model
     * @param id
     *            the identifier
     * @return a key
     * @throws NullPointerException
     *             if the parentKey parameter is null or if the modelMeta
     *             parameter is null
     */
    public Key createKey(Key parentKey, ModelMeta<?> modelMeta, long id)
            throws NullPointerException {
        if (modelMeta == null) {
            throw new NullPointerException("The modelMeta parameter is null.");
        }
        return createKey(parentKey, modelMeta.getKind(), id);
    }

    /**
     * Creates a key.
     * 
     * @param parentKey
     *            the parent key
     * @param kind
     *            the kind of entity
     * @param name
     *            the name
     * @return a key
     * @throws NullPointerException
     *             if the parentKey parameter is null or if the kind parameter
     *             is null or if the name parameter is null
     */
    public Key createKey(Key parentKey, String kind, String name)
            throws NullPointerException {
        if (parentKey == null) {
            throw new NullPointerException("The parentKey parameter is null.");
        }
        if (kind == null) {
            throw new NullPointerException("The kind parameter is null.");
        }
        if (name == null) {
            throw new NullPointerException("The name parameter is null.");
        }
        return KeyFactory.createKey(parentKey, kind, name);
    }

    /**
     * Creates a key.
     * 
     * @param parentKey
     *            the parent key
     * @param modelClass
     *            the model class
     * @param name
     *            the name
     * @return a key
     * @throws NullPointerException
     *             if the parentKey is null or if the modelClass parameter is
     *             null or if the name parameter is null
     */
    public Key createKey(Key parentKey, Class<?> modelClass, String name)
            throws NullPointerException {
        if (modelClass == null) {
            throw new NullPointerException("The modelClass parameter is null.");
        }
        return createKey(parentKey, getModelMeta(modelClass), name);
    }

    /**
     * Creates a key.
     * 
     * @param parentKey
     *            the parent key
     * @param modelMeta
     *            the meta data of the model
     * @param name
     *            the name
     * @return a key
     * @throws NullPointerException
     *             if the parentKey is null or if the modelMeta parameter is
     *             null or if the name parameter is null
     */
    public Key createKey(Key parentKey, ModelMeta<?> modelMeta, String name)
            throws NullPointerException {
        if (modelMeta == null) {
            throw new NullPointerException("The modelMeta parameter is null.");
        }
        return createKey(parentKey, modelMeta.getKind(), name);
    }

    /**
     * Converts the key to an encoded string.
     * 
     * @param key
     *            the key
     * @return an encoded string
     * @throws NullPointerException
     *             if the key parameter is null
     */
    public String keyToString(Key key) throws NullPointerException {
        if (key == null) {
            throw new NullPointerException("The key parameter is null.");
        }
        return KeyFactory.keyToString(key);
    }

    /**
     * Converts the encoded string to a key.
     * 
     * @param encodedKey
     *            the encoded string
     * @return a key
     * @throws NullPointerException
     *             if the encodedKey parameter is null
     */
    public Key stringToKey(String encodedKey) throws NullPointerException {
        if (encodedKey == null) {
            throw new NullPointerException("The encodedKey parameter is null.");
        }
        return KeyFactory.stringToKey(encodedKey);
    }

    /**
     * Puts the unique value.
     * 
     * @param uniqueIndexName
     *            the unique index name
     * @param value
     *            the unique value
     * @return whether the unique value is put
     * @throws NullPointerException
     *             if the uniqueIndexName parameter is null or if the value
     *             parameter is null
     */
    public boolean putUniqueValue(String uniqueIndexName, String value)
            throws NullPointerException {
        if (uniqueIndexName == null) {
            throw new NullPointerException(
                "The uniqueIndexName parameter must not be null.");
        }
        if (value == null) {
            throw new NullPointerException(
                "The value parameter must not be null.");
        }
        Key key = createKey(uniqueIndexName, value);
        Transaction tx = beginTransaction();
        try {
            get(tx, key);
            return false;
        } catch (EntityNotFoundRuntimeException e) {
            Entity entity = new Entity(key);
            try {
                put(tx, entity);
                commit(tx);
                return true;
            } catch (ConcurrentModificationException ignore) {
                return false;
            }
        } finally {
            if (tx.isActive()) {
                rollback(tx);
            }
        }
    }

    /**
     * Deletes the unique value.
     * 
     * @param uniqueIndexName
     *            the unique index name
     * @param value
     *            the unique value
     * @throws NullPointerException
     *             if the uniqueIndexName parameter is null or if the value
     *             parameter is null
     */
    public void deleteUniqueValue(String uniqueIndexName, String value)
            throws NullPointerException {
        if (uniqueIndexName == null) {
            throw new NullPointerException(
                "The uniqueIndexName parameter must not be null.");
        }
        if (value == null) {
            throw new NullPointerException("The value parameter is null.");
        }
        Key key = createKey(uniqueIndexName, value);
        delete((Transaction) null, key);
    }

    /**
     * Returns an entity specified by the key. If there is a current
     * transaction, this operation will execute within that transaction.
     * 
     * @param key
     *            the key
     * @return an entity specified by the key
     * @throws NullPointerException
     *             if the key parameter is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     */
    public Entity get(Key key) throws NullPointerException,
            EntityNotFoundRuntimeException {
        return get(ds.getCurrentTransaction(null), key);
    }

    /**
     * Returns an entity specified by the key asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param key
     *            the key
     * @return a future entity specified by the key
     * @throws NullPointerException
     *             if the key parameter is null
     */
    public Future<Entity> getAsync(Key key) throws NullPointerException {
        return getAsync(ds.getCurrentTransaction(null), key);
    }

    /**
     * Returns a model specified by the key. If there is a current transaction,
     * this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @return a model specified by the key
     * @throws NullPointerException
     *             if the modelClass parameter is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> M get(Class<M> modelClass, Key key) throws NullPointerException,
            EntityNotFoundRuntimeException, IllegalArgumentException {
        return get(getModelMeta(modelClass), key);
    }

    /**
     * Returns a model specified by the key asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @return a future model specified by the key
     * @throws NullPointerException
     *             if the modelClass parameter is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> Future<M> getAsync(Class<M> modelClass, Key key)
            throws NullPointerException, EntityNotFoundRuntimeException,
            IllegalArgumentException {
        return getAsync(getModelMeta(modelClass), key);
    }

    /**
     * Returns a model specified by the key. If there is a current transaction,
     * this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @return a model specified by the key
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     */
    public <M> M get(ModelMeta<M> modelMeta, Key key)
            throws NullPointerException, EntityNotFoundRuntimeException {
        return get(ds.getCurrentTransaction(null), modelMeta, key);
    }

    /**
     * Returns a model specified by the key asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @return a future model specified by the key
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * 
     */
    public <M> Future<M> getAsync(final ModelMeta<M> modelMeta, final Key key)
            throws NullPointerException {
        return getAsync(ds.getCurrentTransaction(null), modelMeta, key);
    }

    /**
     * Returns an entity specified by the key. Returns null if no entity is
     * found. If there is a current transaction, this operation will execute
     * within that transaction.
     * 
     * @param key
     *            the key
     * @return an entity specified by the key
     * @throws NullPointerException
     *             if the key parameter is null
     */
    public Entity getOrNull(Key key) throws NullPointerException {
        return getOrNull(ds.getCurrentTransaction(null), key);
    }

    /**
     * Returns an entity specified by the key asynchronously. Returns null if no
     * entity is found. If there is a current transaction, this operation will
     * execute within that transaction.
     * 
     * @param key
     *            the key
     * @return a future entity specified by the key
     * @throws NullPointerException
     *             if the key parameter is null
     */
    public Future<Entity> getOrNullAsync(Key key) throws NullPointerException {
        return getOrNullAsync(ds.getCurrentTransaction(null), key);
    }

    /**
     * Returns a model specified by the key. Returns null if no entity is found.
     * If there is a current transaction, this operation will execute within
     * that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @return a model specified by the key
     * @throws NullPointerException
     *             if the modelClass parameter is null
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> M getOrNull(Class<M> modelClass, Key key)
            throws NullPointerException, IllegalArgumentException {
        return getOrNull(getModelMeta(modelClass), key);
    }

    /**
     * Returns a model specified by the key asynchronously. Returns null if no
     * entity is found. If there is a current transaction, this operation will
     * execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @return a future model specified by the key
     * @throws NullPointerException
     *             if the modelClass parameter is null
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> Future<M> getOrNullAsync(Class<M> modelClass, Key key)
            throws NullPointerException, IllegalArgumentException {
        return getOrNullAsync(getModelMeta(modelClass), key);
    }

    /**
     * Returns a model specified by the key. Returns null if no entity is found.
     * If there is a current transaction, this operation will execute within
     * that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @return a model specified by the key
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     * 
     */
    public <M> M getOrNull(ModelMeta<M> modelMeta, Key key)
            throws NullPointerException, IllegalArgumentException {
        return getOrNull(ds.getCurrentTransaction(null), modelMeta, key);
    }

    /**
     * Returns a model specified by the key asynchronously. Returns null if no
     * entity is found. If there is a current transaction, this operation will
     * execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @return a future model specified by the key
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     * 
     */
    public <M> Future<M> getOrNullAsync(ModelMeta<M> modelMeta, Key key)
            throws NullPointerException, IllegalArgumentException {
        return getOrNullAsync(ds.getCurrentTransaction(null), modelMeta, key);
    }

    /**
     * Returns an entity specified by the key without transaction.
     * 
     * @param key
     *            the key
     * @return an entity specified by the key
     * @throws NullPointerException
     *             if the key parameter is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     */
    public Entity getWithoutTx(Key key) throws NullPointerException,
            EntityNotFoundRuntimeException {
        return get((Transaction) null, key);
    }

    /**
     * Returns an entity specified by the key without transaction
     * asynchronously.
     * 
     * @param key
     *            the key
     * @return a future entity specified by the key
     * @throws NullPointerException
     *             if the key parameter is null
     */
    public Future<Entity> getWithoutTxAsync(Key key)
            throws NullPointerException {
        return getAsync((Transaction) null, key);
    }

    /**
     * Returns a model specified by the key without transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @return a model specified by the key
     * @throws NullPointerException
     *             if the modelClass parameter is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> M getWithoutTx(Class<M> modelClass, Key key)
            throws NullPointerException, EntityNotFoundRuntimeException,
            IllegalArgumentException {
        return getWithoutTx(getModelMeta(modelClass), key);
    }

    /**
     * Returns a model specified by the key without transaction asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @return a future model specified by the key
     * @throws NullPointerException
     *             if the modelClass parameter is null
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> Future<M> getWithoutTxAsync(Class<M> modelClass, Key key)
            throws NullPointerException, IllegalArgumentException {
        return getWithoutTxAsync(getModelMeta(modelClass), key);
    }

    /**
     * Returns a model specified by the key without transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @return a model specified by the key
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     */
    public <M> M getWithoutTx(ModelMeta<M> modelMeta, Key key)
            throws NullPointerException, EntityNotFoundRuntimeException {
        return get(null, modelMeta, key);
    }

    /**
     * Returns a model specified by the key without transaction asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @return a future model specified by the key
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * 
     */
    public <M> Future<M> getWithoutTxAsync(ModelMeta<M> modelMeta, Key key)
            throws NullPointerException {
        return getAsync(null, modelMeta, key);
    }

    /**
     * Returns an entity specified by the key without transaction. Returns null
     * if no entity is found.
     * 
     * @param key
     *            the key
     * @return an entity specified by the key
     * @throws NullPointerException
     *             if the key parameter is null
     */
    public Entity getOrNullWithoutTx(Key key) throws NullPointerException {
        return getOrNull((Transaction) null, key);
    }

    /**
     * Returns an entity specified by the key without transaction
     * asynchronously. Returns null if no entity is found.
     * 
     * @param key
     *            the key
     * @return a future entity specified by the key
     * @throws NullPointerException
     *             if the key parameter is null
     */
    public Future<Entity> getOrNullWithoutTxAsync(Key key)
            throws NullPointerException {
        return getOrNullAsync((Transaction) null, key);
    }

    /**
     * Returns a model specified by the key without transaction. Returns null if
     * no entity is found.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @return a model specified by the key
     * @throws NullPointerException
     *             if the modelClass parameter is null
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> M getOrNullWithoutTx(Class<M> modelClass, Key key)
            throws NullPointerException, IllegalArgumentException {
        return getOrNullWithoutTx(getModelMeta(modelClass), key);
    }

    /**
     * Returns a model specified by the key without transaction asynchronously.
     * Returns null if no entity is found.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @return a future model specified by the key
     * @throws NullPointerException
     *             if the modelClass parameter is null
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> Future<M> getOrNullWithoutTxAsync(Class<M> modelClass, Key key)
            throws NullPointerException, IllegalArgumentException {
        return getOrNullWithoutTxAsync(getModelMeta(modelClass), key);
    }

    /**
     * Returns a model specified by the key without transaction. Returns null if
     * no entity is found.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @return a model specified by the key
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> M getOrNullWithoutTx(ModelMeta<M> modelMeta, Key key)
            throws NullPointerException, IllegalArgumentException {
        return getOrNull((Transaction) null, modelMeta, key);
    }

    /**
     * Returns a model specified by the key without transaction asynchronously.
     * Returns null if no entity is found.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @return a future model specified by the key
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> Future<M> getOrNullWithoutTxAsync(ModelMeta<M> modelMeta, Key key)
            throws NullPointerException, IllegalArgumentException {
        return getOrNullAsync((Transaction) null, modelMeta, key);
    }

    /**
     * Returns a model specified by the key and checks the version. If there is
     * a current transaction, this operation will execute within that
     * transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @param version
     *            the version
     * @return a model specified by the key
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the key parameter
     *             is null or if the version parameter is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     * @throws ConcurrentModificationException
     *             if the version of the model is updated
     */
    public <M> M get(Class<M> modelClass, Key key, Long version)
            throws NullPointerException, EntityNotFoundRuntimeException,
            IllegalArgumentException, ConcurrentModificationException {
        if (modelClass == null) {
            throw new NullPointerException("The modelClass parameter is null.");
        }
        return get(getModelMeta(modelClass), key, version);
    }

    /**
     * Returns a model specified by the key and checks the version
     * asynchronously. If there is a current transaction, this operation will
     * execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @param version
     *            the version
     * @return a future model specified by the key
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the key parameter
     *             is null or if the version parameter is null
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     * @throws ConcurrentModificationException
     *             if the version of the model is updated
     */
    public <M> Future<M> getAsync(Class<M> modelClass, Key key, Long version)
            throws NullPointerException, IllegalArgumentException,
            ConcurrentModificationException {
        return getAsync(getModelMeta(modelClass), key, version);
    }

    /**
     * Returns a model specified by the key and checks the version. If there is
     * a current transaction, this operation will execute within that
     * transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @param version
     *            the version
     * @return a model specified by the key
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the key parameter is
     *             null or if the version parameter is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     * @throws ConcurrentModificationException
     *             if the version of the model is updated
     */
    public <M> M get(ModelMeta<M> modelMeta, Key key, Long version)
            throws NullPointerException, EntityNotFoundRuntimeException,
            IllegalArgumentException, ConcurrentModificationException {
        return get(ds.getCurrentTransaction(null), modelMeta, key, version);
    }

    /**
     * Returns a model specified by the key and checks the version
     * asynchronously. If there is a current transaction, this operation will
     * execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @param version
     *            the version
     * @return a future model specified by the key
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the key parameter is
     *             null or if the version parameter is null
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     * @throws ConcurrentModificationException
     *             if the version of the model is updated
     */
    public <M> Future<M> getAsync(ModelMeta<M> modelMeta, Key key, Long version)
            throws NullPointerException, IllegalArgumentException,
            ConcurrentModificationException {
        return getAsync(ds.getCurrentTransaction(null), modelMeta, key, version);
    }

    /**
     * Returns an entity specified by the key within the provided transaction.
     * 
     * @param tx
     *            the transaction
     * @param key
     *            the key
     * @return an entity specified by the key
     * @throws NullPointerException
     *             if the key parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key is found
     */
    public Entity get(Transaction tx, Key key) throws NullPointerException,
            IllegalStateException, EntityNotFoundRuntimeException {
        return DatastoreUtil.get(ds, tx, key);
    }

    /**
     * Returns an entity specified by the key within the provided transaction
     * asynchronously.
     * 
     * @param tx
     *            the transaction
     * @param key
     *            the key
     * @return a future entity specified by the key
     * @throws NullPointerException
     *             if the key parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public Future<Entity> getAsync(Transaction tx, Key key)
            throws NullPointerException, IllegalStateException {
        return DatastoreUtil.getAsync(ads, tx, key);
    }

    /**
     * Returns a model specified by the key within the provided transaction.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @return a model specified by the key
     * @throws NullPointerException
     *             if the modelClass parameter is null or the key parameter is
     *             null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> M get(Transaction tx, Class<M> modelClass, Key key)
            throws NullPointerException, IllegalStateException,
            EntityNotFoundRuntimeException, IllegalArgumentException {
        return get(tx, getModelMeta(modelClass), key);
    }

    /**
     * Returns a model specified by the key within the provided transaction
     * asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @return a future model specified by the key
     * @throws NullPointerException
     *             if the modelClass parameter is null or the key parameter is
     *             null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> Future<M> getAsync(Transaction tx, Class<M> modelClass, Key key)
            throws NullPointerException, IllegalStateException,
            IllegalArgumentException {
        return getAsync(tx, getModelMeta(modelClass), key);
    }

    /**
     * Returns a model specified by the key within the provided transaction.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @return a model specified by the key
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> M get(Transaction tx, ModelMeta<M> modelMeta, Key key)
            throws NullPointerException, IllegalStateException,
            EntityNotFoundRuntimeException, IllegalArgumentException {
        Entity entity = get(tx, key);
        ModelMeta<M> mm = DatastoreUtil.getModelMeta(modelMeta, entity);
        mm.validateKey(key);
        return mm.entityToModel(entity);
    }

    /**
     * Returns a model specified by the key within the provided transaction
     * asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @return a future model specified by the key
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> Future<M> getAsync(Transaction tx, final ModelMeta<M> modelMeta,
            final Key key) throws NullPointerException, IllegalStateException,
            IllegalArgumentException {
        return new FutureWrapper<Entity, M>(getAsync(tx, key)) {

            @Override
            protected Throwable convertException(Throwable throwable) {
                if (throwable instanceof EntityNotFoundException) {
                    return new EntityNotFoundRuntimeException(key, throwable);
                }
                return throwable;
            }

            @Override
            protected M wrap(Entity entity) throws Exception {
                ModelMeta<M> mm = DatastoreUtil.getModelMeta(modelMeta, entity);
                mm.validateKey(key);
                return mm.entityToModel(entity);
            }
        };
    }

    /**
     * Returns an entity specified by the key within the provided transaction.
     * Returns null if no entity is found.
     * 
     * @param tx
     *            the transaction
     * @param key
     *            the key
     * @return an entity specified by the key
     * @throws NullPointerException
     *             if the key parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public Entity getOrNull(Transaction tx, Key key)
            throws NullPointerException, IllegalStateException {
        return getAsMap(tx, key).get(key);
    }

    /**
     * Returns an entity specified by the key within the provided transaction
     * asynchronously.
     * 
     * @param tx
     *            the transaction
     * @param key
     *            the key
     * @return a future entity specified by the key
     * @throws NullPointerException
     *             if the key parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public Future<Entity> getOrNullAsync(Transaction tx, final Key key)
            throws NullPointerException, IllegalStateException {
        return new FutureWrapper<Map<Key, Entity>, Entity>(getAsMapAsync(
            tx,
            key)) {

            @Override
            protected Throwable convertException(Throwable throwable) {
                if (throwable instanceof EntityNotFoundException) {
                    return new EntityNotFoundRuntimeException(key, throwable);
                }
                return throwable;
            }

            @Override
            protected Entity wrap(Map<Key, Entity> map) throws Exception {
                return map.get(key);
            }
        };
    }

    /**
     * Returns a model specified by the key within the provided transaction.
     * Returns null if no entity is found.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @return a model specified by the key
     * @throws NullPointerException
     *             if the modelClass parameter is null or the key parameter is
     *             null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> M getOrNull(Transaction tx, Class<M> modelClass, Key key)
            throws NullPointerException, IllegalStateException,
            IllegalArgumentException {
        return getOrNull(tx, getModelMeta(modelClass), key);
    }

    /**
     * Returns a model specified by the key within the provided transaction
     * asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @return a future model specified by the key
     * @throws NullPointerException
     *             if the modelClass parameter is null or the key parameter is
     *             null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> Future<M> getOrNullAsync(Transaction tx, Class<M> modelClass,
            Key key) throws NullPointerException, IllegalStateException,
            IllegalArgumentException {
        return getOrNullAsync(tx, getModelMeta(modelClass), key);
    }

    /**
     * Returns a model specified by the key within the provided transaction.
     * Returns null if no entity is found.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @return a model specified by the key
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> M getOrNull(Transaction tx, ModelMeta<M> modelMeta, Key key)
            throws NullPointerException, IllegalStateException,
            IllegalArgumentException {
        return getAsMap(tx, modelMeta, key).get(key);
    }

    /**
     * Returns a model specified by the key within the provided transaction
     * asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @return a future model specified by the key
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> Future<M> getOrNullAsync(Transaction tx,
            final ModelMeta<M> modelMeta, final Key key)
            throws NullPointerException, IllegalStateException,
            IllegalArgumentException {
        return new FutureWrapper<Map<Key, Entity>, M>(getAsMapAsync(tx, key)) {

            @Override
            protected Throwable convertException(Throwable throwable) {
                if (throwable instanceof EntityNotFoundException) {
                    return new EntityNotFoundRuntimeException(key, throwable);
                }
                return throwable;
            }

            @Override
            protected M wrap(Map<Key, Entity> map) throws Exception {
                Entity entity = map.get(key);
                if (entity == null) {
                    return null;
                }
                ModelMeta<M> mm = DatastoreUtil.getModelMeta(modelMeta, entity);
                mm.validateKey(key);
                return mm.entityToModel(entity);
            }
        };
    }

    /**
     * Returns a model specified by the key within the provided transaction.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @param version
     *            the version
     * @return a model specified by the key
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the key parameter
     *             is null or if the version parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     * @throws ConcurrentModificationException
     *             if the version of the model is updated
     */
    public <M> M get(Transaction tx, Class<M> modelClass, Key key, Long version)
            throws NullPointerException, IllegalStateException,
            EntityNotFoundRuntimeException, IllegalArgumentException,
            ConcurrentModificationException {
        return get(tx, getModelMeta(modelClass), key, version);
    }

    /**
     * Returns a model specified by the key within the provided transaction
     * asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @param version
     *            the version
     * @return a future model specified by the key
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the key parameter
     *             is null or if the version parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> Future<M> getAsync(Transaction tx, Class<M> modelClass, Key key,
            Long version) throws NullPointerException, IllegalStateException,
            IllegalArgumentException {
        return getAsync(tx, getModelMeta(modelClass), key, version);
    }

    /**
     * Returns a model specified by the key within the provided transaction.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @param version
     *            the version
     * @return a model specified by the key
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the key parameter is
     *             null or if the version parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     * @throws ConcurrentModificationException
     *             if the version of the model is updated
     */
    public <M> M get(Transaction tx, ModelMeta<M> modelMeta, Key key,
            Long version) throws NullPointerException, IllegalStateException,
            EntityNotFoundRuntimeException, IllegalArgumentException,
            ConcurrentModificationException {
        if (version == null) {
            throw new NullPointerException("The version parameter is null.");
        }
        Entity entity = get(tx, key);
        ModelMeta<M> mm = DatastoreUtil.getModelMeta(modelMeta, entity);
        mm.validateKey(key);
        M model = mm.entityToModel(entity);
        if (version != mm.getVersion(model)) {
            throw new ConcurrentModificationException(
                "Failed optimistic lock by key("
                    + key
                    + ") and version("
                    + version
                    + ").");
        }
        return model;

    }

    /**
     * Returns a model specified by the key within the provided transaction
     * asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @param version
     *            the version
     * @return a future model specified by the key
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the key parameter is
     *             null or if the version parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> Future<M> getAsync(Transaction tx, final ModelMeta<M> modelMeta,
            final Key key, final Long version) throws NullPointerException,
            IllegalStateException, IllegalArgumentException {
        if (version == null) {
            throw new NullPointerException(
                "The version parameter must not be null.");
        }
        return new FutureWrapper<Entity, M>(getAsync(tx, key)) {

            @Override
            protected Throwable convertException(Throwable throwable) {
                if (throwable instanceof EntityNotFoundException) {
                    return new EntityNotFoundRuntimeException(key, throwable);
                }
                return throwable;
            }

            @Override
            protected M wrap(Entity entity) throws Exception {
                ModelMeta<M> mm = DatastoreUtil.getModelMeta(modelMeta, entity);
                mm.validateKey(key);
                M model = mm.entityToModel(entity);
                if (version != mm.getVersion(model)) {
                    throw new ConcurrentModificationException(
                        "Failed optimistic lock by key("
                            + key
                            + ") and version("
                            + version
                            + ").");
                }
                return model;
            }
        };
    }

    /**
     * Returns entities specified by the keys. If there is a current
     * transaction, this operation will execute within that transaction.
     * 
     * @param keys
     *            the keys
     * @return entities specified by the key
     * @throws NullPointerException
     *             if the keys parameter is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     */
    public List<Entity> get(Iterable<Key> keys) throws NullPointerException,
            EntityNotFoundRuntimeException {
        return get(ds.getCurrentTransaction(null), keys);
    }

    /**
     * Returns entities specified by the keys asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param keys
     *            the keys
     * @return future entities specified by the key
     * @throws NullPointerException
     *             if the keys parameter is null
     */
    public Future<List<Entity>> getAsync(Iterable<Key> keys)
            throws NullPointerException {
        return getAsync(ds.getCurrentTransaction(null), keys);
    }

    /**
     * Returns entities specified by the keys. If there is a current
     * transaction, this operation will execute within that transaction.
     * 
     * @param keys
     *            the keys
     * @return entities specified by the key
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     */
    public List<Entity> get(Key... keys) throws EntityNotFoundRuntimeException {
        return get(Arrays.asList(keys));
    }

    /**
     * Returns entities specified by the keys asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param keys
     *            the keys
     * @return future entities specified by the key
     * 
     */
    public Future<List<Entity>> getAsync(Key... keys) {
        return getAsync(Arrays.asList(keys));
    }

    /**
     * Returns models specified by the keys. If there is a current transaction,
     * this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelClass parameter is null of if the keys parameter
     *             is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> List<M> get(Class<M> modelClass, Iterable<Key> keys)
            throws NullPointerException, EntityNotFoundRuntimeException,
            IllegalArgumentException {
        return get(getModelMeta(modelClass), keys);
    }

    /**
     * Returns models specified by the keys asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return future models specified by the keys
     * @throws NullPointerException
     *             if the modelClass parameter is null of if the keys parameter
     *             is null
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> Future<List<M>> getAsync(Class<M> modelClass, Iterable<Key> keys)
            throws NullPointerException, IllegalArgumentException {
        return getAsync(getModelMeta(modelClass), keys);
    }

    /**
     * Returns models specified by the keys. If there is a current transaction,
     * this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelMeta parameter is null of if the keys parameter
     *             is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> List<M> get(ModelMeta<M> modelMeta, Iterable<Key> keys)
            throws NullPointerException, EntityNotFoundRuntimeException,
            IllegalArgumentException {
        return get(ds.getCurrentTransaction(null), modelMeta, keys);

    }

    /**
     * Returns models specified by the keys asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return future models specified by the keys
     * @throws NullPointerException
     *             if the modelMeta parameter is null of if the keys parameter
     *             is null
     * 
     */
    public <M> Future<List<M>> getAsync(ModelMeta<M> modelMeta,
            Iterable<Key> keys) throws NullPointerException {
        return getAsync(ds.getCurrentTransaction(null), modelMeta, keys);

    }

    /**
     * Returns models specified by the keys. If there is a current transaction,
     * this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelClass parameter is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> List<M> get(Class<M> modelClass, Key... keys)
            throws NullPointerException, EntityNotFoundRuntimeException,
            IllegalArgumentException {
        return get(modelClass, Arrays.asList(keys));
    }

    /**
     * Returns models specified by the keys asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return future models specified by the keys
     * @throws NullPointerException
     *             if the modelClass parameter is null
     */
    public <M> Future<List<M>> getAsync(Class<M> modelClass, Key... keys)
            throws NullPointerException {
        return getAsync(modelClass, Arrays.asList(keys));
    }

    /**
     * Returns models specified by the keys. If there is a current transaction,
     * this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the keys parameter
     *             is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> List<M> get(ModelMeta<M> modelMeta, Key... keys)
            throws NullPointerException, EntityNotFoundRuntimeException,
            IllegalArgumentException {
        return get(modelMeta, Arrays.asList(keys));
    }

    /**
     * Returns models specified by the keys asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return future models specified by the keys
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the keys parameter
     *             is null
     */
    public <M> Future<List<M>> getAsync(ModelMeta<M> modelMeta, Key... keys)
            throws NullPointerException {
        return getAsync(modelMeta, Arrays.asList(keys));
    }

    /**
     * Returns entities specified by the keys without transaction.
     * 
     * @param keys
     *            the keys
     * @return entities specified by the key
     * @throws NullPointerException
     *             if the keys parameter is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     */
    public List<Entity> getWithoutTx(Iterable<Key> keys)
            throws NullPointerException, EntityNotFoundRuntimeException {
        return get((Transaction) null, keys);
    }

    /**
     * Returns entities specified by the keys without transaction
     * asynchronously.
     * 
     * @param keys
     *            the keys
     * @return entities specified by the key
     * @throws NullPointerException
     *             if the keys parameter is null
     */
    public Future<List<Entity>> getWithoutTxAsync(Iterable<Key> keys)
            throws NullPointerException {
        return getAsync((Transaction) null, keys);
    }

    /**
     * Returns entities specified by the keys. If there is a current
     * transaction, this operation will execute within that transaction.
     * 
     * @param keys
     *            the keys
     * @return entities specified by the key
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     */
    public List<Entity> getWithoutTx(Key... keys)
            throws EntityNotFoundRuntimeException {
        return getWithoutTx(Arrays.asList(keys));
    }

    /**
     * Returns entities specified by the keys asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param keys
     *            the keys
     * @return entities specified by the key
     * 
     */
    public Future<List<Entity>> getWithoutTxAsync(Key... keys) {
        return getWithoutTxAsync(Arrays.asList(keys));
    }

    /**
     * Returns models specified by the keys without transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelClass parameter is null of if the keys parameter
     *             is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> List<M> getWithoutTx(Class<M> modelClass, Iterable<Key> keys)
            throws NullPointerException, EntityNotFoundRuntimeException,
            IllegalArgumentException {
        return getWithoutTx(getModelMeta(modelClass), keys);
    }

    /**
     * Returns models specified by the keys without transaction asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return future models specified by the keys
     * @throws NullPointerException
     *             if the modelClass parameter is null of if the keys parameter
     *             is null
     */
    public <M> Future<List<M>> getWithoutTxAsync(Class<M> modelClass,
            Iterable<Key> keys) throws NullPointerException {
        return getWithoutTxAsync(getModelMeta(modelClass), keys);
    }

    /**
     * Returns models specified by the keys without transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelMeta parameter is null of if the keys parameter
     *             is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> List<M> getWithoutTx(ModelMeta<M> modelMeta, Iterable<Key> keys)
            throws NullPointerException, EntityNotFoundRuntimeException,
            IllegalArgumentException {
        return get((Transaction) null, modelMeta, keys);

    }

    /**
     * Returns models specified by the keys without transaction asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return future models specified by the keys
     * @throws NullPointerException
     *             if the modelMeta parameter is null of if the keys parameter
     *             is null
     */
    public <M> Future<List<M>> getWithoutTxAsync(ModelMeta<M> modelMeta,
            Iterable<Key> keys) throws NullPointerException {
        return getAsync((Transaction) null, modelMeta, keys);

    }

    /**
     * Returns models specified by the keys without transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelClass parameter is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> List<M> getWithoutTx(Class<M> modelClass, Key... keys)
            throws NullPointerException, EntityNotFoundRuntimeException,
            IllegalArgumentException {
        return getWithoutTx(modelClass, Arrays.asList(keys));
    }

    /**
     * Returns models specified by the keys without transaction asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return future models specified by the keys
     * @throws NullPointerException
     *             if the modelClass parameter is null
     */
    public <M> Future<List<M>> getWithoutTxAsync(Class<M> modelClass,
            Key... keys) throws NullPointerException {
        return getWithoutTxAsync(modelClass, Arrays.asList(keys));
    }

    /**
     * Returns models specified by the keys without transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the keys parameter
     *             is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> List<M> getWithoutTx(ModelMeta<M> modelMeta, Key... keys)
            throws NullPointerException, EntityNotFoundRuntimeException,
            IllegalArgumentException {
        return getWithoutTx(modelMeta, Arrays.asList(keys));
    }

    /**
     * Returns models specified by the keys without transaction asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the keys parameter
     *             is null
     */
    public <M> Future<List<M>> getWithoutTxAsync(ModelMeta<M> modelMeta,
            Key... keys) throws NullPointerException {
        return getWithoutTxAsync(modelMeta, Arrays.asList(keys));
    }

    /**
     * Returns entities specified by the keys within the provided transaction.
     * 
     * @param tx
     *            the transaction
     * @param keys
     *            the keys
     * @return entities specified by the key
     * @throws NullPointerException
     *             if the keys parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     */
    public List<Entity> get(Transaction tx, Iterable<Key> keys)
            throws NullPointerException, IllegalStateException,
            EntityNotFoundRuntimeException {
        return DatastoreUtil.entityMapToEntityList(keys, getAsMap(tx, keys));
    }

    /**
     * Returns entities specified by the keys within the provided transaction
     * asynchronously.
     * 
     * @param tx
     *            the transaction
     * @param keys
     *            the keys
     * @return future entities specified by the key
     * @throws NullPointerException
     *             if the keys parameter is null
     * 
     */
    public Future<List<Entity>> getAsync(Transaction tx,
            final Iterable<Key> keys) throws NullPointerException {
        return new FutureWrapper<Map<Key, Entity>, List<Entity>>(getAsMapAsync(
            tx,
            keys)) {

            @Override
            protected Throwable convertException(Throwable throwable) {
                return throwable;
            }

            @Override
            protected List<Entity> wrap(Map<Key, Entity> map) throws Exception {
                return DatastoreUtil.entityMapToEntityList(keys, map);
            }
        };
    }

    /**
     * Returns entities specified by the keys within the provided transaction.
     * 
     * @param tx
     *            the transaction
     * @param keys
     *            the keys
     * @return entities specified by the key
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     */
    public List<Entity> get(Transaction tx, Key... keys)
            throws IllegalStateException, EntityNotFoundRuntimeException {
        return get(tx, Arrays.asList(keys));
    }

    /**
     * Returns entities specified by the keys within the provided transaction
     * asynchronously.
     * 
     * @param tx
     *            the transaction
     * @param keys
     *            the keys
     * @return future entities specified by the key
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public Future<List<Entity>> getAsync(Transaction tx, Key... keys)
            throws IllegalStateException {
        return getAsync(tx, Arrays.asList(keys));
    }

    /**
     * Returns models specified by the keys within the provided transaction.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelClass parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> List<M> get(Transaction tx, Class<M> modelClass,
            Iterable<Key> keys) throws NullPointerException,
            IllegalStateException, EntityNotFoundRuntimeException,
            IllegalArgumentException {
        return get(tx, getModelMeta(modelClass), keys);
    }

    /**
     * Returns models specified by the keys within the provided transaction
     * asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return future models specified by the keys
     * @throws NullPointerException
     *             if the modelClass parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public <M> Future<List<M>> getAsync(Transaction tx, Class<M> modelClass,
            Iterable<Key> keys) throws NullPointerException,
            IllegalStateException {
        return getAsync(tx, getModelMeta(modelClass), keys);
    }

    /**
     * Returns models specified by the keys within the provided transaction.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> List<M> get(Transaction tx, ModelMeta<M> modelMeta,
            Iterable<Key> keys) throws NullPointerException,
            IllegalStateException, EntityNotFoundRuntimeException,
            IllegalArgumentException {
        return DatastoreUtil.entityMapToModelList(modelMeta, keys, getAsMap(
            tx,
            keys));
    }

    /**
     * Returns models specified by the keys within the provided transaction
     * asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return future models specified by the keys
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public <M> Future<List<M>> getAsync(Transaction tx,
            final ModelMeta<M> modelMeta, final Iterable<Key> keys)
            throws NullPointerException, IllegalStateException {
        return new FutureWrapper<Map<Key, Entity>, List<M>>(getAsMapAsync(
            tx,
            keys)) {

            @Override
            protected Throwable convertException(Throwable throwable) {
                return throwable;
            }

            @Override
            protected List<M> wrap(Map<Key, Entity> map) throws Exception {
                return DatastoreUtil.entityMapToModelList(modelMeta, keys, map);
            }
        };
    }

    /**
     * Returns models specified by the keys within the provided transaction.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelClass parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> List<M> get(Transaction tx, Class<M> modelClass, Key... keys)
            throws NullPointerException, IllegalStateException,
            EntityNotFoundRuntimeException, IllegalArgumentException {
        return get(tx, getModelMeta(modelClass), Arrays.asList(keys));
    }

    /**
     * Returns models specified by the keys within the provided transaction
     * asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return future models specified by the keys
     * @throws NullPointerException
     *             if the modelClass parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public <M> Future<List<M>> getAsync(Transaction tx, Class<M> modelClass,
            Key... keys) throws NullPointerException, IllegalStateException {
        return getAsync(tx, getModelMeta(modelClass), Arrays.asList(keys));
    }

    /**
     * Returns models specified by the keys within the provided transaction.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> List<M> get(Transaction tx, ModelMeta<M> modelMeta, Key... keys)
            throws NullPointerException, IllegalStateException,
            EntityNotFoundRuntimeException, IllegalArgumentException {
        return get(tx, modelMeta, Arrays.asList(keys));
    }

    /**
     * Returns models specified by the keys within the provided transaction
     * asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return future models specified by the keys
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public <M> Future<List<M>> getAsync(Transaction tx, ModelMeta<M> modelMeta,
            Key... keys) throws NullPointerException, IllegalStateException {
        return getAsync(tx, modelMeta, Arrays.asList(keys));
    }

    /**
     * Returns entities specified by the keys. If there is a current
     * transaction, this operation will execute within that transaction.
     * 
     * @param keys
     *            the keys
     * @return entities specified by the keys
     * @throws NullPointerException
     *             if the keys parameter is null
     */
    public Map<Key, Entity> getAsMap(Iterable<Key> keys)
            throws NullPointerException {
        return getAsMap(ds.getCurrentTransaction(null), keys);
    }

    /**
     * Returns entities specified by the keys asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param keys
     *            the keys
     * @return future entities specified by the keys
     * @throws NullPointerException
     *             if the keys parameter is null
     */
    public Future<Map<Key, Entity>> getAsMapAsync(Iterable<Key> keys)
            throws NullPointerException {
        return getAsMapAsync(ds.getCurrentTransaction(null), keys);
    }

    /**
     * Returns entities specified by the keys. If there is a current
     * transaction, this operation will execute within that transaction.
     * 
     * @param keys
     *            the keys
     * @return entities specified by the keys
     */
    public Map<Key, Entity> getAsMap(Key... keys) {
        return getAsMap(Arrays.asList(keys));
    }

    /**
     * Returns entities specified by the keys asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param keys
     *            the keys
     * @return future entities specified by the keys
     */
    public Future<Map<Key, Entity>> getAsMapAsync(Key... keys) {
        return getAsMapAsync(Arrays.asList(keys));
    }

    /**
     * Returns models specified by the keys. If there is a current transaction,
     * this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the keys parameter
     *             is null
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> Map<Key, M> getAsMap(Class<M> modelClass, Iterable<Key> keys)
            throws NullPointerException, IllegalArgumentException {
        return getAsMap(getModelMeta(modelClass), keys);
    }

    /**
     * Returns models specified by the keys asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return future models specified by the keys
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the keys parameter
     *             is null
     */
    public <M> Future<Map<Key, M>> getAsMapAsync(Class<M> modelClass,
            Iterable<Key> keys) throws NullPointerException {
        return getAsMapAsync(getModelMeta(modelClass), keys);
    }

    /**
     * Returns models specified by the keys. If there is a current transaction,
     * this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the keys parameter
     *             is null
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> Map<Key, M> getAsMap(ModelMeta<M> modelMeta, Iterable<Key> keys)
            throws NullPointerException, IllegalArgumentException {
        return getAsMap(ds.getCurrentTransaction(null), modelMeta, keys);
    }

    /**
     * Returns models specified by the keys asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return future models specified by the keys
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the keys parameter
     *             is null
     */
    public <M> Future<Map<Key, M>> getAsMapAsync(ModelMeta<M> modelMeta,
            Iterable<Key> keys) throws NullPointerException {
        return getAsMapAsync(ds.getCurrentTransaction(null), modelMeta, keys);
    }

    /**
     * Returns models specified by the keys. If there is a current transaction,
     * this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelClass parameter is null
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> Map<Key, M> getAsMap(Class<M> modelClass, Key... keys)
            throws NullPointerException, IllegalArgumentException {
        return getAsMap(getModelMeta(modelClass), Arrays.asList(keys));
    }

    /**
     * Returns models specified by the keys asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return future models specified by the keys
     * @throws NullPointerException
     *             if the modelClass parameter is null
     */
    public <M> Future<Map<Key, M>> getAsMapAsync(Class<M> modelClass,
            Key... keys) throws NullPointerException {
        return getAsMapAsync(getModelMeta(modelClass), Arrays.asList(keys));
    }

    /**
     * Returns models specified by the keys. If there is a current transaction,
     * this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> Map<Key, M> getAsMap(ModelMeta<M> modelMeta, Key... keys)
            throws NullPointerException, IllegalArgumentException {
        return getAsMap(modelMeta, Arrays.asList(keys));
    }

    /**
     * Returns models specified by the keys asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return future models specified by the keys
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     */
    public <M> Future<Map<Key, M>> getAsMapAsync(ModelMeta<M> modelMeta,
            Key... keys) throws NullPointerException {
        return getAsMapAsync(modelMeta, Arrays.asList(keys));
    }

    /**
     * Returns entities specified by the keys without transaction.
     * 
     * @param keys
     *            the keys
     * @return entities specified by the keys
     * @throws NullPointerException
     *             if the keys parameter is null
     */
    public Map<Key, Entity> getAsMapWithoutTx(Iterable<Key> keys)
            throws NullPointerException {
        return getAsMap((Transaction) null, keys);
    }

    /**
     * Returns entities specified by the keys without transaction
     * asynchronously.
     * 
     * @param keys
     *            the keys
     * @return future entities specified by the keys
     * @throws NullPointerException
     *             if the keys parameter is null
     */
    public Future<Map<Key, Entity>> getAsMapWithoutTxAsync(Iterable<Key> keys)
            throws NullPointerException {
        return getAsMapAsync((Transaction) null, keys);
    }

    /**
     * Returns entities specified by the keys without transaction.
     * 
     * @param keys
     *            the keys
     * @return entities specified by the keys
     */
    public Map<Key, Entity> getAsMapWithoutTx(Key... keys) {
        return getAsMapWithoutTx(Arrays.asList(keys));
    }

    /**
     * Returns entities specified by the keys without transaction
     * asynchronously.
     * 
     * @param keys
     *            the keys
     * @return future entities specified by the keys
     */
    public Future<Map<Key, Entity>> getAsMapWithoutTxAsync(Key... keys) {
        return getAsMapWithoutTxAsync(Arrays.asList(keys));
    }

    /**
     * Returns models specified by the keys without transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the keys parameter
     *             is null
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> Map<Key, M> getAsMapWithoutTx(Class<M> modelClass,
            Iterable<Key> keys) throws NullPointerException,
            IllegalArgumentException {
        return getAsMapWithoutTx(getModelMeta(modelClass), keys);
    }

    /**
     * Returns models specified by the keys without transaction asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return future models specified by the keys
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the keys parameter
     *             is null
     */
    public <M> Future<Map<Key, M>> getAsMapWithoutTxAsync(Class<M> modelClass,
            Iterable<Key> keys) throws NullPointerException {
        return getAsMapWithoutTxAsync(getModelMeta(modelClass), keys);
    }

    /**
     * Returns models specified by the keys without transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the keys parameter
     *             is null
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> Map<Key, M> getAsMapWithoutTx(ModelMeta<M> modelMeta,
            Iterable<Key> keys) throws NullPointerException,
            IllegalArgumentException {
        return getAsMap((Transaction) null, modelMeta, keys);
    }

    /**
     * Returns models specified by the keys without transaction asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return future models specified by the keys
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the keys parameter
     *             is null
     */
    public <M> Future<Map<Key, M>> getAsMapWithoutTxAsync(
            ModelMeta<M> modelMeta, Iterable<Key> keys)
            throws NullPointerException {
        return getAsMapAsync((Transaction) null, modelMeta, keys);
    }

    /**
     * Returns models specified by the keys without transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelClass parameter is null
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> Map<Key, M> getAsMapWithoutTx(Class<M> modelClass, Key... keys)
            throws NullPointerException, IllegalArgumentException {
        return getAsMapWithoutTx(getModelMeta(modelClass), Arrays.asList(keys));
    }

    /**
     * Returns models specified by the keys without transaction asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return future models specified by the keys
     * @throws NullPointerException
     *             if the modelClass parameter is null
     */
    public <M> Future<Map<Key, M>> getAsMapWithoutTxAsync(Class<M> modelClass,
            Key... keys) throws NullPointerException {
        return getAsMapWithoutTxAsync(getModelMeta(modelClass), Arrays
            .asList(keys));
    }

    /**
     * Returns models specified by the keys without transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> Map<Key, M> getAsMapWithoutTx(ModelMeta<M> modelMeta,
            Key... keys) throws NullPointerException, IllegalArgumentException {
        return getAsMapWithoutTx(modelMeta, Arrays.asList(keys));
    }

    /**
     * Returns models specified by the keys without transaction asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return future models specified by the keys
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     */
    public <M> Future<Map<Key, M>> getAsMapWithoutTxAsync(
            ModelMeta<M> modelMeta, Key... keys) throws NullPointerException {
        return getAsMapWithoutTxAsync(modelMeta, Arrays.asList(keys));
    }

    /**
     * Returns entities specified by the keys within the provided transaction.
     * 
     * @param tx
     *            the transaction
     * @param keys
     *            the keys
     * @return entities specified by the keys
     * @throws NullPointerException
     *             if the keys parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public Map<Key, Entity> getAsMap(Transaction tx, Iterable<Key> keys)
            throws NullPointerException, IllegalStateException {
        return DatastoreUtil.getAsMap(ds, tx, keys);
    }

    /**
     * Returns entities specified by the keys within the provided transaction
     * asynchronously.
     * 
     * @param tx
     *            the transaction
     * @param keys
     *            the keys
     * @return future entities specified by the keys
     * @throws NullPointerException
     *             if the keys parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public Future<Map<Key, Entity>> getAsMapAsync(Transaction tx,
            Iterable<Key> keys) throws NullPointerException,
            IllegalStateException {
        return DatastoreUtil.getAsMapAsync(ads, tx, keys);
    }

    /**
     * Returns entities specified by the keys within the provided transaction.
     * 
     * @param tx
     *            the transaction
     * @param keys
     *            the keys
     * @return entities specified by the keys
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public Map<Key, Entity> getAsMap(Transaction tx, Key... keys)
            throws IllegalStateException {
        return getAsMap(tx, Arrays.asList(keys));
    }

    /**
     * Returns entities specified by the keys within the provided transaction
     * asynchronously.
     * 
     * @param tx
     *            the transaction
     * @param keys
     *            the keys
     * @return future entities specified by the keys
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public Future<Map<Key, Entity>> getAsMapAsync(Transaction tx, Key... keys)
            throws IllegalStateException {
        return getAsMapAsync(tx, Arrays.asList(keys));
    }

    /**
     * Returns models specified by the keys within the provided transaction.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return entities specified by the key
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the keys parameter
     *             is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> Map<Key, M> getAsMap(Transaction tx, Class<M> modelClass,
            Iterable<Key> keys) throws NullPointerException,
            IllegalStateException, IllegalArgumentException {
        return getAsMap(tx, getModelMeta(modelClass), keys);
    }

    /**
     * Returns models specified by the keys within the provided transaction
     * asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return future entities specified by the key
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the keys parameter
     *             is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public <M> Future<Map<Key, M>> getAsMapAsync(Transaction tx,
            Class<M> modelClass, Iterable<Key> keys)
            throws NullPointerException, IllegalStateException {
        return getAsMapAsync(tx, getModelMeta(modelClass), keys);
    }

    /**
     * Returns models specified by the keys within the provided transaction.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return entities specified by the key
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the keys parameter
     *             is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> Map<Key, M> getAsMap(Transaction tx, ModelMeta<M> modelMeta,
            Iterable<Key> keys) throws NullPointerException,
            IllegalStateException, IllegalArgumentException {
        return DatastoreUtil.entityMapToModelMap(modelMeta, getAsMap(tx, keys));

    }

    /**
     * Returns models specified by the keys within the provided transaction
     * asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return future entities specified by the key
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the keys parameter
     *             is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public <M> Future<Map<Key, M>> getAsMapAsync(Transaction tx,
            final ModelMeta<M> modelMeta, final Iterable<Key> keys)
            throws NullPointerException, IllegalStateException {
        return new FutureWrapper<Map<Key, Entity>, Map<Key, M>>(getAsMapAsync(
            tx,
            keys)) {

            @Override
            protected Throwable convertException(Throwable throwable) {
                return throwable;
            }

            @Override
            protected Map<Key, M> wrap(Map<Key, Entity> map) throws Exception {
                return DatastoreUtil.entityMapToModelMap(modelMeta, map);
            }
        };
    }

    /**
     * Returns models specified by the keys within the provided transaction.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return entities specified by the key
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the keys parameter
     *             is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> Map<Key, M> getAsMap(Transaction tx, Class<M> modelClass,
            Key... keys) throws NullPointerException, IllegalStateException,
            IllegalArgumentException {
        return getAsMap(tx, getModelMeta(modelClass), Arrays.asList(keys));
    }

    /**
     * Returns models specified by the keys within the provided transaction
     * asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return future entities specified by the key
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the keys parameter
     *             is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public <M> Future<Map<Key, M>> getAsMapAsync(Transaction tx,
            Class<M> modelClass, Key... keys) throws NullPointerException,
            IllegalStateException {
        return getAsMapAsync(tx, getModelMeta(modelClass), Arrays.asList(keys));
    }

    /**
     * Returns models specified by the keys within the provided transaction.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return entities specified by the key
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the keys parameter
     *             is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public <M> Map<Key, M> getAsMap(Transaction tx, ModelMeta<M> modelMeta,
            Key... keys) throws NullPointerException, IllegalStateException,
            IllegalArgumentException {
        return getAsMap(tx, modelMeta, Arrays.asList(keys));
    }

    /**
     * Returns models specified by the keys within the provided transaction
     * asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return future entities specified by the key
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the keys parameter
     *             is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public <M> Future<Map<Key, M>> getAsMapAsync(Transaction tx,
            ModelMeta<M> modelMeta, Key... keys) throws NullPointerException,
            IllegalStateException {
        return getAsMapAsync(tx, modelMeta, Arrays.asList(keys));
    }

    /**
     * Puts the entity to datastore. If there is a current transaction, this
     * operation will execute within that transaction.
     * 
     * @param entity
     *            the entity
     * @return a key
     * @throws NullPointerException
     *             if the entity parameter is null
     */
    public Key put(Entity entity) throws NullPointerException {
        return DatastoreUtil.put(ds, entity);
    }

    /**
     * Puts the entity to datastore without transaction.
     * 
     * @param entity
     *            the entity
     * @return a key
     * @throws NullPointerException
     *             if the entity parameter is null
     */
    public Key putWithoutTx(Entity entity) throws NullPointerException {
        return DatastoreUtil.put(ds, (Transaction) null, entity);
    }

    /**
     * Puts the model to datastore. If there is a current transaction, this
     * operation will execute within that transaction.
     * 
     * @param model
     *            the model
     * @return a key
     * @throws NullPointerException
     *             if the model parameter is null
     */
    public Key put(Object model) throws NullPointerException {
        Entity entity = DatastoreUtil.modelToEntity(ds, model);
        return DatastoreUtil.put(ds, entity);
    }

    /**
     * Puts the model to datastore without transaction.
     * 
     * @param model
     *            the model
     * @return a key
     * @throws NullPointerException
     *             if the model parameter is null
     */
    public Key putWithoutTx(Object model) throws NullPointerException {
        Entity entity = DatastoreUtil.modelToEntity(ds, model);
        return DatastoreUtil.put(ds, (Transaction) null, entity);
    }

    /**
     * Puts the entity to datastore within the provided transaction.
     * 
     * @param tx
     *            the transaction
     * @param entity
     *            the entity
     * @return a key
     * @throws NullPointerException
     *             if the entity parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public Key put(Transaction tx, Entity entity) throws NullPointerException,
            IllegalStateException {
        return DatastoreUtil.put(ds, tx, entity);
    }

    /**
     * Puts the model to datastore within the provided transaction.
     * 
     * @param tx
     *            the transaction
     * @param model
     *            the model
     * @return a key
     * @throws NullPointerException
     *             if the model parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public Key put(Transaction tx, Object model) throws NullPointerException,
            IllegalStateException {
        Entity entity = DatastoreUtil.modelToEntity(ds, model);
        return DatastoreUtil.put(ds, tx, entity);
    }

    /**
     * Puts the models or entities to datastore. If there is a current
     * transaction, this operation will execute within that transaction.
     * 
     * @param models
     *            the models or entities
     * @return a list of keys
     * @throws NullPointerException
     *             if the models parameter is null
     */
    public List<Key> put(Iterable<?> models) throws NullPointerException {
        List<Entity> entities = DatastoreUtil.modelsToEntities(ds, models);
        return DatastoreUtil.put(ds, entities);
    }

    /**
     * Puts the models or entities to datastore without transaction.
     * 
     * @param models
     *            the models or entities
     * @return a list of keys
     * @throws NullPointerException
     *             if the models parameter is null
     */
    public List<Key> putWithoutTx(Iterable<?> models)
            throws NullPointerException {
        List<Entity> entities = DatastoreUtil.modelsToEntities(ds, models);
        return DatastoreUtil.put(ds, (Transaction) null, entities);
    }

    /**
     * Puts the models or entities to datastore. If there is a current
     * transaction, this operation will execute within that transaction.
     * 
     * @param models
     *            the models or entities
     * @return a list of keys
     */
    public List<Key> put(Object... models) {
        return put(Arrays.asList(models));
    }

    /**
     * Puts the models or entities to datastore without transaction.
     * 
     * @param models
     *            the models or entities
     * @return a list of keys
     */
    public List<Key> putWithoutTx(Object... models) {
        return putWithoutTx(Arrays.asList(models));
    }

    /**
     * Puts the models or entities to datastore within the provided transaction.
     * 
     * @param tx
     *            the transaction
     * @param models
     *            the models or entities
     * @return a list of keys
     * @throws NullPointerException
     *             if the models parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public List<Key> put(Transaction tx, Iterable<?> models)
            throws NullPointerException, IllegalStateException {
        List<Entity> entities = DatastoreUtil.modelsToEntities(ds, models);
        return DatastoreUtil.put(ds, tx, entities);
    }

    /**
     * Puts the models or entities to datastore within the provided transaction.
     * 
     * @param tx
     *            the transaction
     * @param models
     *            the models or entities
     * @return a list of keys
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public List<Key> put(Transaction tx, Object... models)
            throws IllegalStateException {
        return put(tx, Arrays.asList(models));
    }

    /**
     * Deletes entities specified by the keys. If there is a current
     * transaction, this operation will execute within that transaction.
     * 
     * @param keys
     *            the keys
     * @throws NullPointerException
     *             if the keys parameter is null
     */
    public void delete(Iterable<Key> keys) throws NullPointerException {
        DatastoreUtil.delete(ds, keys);
    }

    /**
     * Deletes entities specified by the keys without transaction.
     * 
     * @param keys
     *            the keys
     * @throws NullPointerException
     *             if the keys parameter is null
     */
    public void deleteWithoutTx(Iterable<Key> keys) throws NullPointerException {
        DatastoreUtil.delete(ds, (Transaction) null, keys);
    }

    /**
     * Deletes entities specified by the keys. If there is a current
     * transaction, this operation will execute within that transaction.
     * 
     * @param keys
     *            the keys
     */
    public void delete(Key... keys) {
        delete(Arrays.asList(keys));
    }

    /**
     * Deletes entities specified by the keys without transaction.
     * 
     * @param keys
     *            the keys
     */
    public void deleteWithoutTx(Key... keys) {
        deleteWithoutTx(Arrays.asList(keys));
    }

    /**
     * Deletes entities specified by the keys within the provided transaction.
     * 
     * @param tx
     *            the transaction
     * @param keys
     *            the keys
     * @throws NullPointerException
     *             if the keys parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public void delete(Transaction tx, Iterable<Key> keys)
            throws NullPointerException, IllegalStateException {
        DatastoreUtil.delete(ds, tx, keys);
    }

    /**
     * Deletes entities specified by the keys within the provided transaction.
     * 
     * @param tx
     *            the transaction
     * @param keys
     *            the keys
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public void delete(Transaction tx, Key... keys)
            throws IllegalStateException {
        delete(tx, Arrays.asList(keys));
    }

    /**
     * Deletes all descendant entities.
     * 
     * @param ancestorKey
     *            the ancestor key
     * @throws NullPointerException
     *             if the ancestorKey parameter is null
     */
    public void deleteAll(Key ancestorKey) throws NullPointerException {
        if (ancestorKey == null) {
            throw new NullPointerException(
                "The ancestorKey parameter must not be null.");
        }
        delete(query(ancestorKey).asKeyList());
    }

    /**
     * Deletes all descendant entities within the provided transaction.
     * 
     * @param tx
     *            the transaction
     * @param ancestorKey
     *            the ancestor key
     * @throws NullPointerException
     *             if the ancestorKey parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public void deleteAll(Transaction tx, Key ancestorKey)
            throws NullPointerException, IllegalStateException {
        delete(tx, query(ancestorKey).asKeyList());
    }

    /**
     * Deletes all descendant entities without transaction.
     * 
     * @param ancestorKey
     *            the ancestor key
     * @throws NullPointerException
     *             if the ancestorKey parameter is null
     */
    public void deleteAllWithoutTx(Key ancestorKey) throws NullPointerException {
        deleteWithoutTx(query(ancestorKey).asKeyList());
    }

    /**
     * Returns a {@link ModelQuery}.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @return a {@link ModelQuery}
     * @throws NullPointerException
     *             if the modelClass parameter is null
     */
    public <M> ModelQuery<M> query(Class<M> modelClass)
            throws NullPointerException {
        return new ModelQuery<M>(ds, getModelMeta(modelClass));
    }

    /**
     * Returns a {@link ModelQuery}.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @return a {@link ModelQuery}
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     */
    public <M> ModelQuery<M> query(ModelMeta<M> modelMeta)
            throws NullPointerException {
        return new ModelQuery<M>(ds, modelMeta);
    }

    /**
     * Returns a {@link ModelQuery}.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param ancestorKey
     *            the ancestor key
     * @return a {@link ModelQuery}
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the ancestorKey
     *             parameter is null
     */
    public <M> ModelQuery<M> query(Class<M> modelClass, Key ancestorKey)
            throws NullPointerException {
        return new ModelQuery<M>(ds, getModelMeta(modelClass), ancestorKey);
    }

    /**
     * Returns a {@link ModelQuery}.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param ancestorKey
     *            the ancestor key
     * @return a {@link ModelQuery}
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the ancestorKey
     *             parameter is null
     */
    public <M> ModelQuery<M> query(ModelMeta<M> modelMeta, Key ancestorKey)
            throws NullPointerException {
        return new ModelQuery<M>(ds, modelMeta, ancestorKey);
    }

    /**
     * Returns a {@link ModelQuery}.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelClass
     *            the model class
     * @param ancestorKey
     *            the ancestor key
     * @return a {@link ModelQuery}
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the ancestorKey
     *             parameter is null
     */
    public <M> ModelQuery<M> query(Transaction tx, Class<M> modelClass,
            Key ancestorKey) throws NullPointerException {
        return query(tx, getModelMeta(modelClass), ancestorKey);
    }

    /**
     * Returns a {@link ModelQuery}.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelMeta
     *            the meta data of model
     * @param ancestorKey
     *            the ancestor key
     * @return a {@link ModelQuery}
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the ancestorKey
     *             parameter is null
     */
    public <M> ModelQuery<M> query(Transaction tx, ModelMeta<M> modelMeta,
            Key ancestorKey) throws NullPointerException {
        return new ModelQuery<M>(ds, tx, modelMeta, ancestorKey);
    }

    /**
     * Returns an {@link EntityQuery}.
     * 
     * @param kind
     *            the kind
     * @return an {@link EntityQuery}
     * @throws NullPointerException
     *             if the kind parameter is null
     */
    public EntityQuery query(String kind) throws NullPointerException {
        return new EntityQuery(ds, kind);
    }

    /**
     * Returns an {@link EntityQuery}.
     * 
     * @param kind
     *            the kind
     * @param ancestorKey
     *            the ancestor key
     * @return an {@link EntityQuery}
     * @throws NullPointerException
     *             if the kind parameter is null or if the ancestorKey parameter
     *             is null
     */
    public EntityQuery query(String kind, Key ancestorKey)
            throws NullPointerException {
        return new EntityQuery(ds, kind, ancestorKey);
    }

    /**
     * Returns an {@link EntityQuery}.
     * 
     * @param tx
     *            the transaction
     * @param kind
     *            the kind
     * @param ancestorKey
     *            the ancestor key
     * @return an {@link EntityQuery}
     * @throws NullPointerException
     *             if the kind parameter is null or if the ancestorKey parameter
     *             is null
     */
    public EntityQuery query(Transaction tx, String kind, Key ancestorKey)
            throws NullPointerException {
        return new EntityQuery(ds, tx, kind, ancestorKey);
    }

    /**
     * Returns a {@link KindlessQuery}.
     * 
     * @return a {@link KindlessQuery}
     */
    public KindlessQuery query() {
        return new KindlessQuery(ds);
    }

    /**
     * Returns a {@link KindlessQuery}.
     * 
     * @param ancestorKey
     *            the ancestor key
     * @return a {@link KindlessQuery}
     * @throws NullPointerException
     *             if the ancestorKey parameter is null
     */
    public KindlessQuery query(Key ancestorKey) throws NullPointerException {
        return new KindlessQuery(ds, ancestorKey);
    }

    /**
     * Returns a {@link KindlessQuery}.
     * 
     * @param tx
     *            the transaction
     * @param ancestorKey
     *            the ancestor key
     * @return a {@link KindlessQuery}
     * @throws NullPointerException
     *             if the ancestorKey parameter is null
     */
    public KindlessQuery query(Transaction tx, Key ancestorKey)
            throws NullPointerException {
        return new KindlessQuery(ds, tx, ancestorKey);
    }

    /**
     * Returns a meta data of the model
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @return a meta data of the model
     * @throws NullPointerException
     *             if the modelClass parameter is null
     */
    public <M> ModelMeta<M> getModelMeta(Class<M> modelClass)
            throws NullPointerException {
        return DatastoreUtil.getModelMeta(modelClass);
    }

    /**
     * Filters the list in memory.
     * 
     * @param <M>
     *            the model type
     * @param list
     *            the model list
     * @param criteria
     *            the filter criteria
     * @return the filtered list.
     * @throws NullPointerException
     *             if the list parameter is null or if the model of the list is
     *             null
     */
    public <M> List<M> filterInMemory(List<M> list,
            InMemoryFilterCriterion... criteria) throws NullPointerException {
        return DatastoreUtil.filterInMemory(list, Arrays.asList(criteria));
    }

    /**
     * Sorts the list.
     * 
     * @param <M>
     *            the model type
     * @param list
     *            the model list
     * @param criteria
     *            criteria to sort
     * @return the sorted list
     * @throws NullPointerException
     *             if the list parameter is null or if the criteria parameter is
     *             null
     */
    public <M> List<M> sortInMemory(List<M> list,
            InMemorySortCriterion... criteria) throws NullPointerException {
        return DatastoreUtil.sortInMemory(list, Arrays.asList(criteria));
    }

    /**
     * Sets the limited key for cipher to the current thread.
     * 
     * @param key
     *            the key
     * @since 1.0.6
     */
    public void setLimitedCipherKey(String key) {
        CipherFactory.getFactory().setLimitedKey(key);
    }

    /**
     * Sets the global key for cipher.
     * 
     * @param key
     *            the key
     * @since 1.0.6
     */
    public void setGlobalCipherKey(String key) {
        CipherFactory.getFactory().setGlobalKey(key);
    }
}