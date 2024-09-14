Per testare l'applicazione Spring Boot con JWT e configurazione di sicurezza aggiornata, segui questi passaggi:

### 1. Avvio dell'applicazione

1. Assicurati di avere tutte le dipendenze necessarie nel tuo progetto, specialmente per Spring Boot, Spring Security, JWT, e H2 Database.
2. Se hai un file `application.properties` simile a quello indicato, puoi avviare l'applicazione tramite il comando:

   ```bash
   mvn spring-boot:run
   ```

Assicurati che l'applicazione parta correttamente e che sia in ascolto sulla porta 8080 (o una porta diversa, se specificata).

### 2. Registrazione di un nuovo utente

- **Endpoint**: `POST /auth/register`
- **Body** (JSON):
```json
{
    "username": "user1",
    "password": "password",
    "role": "USER"
}
```

- **Test con Postman o cURL**:
   Esegui la richiesta in Postman o cURL:
   ```bash
   curl -X POST http://localhost:8080/auth/register -H "Content-Type: application/json" -d '{"username": "user1", "password": "password", "role": "USER"}'
   ```

- **Risultato atteso**: Risposta 200 con la conferma che l'utente è stato registrato.

### 3. Login e ottenere il token JWT

- **Endpoint**: `POST /auth/login`
- **Body** (JSON):
```json
{
    "username": "user1",
    "password": "password"
}
```

- **Test con Postman o cURL**:
   Esegui la richiesta in Postman o cURL:
   ```bash
   curl -X POST http://localhost:8080/auth/login -H "Content-Type: application/json" -d '{"username": "user1", "password": "password"}'
   ```

- **Risultato atteso**: Dovresti ricevere un token JWT come risposta.

   Esempio di risposta:
   ```json
   {
       "token": "eyJhbGciOiJIUzI1NiIsInR..."
   }
   ```

### 4. Visualizzazione dei libri (per utenti autenticati)

Dopo aver ottenuto il token JWT, puoi usarlo per autenticarti e accedere all'elenco dei libri.

- **Endpoint**: `GET /books`
- **Header**: Aggiungi l'header `Authorization` con il token JWT:
```
Authorization: Bearer <jwt_token>
```

- **Test con Postman o cURL**:
   Esegui la richiesta in Postman o cURL:
   ```bash
   curl -X GET http://localhost:8080/books -H "Authorization: Bearer <jwt_token>"
   ```

- **Risultato atteso**: Risposta 200 con l'elenco dei libri disponibili (se esistono libri nel database).

### 5. Aggiungere un libro (solo ADMIN)

Solo un utente con il ruolo `ADMIN` può aggiungere nuovi libri. Usa un token JWT di un utente admin.

- **Endpoint**: `POST /books`
- **Body** (JSON):
```json
{
    "title": "Spring Boot in Action",
    "author": "Craig Walls"
}
```
- **Header**: Aggiungi l'header `Authorization` con il token JWT di un utente admin:
```
Authorization: Bearer <admin_jwt_token>
```

- **Test con Postman o cURL**:
   Esegui la richiesta in Postman o cURL:
   ```bash
   curl -X POST http://localhost:8080/books -H "Authorization: Bearer <admin_jwt_token>" -H "Content-Type: application/json" -d '{"title": "Spring Boot in Action", "author": "Craig Walls"}'
   ```

- **Risultato atteso**: Risposta 200 con conferma che il libro è stato aggiunto.

### 6. Ricerca libri

Puoi cercare libri utilizzando una query, ad esempio per titolo.

- **Endpoint**: `GET /ricerca?parola=Spring`
- **Header**: Aggiungi l'header `Authorization` con il token JWT:
```
Authorization: Bearer <jwt_token>
```

- **Test con Postman o cURL**:
   ```bash
   curl -X GET http://localhost:8080/ricerca?parola=Spring -H "Authorization: Bearer <jwt_token>"
   ```

- **Risultato atteso**: Risposta 200 con l'elenco dei libri che contengono la parola "Spring".

### 7. Aggiornare un libro (solo ADMIN)

Solo un admin può aggiornare un libro.

- **Endpoint**: `PUT /ricerca/aggiorna/{isbn}`
- **Body** (JSON):
```json
{
    "title": "Updated Book Title",
    "author": "Updated Author"
}
```

- **Header**: Aggiungi l'header `Authorization` con il token JWT di un admin:
```
Authorization: Bearer <admin_jwt_token>
```

- **Test con Postman o cURL**:
   Esegui la richiesta:
   ```bash
   curl -X PUT http://localhost:8080/ricerca/aggiorna/{isbn} -H "Authorization: Bearer <admin_jwt_token>" -H "Content-Type: application/json" -d '{"title": "Updated Book Title", "author": "Updated Author"}'
   ```

- **Risultato atteso**: Risposta 200 con conferma che il libro è stato aggiornato.

### 8. Cancellare un libro (solo ADMIN)

Solo un admin può cancellare un libro.

- **Endpoint**: `DELETE /ricerca/cancella/{isbn}`
- **Header**: Aggiungi l'header `Authorization` con il token JWT di un admin:
```
Authorization: Bearer <admin_jwt_token>
```

- **Test con Postman o cURL**:
   ```bash
   curl -X DELETE http://localhost:8080/ricerca/cancella/{isbn} -H "Authorization: Bearer <admin_jwt_token>"
   ```

- **Risultato atteso**: Risposta 200 con conferma che il libro è stato cancellato.

### 9. Accedere alla console H2

Puoi verificare i dati nel database H2 accedendo alla console H2 su:
```
http://localhost:8080/h2-console
```

- **JDBC URL**: `jdbc:h2:mem:testdb`
- **User**: `sa`
- **Password**: `password`

---

Seguendo questi passaggi, dovresti essere in grado di testare completamente l'applicazione e verificare che JWT e i ruoli utente funzionino come previsto. Se qualcosa non funziona come dovrebbe, fammi sapere e possiamo esaminare eventuali errori o problemi di configurazione.