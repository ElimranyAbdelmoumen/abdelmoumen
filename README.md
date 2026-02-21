# BookShop API

API REST de boutique de livres en ligne — Spring Boot, JWT, MySQL, Docker, GitHub Actions.

## Convention projet (barème DevOps)

- **Utilisateur Linux** : `abdelmoumen`
- **Dossier de travail** : `/home/abdelmoumen/bookshop`
- **Dépôt GitHub** : `abdelmoumen`

### Commandes utilisées sur le serveur (création utilisateur et dossier)

```bash
sudo useradd -m -s /bin/bash abdelmoumen
sudo passwd abdelmoumen
sudo usermod -aG sudo abdelmoumen
sudo usermod -aG docker abdelmoumen
mkdir -p /home/abdelmoumen/bookshop
```

Connexion au serveur : `ssh abdelmoumen@37.27.214.35`

## Prérequis

- Java 21, Maven 3.9+
- MySQL (préinstallé sur le serveur : root / mot de passe fourni par l’équipe)
- Docker (pour build image et déploiement)

## Base de données MySQL

Sur le serveur, créer la base si besoin :

```sql
CREATE DATABASE IF NOT EXISTS bookshop;
```

Les identifiants MySQL ne doivent **pas** être dans le code : utiliser des variables d’environnement (ou un fichier `.env` non versionné).

## Lancement en local

### Option A — Sans MySQL (profil `local`, H2 en mémoire)

```powershell
mvn spring-boot:run "-Dspring-boot.run.profiles=local"
```

L’API écoute sur le port 8080. Données en mémoire (seed via `DataLoader`).

### Option B — Avec MySQL

1. Variables d’environnement (ou profil par défaut avec `application.yml`) :
   - `MYSQL_HOST`, `MYSQL_PORT`, `MYSQL_DATABASE`, `MYSQL_USER`, `MYSQL_PASSWORD`
   - Optionnel : `JWT_SECRET`

2. Lancer l’API :

```bash
mvn spring-boot:run
```

L’API écoute sur le port 8080. Les données de seed créent un admin et un user de test (voir `DataLoader`).

## Lancement avec Docker (local ou serveur)

1. Créer un fichier `.env` (à partir de `.env.example`) avec au minimum :
   - `MYSQL_PASSWORD`
   - `JWT_SECRET`
   - Sur Linux, si le conteneur ne joint pas MySQL : `MYSQL_HOST=172.17.0.1` ou activer `host.docker.internal` selon la version de Docker.

2. Démarrer :

```bash
docker compose up -d --build
```

L’API est disponible sur `http://localhost:8080`.

## Routes de l’API

### Public (sans JWT)

- `GET /api/public/categories` — liste des catégories
- `GET /api/public/books?page=0&size=5` — livres paginés
- `GET /api/public/books/{id}` — détail d’un livre

### Authentification

- `POST /api/auth/login` — body `{ "email", "password" }` → `{ "token": "..." }`

### Panier (JWT requis)

- `GET /api/cart` — panier de l’utilisateur connecté
- `POST /api/cart/items` — body `{ "bookId", "quantity" }`
- `PUT /api/cart/items/{itemId}` — body `{ "quantity" }`
- `DELETE /api/cart/items/{itemId}`

### Admin (JWT + rôle ADMIN)

- `POST /api/admin/books` — création d’un livre
- `DELETE /api/admin/books/{id}`

Header pour les routes protégées : `Authorization: Bearer <token>`.

## CI/CD (GitHub Actions)

À chaque push sur `main` :

1. Build Maven et tests
2. Build de l’image Docker
3. Déploiement par SSH sur le serveur : `git pull` dans `/home/abdelmoumen/bookshop`, puis `docker compose up -d --build`
4. Health check : `curl` sur `/api/public/categories`

Secrets GitHub à configurer : `SERVER_SSH_KEY`, `SERVER_HOST`, `SERVER_USER`. Les mots de passe MySQL et JWT restent sur le serveur (fichier `.env` non versionné).

## Livrables dans le dépôt

- Code Spring Boot
- `Dockerfile`
- `docker-compose.yml`
- Workflow GitHub Actions (`.github/workflows/deploy.yml`)
- README (ce fichier)

## Utilisateurs de test (seed)

- Admin : `admin@bookshop.com` / `admin123`
- User : `user@bookshop.com` / `user123`

*(À modifier en production ; ne pas committer de vrais mots de passe.)*
