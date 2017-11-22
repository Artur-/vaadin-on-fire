package org.vaadin.artur.firebase;

import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.stream.Stream;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.vaadin.data.provider.AbstractDataProvider;
import com.vaadin.data.provider.DataProviderListener;
import com.vaadin.data.provider.Query;
import com.vaadin.server.SerializablePredicate;
import com.vaadin.shared.Registration;

/**
 * A data provider connected to a given child in a Firebase database.
 *
 * @param <T>
 */
public class FirebaseDataProvider<T extends HasKey>
        extends AbstractDataProvider<T, SerializablePredicate<T>>
        implements ChildEventListener {

    private LinkedHashMap<String, T> data = new LinkedHashMap<>();
    private DatabaseReference databaseReference;
    private Class<T> type;
    AtomicInteger registeredListeners = new AtomicInteger(0);

    /**
     * Constructs a new ListDataProvider.
     * <p>
     * No protective copy is made of the list, and changes in the provided
     * backing Collection will be visible via this data provider. The caller
     * should copy the list if necessary.
     *
     * @param databaseReference
     *
     * @param items
     *            the initial data, not null
     */
    public FirebaseDataProvider(Class<T> type,
            DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
        this.type = type;
    }

    @Override
    public String getId(T item) {
        return item.getKey();
    }

    @Override
    public Stream<T> fetch(Query<T, SerializablePredicate<T>> query) {
        return data.values().stream();
    }

    @Override
    public int size(Query<T, SerializablePredicate<T>> query) {
        return data.size();
    }

    @Override
    public boolean isInMemory() {
        return false;
    }

    @Override
    public Registration addDataProviderListener(
            DataProviderListener<T> listener) {
        if (registeredListeners.incrementAndGet() == 1) {
            registerFirebaseListener();
        }
        Registration realRegistration = super.addDataProviderListener(listener);
        return () -> {
            realRegistration.remove();
            if (registeredListeners.decrementAndGet() == 0) {
                unregisterFirebaseListener();
            }
        };
    }

    private void registerFirebaseListener() {
        databaseReference.addChildEventListener(this);
    }

    private void unregisterFirebaseListener() {
        databaseReference.removeEventListener(this);
    }

    private Logger getLogger() {
        return Logger.getLogger(FirebaseDataProvider.class.getName());
    }

    @Override
    public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
        T added = snapshot.getValue(type);
        String key = snapshot.getKey();
        added.setKey(key);

        data.put(key, added);
        refreshAll();
    }

    @Override
    public void onChildChanged(DataSnapshot snapshot,
            String previousChildName) {
        T updated = snapshot.getValue(type);
        String key = snapshot.getKey();
        updated.setKey(key);

        data.put(key, updated);
        refreshItem(updated);
    }

    @Override
    public void onChildRemoved(DataSnapshot snapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot snapshot, String previousChildName) {
    }

    @Override
    public void onCancelled(DatabaseError error) {
    }
}
