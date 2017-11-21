package org.vaadin.artur.firebase.db;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.artur.firebase.db.data.User;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.Transaction.Handler;
import com.google.firebase.database.Transaction.Result;

public class DataGenerator {
    public static void maybeCreateInitialDb(DatabaseReference usersDatabase) {
        usersDatabase.runTransaction(new Handler() {
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
}
