/**
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.aerogear.android;

import java.util.HashMap;
import java.util.Map;

import org.jboss.aerogear.android.datamanager.IdGenerator;
import org.jboss.aerogear.android.datamanager.Store;
import org.jboss.aerogear.android.datamanager.StoreFactory;
import org.jboss.aerogear.android.impl.datamanager.DefaultIdGenerator;
import org.jboss.aerogear.android.impl.datamanager.DefaultStoreFactory;
import org.jboss.aerogear.android.impl.datamanager.StoreConfig;

/**
 * Represents an abstraction layer for a storage system.
 * <p/>
 * As a note, you should NOT extend this class for production or application
 * purposes. This class is made non-final ONLY for testing/mocking/academic
 * purposes.
 */
public class DataManager {

    private final Map<String, Store> stores = new HashMap<String, Store>();
    /**
     * This will default to {@link DefaultIdGenerator} if not provided.
     */
    private final IdGenerator idGenerator;
    /**
     * This will default to {@link DefaultStoreFactory} if not provided.
     */
    private final StoreFactory storeFactory;

    /**
     * Creates a new DataManager using {@link  DefaultIdGenerator} and
     * {@link DefaultStoreFactory}
     */
    public DataManager() {
        this(new DefaultIdGenerator(), new DefaultStoreFactory());
    }

    /**
     * Creates a new DataManager using the idGenerator parameter and
     * {@link DefaultStoreFactory}
     *
     * @param idGenerator
     * @throws IllegalArgumentException if idGenerator is null
     *
     */
    public DataManager(IdGenerator idGenerator) {
        this(idGenerator, new DefaultStoreFactory());
    }

    /**
     * Creates a new DataManager using the storeFactory parameter and
     * {@link DefaultIdGenerator}
     *
     * @param storeFactory
     * @throws IllegalArgumentException if storeFactory is null
     */
    public DataManager(StoreFactory storeFactory) {
        this(new DefaultIdGenerator(), storeFactory);
    }

    /**
     *
     * Creates a DataManager using the supplied parameters
     *
     * @param idGenerator
     * @param storeFactory
     * @throws IllegalArgumentException if idGenerator is null
     * @throws IllegalArgumentException if storeFactory is null
     */
    public DataManager(IdGenerator idGenerator, StoreFactory storeFactory) {
        if (idGenerator == null) {
            throw new IllegalArgumentException(
                    "Id Generator should not be null");
        }

        if (storeFactory == null) {
            throw new IllegalArgumentException(
                    "StoreFactory should not be null");
        }

        this.idGenerator = idGenerator;
        this.storeFactory = storeFactory;

    }

    /**
     * Creates a new default (in memory) Store implementation.
     *
     * @param storeName The name of the actual data store object.
     */
    public Store store(String storeName) {
        return store(storeName, new StoreConfig());
    }

    /**
     * Creates a new Store implementation. The actual type is determined by the
     * type argument.
     *
     * @param storeName The name of the actual data store object.
     * @param config The config object used to build the store
     */
    public Store store(String storeName, StoreConfig config) {
        Store store = storeFactory.createStore(config);
        stores.put(storeName, store);
        return store;
    }

    /**
     * Removes a Store implementation from the DataManager. The store to be
     * removed is determined by the storeName argument.
     *
     * @param storeName The name of the actual data store object.
     */
    public Store remove(String storeName) {
        return stores.remove(storeName);
    }

    /**
     * Loads a given Store implementation, based on the given storeName argument.
     *
     * @param storeName The name of the actual data store object.
     */
    public Store get(String storeName) {
        return stores.get(storeName);
    }
}
