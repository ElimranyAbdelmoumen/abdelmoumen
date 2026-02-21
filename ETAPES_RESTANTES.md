# Étapes restantes (workflow examen)

Ce qui a été fait (analyse et refait) :

- **Étape 1** : `git fetch origin`, `git checkout main`, `git pull origin main` — fait.
- **Étape 2** : `git checkout dev`, `git pull origin dev` — fait.
- **Étape 4 (code)** : Les 3 branches feature existent sur GitHub (`feature/swagger`, `feature/health-check`, `feature/tests-panier`).

Ce qu’il reste à faire **à la main sur GitHub** (pas d’API token / gh CLI disponible) :

| Étape | Action | Fichier / lien utile |
|-------|--------|----------------------|
| **3** | Créer **6 issues** (titre + description) | [ISSUES_A_CREER.md](ISSUES_A_CREER.md) — copier/coller chaque bloc vers https://github.com/ElimranyAbdelmoumen/abdelmoumen/issues/new |
| **4** | Ouvrir **3 PR** (base = dev, compare = chaque feature) | [LIENS_PR_GITHUB.md](LIENS_PR_GITHUB.md) — un lien par PR |
| **5** | **Review** : pour chaque PR, un autre membre fait **Approve** | GitHub → PR → Review changes → Approve → Submit review |
| **6** | **Merge** les 3 PR dans `dev` | GitHub → chaque PR → Merge pull request → Confirm merge |

Optionnel : ensuite, une PR `dev` → `main` puis merge pour déclencher le CI/CD (déploiement).
