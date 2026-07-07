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
