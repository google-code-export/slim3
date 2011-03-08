/*
 * Copyright 2004-2010 the original author or authors.
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

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class S3QueryResultList<T> implements List<T>, Serializable {

    private static final long serialVersionUID = 1L;
    
    protected List<T> delegate;

    protected String encodedCursor;

    protected String encodedFilters;

    protected String encodedSorts;

    protected boolean hasNext;

    protected S3QueryResultList() {
    }

    public S3QueryResultList(List<T> delegate, String encodedCursor,
            String encodedFilters, String encodedSorts, boolean hasNext)
            throws NullPointerException {
        if (delegate == null) {
            throw new NullPointerException(
                "The delegate parameter must not be null.");
        }
        if (encodedCursor == null) {
            throw new NullPointerException(
                "The encodedCursor parameter must not be null.");
        }
        if (encodedFilters == null) {
            throw new NullPointerException(
                "The encodedFilters parameter must not be null.");
        }
        if (encodedSorts == null) {
            throw new NullPointerException(
                "The encodedSorts parameter must not be null.");
        }
        this.delegate = delegate;
        this.encodedCursor = encodedCursor;
        this.encodedFilters = encodedFilters;
        this.encodedSorts = encodedSorts;
        this.hasNext = hasNext;
    }

    /**
     * Returns the cursor as encoded string.
     * 
     * @return the cursor as encoded string
     */
    public String getEncodedCursor() {
        return encodedCursor;
    }

    /**
     * Returns the array of filters as encoded string.
     * 
     * @return the array of filters as encoded string
     */
    public String getEncodedFilters() {
        return encodedFilters;
    }

    /**
     * Returns the array of sorts as encoded string.
     * 
     * @return the array of sorts as encoded string
     */
    public String getEncodedSorts() {
        return encodedSorts;
    }

    /**
     * Determines if a next entry exists
     * 
     * @return whether a next entry exists
     */
    public boolean hasNext() {
        return hasNext;
    }

    public void add(int index, T element) {
        delegate.add(index, element);
    }

    public boolean add(T o) {
        return delegate.add(o);
    }

    public boolean addAll(Collection<? extends T> c) {
        return delegate.addAll(c);
    }

    public boolean addAll(int index, Collection<? extends T> c) {
        return delegate.addAll(index, c);
    }

    public void clear() {
        delegate.clear();
    }

    public boolean contains(Object o) {
        return delegate.contains(o);
    }

    public boolean containsAll(Collection<?> c) {
        return delegate.containsAll(c);
    }

    public T get(int index) {
        return delegate.get(index);
    }

    public int indexOf(Object o) {
        return delegate.indexOf(o);
    }

    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    public Iterator<T> iterator() {
        return delegate.iterator();
    }

    public int lastIndexOf(Object o) {
        return delegate.lastIndexOf(o);
    }

    public ListIterator<T> listIterator() {
        return delegate.listIterator();
    }

    public ListIterator<T> listIterator(int index) {
        return delegate.listIterator(index);
    }

    public T remove(int index) {
        return delegate.remove(index);
    }

    public boolean remove(Object o) {
        return delegate.remove(o);
    }

    public boolean removeAll(Collection<?> c) {
        return delegate.removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        return delegate.retainAll(c);
    }

    public T set(int index, T element) {
        return delegate.set(index, element);
    }

    public int size() {
        return delegate.size();
    }

    public List<T> subList(int fromIndex, int toIndex) {
        return delegate.subList(fromIndex, toIndex);
    }

    public Object[] toArray() {
        return delegate.toArray();
    }

    public <A> A[] toArray(A[] a) {
        return delegate.toArray(a);
    }
}