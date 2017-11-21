Example of using Vaadin and Firebase
==============

To get started:
1. Go to https://console.firebase.google.com/ and create a new project
2. Go to project settings -> service accounts, e.g. https://console.firebase.google.com/project/YOUR_PROJECT_ID/settings/serviceaccounts/adminsdk
 1. Select Java
 1. Click "Generate new private key"
 1. Store the download JSON file in src/main/resources/org/vaadin/artur/firebase/serviceAccount.json
3. Build and start the project using `mvn jetty:run`