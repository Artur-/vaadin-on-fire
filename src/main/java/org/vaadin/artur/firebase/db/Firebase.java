package org.vaadin.artur.firebase.db;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import elemental.json.Json;
import elemental.json.JsonObject;

public class Firebase {
    private static FirebaseApp app;

    public static void setup() throws IOException {
        if (app != null) {
            return;
        }

        String serviceAccount = IOUtils.toString(
                Firebase.class.getResourceAsStream("serviceAccount.json"),
                StandardCharsets.UTF_8);
        JsonObject serviceAccountJson = Json.parse(serviceAccount);
        String projectId = serviceAccountJson.getString("project_id");
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials
                        .fromStream(IOUtils.toInputStream(serviceAccount)))
                .setDatabaseUrl("https://" + projectId + ".firebaseio.com")
                .build();

        app = FirebaseApp.initializeApp(options);

        maybeGenerateData();

    }

    /**
     * Creates some initial data if the database is empty
     */
    private static void maybeGenerateData() {
        getDb().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // Wait for initial data before deciding to create or not
                UserDB.maybeCreateInitialData();
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    public static DatabaseReference getDb() {
        return FirebaseDatabase.getInstance(app).getReference();
    }

}
