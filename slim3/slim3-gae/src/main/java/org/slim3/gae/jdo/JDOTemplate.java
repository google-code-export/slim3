/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.slim3.gae.jdo;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.slim3.commons.util.BooleanUtil;
import org.slim3.commons.util.ByteUtil;
import org.slim3.commons.util.DateUtil;
import org.slim3.commons.util.DoubleUtil;
import org.slim3.commons.util.FloatUtil;
import org.slim3.commons.util.IntegerUtil;
import org.slim3.commons.util.LongUtil;
import org.slim3.commons.util.RuntimeExceptionUtil;
import org.slim3.commons.util.ShortUtil;

/**
 * A template class for JDO.
 * 
 * @author higa
 * @param <R>
 *            the return type
 * @since 3.0
 * 
 */
public abstract class JDOTemplate<R> {

    /**
     * The persistence manager.
     */
    protected PersistenceManager pm;

    /**
     * The transaction.
     */
    protected Transaction tx;

    /**
     * Executes an action.
     * 
     * @return the executed result
     */
    public final R execute() {
        R returnValue = null;
        pm = PM.getCurrent();
        assertPersistenceManagerIsActive();
        tx = pm.currentTransaction();
        try {
            beforeExecution();
            returnValue = doExecute();
            afterExecution(returnValue);
        } catch (Throwable t) {
            handleThrowable(t);
        }
        return returnValue;
    }

    /**
     * Processes an action before execution.
     */
    protected void beforeExecution() {
        tx.begin();
    }

    /**
     * Executes an action.
     * 
     * @return the executed result
     */
    protected abstract R doExecute();

    /**
     * Processes an action after execution.
     * 
     * @param returnValue
     *            the return value
     */
    protected void afterExecution(R returnValue) {
        if (tx.getRollbackOnly()) {
            tx.rollback();
        } else {
            tx.commit();
        }
    }

    /**
     * Handles the exception.
     * 
     * @param t
     *            the exception
     */
    protected void handleThrowable(Throwable t) {
        tx.rollback();
        RuntimeExceptionUtil.wrapAndThrow(t);
    }

    /**
     * Creates a new {@link SelectQuery}.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @return a new {@link SelectQuery}
     */
    protected <M> SelectQuery<M> from(ModelMeta<M> modelMeta) {
        assertPersistenceManagerIsActive();
        return new SelectQuery<M>(modelMeta, pm);
    }

    /**
     * Converts the object to the boolean object.
     * 
     * @param o
     *            the object
     * @return the boolean object
     */
    protected Boolean toBoolean(Object o) {
        return BooleanUtil.toBoolean(o);
    }

    /**
     * Converts the object to the byte object.
     * 
     * @param o
     *            the object
     * @return the byte object
     */
    protected Byte toByte(Object o) {
        return ByteUtil.toByte(o);
    }

    /**
     * Converts the object to the short object.
     * 
     * @param o
     *            the object
     * @return the short object
     */
    protected Short toShort(Object o) {
        return ShortUtil.toShort(o);
    }

    /**
     * Converts the object to the integer object.
     * 
     * @param o
     *            the object
     * @return the integer object
     */
    protected Integer toInteger(Object o) {
        return IntegerUtil.toInteger(o);
    }

    /**
     * Converts the object to the long object.
     * 
     * @param o
     *            the object
     * @return the long object
     */
    protected Long toLong(Object o) {
        return LongUtil.toLong(o);
    }

    /**
     * Converts the object to the float object.
     * 
     * @param o
     *            the object
     * @return the float object
     */
    protected Float toFloat(Object o) {
        return FloatUtil.toFloat(o);
    }

    /**
     * Converts the object to the double object.
     * 
     * @param o
     *            the object
     * @return the double object
     */
    protected Double toDouble(Object o) {
        return DoubleUtil.toDouble(o);
    }

    /**
     * Converts the object to the date object.
     * 
     * @param o
     *            the object
     * @return the date object
     */
    protected Date toDate(Object o) {
        return DateUtil.toDate(o);
    }

    /**
     * Converts the object to the date object.
     * 
     * @param text
     *            the text
     * @param pattern
     *            the pattern for {@link SimpleDateFormat}
     * @return the date object
     */
    protected Date toDate(String text, String pattern) {
        return DateUtil.toDate(text, pattern);
    }

    /**
     * Asserts that the current persistence manager is active.
     * 
     * @throws IllegalStateException
     *             if the persistence manager attached to the current thread is
     *             not found or if the persistence manager attached to the
     *             current thread is already closed
     */
    protected void assertPersistenceManagerIsActive()
            throws IllegalStateException {
        if (pm == null) {
            throw new IllegalStateException(
                    "The persistence manager attached to the current thread is not found.");
        }
        if (pm.isClosed()) {
            throw new IllegalStateException(
                    "The persistence manager attached to the current thread is already closed.");
        }
    }
}