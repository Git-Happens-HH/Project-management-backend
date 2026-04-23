# Mitä pitää tehdä GitHubissa ja VS Coden ulkopuolella

Tama checklist on pakollinen, jotta workflowt toimivat kaytannossa.

## 1. GitHub: Environments ja approval gate

Luo repositorioon environmentit:
- `staging`
- `production`

Aseta `production` environmentiin:
- Required reviewers (esim. sina + ohjaaja/tiimikaveri)
- Optional wait timer (esim. 5-10 min)

Tama tekee production-julkaisusta hyväksynnan vaativan portin.

## 2. GitHub: Secrets

Lisaa repository tai environment secretsit:
- `OPENSHIFT_SERVER`
- `OPENSHIFT_TOKEN`
- `OPENSHIFT_NAMESPACE_STAGING`
- `OPENSHIFT_NAMESPACE_PRODUCTION`
- `OPENSHIFT_ROUTE_HOST_STAGING` (optional, jos haluat pakottaa health-checkin tiettyyn hostiin)
- `OPENSHIFT_ROUTE_HOST_PRODUCTION` (optional, jos haluat pakottaa health-checkin tiettyyn hostiin)

Suositus:
- `OPENSHIFT_TOKEN` environment-kohtaisesti (`staging` ja `production` erikseen), ei globaalina jos mahdollista.
- Jos kaytossa on kiintea domain, aseta route-host secretit eksplisiittisesti (esim. production-host).

## 3. GitHub: Branch protection (main)

Aseta `main`-haaralle:
- Require pull request before merging
- Require approvals (1-2)
- Require status checks to pass
- Pakollinen check: `Build, Test, and Security Gates`
- Restrict who can push directly (tai disable direct push)

## 4. GitHub: Container registry (GHCR)

Varmista:
- Actions saa pushata GHCR:aan (`packages: write` on workflowssa)
- Organisaatio/repository policy sallii pakettien luonnin
- Tarvittaessa package visibility asetukset kuntoon

## 5. OpenShift/Rahti: Namespace ja oikeudet

Varmista OpenShiftissa:
- Staging ja production namespace olemassa
- Tokenilla on oikeus:
  - `oc apply`
  - `oc set image`
  - `oc rollout status`
  - `oc rollout undo`
- Sovellukselle tarvittava secret on olemassa:
  - `project-management-app-secrets`

Secretin sisalto minimi:
- `POSTGRESQL_DATABASE`
- `POSTGRESQL_USER`
- `POSTGRESQL_PASSWORD`

## 6. OpenShift/Rahti: image pull

Jos cluster ei saa suoraan lukea GHCR-imagea:
- Luo imagePullSecret (GHCR PAT)
- Linkita se service accountiin tai deploymentiin

## 7. Seminaaridemoa varten

Suunnittele 3 demon ajoa:
1. PR-check fail (esim. tarkoituksella haavoittuva dependency) -> gate estaa mergea.
2. Staging deploy success -> rollout + health check vihreana.
3. Production deploy + manual approval + rollback-demo.

Dokumentoi jokaisesta:
- aloitusaika
- valmistumisaika
- onnistuiko/failasiko
- tarvittavat manuaaliset toimenpiteet

## 8. Suositeltu taggaus productioniin

Kun haluat julkaista tuotantoon:
1. Varmista etta `main` on vihrea.
2. Luo release tagi:
   - `git tag v1.0.0`
   - `git push origin v1.0.0`
3. Hyvaksy production environment GitHubissa.
4. Varmista deploy tulos OpenShiftista.
