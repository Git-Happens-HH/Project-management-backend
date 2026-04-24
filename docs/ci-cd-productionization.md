# CI/CD-putken tuotantokelpoistaminen (Spring Boot + Docker + Rahti/OpenShift)

## 1. Aihe ja tavoite

Tässä seminaarityössä laitan kasaan ohjelmistoprojekti 2:n Spring Boot sovelluksellemme tuotantokelpoisen CI/CD-putken.
Projektin tavoitteena on automatisoida build-, testaus-, turvallisuus- ja deploy-prosessit sekä parantaa julkaisuvarmuutta
ja palautumiskykyä.

Työssä on käytössä staging- ja production-deployt Openshiftissä, productionissa approval gate sekä rolloutin varmistus ja rollback-toiminto.

## 2. Toteutusymparisto ja teknologiat

- Sovellus: Spring Boot (Java 21), Maven, PostgreSQL
- CI/CD: GitHub Actions
- Kontitus: Docker, GHCR
- Deploy: OpenShift / Rahti
- Security gate: OWASP Dependency-Check + Trivy
- Operointi: rollout verify + rollback shell-skriptit

## 3. Ratkaisun arkkitehtuuri

```mermaid
flowchart LR
  A[Feature branch] --> B[Pull Request]
  B --> C[PR Check: build test + Trivy]
  C -->|Pass| D[Merge main]
  D --> S[Security Scan workflow: OWASP + Trivy]
  D --> E[Staging deploy]
  E --> F[Rollout + health verify]
  F --> G[Create release tag vX.Y.Z]
  G --> H[Production workflow]
  H --> I[Manual approval gate]
  I --> J[Production deploy]
  J --> K[Rollout + health verify]
  K --> L[Rollback if needed]
```

## 4. Mitä toteutettiin koodiin

### 4.1 PR-laatu- ja tietoturvaportti

Workflow: .github/workflows/pr-check.yml

Toteutetut vaiheet:
- Maven build ja testit (`mvn clean verify`)
- Docker image build
- Trivy image scan (HIGH/CRITICAL -> fail)

PR-portin tarkoitus on varmistaa, etta mergeen menevä muutos on teknisesti ehjä ja ettei konttikuvassa ole kriittisia löytöjä.

### 4.2 Erillinen security-scan workflow

Workflow: .github/workflows/security-scan.yml

Toteutetut vaiheet:
- OWASP Dependency-Check Maven-pluginilla (CVSS-raja)
- Trivy container scan
- Dependency-Check-raportin julkaisu artifactina

Triggerit:
- manual workflow_dispatch
- ajastettu ajo (cron)

Perustelu muutokselle:
- OWASP-skannaus oli raskas ja pidensi PR-putkea merkittävästi
- erillisessa workflowssa turvallisuustarkistus on vakaampi ja helpompi seurata

### 4.3 Staging deploy

Workflow: .github/workflows/deploy-staging.yml

Sisaltö:
- image build + push GHCR:aan
- deploy OpenShiftiin
- rolloutin ja healthin varmistus skriptilla

### 4.4 Production deploy + approval gate

Workflow: .github/workflows/deploy-production.yml

Sisaltö:
- trigger tagista (`v*.*.*`) tai manuaalisesti
- deploy production namespaceen
- GitHub environment `production` ja required reviewers

### 4.5 OpenShift-manifestit ja operointiskriptit

- ops/openshift/deployment.yaml
- ops/openshift/service.yaml
- ops/openshift/route.yaml
- ops/openshift/deploy.sh
- ops/openshift/verify-rollout.sh
- ops/openshift/rollback.sh

### 4.6 Sovelluksen valmius health-probeihin

Sovellukseen lisättiin tarvittavat muutokset:
- actuator health/info exposed profileen
- security-configiin sallinnat health-endpointeille

## 5. Governance ja julkaisumalli

Käyttöön otettiin seuraavat hallintakäytannot:
- branch protection `main`-haaralle
- merge vain PR:n kautta
- pakollinen onnistunut status check ennen mergeä
- vaaditut reviewerit production-julkaisuun
- release tag -malli (`vMAJOR.MINOR.PATCH`)

Tälla mallilla julkaisu ei ole enaa yksittäisen kehittäjän käsityota, vaan hallittu prosessi.

## 6. Ennen vs jalkeen

| Mittari | Ennen | Jalkeen |
|---|---|---|
| PR-laadunvarmistus | Ei yhtenäistä gatea | Build + test + Trivy automaattisesti |
| Riippuvuusturvallisuus | Manuaalinen tai satunnainen | OWASP Security Scan erillisessa workflowssa |
| Staging-julkaisu | Pääosin manuaalinen | Automatisoitu workflow |
| Production-julkaisu | Ei virallista approval gatea | GitHub environment approval gate |
| Rollback | Ei vakioitua prosessia | Scriptattu rollback |
| Julkaisun toistettavuus | Vaihteleva | Dokumentoitu ja toistettava |

Huomio:
- OWASP Security Scan voi olla ensimmaisella ajolla hidas NVD-datan paivityksen takia.
- `NVD_API_KEY` nopeuttaa skannauksia merkittavasti.

## 7. Ongelmia ja niiden ratkaisut

Tyon aikana kohtasin useita CI/CD-ongelmia:
- Action-versioiden yhteensopivuusongelmat
- riippuvuus actioneista, jotka eivat toimineet odotetusti
- Docker-pohjaisen OWASP-ajon pitkät jumit image pullissa ja datapäivityksissä
- pitkät skannausajat ilman NVD API keyta

Ratkaisuperiaate oli:
- yksinkertaistettiin putkea
- poistettiin hauraat riippuvuudet
- siirrettiin OWASP-scan erilliseen security-scan workflowyn Maven-pluginiin
- lisättiin selkeat fail-kriteerit

## 8. Mitä opin

Tärkeimmat oppimani asiat:
- tuotantokelpoinen CI/CD on ennen kaikkea riskienhallintaa
- security gate tulee suunnitella niin, etta se on vakaa ja toistettava
- deployment ei riitä ilman verifiointia (rollout + health)
- rollback kannattaa tuotteistaa etukäteen, ei vasta ongelmatilanteessa
- GitHub branch protection + environment approvals ovat olennainen osa teknistä laatua

## 9. Jatkokehitysideat

- lisää integroidut smoke-testit staging deployn jälkeen
- lisää image signing (esim. Cosign) ennen production deployta
- ota käyttöön SARIF-raportit security-löydoksille
- mittaa lead time ja MTTR automaattisesti (dashboard)
- erottele dependency-checkin data-cache pysyvämmin CI-ymparistöön

## 10. Oma osuus ja lahteet

### Oma osuus

Suunnittelin ja toteutin putken rakenteen, workflowt, OpenShift-manifestit, operointiskriptit seka tarvittavat sovellusmuutokset. Lisäksi debuggasin käytännön CI-ajojen virheitä iteratiivisesti, kunnes putki eteni stabiiliin tilaan.

### Lähteet

- GitHub Actions documentation: https://docs.github.com/actions
- OWASP Dependency-Check: https://jeremylong.github.io/DependencyCheck/
- Trivy documentation: https://trivy.dev/latest/
- OpenShift docs: https://docs.openshift.com/
- Spring Boot Actuator: https://docs.spring.io/spring-boot/reference/actuator/

## 11. Videolinkki

Lisää tähan lopullinen video (n. 5 min):
- PR-checkin fail/pass -kayttaytymisen
- staging deployn onnistuminen
- production approval gate
- rollbackin tai sen demonstratiivinen ajo

Videolinkki:
