# 🏆 FantAsta — Auction Service

> Microservizio backend per la gestione delle aste di fantacalcio, parte dell'architettura distribuita di **FantAsta**.

![Java](https://img.shields.io/badge/Java-17-orange?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.5-brightgreen?logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Supabase-blue?logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-ready-2496ED?logo=docker)
![License](https://img.shields.io/badge/license-All%20Rights%20Reserved-red)

---

## 📌 Descrizione

**Auction Service** è il microservizio responsabile della creazione, gestione e conduzione delle aste all'interno della piattaforma FantAsta. Gestisce il ciclo di vita completo di un'asta (dalla creazione all'assegnazione dei giocatori), la partecipazione degli utenti e la composizione dei team di fantacalcio.

Comunica con l'**Auth Service** esterno per la validazione dei token JWT, garantendo che ogni operazione sia eseguita da utenti autenticati e autorizzati.

---

## 🏗️ Architettura

Il servizio fa parte di un'architettura a **microservizi**. La comunicazione con gli altri servizi avviene tramite chiamate HTTP REST.


---

## 🚀 Tecnologie

| Tecnologia | Versione | Utilizzo |
|---|---|---|
| Java | 17 | Linguaggio principale |
| Spring Boot | 3.4.5 | Framework applicativo |
| Spring Security | — | Configurazione sicurezza HTTP |
| Spring Data JPA | — | Accesso al database ORM |
| PostgreSQL | — | Database relazionale (via Supabase) |
| Lombok | — | Riduzione boilerplate |
| Docker | — | Containerizzazione e deploy |
| Maven | 3.9.8 | Build e gestione dipendenze |

---

## 📁 Struttura del Progetto

'''
src/main/java/com/example/fantasta/auction_service/
├── client/ # Client HTTP per comunicazione con Auth Service
├── config/ # Configurazioni Spring (CORS, beans, ecc.)
├── controller/ # REST Controllers (endpoint API)
├── dto/ # Data Transfer Objects (request/response)
├── entity/ # Entità JPA (Auction, FantasyTeam, PlayerAssignment, ...)
├── enumeration/ # Enum di dominio (es. AuctionStatus)
├── exception/ # Eccezioni personalizzate
├── repository/ # Repository JPA
├── security/ # Configurazione Spring Security
└── service/ # Business logic
'''

---

## 📦 Entità Principali

- **`Auction`** — Rappresenta un'asta con nome, stato, configurazione slot giocatori e crediti iniziali
- **`AuctionParticipant`** — Associa un utente a un'asta come partecipante
- **`FantasyTeam`** — Il team di fantacalcio di un partecipante all'interno di un'asta
- **`PlayerAssignment`** — Registra l'assegnazione di un giocatore a un team con il prezzo pagato

---

## 🔗 API Endpoints

### Aste — `/api/auctions`

| Metodo | Endpoint | Descrizione | Autenticazione |
|--------|----------|-------------|----------------|
| `POST` | `/api/auctions/create` | Crea una nuova asta | ✅ JWT |
| `GET` | `/api/auctions/{auctionId}` | Recupera un'asta per ID | ✅ JWT |
| `PUT` | `/api/auctions/{auctionId}` | Aggiorna i dettagli di un'asta | ✅ JWT (owner) |
| `PATCH` | `/api/auctions/{auctionId}/status` | Aggiorna lo stato dell'asta | ✅ JWT |
| `DELETE` | `/api/auctions/{auctionId}` | Elimina un'asta | ✅ JWT (owner) |

### Partecipanti — `/api/participants`

| Metodo | Endpoint | Descrizione | Autenticazione |
|--------|----------|-------------|----------------|
| `POST` | `/api/participants/{auctionId}/join` | Partecipa a un'asta aperta | ✅ JWT |
| `GET` | `/api/participants/{auctionId}/participants` | Lista partecipanti (solo owner) | ✅ JWT (owner) |

### Fantasy Team — `/api/fantasy-teams`

| Metodo | Endpoint | Descrizione | Autenticazione |
|--------|----------|-------------|----------------|
| `GET` | `/api/fantasy-teams/...` | Operazioni sui team | ✅ JWT |

### Assegnazione Giocatori — `/api/player-assignments`

| Metodo | Endpoint | Descrizione | Autenticazione |
|--------|----------|-------------|----------------|
| `POST/GET` | `/api/player-assignments/...` | Gestione assegnazioni giocatori | ✅ JWT |

> Tutte le richieste richiedono l'header `Authorization: Bearer <token>`.

---

## 🔐 Sicurezza

Ogni endpoint è protetto tramite **JWT Bearer Token**. Il token viene validato delegando la chiamata all'**Auth Service** esterno tramite `AuthServiceClient`. In caso di token non valido, scaduto o mancante, il servizio risponde con `401 Unauthorized`.

I permessi sono strutturati su due livelli:
- **Utente autenticato** — può accedere e partecipare alle aste
- **Owner dell'asta** — può modificare, eliminare e visualizzare i partecipanti

---

## ⚙️ Configurazione

Crea un file `src/main/resources/application.properties` (o `application.yml`) con le seguenti variabili:

```properties
# Server
server.port=7071

# Database
spring.datasource.url=jdbc:postgresql://<host>:<port>/<database>
spring.datasource.username=<username>
spring.datasource.password=<password>
spring.jpa.hibernate.ddl-auto=update

# Auth Service
auth.service.url=<url-auth-service>
```

> ⚠️ Non committare mai credenziali reali nel repository. Usa variabili d'ambiente in produzione.

---

## 🐳 Docker

Il servizio include un `Dockerfile` multi-stage ottimizzato.

**Build e avvio:**

```bash
# Build dell'immagine
docker build -t fantasta-auction-service .

# Avvio del container
docker run -p 7071:7071 \
  -e SPRING_DATASOURCE_URL=<db-url> \
  -e SPRING_DATASOURCE_USERNAME=<user> \
  -e SPRING_DATASOURCE_PASSWORD=<password> \
  fantasta-auction-service
```

Il servizio si espone sulla porta **`7071`**.

---

## 🛠️ Avvio Locale

**Prerequisiti:** Java 17+, Maven 3.9+, PostgreSQL

```bash
# Clona il repository
git clone https://github.com/francesc05Cervera/BackEnd-FantAsta-AuctionService.git
cd BackEnd-FantAsta-AuctionService

# Compila ed esegui
./mvnw spring-boot:run
```

Oppure tramite Maven Wrapper su Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

---

## 👤 Autore

**Francesco Cervera**
© Tutti i diritti riservati
