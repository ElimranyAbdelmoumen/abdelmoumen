# Checklist des tests de non-régression (BookShop API)

À exécuter manuellement ou via Swagger/Postman après tout correctif.  
Les tests automatisés (ApiIntegrationTest + CartServiceTest) couvrent une partie ; cette checklist couvre l’ensemble du plan.

## Prérequis

- API lancée : `mvn spring-boot:run "-Dspring-boot.run.profiles=local"`
- Base URL : `http://localhost:8080`
- Comptes : `admin@bookshop.com` / `admin123`, `user@bookshop.com` / `user123`

---

## 1.2 Public et health

| # | Test | Méthode | URI | Attendu |
|---|------|---------|-----|---------|
| 1 | Health | GET | `/actuator/health` | 200, `"status":"UP"` |
| 2 | Swagger UI | GET | `/swagger-ui.html` | 200, page Swagger |
| 3 | Catégories | GET | `/api/public/categories` | 200, liste (Roman, Bande dessinée, Informatique) |
| 4 | Liste livres | GET | `/api/public/books?page=0&size=10` | 200, `content` avec `categoryName` (pas de 500) |
| 5 | Détail livre | GET | `/api/public/books/1` | 200, détail livre |
| 6 | Livre inexistant | GET | `/api/public/books/99999` | 404 |

## 1.3 Authentification

| # | Test | Méthode | URI | Body / action | Attendu |
|---|------|---------|-----|----------------|---------|
| 7 | Login admin | POST | `/api/auth/login` | `{"email":"admin@bookshop.com","password":"admin123"}` | 200, `token` présent |
| 8 | Login user | POST | `/api/auth/login` | `{"email":"user@bookshop.com","password":"user123"}` | 200, `token` présent |
| 9 | Login invalide | POST | `/api/auth/login` | `{"email":"admin@bookshop.com","password":"wrong"}` | 401 ou 403 |
| 10 | GET login (navigateur) | GET | `/api/auth/login` | — | 200 ou 405 (pas 403) |

## 1.4 Panier (avec token)

Header : `Authorization: Bearer <token>` (token sans guillemets dans Swagger Authorize).

| # | Test | Méthode | URI | Body | Attendu |
|---|------|---------|-----|------|---------|
| 11 | Panier vide | GET | `/api/cart` | — | 200, `items: []`, `total: 0` |
| 12 | Ajouter au panier | POST | `/api/cart/items` | `{"bookId":1,"quantity":2}` | 200, item avec subTotal |
| 13 | Panier avec articles | GET | `/api/cart` | — | 200, items, total > 0 |
| 14 | Modifier quantité | PUT | `/api/cart/items/{itemId}` | `{"quantity":3}` | 200 |
| 15 | Supprimer du panier | DELETE | `/api/cart/items/{itemId}` | — | 204 |
| 16 | Panier sans token | GET | `/api/cart` | pas d’Authorization | 401 ou 403 |
| 17 | Stock insuffisant | POST | `/api/cart/items` | `{"bookId":1,"quantity":99999}` | 400 ou message d’erreur |

## 1.5 Admin (token ADMIN)

| # | Test | Méthode | URI | Body | Attendu |
|---|------|---------|-----|------|---------|
| 18 | Créer livre | POST | `/api/admin/books` | `{"title":"Nouveau","author":"Auteur","price":19.99,"stock":5,"categoryId":1}` | 200 ou 201 |
| 19 | Supprimer livre | DELETE | `/api/admin/books/{id}` | id existant | 204 |
| 20 | Admin avec token USER | POST | `/api/admin/books` | même body, token user | 403 |

## 1.6 Swagger / Authorize

| # | Test | Action | Attendu |
|---|------|--------|---------|
| 21 | Bouton Authorize | Ouvrir `/swagger-ui.html` | Bouton **Authorize** visible |
| 22 | Token avec guillemets | Authorize avec token collé avec guillemets | Requêtes protégées OK (200/204) |
| 23 | Token sans guillemets | Authorize avec token sans guillemets | Idem |

## 1.7 Tests Maven

| # | Test | Commande | Attendu |
|---|------|----------|---------|
| 24 | Tous les tests | `mvn -q test` | BUILD SUCCESS |
| 25 | Tests panier | `mvn -q -Dtest=CartServiceTest test` | Tous passent |

---

Cocher au fur et à mesure pour valider la non-régression.
