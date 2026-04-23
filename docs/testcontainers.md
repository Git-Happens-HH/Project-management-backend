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


# Testcontainers Spring Boot -projektissa

# 1. Johdanto

## 1.1. Aihealueen esittely

Ohjelmistokehityksen tärkeimpiä kulmakiviä on ohjelmiston testaus. Testauksella arvioidaan ohjelmiston laatua ja havaitaan ohjelmiston eri osissa piileviä vikoja. 

Testauksessa on perinteisesti 3 testikerrosta:

```
                 /\
                /  \
               /    \
              /      \          <-- Käyttäjän polun simulointi 
             /  E2E   \
            /----------\    
           /            \
          / INTEGRAATIO- \      <-- Ohjelmiston eri osien yhteentoimivuus
         /    TESTIT      \
        /----------------- \
       /                    \
      /     YKSIKKÖTESTIT    \  <-- Yksittäiset luokat tai moduulit
     /________________________\

```
Yksikkötestit ovat jo tulleet melkoisen tutuiksi eri ohjelmointikursseilta. Tässä seminaarityössä haluan perehtyä enemmän keskimmäiseen tasoon, eli integraatiotestaukseen. Valitsin tässä työssä tutkittavaksi teknologiaksi Testcontainers-kirjaston, jolla voin testata sovelluksen ja tietokannan välistä integraatiota. 

```
                 /\
                /  \
               /    \
              /      \          
             /        \
            /----------\    
           /            \
          / Ollaan Test- \     
         / containersin   \
        /  kanssa täällä...\
       /--------------------\
      /                      \
     /                        \  
    /__________________________\

```

## 1.2. Kohdeprojektin esittely

Toteutan integraatiotestausta "Ohjelmistoprojekti II" -kurssin projektiimme [Prokress](https://github.com/orgs/Git-Happens-HH/repositories). Prokress on tehtävänhallintatyökalu, jossa tiimin jäsenet voivat yhdessä hallita tehtäviä Kanban-tyylisellä taululla. Tehtäviä voidaan lisätä, muokata, poistaa ja siirtää "drag & drop" -tyylisesti tehtävälistoilta toisille.

Projektimme stack on seuraavanlainen:

Front-end:
- Typescript + React

Back-end: 
- Java + Maven + Spring Boot

Tietokannat:
- H2-in-memory tietokanta (devaamiseen ja testaukseen) 
- PostgreSQL-tietokanta (tuotantoympäristössä)


## 1.3. Tämän seminaarityön tavoitteet


_______________________________________________________________________________


# 2. Testcontainers

# 3. Projektin lähtötilanne

# 4. Testcontainersin toteutus 

# 5. Käytännön esimerkit testeistä

# 6. Vertailu H2-testien sekä Testcontainers-testien välillä

# 7. Haasteet ja opit 

# 8. Jatkokehitys

# 9. Yhteenveto


