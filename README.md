# Job Searcher

## Leírás
A **Job Searcher** egy Spring Boot alapú webalkalmazás, amely lehetővé teszi a felhasználók számára, hogy munkákat keressenek helyszín és kulcsszavak alapján. Az alkalmazás H2 adatbázist használ a munkahelyek adatainak tárolására, és REST API-t biztosít a munkahelyek lekérdezésére.

## Funkcionalitások

1. **Kliensalkalmazások regisztrációja** (`POST /client`)
    - **Input**: Név (max 100 karakter), E-mail cím (érvényes e-mail cím formátum, egyediség ellenőrzés)
    - **Output**: API kulcs (UUID formátum)

2. **Állások létrehozása** (`POST /position`)
    - **Input**: Állás megnevezése (max 50 karakter), Munkavégzés helye (max 50 karakter)
    - **Output**: URL az újonnan létrehozott pozícióhoz

3. **Állások keresése** (`GET /position/search`)
    - **Input**: Keresett keyword (max 50 karakter), Lokáció (max 50 karakter)
    - **Output**: Találatok URL listája

4. **Állás részleteinek lekérése** (`GET /position/{id}`)
    - **Input**: Állás ID
    - **Output**: Állás részletei

## Kezdeti lépések

### Előfeltételek
- Java 17
- Gradle 8.8
- H2 Adatbázis

### Telepítés
1. Klónozzuk a repozitóriumot:
   ```
   git clone https://github.com/MonikaVera/job_searcher
   ```
2. Fordítás
    ```
   gradlew.bat build
   ```
3. Futtatás
    ```
   gradlew.bat bootRun
   ```
4. Adatbázis Elérése

    Az H2 adatbázis konzol elérése böngészőben:
    ```
    http://localhost:8080/h2-console
    ```
    JDBC URL:
    ```
    jdbc:h2:mem:testdb
    ```

## Konfiguráció
Az alkalmazás konfigurálható a src/main/resources/application.properties fájlban.

## Továbbfejlesztési Lehetőségek
- bejelentkezés és kijelentkezés, profil adatok módosítása
- jelentkezés az állásokra
- önéletrajz feltöltése
- egység és integrációs tesztelés
- JWT token alapú autentikáció
- Docker használata az alkalmazás konténerizálásához
