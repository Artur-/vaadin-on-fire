package org.vaadin.artur.firebase.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.annotation.WebServlet;

import org.vaadin.artur.firebase.db.Firebase;
import org.vaadin.artur.firebase.db.data.User;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.UIDetachedException;

@Push
public class FireUI extends UI {

    private Grid<User> grid = new Grid<>(User.class);

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        try {
            Firebase.setup();
        } catch (Exception e) {
            setContent(new Label(
                    "Unable to setup Firebase connection. See the log for details"));
            Logger.getLogger(getClass().getName()).log(Level.SEVERE,
                    "Failed to init firebase", e);
        }

        DatabaseReference database = Firebase.getDb();

        new UsersListener(database.child("users"));

        setContent(grid);
    }

    private final class UsersListener implements ValueEventListener {
        private final DatabaseReference usersReference;

        private UsersListener(DatabaseReference usersReference) {
            this.usersReference = usersReference;
            usersReference.addValueEventListener(this);
        }

        @Override
        public void onDataChange(DataSnapshot snapshot) {
            GenericTypeIndicator<List<User>> t = new GenericTypeIndicator<List<User>>() {
            };
            List<User> dbUsers = snapshot.getValue(t);
            List<User> users = dbUsers == null ? new ArrayList<>() : dbUsers;
            try {
                access(() -> {
                    grid.setItems(users);
                });
            } catch (UIDetachedException e) {
                usersReference.removeEventListener(this);
            }
        }

        @Override
        public void onCancelled(DatabaseError error) {

        }
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = FireUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
