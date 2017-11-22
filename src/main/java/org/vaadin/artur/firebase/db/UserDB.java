package org.vaadin.artur.firebase.db;

import java.util.HashMap;
import java.util.logging.Logger;

import org.vaadin.artur.firebase.db.data.User;

import com.google.firebase.database.DatabaseReference;

public class UserDB {

    public static DatabaseReference getUsersDb() {
        return Firebase.getDb().child("users2");
    }

    public static void maybeCreateInitialData() {
        add(new User("Foo", 12));
        add(new User("Bar", 56));
    }

    public static void add(User item) {
        getUsersDb().push().setValueAsync(item);
    }

    protected static Logger getLogger() {
        return Logger.getLogger("UserDB");
    }

    public static void update(String key, User item) {
        getLogger().info("Set user " + key + " to " + item);
        HashMap<String, Object> toUpdate = new HashMap<>();
        toUpdate.put(key, item);
        getUsersDb().updateChildrenAsync(toUpdate);
    }
}
