package org.vaadin.artur.firebase.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.vaadin.artur.firebase.db.UserDB;
import org.vaadin.artur.firebase.db.data.User;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.UIDetachedException;
import com.vaadin.ui.VerticalLayout;

public class CrudView extends VerticalLayout {
    private Grid<User> grid = new Grid<>(User.class);
    private List<User> gridItems = new ArrayList<>();
    private Button newButton = new Button("New");
    private UserForm form = new UserForm();
    private ListDataProvider<User> dataProvider;

    public CrudView() {
        dataProvider = new ListDataProvider<>(gridItems);
        grid.setDataProvider(dataProvider);
        grid.removeColumn("key");
        grid.addSelectionListener(e -> {
            if (!e.isUserOriginated()) {
                return;
            }

            Optional<User> selected = e.getFirstSelectedItem();
            if (selected.isPresent()) {
                User user = selected.get();
                form.setItem(user.getKey(), user);
            } else {
                form.reset();
            }
        });

        newButton.addClickListener(e -> {
            form.newItem();
        });

        Link link = new Link("https://github.com/Artur-/vaadin-on-fire",
                new ExternalResource(
                        "https://github.com/Artur-/vaadin-on-fire"));
        link.setTargetName("_blank");
        addComponents(new Label(
                "This is a simple CRUD which uses Firebase and push to support multiple users simultaneously editing the same data"),
                link, grid, newButton, form);
    }

    @Override
    public void attach() {
        super.attach();
        new UsersListener(UserDB.getUsersDb());
    }

    private class UsersListener implements ChildEventListener {

        private final DatabaseReference usersReference;

        private UsersListener(DatabaseReference usersReference) {
            this.usersReference = usersReference;
            register();
        }

        private void register() {
            usersReference.addChildEventListener(this);
        }

        private void unregister() {
            usersReference.removeEventListener(this);

        }

        @Override
        public void onChildAdded(DataSnapshot snapshot,
                String previousChildName) {
            User added = snapshot.getValue(User.class);
            added.setKey(snapshot.getKey());
            try {
                getUI().access(() -> {
                    dataProvider.getItems().add(added);
                    dataProvider.refreshAll();
                });
            } catch (UIDetachedException e) {
                unregister();
            }

        }

        @Override
        public void onChildChanged(DataSnapshot snapshot,
                String previousChildName) {
            User updated = snapshot.getValue(User.class);
            updated.setKey(snapshot.getKey());

            try {
                getUI().access(() -> {
                    for (int i = 0; i < gridItems.size(); i++) {
                        User user = gridItems.get(i);
                        if (updated.equals(user)) {
                            gridItems.set(i, updated);
                            dataProvider.refreshItem(updated);
                            return;
                        }
                    }
                });
            } catch (UIDetachedException e) {
                unregister();
            }

        }

        @Override
        public void onChildRemoved(DataSnapshot snapshot) {
        }

        @Override
        public void onChildMoved(DataSnapshot snapshot,
                String previousChildName) {
        }

        @Override
        public void onCancelled(DatabaseError error) {
        }

    }

}
