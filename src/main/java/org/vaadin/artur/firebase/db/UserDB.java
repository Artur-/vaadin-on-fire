package org.vaadin.artur.firebase.db;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.vaadin.artur.firebase.db.data.User;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.Transaction.Handler;
import com.google.firebase.database.Transaction.Result;

public class UserDB {

    public static final UserList LIST = new UserList();

    private static class UserList extends GenericTypeIndicator<List<User>> {
    }

    public static void maybeCreateInitialData() {
        getUsersDb().runTransaction(new Handler() {
            @Override
            public void onComplete(DatabaseError error, boolean committed,
                    DataSnapshot currentData) {
            }

            @Override
            public Result doTransaction(MutableData usersData) {
                if (!usersData.hasChildren()) {
                    List<User> users = new ArrayList<>();
                    users.add(new User("Foo", 12));
                    users.add(new User("Bar", 56));
                    usersData.setValue(users);
                }
                return Transaction.success(usersData);
            }
        });
    }

    public static DatabaseReference getUsersDb() {
        return Firebase.getDb().child("users");
    }

    public static void add(User item) {
        AtomicBoolean added = new AtomicBoolean(false);
        getUsersDb().runTransaction(new Handler() {

            @Override
            public void onComplete(DatabaseError error, boolean committed,
                    DataSnapshot currentData) {

            }

            @Override
            public Result doTransaction(MutableData currentData) {
                if (!added.get()) {
                    List<User> newList = currentData.getValue(LIST);
                    newList.add(item);
                    currentData.setValue(newList);
                    added.set(true);
                }
                return Transaction.success(currentData);
            }
        });

    }

    public static void update(int id, User item) {
        AtomicBoolean modified = new AtomicBoolean(false);
        getUsersDb().runTransaction(new Handler() {

            @Override
            public void onComplete(DatabaseError error, boolean committed,
                    DataSnapshot currentData) {

            }

            @Override
            public Result doTransaction(MutableData currentData) {
                if (!modified.get()) {
                    List<User> newList = currentData.getValue(LIST);
                    newList.set(id, item);
                    currentData.setValue(newList);
                    modified.set(true);
                }
                return Transaction.success(currentData);
            }
        });
    }
}
