package org.vaadin.artur.firebase.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.vaadin.artur.firebase.db.UserDB;
import org.vaadin.artur.firebase.db.data.User;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.UIDetachedException;
import com.vaadin.ui.VerticalLayout;

public class CrudView extends VerticalLayout {
    private Grid<User> grid = new Grid<>(User.class);
    private List<User> gridItems;
    private Button newButton = new Button("New");
    private UserForm form = new UserForm();

    public CrudView() {
        grid.addSelectionListener(e -> {
            if (!e.isUserOriginated()) {
                return;
            }

            Optional<User> selected = e.getFirstSelectedItem();
            if (selected.isPresent()) {
                form.setItem(gridItems.indexOf(selected.get()), selected.get());
            } else {
                form.reset();
            }
        });

        newButton.addClickListener(e -> {
            form.newItem();
        });

        addComponents(new Label(
                "This is a simple CRUD which uses Firebase and push to support multiple users simultaneously editing the same data"),
                grid, newButton, form);
    }

    @Override
    public void attach() {
        super.attach();
        new UsersListener(UserDB.getUsersDb());
    }

    private final class UsersListener implements ValueEventListener {

        private final DatabaseReference usersReference;

        private UsersListener(DatabaseReference usersReference) {
            this.usersReference = usersReference;
            usersReference.addValueEventListener(this);
        }

        @Override
        public void onDataChange(DataSnapshot snapshot) {
            List<User> dbUsers = snapshot.getValue(UserDB.LIST);
            try {
                getUI().access(() -> {
                    gridItems = dbUsers != null ? dbUsers : new ArrayList<>();
                    grid.setItems(gridItems);
                });
            } catch (UIDetachedException e) {
                usersReference.removeEventListener(this);
            }
        }

        @Override
        public void onCancelled(DatabaseError error) {

        }
    }

}
