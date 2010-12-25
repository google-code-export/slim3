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
package com.google.appengine.api.datastore;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

/**
 * Custom field serializer for Category.
 * 
 * @author galdolber
 */
public final class Category_CustomFieldSerializer {

    /**
     * Deserializes the key.
     * 
     * @param streamReader
     *            the stream reader
     * @param instance
     *            the key
     * @throws SerializationException
     *             if an exception occurred when deserializing
     */
    public static void deserialize(SerializationStreamReader streamReader,
            Category instance) throws SerializationException {
    }

    /**
     * @param streamReader
     * @return Category
     * @throws SerializationException
     */
    public static Category instantiate(SerializationStreamReader streamReader)
            throws SerializationException {
        return new Category(streamReader.readString());
    }

    /**
     * @param streamWriter
     * @param instance
     * @throws SerializationException
     */
    public static void serialize(SerializationStreamWriter streamWriter,
            Category instance) throws SerializationException {
        streamWriter.writeString(instance.getCategory());
    }
}