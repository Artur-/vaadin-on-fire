package org.vaadin.artur.firebase.ui;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.annotation.WebServlet;

import org.vaadin.artur.firebase.db.Firebase;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

@Push
public class FireUI extends UI {

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

        setContent(new CrudView());
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = FireUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
