# Suite du workflow Git/GitHub (étapes 3–6)

**État actuel :** Les étapes 1–2 sont refaites (main et dev à jour). Les 3 branches feature sont **déjà sur origin** (`feature/swagger`, `feature/health-check`, `feature/tests-panier`).

## Étape 3 — Créer les 6 issues (GitHub)

Utiliser le fichier **[ISSUES_A_CREER.md](ISSUES_A_CREER.md)** : il contient les 6 titres et descriptions.  
Créer chaque issue ici : https://github.com/ElimranyAbdelmoumen/abdelmoumen/issues/new

## Étape 4 — Créer les 3 PR (feature → dev)

Utiliser **[LIENS_PR_GITHUB.md](LIENS_PR_GITHUB.md)** : un lien par PR (base = dev, compare = branche feature). Cliquer sur chaque lien pour ouvrir la page de création de PR, vérifier base = `dev`, puis **Create pull request**.

Ou manuellement : **Pull requests** → **New pull request** → Base : `dev`, Compare : `feature/swagger` puis `feature/health-check` puis `feature/tests-panier`.

## Étape 5 — Reviews

Pour **chaque** PR : un **autre** membre (pas l’auteur) ouvre la PR → **Files changed** → **Review changes** → commentaire + **Approve** → **Submit review**.

## Étape 6 — Merge

Sur chaque PR, après au moins une review Approve : **Merge pull request** → **Confirm merge**.

---

Résumé des 3 features (déjà poussées) :

| Branche                 | Contenu |
|-------------------------|--------|
| `feature/swagger`       | SpringDoc OpenAPI (dépendance + config + autorisation dans SecurityConfig) |
| `feature/health-check`  | Spring Boot Actuator, endpoint `/actuator/health` exposé et autorisé en public |
| `feature/tests-panier` | Tests unitaires `CartServiceTest` (getCart, addItem, updateItemQuantity, removeItem + cas d’erreur) |
