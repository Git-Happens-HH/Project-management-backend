


# Testcontainers Spring Boot -projektissa

## 1. Johdanto

### 1.1. Aihealueen esittely

Ohjelmistokehityksen tärkeimpiä kulmakiviä on ohjelmiston testaus. Testauksella arvioidaan ohjelmiston laatua ja havaitaan ohjelmiston eri osissa piileviä vikoja. 
Testauksessa on perinteisesti 3 testikerrosta:

```
                 /\
                /  \
               /    \
              /      \         
             /  E2E   \
            /----------\    
           /            \
          / INTEGRAATIO- \      
         /    TESTIT      \
        /----------------- \
       /                    \
      /     YKSIKKÖTESTIT    \  
     /________________________\

```
Yksikkötestit ovat jo tulleet melkoisen tutuiksi eri ohjelmointikursseilta. Tässä seminaarityössä haluan perehtyä enemmän keskimmäiseen tasoon, eli integraatiotestaukseen. Valitsin tässä työssä tutkittavaksi teknologiaksi Testcontainers-kirjaston, jolla voin testata sovelluksen ja tietokannan välistä integraatiota. Testcontainers mahdollistaa tuotantoympäristöä vastaavan tietokannan käytön testeissä,
mikä parantaa testien luotettavuutta ja realistisuutta verrattuna esimerkiksi H2-tietokantaan. (Testcontainers, s.a., Kurian 2025)


### 1.2. Kohdeprojektin esittely

