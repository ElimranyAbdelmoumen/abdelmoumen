# BookShop API — Script de présentation vidéo

Vidéo courte (5–10 min) : projet, stack, lancement, parcours utilisateur complet.

---

## Structure

```
Intro projet → Stack technique → Lancement app → Demo publique → Login et Swagger → Panier → Admin → CI/CD et conclusion
```

---

## Scénario détaillé (à enregistrer)

| Séquence | Durée | Contenu |
|----------|-------|--------|
| **1. Intro** | 0:00–0:30 | Titre : « BookShop API – Présentation ». Rappel : API REST boutique de livres, examen DevOps / Spring Boot. |
| **2. Stack** | 0:30–1:00 | Spring Boot 3, Java 21, JWT, Spring Data JPA, MySQL/H2, Docker, GitHub Actions. Montrer la structure du projet (controllers, services, config). |
| **3. Lancement** | 1:00–1:30 | Terminal : `mvn spring-boot:run "-Dspring-boot.run.profiles=local"`. Montrer « Started BookshopApplication » et port 8080. |
| **4. Demo publique** | 1:30–2:30 | Navigateur : `GET /api/public/categories` puis `GET /api/public/books?page=0&size=10`. Optionnel : `GET /api/public/books/1`, `GET /actuator/health`. |
| **5. Swagger et login** | 2:30–4:00 | Ouvrir `http://localhost:8080/swagger-ui.html`. POST /api/auth/login avec admin, montrer le token. Authorize → coller le token **sans guillemets**. |
| **6. Panier** | 4:00–5:00 | GET /api/cart (vide). POST /api/cart/items avec body ci-dessous. GET /api/cart. DELETE /api/cart/items/{itemId}. GET /api/cart. |
| **7. Admin** | 5:00–5:45 | POST /api/admin/books avec body ci-dessous. GET /api/public/books pour voir le nouveau livre. Optionnel : DELETE /api/admin/books/{id}. |
| **8. CI/CD et conclusion** | 5:45–6:30 | Dépôt GitHub, `.github/workflows/deploy.yml`. Conclusion : fonctionnalités et points DevOps. |

---

## JSON à copier-coller pendant la démo

### Login (POST /api/auth/login)

```json
{
  "email": "admin@bookshop.com",
  "password": "admin123"
}
```

### Ajouter au panier (POST /api/cart/items)

```json
{
  "bookId": 1,
  "quantity": 2
}
```

### Créer un livre (POST /api/admin/books)

```json
{
  "title": "Nouveau",
  "author": "Auteur",
  "price": 19.99,
  "stock": 5,
  "categoryId": 1
}
```

### Modifier quantité (PUT /api/cart/items/{itemId})

```json
{
  "quantity": 3
}
```

---

## Conseils

- Vérifier qu’aucun processus n’utilise le port 8080 avant de lancer l’app.
- Parler brièvement à chaque étape (« Je me connecte en admin », « J’ajoute un livre au panier », etc.).
- Dans Swagger Authorize : coller le token **sans** les guillemets de la réponse JSON.
