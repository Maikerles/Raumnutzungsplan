
# SAE Raumnutzungsplan

Benötigte Installationen

- [JDK 11 installieren](https://download.java.net/openjdk/jdk11/ri/openjdk-11+28_windows-x64_bin.zip)
- [Maven installieren](https://dlcdn.apache.org/maven/maven-3/3.8.4/binaries/apache-maven-3.8.4-bin.zip)

#### Umgebungsvariablen zu der neuen JDK version ändern 

###

| execute | 
| :-------- |
| `DB_raumnutzungsplan.sql` |

###

#### Projektordner auswählen
```bash
  cd C:/user/'project folder'
```
#### Maven Installation
```bash
  mvn clean install
```
#### Den erstellten 'target' Ordner auswählen
```bash
  cd target
```
#### Spring Boot ausführen
```bash
  java -jar .\raumnutzungsplan-1.0.jar
```
