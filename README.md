# Biblioteca Sicura

Il progetto Biblioteca Sicura propone una soluzione per gestire i seguenti usecase:

- login
- visualizza libri (utente)
- carica/cancella libri (admin)

Uno dei requisiti prevede che il progetto sia conforme ai principi di **Secure SDLC (Software Development Life Cycle)**, considerando i vari aspetti della sicurezza durante tutte le fasi dello sviluppo.

## Creazione delle Tabelle

Il primo punto debole per la sicurezza è l'accesso al database su cui sono salvate le credenziali dell'utente.


Per creare il database si utilizzano script e si crea il file che contiene le DDL, con i comandi SQL provide instructions for executing it, follow these steps:

### 1. Create the SQL File

Client MySql:
http://www.mysql.com/downloads/mysql/

Create a file named `setup_login_system.sql` and add the following content:

```sql
-- Create the database
CREATE DATABASE IF NOT EXISTS login_system;
USE login_system;

```

### 2. Start the application

> Eseguire il seguente comando Maven per avviare il progetto Spring.

```bash
mvn spring-boot:run               
```

> Eseguire questo script SQL per inserire i dati nella table Users.
```sql
-- Run this procedure to test /auth/login endpoint
INSERT INTO `users` VALUES
(1,'ramesh@gmail.com','ramesh','$2a$10$5PiyN0MsG0y886d8xWXtwuLXK0Y7zZwcN5xm82b4oDSVr7yF0O6em','ramesh'),
(2,'admin@gmail.com','admin','$2a$10$gqHrslMttQWSsDSVRTK1OehkkBiXsJ/a4z2OURU./dizwOQu5Lovu','admin');

INSERT INTO `roles` VALUES (1,'ROLE_ADMIN'),(2,'ROLE_USER');

INSERT INTO `users_roles` VALUES (2,1),(1,2);
```

> Eseguire la seguente richiesta di tipo POST. Url:
```bash
http://localhost:8080/api/auth/login
```

> Body > raw (json)
```json
{
    "usernameOrEmail": "admin@gmail.com",
    "password": "admin"
}
```



## Ricerca e Sviluppo di Nuove Funzionalità

Ecco alcuni linee guida e punti che si possono aggiungere o migliorare in un progetto per garantire standard di sicurezza più elevati.

### 1. **Requisiti di Esempio per la Sicurezza del Codice**

Considerare i problemi di sicurezza. Dalla call del 14/09 emergono alcuni requisiti.

- GDPR : i dati devono poter essere cancellati.
- CRITTOGRAFIA : tecniche di crittografia per proteggere la privacy e nascondere dati sensibili.
- LIVELLO DI LOGGING : aspetto delle singole funzionalità. (INFO, ERROR) Non loggare il nome del singolo componente.

Inoltre, sono necessarie linee guida di sicurezza un po' più specifiche, affinchè siano rispettati i requisiti durante lo sviluppo. 
Studio solido su quali problemi di sicurezza possono esserci e come risolvere. 

#### a. **Gestione degli Errori e Logging**

Assicurati che le informazioni sensibili non vengano esposte nei log o nei messaggi di errore.

- **Configura i log** per non includere informazioni sensibili come stack trace dettagliati in ambienti di produzione.
- Usa strumenti come **Spring Boot Actuator** per monitorare e gestire lo stato dell'applicazione.

```properties
# src/main/resources/application.properties
logging.level.org.springframework.security=ERROR
```

#### b. **Protezione dalle Iniezioni**

- **SQL Injection**: Usa **Spring Data JPA** per evitare SQL injection attraverso l'uso di query parametrizzate.
- **Command Injection**: Evita di eseguire comandi di sistema o operazioni non sicure.

#### c. **Validazione e Sanitizzazione dei Dati**

- **Input Validation**: Validare e sanitizzare tutti i dati di input per prevenire attacchi come Cross-Site Scripting (XSS) e Injections.

```java
// Esempio di validazione usando Bean Validation
import javax.validation.constraints.NotEmpty;

public class Book {
    @NotEmpty(message = "Title cannot be empty")
    private String title;

    @NotEmpty(message = "Author cannot be empty")
    private String author;
}
```

### 2. **Autenticazione e Autorizzazione**

#### a. **Autenticazione**

- Usa **Spring Security** per gestire l'autenticazione. Configura correttamente i meccanismi di autenticazione (es. form login, OAuth2, JWT).
- **Password Hashing**: Assicurati che le password siano salvate in modo sicuro usando algoritmi di hashing sicuri come BCrypt.

```java
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

#### b. **Autorizzazione**

- Implementa un sistema di **ruoli e permessi** per garantire che solo gli utenti autorizzati possano accedere a determinate risorse.
- Verifica i permessi in base ai ruoli utente (es. admin può aggiungere libri, utenti normali solo visualizzare).

```java
@Override
protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
            .antMatchers("/admin/**").hasRole("ADMIN")
            .antMatchers("/user/**").hasRole("USER")
            .anyRequest().authenticated()
        .and()
        .formLogin()
            .loginPage("/login")
            .permitAll()
        .and()
        .logout()
            .permitAll();
}
```

### 3. **Sicurezza delle Comunicazioni**

- **HTTPS**: Configura il tuo server per utilizzare HTTPS per cifrare il traffico tra il client e il server.
- **Certificati SSL**: Usa certificati SSL/TLS validi e aggiornati.

### 4. **Gestione delle Dipendenze**

- **Aggiorna le Dipendenze**: Usa strumenti come [OWASP Dependency-Check](https://owasp.org/www-project-dependency-check/) per identificare vulnerabilità note nelle librerie di terze parti.
- **Verifica le Versioni**: Assicurati che le librerie e i framework usati siano aggiornati e privi di vulnerabilità conosciute.

### 5. **Protezione Contro Attacchi Comuni**

- **CSRF (Cross-Site Request Forgery)**: Configura la protezione CSRF se utilizzi sessioni e cookie per la gestione dell’autenticazione.

```java
http
    .csrf()
    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
```

- **XSS (Cross-Site Scripting)**: Escapa o sanitizza i dati prima di visualizzarli nel frontend.

### 6. **Backup e Recupero**

- **Backup dei Dati**: Implementa una strategia di backup regolare per i dati del database.
- **Piano di Recupero**: Crea e testa un piano di recupero in caso di disastri.

### 7. **Test di Sicurezza**

- **Penetration Testing**: Esegui test di penetrazione per identificare vulnerabilità.
- **Static Code Analysis**: Usa strumenti di analisi statica per esaminare il codice sorgente alla ricerca di vulnerabilità di sicurezza.

### 8. **Documentazione e Formazione**

- **Documentazione**: Documenta le pratiche di sicurezza e le configurazioni.
- **Formazione**: Forma il team di sviluppo su pratiche di sicurezza e consapevolezza delle minacce.

### 9. **Compliance e Normative**

- **Compliance**: Verifica che il progetto rispetti le normative e le leggi applicabili (es. GDPR, CCPA).

Implementando queste pratiche, potrai assicurarti che il tuo progetto rispetti i principi del Secure SDLC e offra un livello di sicurezza adeguato. Se hai bisogno di ulteriori dettagli su uno di questi punti, fammelo sapere!

