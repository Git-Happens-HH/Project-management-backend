# CI/CD-putken tuotantokelpoistaminen (Spring Boot + Docker + Rahti/OpenShift)

Tama dokumentti kuvaa toteutetun putken rakenteen, release governance -mallin ja mittarit seminaarityohon.

## 1. Putken rakenne

### PR-check (laadun ja turvallisuuden portti)
Workflow: `.github/workflows/pr-check.yml`

Sisalto:
- Maven build + test (`mvn clean verify`)
- OWASP Dependency-Check (`failOnCVSS=7`)
- Docker image build
- Trivy container scan (fail jos loytyy `HIGH` tai `CRITICAL`)

Hyvaksyntakriteeri:
- Kaikki vaiheet vihreana ennen mergea `main`-haaraan.

### Staging-deploy
Workflow: `.github/workflows/deploy-staging.yml`

Trigger:
- Push `main`-haaraan
- Manual `workflow_dispatch`

Sisalto:
- Build + push image GHCR:aan
- Deploy OpenShiftiin (`ops/openshift/deploy.sh`)
- Rollout- ja health-varmistus (`ops/openshift/verify-rollout.sh`)

### Production-deploy (approval gate)
Workflow: `.github/workflows/deploy-production.yml`

Trigger:
- Tagit `v*.*.*`
- Manual `workflow_dispatch` (valinnainen image_tag)

Sisalto:
- Release-imagen build/push (tag-triggerilla)
- Deploy production namespaceen
- Rollout + health-varmistus

Hyvaksyntaportti:
- `environment: production` (vaatii GitHubissa `Required reviewers`)

## 2. Release governance: branch- ja tag-strategia

Suositeltu malli:
- `main`: aina deploy-kelpoinen, suojattu haara
- feature-haarat: `feature/*` (tyo tehdan taalla)
- korjaushaarat: `hotfix/*`
- release-tagi productioniin: `vMAJOR.MINOR.PATCH` (esim. `v1.3.0`)

Saannot:
- Merge `main`-haaraan vain PR:n kautta
- Vahintaan 1-2 reviewta PR:lle
- Pakollinen status check: `PR Check / Build, Test, and Security Gates`
- Ei suoria pushauksia `main`-haaraan

## 3. Turvallisuus osana putkea

Toteutetut kontrollit:
- Dependency-tason haavoittuvuustarkistus (OWASP Dependency-Check)
- Konttikuvan haavoittuvuustarkistus (Trivy)
- Build failataan automaattisesti kriteerien ylittyessa

Fail-kriteerit:
- OWASP: CVSS >= 7
- Trivy: loytyy HIGH/CRITICAL haavoittuvuuksia

## 4. Deployment reliability ja operointi

OpenShift-artifaktit:
- `ops/openshift/deployment.yaml`
- `ops/openshift/service.yaml`
- `ops/openshift/route.yaml`

Operointiskriptit:
- Deploy: `ops/openshift/deploy.sh`
- Rollout + health verify: `ops/openshift/verify-rollout.sh`
- Rollback: `ops/openshift/rollback.sh`

Sovelluksen valmius health-checkeihin:
- Actuator health/info exposed (`application-rahti.properties`)
- Securityssa sallittu `/actuator/health/**` ja `/actuator/info`

Rollback-demo (seminaariin):
1. Deployaa uusi versio stagingiin/productioniin.
2. Simuloi virhe (esim. deployaa tahallisesti viallinen image).
3. Aja rollback:
   - `ops/openshift/rollback.sh <namespace> project-management-app`
4. Todista palautuminen:
   - rollout status onnistuu
   - health endpoint vastaa onnistuneesti

## 5. Mittarit ennen vs jalkeen

Alla mittarit, joilla osoitat tuotantokelpoistamisen vaikutuksen.

| Mittari | Ennen | Jalkeen | Miten mitataan |
|---|---|---|---|
| Lapimenoaika (commit -> staging) | Manuaalinen arvio | GitHub Actions duration | Workflow runin alusta staging verifyyn |
| Manuaalivaiheiden maara | Korkea | Matalampi | Laske julkaisun kasin tehtavat stepit |
| Muutoksesta tuotantoon (lead time) | Pitka/epavakaa | Toistettava | Tagin luonnista production deploy successiin |
| Palautumisaika (rollback MTTR) | Ei vakioitu | Mitattavissa | Virheen havaitsemisesta rollback successiin |
| Turvallisuuspoikkeamat ennen deployta | Vaihteleva | Gate estaa riskit | PR-checkin OWASP + Trivy tulokset |

Suositus seminaaridiagnoosiin:
- Kerää 3-5 oikeaa pipeline-ajon dataa.
- Raportoi mediaani ja vaihteluvali, ei vain yksittainen paras ajo.

## 6. Rajaus

Taman tyon fokus:
- CI/CD-prosessi
- release governance
- turvallisuuskontrollit putkessa
- deployment reliability (verify + rollback)

Yksikkotestaus on mukana quality gate -roolissa, mutta testausmenetelmien syvakehitys on rajattu taman tyon ulkopuolelle.
