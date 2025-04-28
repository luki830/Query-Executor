QUERY EXECUTOR
==============

Leírás:
Egy Spring Boot alapú alkalmazás, amely SQL lekérdezéseket hajt végre fájlokból betöltött definíciók alapján, MySQL adatbázissal.

Fő Funkciók:
- SQL lekérdezések végrehajtása a `queries` mappában található `.sql` fájlok alapján.
- Környezeti változók segítségével konfigurálható adatbázis kapcsolat (host, felhasználónév, jelszó, adatbázisnév).
- TLS támogatás az adatbázis kapcsolathoz.
- Minden hívás naplózása (lekérdezés azonosító, végrehajtási idő, státuszkód).

Követelmények:
- Java 17
- Maven 3.8+
- Docker

Használat:
1. Projekt buildelése:
   mvn clean package

2. Docker konténer indítása:
   docker-compose up --build

3. API hívás példa:
   curl "http://localhost:8080/execute-query?query_identifier=get_user_data"

Környezeti Változók:
Az adatbázis kapcsolat konfigurációja:
- DB_HOST: Adatbázis host (pl. localhost).
- DB_PORT: Adatbázis port (pl. 3306).
- DB_NAME: Adatbázis neve (pl. mydb).
- DB_USER: Felhasználónév (pl. root).
- DB_PASSWORD: Jelszó (pl. example).

Tesztelés:
Egységtesztek futtatása:
   mvn test

Fájlstruktúra:
src/
├── main/
│   ├── java/com/example/queryexecutor/
│   │   ├── QueryExecutorApplication.java
│   │   └── QueryController.java
│   └── resources/
│       ├── application.yml
│       └── queries/
│           └── get_user_data.sql
└── test/
    ├── java/com/example/queryexecutor/
    │   ├── FileLoadingTest.java
    │   └── QueryExecutionTest.java

Naplózás:
A naplók az API hívások részleteit tartalmazzák:
- Lekérdezés azonosító
- Végrehajtási idő (ms)
- Státuszkód

Licenc:
Szabadon használható és módosítható.
