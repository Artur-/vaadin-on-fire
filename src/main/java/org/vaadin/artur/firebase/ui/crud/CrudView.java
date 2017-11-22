package org.vaadin.artur.firebase.ui.crud;

import java.util.Optional;

import org.vaadin.artur.firebase.FirebaseDataProvider;
import org.vaadin.artur.firebase.db.UserDB;
import org.vaadin.artur.firebase.db.data.User;

import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;

public class CrudView extends VerticalLayout {
    private Grid<User> grid = new Grid<>(User.class);
    private Button newButton = new Button("New");
    private Button deleteButton = new Button("Delete");
    private UserForm form = new UserForm();
    private FirebaseDataProvider<User> dataProvider;

    public CrudView() {
        dataProvider = new FirebaseDataProvider<>(User.class,
                UserDB.getUsersDb());
        grid.setDataProvider(dataProvider);
        grid.removeColumn("key");
        grid.addSelectionListener(e -> {
            if (!e.isUserOriginated()) {
                return;
            }

            Optional<User> selected = e.getFirstSelectedItem();
            if (selected.isPresent()) {
                User user = selected.get();
                form.setItem(dataProvider.getId(user), user);
                deleteButton.setEnabled(true);
            } else {
                form.reset();
                deleteButton.setEnabled(false);
            }
        });

        newButton.addClickListener(e -> {
            form.newItem();
        });
        deleteButton.setEnabled(false);
        deleteButton.addClickListener(e -> {
            if (grid.getSelectedItems().isEmpty()) {
                return;
            }

            UserDB.delete(grid.getSelectedItems().iterator().next());
        });
        Link link = new Link("https://github.com/Artur-/vaadin-on-fire",
                new ExternalResource(
                        "https://github.com/Artur-/vaadin-on-fire"));
        link.setTargetName("_blank");
        addComponents(new Label(
                "This is a simple CRUD which uses Firebase and push to support multiple users simultaneously editing the same data"),
                link, grid, new HorizontalLayout(newButton, deleteButton),
                form);
    }

}
