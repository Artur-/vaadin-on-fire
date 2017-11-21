package org.vaadin.artur.firebase.ui;

import org.vaadin.artur.firebase.db.UserDB;
import org.vaadin.artur.firebase.db.data.User;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class UserForm extends VerticalLayout {
    private TextField name = new TextField("Name");
    private TextField age = new TextField("Age");
    private Button save = new Button("Save");
    private int id;
    private Binder<User> binder;

    public UserForm() {
        addComponents(name, age, save);
        binder = new Binder<>(User.class);
        binder.forField(age)
                .withConverter(new StringToIntegerConverter(0, "Not a number"))
                .bind("age");
        binder.bindInstanceFields(this);

        save.addClickListener(e -> {
            saveItem();
        });

        setEnabled(false);
    }

    public void newItem() {
        setItem(-1, new User("", 0));
    }

    public void setItem(int id, User user) {
        this.id = id;
        binder.setBean(user);
        setEnabled(true);
        name.focus();
    }

    private void saveItem() {
        User item = binder.getBean();
        if (id == -1) {
            UserDB.add(item);
        } else {
            UserDB.update(id, item);
        }
        setEnabled(false);
    }

    public void reset() {
        setEnabled(false);
        binder.setBean(new User());
        id = -2;
    }

}
