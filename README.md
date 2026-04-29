<div align="center">

# 💊 MediRemind


<br/>

> 🏥 A **production-style Healthcare REST API** built with Spring Boot that combines **automated email reminders**, **background scheduling**, and **real AI integration** to help patients manage their medicine schedules intelligently.

<br/>

[Features](#-features) • [Architecture](#-architecture) • [Tech Stack](#-tech-stack) • [Getting Started](#-getting-started) • [API Reference](#-api-reference) • [AI Integration](#-ai-integration) • [Project Structure](#-project-structure)

</div>

---

## 🎯 The Problem

> *"I forgot to take my medicine"* — said by millions of patients every day.

Medication non-adherence is one of the **leading causes of preventable disease complications** worldwide. Patients don't just need reminders — they need **intelligent reminders** that adapt to their lifestyle and **health insights** that show them the real impact of missed doses.

MediRemind solves this with a clean, well-architected backend system that handles everything automatically.

---

## ✨ Features

| Feature | Description |
|---|---|
| 👤 **Patient Management** | Register patients, store profiles with meal times |
| 💊 **Medicine Tracking** | Add medicines with dosage, frequency and active period |
| 📋 **Dose Logging** | Mark doses as TAKEN or MISSED with date tracking |
| 📧 **Auto Email Reminders** | HTML emails sent automatically based on meal schedule |
| ⏰ **Smart Scheduling** | Spring Scheduler checks every 60 seconds in background |
| 🤖 **AI Dose Analyzer** | LLaMA 3.3 analyzes missed patterns & gives health tips |
| ⚡ **AI Smart Timing** | AI suggests best medicine times based on meal schedule |
| 🛡️ **Data Validation** | Input validation with meaningful error messages |

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────┐
│                     CLIENT (Postman)                     │
└──────────────────────────┬──────────────────────────────┘
                           │ HTTP Request
┌──────────────────────────▼──────────────────────────────┐
│                   CONTROLLER LAYER                       │
│     PatientController  MedicineController  AIController  │
└──────────────────────────┬──────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────┐
│                    SERVICE LAYER                         │
│   PatientService  MedicineService  DoseLogService        │
│         EmailService      AIService                      │
└──────────┬───────────────┬──────────────────────────────┘
           │               │
┌──────────▼──────┐ ┌──────▼──────────────────────────────┐
│  REPOSITORY     │ │         EXTERNAL SERVICES            │
│  LAYER (JPA)    │ │  Gmail SMTP   │   Groq AI API        │
│                 │ │  (Reminders)  │   (LLaMA 3.3)        │
└──────────┬──────┘ └─────────────────────────────────────┘
           │
┌──────────▼──────────────────────────────────────────────┐
│                     MySQL DATABASE                       │
│         patients  │  medicines  │  dose_logs             │
└─────────────────────────────────────────────────────────┘

          ⏰ ReminderScheduler runs every 60s independently
```

---

## 🗄️ Database Design

```
patients
├── id (PK, AUTO_INCREMENT)
├── name
├── age
├── email (UNIQUE)
├── password
├── breakfast_time
├── lunch_time
└── dinner_time
        │
        │ 1 : Many
        ▼
medicines
├── id (PK, AUTO_INCREMENT)
├── name
├── dosage
├── frequency (ONCE_DAILY / TWICE_DAILY / THRICE_DAILY)
├── start_date
├── end_date
└── patient_id (FK → patients)
        │
        │ 1 : Many
        ▼
dose_logs
├── id (PK, AUTO_INCREMENT)
├── taken_date
├── status (TAKEN / MISSED)
└── medicine_id (FK → medicines)
```

---

## 🛠️ Tech Stack

| Layer | Technology | Purpose |
|---|---|---|
| **Framework** | Spring Boot 3.2 | Core application framework |
| **Language** | Java 17 | Primary language |
| **Database** | MySQL 8.0 | Persistent data storage |
| **ORM** | Spring Data JPA + Hibernate | Database abstraction |
| **Email** | Spring Mail + Gmail SMTP | Automated HTML reminders |
| **Scheduler** | Spring `@Scheduled` | Background reminder jobs |
| **AI** | Groq API (LLaMA 3.3-70b) | Intelligent health analysis |
| **HTTP Client** | RestTemplate | External API communication |
| **JSON** | Jackson ObjectMapper | Request/response parsing |
| **Validation** | Spring Validation | Input validation |
| **Boilerplate** | Lombok | Cleaner code |
| **Build** | Maven | Dependency management |
| **Testing** | Postman | API testing |

---

## 🚀 Getting Started

### Prerequisites
- Java 17+
- MySQL 8.0+
- Maven 3.6+
- Gmail account with App Password
- Groq API Key (free at [console.groq.com](https://console.groq.com))

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/mediremind.git
cd mediremind
```

### 2. Create MySQL Database
```sql
CREATE DATABASE mediremind_db;
```

### 3. Configure application.properties
```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/mediremind_db
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# Mail
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=YOUR_GMAIL@gmail.com
spring.mail.password=YOUR_APP_PASSWORD
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# Groq AI
ai.groq.api-key=YOUR_GROQ_API_KEY
ai.groq.api-url=https://api.groq.com/openai/v1/chat/completions
ai.groq.model=llama-3.3-70b-versatile
```

### 4. Run the Application
```bash
mvn spring-boot:run
```

App starts at `http://localhost:8080` ✅

---

## 📡 API Reference

### 👤 Patient APIs

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/patients/register` | Register a new patient |
| `GET` | `/api/patients/{id}` | Get patient by ID |
| `PUT` | `/api/patients/{id}` | Update meal times |
| `DELETE` | `/api/patients/{id}` | Delete patient |

**Register Patient — Sample Request:**
```json
POST /api/patients/register
{
  "name": "John",
  "age": 28,
  "email": "john@gmail.com",
  "password": "pass123",
  "breakfastTime": "08:00:00",
  "lunchTime": "13:00:00",
  "dinnerTime": "20:00:00"
}
```

---

### 💊 Medicine APIs

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/medicines` | Add a new medicine |
| `GET` | `/api/medicines/patient/{id}` | Get all medicines for a patient |
| `DELETE` | `/api/medicines/{id}` | Remove a medicine |

**Add Medicine — Sample Request:**
```json
POST /api/medicines
{
  "name": "Metformin",
  "dosage": "500mg",
  "frequency": "TWICE_DAILY",
  "startDate": "2026-04-01",
  "endDate": "2026-06-30",
  "patientId": 1
}
```

---

### 📋 Dose Log APIs

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/doselogs/mark` | Mark a dose as TAKEN or MISSED |
| `GET` | `/api/doselogs/patient/{id}` | Get full dose history |

**Mark Dose — Sample Request:**
```json
POST /api/doselogs/mark
{
  "medicineId": 1,
  "takenDate": "2026-04-29",
  "status": "MISSED"
}
```

---

### 🤖 AI APIs

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/ai/analyze/{patientId}` | AI missed dose pattern analysis |
| `GET` | `/api/ai/smart-timing/{patientId}` | AI smart reminder timing suggestions |

---

## 🤖 AI Integration

MediRemind uses **Groq's LLaMA 3.3-70b** model for two intelligent features:

### Feature 1 — Missed Dose Pattern Analyzer
```
How it works:
1. Fetches all MISSED dose logs for the patient from MySQL
2. Groups missed doses by medicine name using Java Streams
3. Builds a structured clinical prompt with patient age + history
4. Calls Groq API via RestTemplate (HTTP POST)
5. Parses JSON response using Jackson ObjectMapper
6. Returns personalized health tips + pattern analysis
```

**Sample AI Response:**
```json
{
  "patientName": "John",
  "totalMissedDoses": 4,
  "analysis": "John, I noticed you've missed Metformin on April 20, 22, 24, 26
               — an alternate-day pattern suggesting you may be skipping your
               evening dose. Metformin is critical for blood sugar regulation.
               Try keeping it next to your dinner plate as a visual cue.
               You're taking it consistently in the mornings — great job! 💪",
  "generatedAt": "2026-04-29 10:30:00"
}
```

### Feature 2 — Smart Reminder Timing
```
How it works:
1. Fetches patient's meal times and active medicines
2. Builds a prompt combining meal schedule + medicine frequencies
3. AI applies medical timing guidelines (e.g. with food, every 8 hrs)
4. Returns specific suggested times for each medicine
```

---

## 📁 Project Structure

```
src/main/java/com/mediremind/
│
├── controller/
│   ├── PatientController.java
│   ├── MedicineController.java
│   ├── DoseLogController.java
│   └── AIController.java
│
├── service/
│   ├── PatientService.java
│   ├── MedicineService.java
│   ├── DoseLogService.java
│   ├── EmailService.java
│   └── AIService.java
│
├── repository/
│   ├── PatientRepository.java
│   ├── MedicineRepository.java
│   └── DoseLogRepository.java
│
├── model/
│   ├── Patient.java
│   ├── Medicine.java
│   ├── DoseLog.java
│   ├── Frequency.java       (enum)
│   └── DoseStatus.java      (enum)
│
├── dto/
│   ├── PatientRequestDTO.java
│   ├── PatientResponseDTO.java
│   ├── MedicineRequestDTO.java
│   ├── MedicineResponseDTO.java
│   ├── DoseLogRequestDTO.java
│   ├── DoseLogResponseDTO.java
│   └── AIAnalysisResponseDTO.java
│
├── scheduler/
│   └── ReminderScheduler.java
│
├── config/
│   └── GroqConfig.java
│
└── MediremindApplication.java
```

---

## 💡 Key Design Decisions

**Why 3-layer architecture?**
Each layer has a single responsibility. Controllers handle HTTP, Services handle logic, Repositories handle data. This makes the code testable, maintainable and scalable.

**Why DTOs instead of exposing Entities directly?**
Entities contain sensitive fields (password) and JPA relationships that cause infinite loops in JSON serialization. DTOs give us full control over what goes in and out.

**Why Groq instead of OpenAI?**
Groq provides a completely free tier with the LLaMA 3.3-70b model which is powerful enough for health analysis. No credit card required — perfect for open source projects.

**Why 15 minutes after meal time for reminders?**
Most medicines are prescribed to be taken with or after food. 15 minutes gives the patient time to finish eating before the reminder fires — a real medical guideline baked into the logic.

---

## 🔮 Future Enhancements

- [ ] JWT Authentication & Authorization
- [ ] Weekly PDF health report generation
- [ ] Swagger UI API documentation
- [ ] Docker containerization
- [ ] Medicine refill alerts (when end date approaches)
- [ ] Frontend dashboard (React)

---

## 🧑‍💻 Author

**Your Name**
- LinkedIn: [linkedin.com/in/kavinayasri-m](https://linkedin.com)
- GitHub: [github.com/Kavi-654](https://github.com)
- Email: your@email.com

---

## 📄 License

This project is licensed under the MIT License — feel free to use it, learn from it, and build on top of it.

---

<div align="center">

⭐ **If this project helped you or inspired you, please give it a star!** ⭐

*Built with ❤️ to solve a real healthcare problem*

</div>
