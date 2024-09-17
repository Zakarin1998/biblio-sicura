Certamente! Ti guiderò attraverso la creazione di un progetto di biblioteca usando Spring Boot. Questo progetto includerà:

1. **Creazione del database per utenti e libri**
2. **Gestione degli utenti e autenticazione**
3. **Gestione dei libri, con possibilità di aggiungere libri solo se l'utente è un admin**

### 1. Configurazione del Progetto

#### a. Creazione del Progetto

1. Vai su [Spring Initializr](https://start.spring.io/).
2. Seleziona le seguenti opzioni:
   - **Project:** Maven Project
   - **Language:** Java
   - **Spring Boot Version:** (scegli l'ultima versione stabile)
   - **Project Metadata:**
     - **Group:** com.example
     - **Artifact:** biblioteca
     - **Name:** biblioteca
     - **Description:** Biblioteca management system
     - **Package name:** com.example.biblioteca
     - **Packaging:** Jar
     - **Java:** 11 o superiore
   - **Dependencies:**
     - Spring Web
     - Spring Data JPA
     - H2 Database (per il database in memoria)
     - Spring Security
     - Lombok (opzionale, ma utile per ridurre il boilerplate code)

3. Clicca su "Generate" per scaricare il progetto e poi estrai il file zip.

#### b. Importazione in IDE

1. Importa il progetto nel tuo IDE (Eclipse, IntelliJ IDEA, etc.) come progetto Maven.

### 2. Configurazione del Database

#### a. Creazione delle Entità

Crea due entità, `User` e `Book`, con le rispettive proprietà.

#### b. Creazione dei Repository

Crea i repository per le entità `User` e `Book`.
 username);

```java
public interface BookRepository extends JpaRepository<Book, Long> {}
```


Per testare l'applicazione Spring Boot che abbiamo creato, segui questi passaggi. Ti guiderò attraverso tutto il processo, dall'esecuzione del progetto alla configurazione di strumenti utili per il test.

### Prerequisiti:
1. **JDK 11 o superiore** installato.
2. **Maven** per gestire le dipendenze.
3. **Un IDE** come IntelliJ IDEA o Eclipse, oppure puoi usare un editor di testo con terminale integrato.
4. **Postman** o **cURL** per testare le API REST.

### 1. Struttura del Progetto
La struttura del progetto dovrebbe essere simile a questa:

```
src/
 └── main/
     └── java/
         └── com/
             └── example/
                 └── library/
                     ├── controller/
                     │   ├── AuthController.java
                     │   ├── BookController.java
                     │   └── SearchController.java
                     ├── model/
                     │   ├── Book.java
                     │   └── User.java
                     ├── repository/
                     │   ├── BookRepository.java
                     │   └── UserRepository.java
                     ├── security/
                     │   ├── JwtAuthenticationFilter.java
                     │   ├── JwtTokenUtil.java
                     │   └── SecurityConfig.java
                     ├── LibraryApplication.java
                     └── configuration/
                         └── application.properties
```

### 2. Dipendenze Maven (pom.xml)
Nel tuo file `pom.xml`, assicurati di avere le dipendenze per Spring Boot, Spring Security, JWT e H2 database. Ecco una configurazione di base:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>library</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <packaging>jar</packaging>

    <name>library</name>
    <description>Library Application</description>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.0.0</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>

    <properties>
        <java.version>11</java.version>
    </properties>

    <dependencies>
        <!-- Spring Boot Starter Web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- Spring Boot Starter Security -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>

        <!-- JWT -->
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-api</artifactId>
            <version>0.11.2</version>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-impl</artifactId>
            <version>0.11.2</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-jackson</artifactId>
            <version>0.11.2</version>
            <scope>runtime</scope>
        </dependency>

        <!-- Spring Boot Starter JPA (for database) -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>

        <!-- H2 Database (in-memory) -->
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>runtime</scope>
        </dependency>

        <!-- Lombok (optional, for reducing boilerplate code) -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <scope>provided</scope>
        </dependency>

        <!-- Spring Boot Test -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

### 3. Configurazione (`application.properties`)

Imposta il file `application.properties` per configurare H2 Database, JWT, e altre proprietà:

```properties
# H2 database configuration
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true

# JWT Configuration
jwt.secret=your-secret-key
jwt.expiration=10800000 # 3 hours in milliseconds

# Spring Security
spring.security.user.name=admin
spring.security.user.password=adminpass
spring.security.user.roles=ADMIN

# Server Port
server.port=8080
```

### 4. Avvio dell'applicazione

Per avviare l'applicazione, apri il terminale e digita:

```bash
mvn spring-boot:run
```

Questo avvierà l'applicazione Spring Boot.

### 5. Testare gli endpoint con Postman o cURL

#### Registrazione di un nuovo utente:

Esegui una richiesta POST a:
```
POST http://localhost:8080/auth/register
```
Body (JSON):
```json
{
    "username": "user1",
    "password": "password",
    "role": "USER"
}
```

#### Login e ottenere il token JWT:

Esegui una richiesta POST a:
```
POST http://localhost:8080/auth/login
```
Body (Form-Data):
```
username: user1
password: password
```

In risposta, otterrai un token JWT. Questo token sarà necessario per accedere agli endpoint protetti.

#### Visualizzazione dei libri (per utenti autenticati):

Dopo aver ottenuto il token JWT, puoi accedere ai libri inviando il token nel campo `Authorization` come `Bearer`.

Esegui una richiesta GET a:
```
GET http://localhost:8080/books
```

Nell'intestazione della richiesta (Headers), aggiungi:
```
Authorization: Bearer <jwt_token>
```

#### Aggiunta di un libro (solo admin):

Esegui una richiesta POST a:
```
POST http://localhost:8080/books
```
Body (JSON):
```json
{
    "title": "Spring Boot in Action",
    "author": "Craig Walls"
}
```
Nell'intestazione della richiesta (Headers), aggiungi il token JWT di un utente admin:
```
Authorization: Bearer <admin_jwt_token>
```

#### Ricerca di libri (per utenti autenticati):

Esegui una richiesta GET a:
```
GET http://localhost:8080/ricerca?parola=Spring
```

#### Aggiornare un libro (solo admin):

Esegui una richiesta PUT a:
```
PUT http://localhost:8080/ricerca/aggiorna/{isbn}
```
Body (JSON):
```json
{
    "title": "Updated Book Title",
    "author": "Updated Author"
}
```

#### Cancellare un libro (solo admin):

Esegui una richiesta DELETE a:
```
DELETE http://localhost:8080/ricerca/cancella/{isbn}
```

### 6. Accedere alla console H2

Puoi accedere alla console H2 per verificare i dati archiviati in memoria durante il test.

Vai su:
```
http://localhost:8080/h2-console
```

- **JDBC URL**: `jdbc:h2:mem:testdb`
- **User**: `sa`
- **Password**: `password`

### Conclusione

Ora puoi eseguire e testare tutti gli endpoint che abbiamo creato. Assicurati di avere un token JWT valido per accedere alle operazioni che richiedono autenticazione.

To implement this scenario, where a `Book` is a specific type of `Product` with additional attributes (like `isbn`, `titolo`, `autore`, etc.), you can use inheritance in Java with JPA. Specifically, you can create a `Book` class that extends the `Product` class.

### Step-by-Step Implementation

1. **Extend the `Product` Class**: The `Book` class will inherit the common attributes from `Product` (like `name`, `sku`, `price`, and `categories`), and you'll add book-specific attributes such as `isbn`, `titolo`, `autore`, etc.
2. **Inheritance with JPA**: Use `@Inheritance(strategy = InheritanceType.JOINED)` or `@Inheritance(strategy = InheritanceType.SINGLE_TABLE)` to map the `Product` and `Book` entities in the database.

Here’s how you can model this:

### `Product` Class

```java
package com.example.biblioteca_sicura.model;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
@Inheritance(strategy = InheritanceType.JOINED) // Using JOINED inheritance strategy
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String sku; // Stock Keeping Unit

    @Column(nullable = false)
    private Double price;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "products_categories",
        joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id")
    )
    private Set<Category> categories;
}
```

### `Book` Class

Now we extend the `Product` class to create a `Book` class with additional attributes like `isbn`, `titolo`, `autore`, etc.

```java
package com.example.biblioteca_sicura.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Book extends Product {

    @Column(nullable = false, unique = true)
    private Long isbn;

    @Column(nullable = false)
    private String titolo;

    @Column(nullable = false)
    private String autore;

    @Column(nullable = false)
    private String editore;

    @Column
    private String img; // URL or path to image
}
```

### Explanation of the Changes:
- **Inheritance**: The `Book` class extends `Product` and inherits all the basic fields (`name`, `sku`, `price`, etc.) while adding book-specific fields such as `isbn`, `titolo`, `autore`, `editore`, and `img`.
  
- **JPA Inheritance Strategy**:
  - The `@Inheritance(strategy = InheritanceType.JOINED)` annotation in the `Product` class is used to map the inheritance hierarchy to separate tables (`products` and `books`). 
    - A `JOINED` strategy means that there will be a `products` table containing the common attributes and a `books` table containing the additional attributes (and a foreign key reference to `products`).
  - Alternatively, you can use `@Inheritance(strategy = InheritanceType.SINGLE_TABLE)` if you want all product types (including books) stored in a single table with nullable fields for specific types.

---

### Database Structure

With this setup:
- There will be a `products` table that holds the common fields (`id`, `name`, `sku`, `price`).
- There will also be a `books` table with fields (`id`, `isbn`, `titolo`, `autore`, `editore`, `img`), and the `id` in the `books` table will reference the `id` in the `products` table.

### Example SQL Output (With `JOINED` Inheritance)

- **products** table:
  ```sql
  +----+-------+--------+-------+
  | id | name  | sku    | price |
  +----+-------+--------+-------+
  | 1  | Book1 | SKU001 | 19.99 |
  +----+-------+--------+-------+
  ```

- **books** table:
  ```sql
  +----+------------+----------+--------+----------+------------+
  | id | isbn       | titolo   | autore | editore  | img        |
  +----+------------+----------+--------+----------+------------+
  | 1  | 1234567890 | My Book  | Author | Publisher | img_url    |
  +----+------------+----------+--------+----------+------------+
  ```

Would you like more details on how to configure the database or additional functionality for this setup?