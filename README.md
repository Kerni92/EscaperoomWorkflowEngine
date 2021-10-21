# EscaperoomWorkflowEngine

## Systemvoraussetzungen

1. Installiertes Java in der Version 11 oder neuer
2. Lauffähige MariaDB in der Version 10.3.28 oder neuer
    1. Im Verzeichnis docker liegt eine docker-compose.yml die eine Definition für einen passenden MariaDB Container
       enthält
3. Aktueller Webbrowser

## Software starten

1. Projekt via Gradle Bauen
2. RunConfiguration wie im Screenshot anlegen im IntelliJ anlegen
   ![RunConfiguration](others/RunConfiguration.PNG)
3. Verbindung zur Datenbank in der src/main/resources/application.properties einstellen
    1. Defaultwerte:
        1. Datenbankname: escaperoomgame
        2. Datenbankbenutzer: root
        3. Datenbankpassword: escaperoomgamedb
    2. Bei der Verwendung des Dockerfiles im _Verzeichnis_ docker der Pfad für die Speicherung der Datenbank angepasst
       werden!
        1. Zu ersetzen ist unter _volumes_ der Windowspfad in der Datei durch einen Pfad der Wahl
4. Datenbank starten
5. Runconfiguration ausführen
6. http://localhost:8080 im Browser aufrufen
7. Defaultbenutzer für die Anlage von Workflows im System:
    1. Benutzer: admin PW: testpassword
8. Die Werte für Benutzer und Passwörter sind auf die verwendete Umgebung **anzupassen** und vor allem so **NICHT im
   Produktivbetrieb, d.h. der Server darf nicht aus dem Internet zu erreichen sein, zu verwenden!**
9. Ein Beispielescaperoom für den Import ist im Verzeichnis _others/beispielescaperoom.json_ zu finden

## Voraussetzungen und Ausführung der Integrationstests

1. Die Integrationstests verwendern die Bibliothek _testcontainers_. Diese setzt eine lauffähige Containerumgebung wie
   zum Beispiel Docker voraus.
2. Vor der Ausführung der Integrationstests muss die Containerisierungsplatform wie zum Beispiel Docker gestartet sein,
   andernfalls ist eine Ausführung nicht möglich und die Tests fallen hin.