Toteutan integraatiotestausta "Ohjelmistoprojekti II" -kurssin projektiimme [Prokress](https://github.com/orgs/Git-Happens-HH/repositories). Prokress on tehtävänhallintatyökalu, jossa tiimin jäsenet voivat yhdessä hallita tehtäviä Kanban-tyylisellä taululla. Tehtäviä voidaan lisätä, muokata, poistaa ja siirtää "drag & drop" -tyylisesti tehtävälistoilta toisille.

Projektimme stack on seuraavanlainen:

Front-end:
- Typescript + React

Back-end: 
- Java + Maven + Spring Boot

Tietokannat:
- H2-in-memory tietokanta (devaamiseen ja testaukseen) 
- PostgreSQL-tietokanta (tuotantoympäristössä)


### 1.3. Tämän seminaarityön tavoitteet

1. Ottaa Testcontainers käyttöön sovellukseen
2. Integraatiotestien toteuttaminen repositorykerrokseen Testcontainersilla
3. Testcontainers-testien vertailu projektin nykyisiin H2-testeihin
4. Analysoida Testcontainersin käytön tuomia hyötyjä ja haasteita


## 2. Testcontainers

### 2.1. Lyhyt esittely Testcontainers-kirjastosta

[Testcontainers](https://testcontainers.com/getting-started/#what-is-testcontainers) on avoimen lähdekoodin kirjasto, joka tarjoaa helppokäyttöiset rajapinnat testien ja kehitysympäristön riipuvuuksien (kuten tietokantojen) käynnistämiseen [Docker-kontteina](https://docs.docker.com/get-started/docker-overview/). Testcontainers-kirjastoa käyttävät sekä monet pienemmät open source -projektit että suuret yritykset, kuten esimerkiksi Google, eBay, Skyscanner ja Wise. (Testcontainers, s.a)

Testcontainers toimii useilla ohjelmointikielillä ja alustoilla:

- Java
- Go
- .NET
- Node.js
- Clojure
- Elixir
- Haskell
- Python
- Ruby
- Rust
- PHP
- Native (kuten C, C++ ja Swift)

Testcontainers tukee useita moduuleja eri teknologioille; mm. tietokantoja, viestijärjestelmiä, hakukoneita ja pilvipalveluemulaattoreihin:

- PostgreSQL
- MySQL
- MongoDB
- Redis
- MariaDB
- Azurite (Azure Storage -emulaattori)
- Kafka
- RabbitMQ
- Ja lukuisia muita...

Testcontainers käynnistää tarvittavat palvelut automaattisesti Docker-kontteina testien ajaksi ja poistaa ne testien jälkeen. Testcontainers siis mahdollistaa testien ajamisen ympäristössä, joka vastaa todellista tuotantoympäristöä paremmin kuin esimerkiksi mockit tai in-memory-ratkaisut (Testcontainers, s.a, Trandafir 2026). Toisaalta Testcontainers-estit ovat hitaampia suorittaa, kuin nopeat in-memory-tietokantatestit.

### 2.2. Docker-teknologian rooli

Jotta Testcontainers voi tarjota realistisen tuotantoympäristön testiajon taustalle, käyttää se Docker-teknologiaa. Docker-teknologia erottaa sovelluksen infrastruktuurista. Dockerin avulla palvelu tai sovellus paketoidaan yhteen eristettyyn pakettiin (eli konttiin), jotta se toimii luotettavasti missä tahansa ympäristössä, riippumatta isäntäkoneesta. (Docker, s.a., Katamreddy 2024)

Testcontainers-kirjasto tarvitsee toimiakseen Docker-API-yhteensopivan ajoympäristön, kuten Docker Desktopin tai Docker Enginen. Testcontainers toimii Dockerin asiakkaana (client), joka lähettää pyyntöjä Docker-taustaprosessille (daemon), joka suorittaa konttien rakentamisen ja ajamisen. (Docker, s.a.)

### 2.3. Testin käynnistys ja odotusstategia

Kun testi aloitetaan, Testcontainers käynnistää tarvittavat palvelut (kuten PostgreSQL) Docker-kontteina. Nämä kontit voidaan itse konfiguroida, käyttää valmista moduulia tai tehdä komposiittiratkaisu. (Testcontainers, s.a.)

Käynnistyksen jälkeen Testcontainers ensin odottaa, että onko kontin sisällä oleva sovellus todella valmis vastaanottamaan pyyntöjä. Tätä kutsutaan odotusstrategiaksi (wait strategy). Odotustrategia on määritelty konttiin. Ilman tätä testit saattaisivat yrittää ottaa yhteyttä palveluun ennen kuin se on valmis, joka voisi johtaa epäluotettaviin testeihin. 

Valmis moduuli sisältää jo tarvittavat konfiguraatiot ja protokollat odotustrategiaan, kuten:
 - Porttiristiriitojen ratkaisu: Kontin sisäiset portit mapataan isäntäkoneella oleviin satunnaisiin vapaisiin portteihin (Testcontainers, s.a.). Näin esimerkiksi useat rinnakkain pyörivät testiputket eivät yritä käyttää samaa porttia. Testikoodi voi hakea näitä dynamiisia tietoja ajon aikana metodeilla, kuten `getHost()` ja `getMappedPort(int)` (Katamreddy, 2024).
  - Lokiviestin lukeminen: Voidaan odottaa, että kontti saa jonkin standardilokiviestin, ja jatkaa suoritusta vasta sen jälkeen. Esimerkki Testcontainersin sivulta:
```java
  _ = Wait.ForUnixContainer()
  .UntilMessageIsLogged("Server started", o => o.WithTimeout(TimeSpan.FromMinutes(1)));
```

Yllä olevassa koodiesimerkissä ohjelma odottaa, että lokiviesti saapuu ("Server started"). Siinä tapauksessa, jos viestiä ei kuulu yhteen minuuttiin, odotus keskeytyy.

 - HTTP-vastaus: Kontti lähettää HTTP-pyynnön API:in, ja odottaa siltä tiettyä vastausta (kuten 200).
 - Sisäinen TCP-portti: Tarkistaa kontin sisältä päin, että kuunteleeko sovellus kyseistä porttia. 
 - Ulkoinen TCP-portti: Tarkistaa kontun ulkopuolella, että isäntäkoneen ja kontin välinen porttimappaus toimii ja yhteys saadaan muodostettua niiden välille.
 - Docker-verkon luominen: Kehittäjä voi halutessaan myös luoda Docker-verkkoja, jotka yhdistää useita kontteja eri toisiinsa, jotta ne puhuvat toisilleen staattisilla verkkoaliaksilla.

### 2.4. Testin päättäminen ja resurssien siivous

Testin päätyttyä Testcontainers sammuttaa ja poistaa kontit, verkkoasetukset ja volyymit automaattisesti. Tämä prosessi toistetaan aina, riippumatta siitä, että onnistuiko, epäonnistuiko vai kaatuiko testi ajon aika. Tästä resurssienhallinnasta pitää huolta taustalla toimiva "Moby Ryuk"-niminen apukontti. Toinen nimi Moby Ryukille on "Resource Reaper". 

### Moby Ryukin nimi tulee Death Note -animesarjan hahmosta Ryuk
<p align="left">
  <img src="ryuk.png" alt="Piirretty kuva Death Note -sarjan Ryuk-hahmosta" width="300"><br>
  <em>Kuva 1. Death Note: Ryuk (Cheu-Sae 2013, <a href="https://creativecommons.org/licenses/by-nc-nd/3.0/">CC BY-NC-ND 3.0</a>)</em>
</p>

Kun Testcontainers käynnistää tarvittavat kontit, se lisää niihin uniikit labelit ja istuntotunnukset (session id). Ryuk käynnistyy myös ja tarkkailee noita tunnisteita. Ryuk tunnistaa, kun testi-istunto päättyy, ja tuhoaa ne automaattisesti. Kuten edellä todettiin, Ryuk toistaa tämän resurssintuhoamisen riippumatta siitä, että mikä testin status oli: onnistunut, epäonnistunut, kaatunut tai keskeytynyt. Ryuk on siis luotettava myös poikkeustilanteissa, kuten jos se saa "SIGKILL"-signaalin. Näin tietokone, testausympäristö ja CI/CD-putki pysyy siistinä, eikä täyty turhista konteista.

Muita huomionarvioisia seikkoja Ryukista:
- Ryuk tukee vain Linux-kontteja
- Ryukia ei suositella ottamaan pois käytöstä, ellei testiympäristössä ole muuta erillista tapaa siivota resursseja

## 3. Projektin lähtötilanne

### 1.1. Olemassaolevat testit

Tein tätä seminaarityötä varten Prokress-projektiimme [yksinkertaiset H2-testit, joilla testataan repositorykerroksen metodeita](https://github.com/Git-Happens-HH/Project-management-backend/tree/testcontainer/project-management-app/src/test/java/githappens/hh/project_management_app/RepositoryTests).

Otetaan malliesimerkiksi TaskRepositoryTests.java. Sen takana oleva Task.java domain-entiteeteistä monimutkaisin, sillä se yhdistyy useaan muuhun entiteettiin: TaskList, AppUser ja Project. TaskRepositoryTests testaa Spring Data JPA:n tietokantakerrosta - tarkemmin sanoen repositorykerrosta. Siihen on injektoitu TaskRepository, ProjectRepository, AppUserRepository sekä TaskListRepository. JPA hoitaa SQL-generoinnin, entity mappingin ja custom queryjen teon (kuten findByTitle)

TaskRepositoryTests hyödyntää useita Spring Boot Starter Tests -riippuvuuden tarjoamia teknologioita:
- `@Test`-annotaatio tulee JUnit 5:sta. Ohjelma tunnistaa testimetodin annotaation avulla.
- `@SpringBootTest` on Spring Bootin annotaatio, joka käynnistää koko Spring-sovelluksen contextin. Se lataa kaikki beanit, repositoryt, servicet, jne. Se simuloi "oikeaa sovellusta".
- `@Transactional`-annotaatio tekee testiluokan tietokantamuutokset väliaikaisesti H2-tietokantaan, mutta testin jälkeen peruu (rollback) nämä muutokset automaattisesti. 
(Spring, s.a)

Alla on esimerkki yksinkertaisesta CRUD-toiminnon testistä, jossa testataan repositorykerroksen kykyä luoda ja tallentaa uusi task sujuvasti:
```java
@Test
    public void createNewTask() {

        // luodaan uusi testikäyttäjä ja tallennetaan se
        AppUser user1 = new AppUser("test1", "Test", "User", "test1@hh.com", "Test123!", LocalDateTime.now());
        appUserRepository.save(user1);

        // luodaan uusi testiprojekti ja tallennetaan se
        Project project1 = new Project("Test Project1", "Description", LocalDateTime.now(), false);
        projectRepository.save(project1);

        // luodaan uusi testitasklist ja tallennetaan se
        TaskList taskList1 = new TaskList(project1, "Test TaskList1", LocalDateTime.now());
        taskListRepository.save(taskList1);

        // luodaan uusi task ja tallennetaan se
        Task task1 = new Task(taskList1, "Test Task1", "Testing task creation1", user1, user1, LocalDateTime.now().plusDays(1));
        taskRepository.save(task1);

        // tehdään assertiot, joilla varmistetaan luonnin ja tallennuksen toimivuus
        assertThat(task1.getTaskId()).isNotNull();
        assertThat(task1.getTitle()).isEqualTo("Test Task1");
        assertThat(task1.getDescription()).isEqualTo("Testing task creation1");
        assertThat(task1.getAssignedUser().getAppUserId()).isEqualTo(user1.getAppUserId());
    }
```

KIRJOITA YLÖS MYÖS TESTIEN NOPEUS YM PARAMETRIT

 

## 4. Testcontainersin toteutus 

## 5. Käytännön esimerkit testeistä

## 6. Vertailu H2-testien sekä Testcontainers-testien välillä

## 7. Haasteet ja opit 

## 8. Jatkokehitys

## 9. Yhteenveto

## 10. Lähteet

https://www.baeldung.com/spring-boot-built-in-testcontainers

https://docs.spring.io/spring-boot/reference/testing/testcontainers.html

https://testcontainers.com/getting-started/#testcontainers-workflow

https://java.testcontainers.org/

https://testcontainers.com/guides/testing-spring-boot-rest-api-using-testcontainers/

https://blog.jetbrains.com/idea/2024/12/testing-spring-boot-applications-using-testcontainers/

https://www.baeldung.com/spring-boot-built-in-testcontainers

https://stackshare.io/stackups/h2-database-vs-postgresql

https://devblogs.microsoft.com/ise/testing-with-testcontainers/

https://docs.docker.com/get-started/docker-overview/

Cheu-Sae. 2013. Death Note: Ryuk. Luettavissa: https://www.deviantart.com/cheu-sae/art/Death-Note-Ryuk-365460595. Lisenssi: CC BY-NC-ND 3.0.

https://docs.spring.io/spring-boot/reference/testing/spring-boot-applications.html

https://docs.junit.org/6.0.3/overview.html

https://assertj.github.io/doc/









# MARKDOWN OHJEET:


# OTSIKOT:

# Pääotsikko
## Väliotsikko
### Pienempi otsikko

_____________________________________________


# KOODI:

1. Yksi rivi:
`System.out.println("Hello");` 

2. Useampi rivi: 

```java
System.out.println("Hello");
```

______________________________________________

# LINKKI:

[Projekti](https://github.com/Git-Happens-HH/Project-management-backend/tree/testcontainer)

________________________________________________

# PREVIEW:

VS-codessa:

ctrl+shift+v

______________________________________________












