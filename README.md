Example of using Vaadin and Firebase
==============

To get started:
* Clone this project
* Go to https://console.firebase.google.com/ and create a new project
* Go to project settings -> service accounts, e.g. https://console.firebase.google.com/project/YOUR_PROJECT_ID/settings/serviceaccounts/adminsdk
  * Select Java
  * Click "Generate new private key"
  * Store the download JSON file in src/main/resources/org/vaadin/artur/firebase/db/serviceAccount.json
* Build and start the project using `mvn jetty:run`
* Open http://localhost:8080 in multiple browsers to see that live editing and listening to Firebase changes work
* Open https://console.firebase.google.com/project/YOUR_PROJECT_ID/database/YOUR_PROJECT_ID/data to see what is in the database