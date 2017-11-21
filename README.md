Example of using Vaadin and Firebase
==============

To get started:
* Go to https://console.firebase.google.com/ and create a new project
* Go to project settings -> service accounts, e.g. https://console.firebase.google.com/project/YOUR_PROJECT_ID/settings/serviceaccounts/adminsdk
  * Select Java
  * Click "Generate new private key"
  * Store the download JSON file in src/main/resources/org/vaadin/artur/firebase/db/serviceAccount.json
* Build and start the project using `mvn jetty:run`
