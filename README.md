# 🤖 AI Interview Practice Platform

> **An AI-powered technical interview practice platform built with Spring Boot, MySQL, Thymeleaf, Spring Security, and Google Gemini AI.**

The **AI Interview Practice Platform** is a web-based application designed to help students and job seekers prepare for technical and HR interviews through interactive mock interview sessions.

Users can select an interview domain, answer randomly selected questions, submit their responses, and receive a performance evaluation. When the Gemini AI API is configured, answers are evaluated using AI based on **technical accuracy, concept coverage, and clarity**.

The platform also includes a complete **Admin Dashboard** for managing users, interview domains, and questions.

---

## 🌐 Project Overview

Preparing for technical interviews can be difficult without a structured environment for practice and feedback.

This project provides a centralized platform where users can:

* Select an interview domain
* Attempt mock interview questions
* Submit answers
* Receive scores
* Get AI-generated feedback
* View strengths and weaknesses
* Receive recommended topics for improvement
* Review previous interview attempts
* Track interview performance

Administrators can manage the platform through a dedicated admin panel.

---

# ✨ Features

## 👨‍🎓 Student Features

### 🔐 Authentication

Users can:

* Register a new account
* Login securely
* Logout
* Access role-based pages
* Manage their profile

Passwords are securely stored using **BCrypt password hashing**.

---

### 🎯 Interview Domain Selection

Users can select from multiple interview domains.

The default domains include:

* ☕ Java
* 🧩 Data Structures & Algorithms
* 🗄️ DBMS
* 💻 Operating Systems
* 🌐 Computer Networks
* 🍃 Spring Boot
* 👔 HR Interview

Each domain contains questions with different difficulty levels.

---

### 📝 Mock Interview

After selecting a domain, the application creates an interview session and selects **10 random questions**.

The user can answer each question and submit the complete interview.

The system records:

* Interview domain
* Questions
* Answers
* Start time
* End time
* Duration
* Score
* Evaluation status

---

# 🧠 AI Answer Evaluation

One of the main features of the project is AI-powered answer evaluation using **Google Gemini**.

The system sends the following information to the AI:

```text
Question
Reference Answer
Student Answer
```

The AI evaluates the response based on:

* Technical accuracy
* Coverage of important concepts
* Clarity of explanation

The evaluation returns:

```text
Score
Feedback
Strengths
Weaknesses
Recommended Topics
```

Example evaluation structure:

```json
{
  "score": 85,
  "feedback": "The answer demonstrates a strong understanding of the concept...",
  "strengths": [
    "Correct explanation of the main concept",
    "Relevant technical terminology"
  ],
  "weaknesses": [
    "Could include more implementation details"
  ],
  "recommendedTopics": [
    "Advanced concepts",
    "Practical examples"
  ]
}
```

---

# 🛡️ AI Fallback Evaluation

The application does **not completely depend on the Gemini API**.

If the Gemini API key is unavailable or the AI request fails, the application automatically falls back to a **keyword-based evaluation system**.

The fallback system:

1. Extracts important keywords from the reference answer.
2. Extracts keywords from the student's answer.
3. Compares matching concepts.
4. Calculates a percentage score.
5. Generates basic feedback.
6. Identifies missing concepts.

This allows the interview platform to continue working even without an active Gemini API connection.

---

# 📊 Interview Results

After submitting an interview, users receive a detailed result page containing:

* Overall score
* Performance label
* Interview duration
* Individual questions
* Student answers
* Evaluation feedback
* Strengths
* Weaknesses
* Recommended topics
* AI evaluation status

Performance levels are calculated from the score.

|    Score | Performance   |
| -------: | ------------- |
| 90 – 100 | Excellent     |
|  75 – 89 | Good          |
|  60 – 74 | Average       |
|  40 – 59 | Below Average |
| Below 40 | Poor          |

---

# 📚 Interview Question Bank

The application is initialized with **84 sample interview questions** across **7 domains**.

### Question Distribution

| Domain            | Questions |
| ----------------- | --------: |
| Java              |        12 |
| DSA               |        12 |
| DBMS              |        12 |
| Operating Systems |        12 |
| Computer Networks |        12 |
| Spring Boot       |        12 |
| HR Interview      |        12 |
| **Total**         |    **84** |

Questions are categorized by difficulty:

* 🟢 Easy
* 🟡 Medium
* 🔴 Hard

---

# 👨‍💼 Admin Dashboard

The platform includes a dedicated administration system.

Administrators can manage the application through:

```text
/admin/dashboard
```

## Admin Features

### 📊 Dashboard

The admin dashboard provides platform statistics and user information.

---

### 📚 Domain Management

Administrators can:

* Add domains
* Edit domains
* Delete domains
* View available domains

Each domain contains:

* Name
* Description
* Icon

---

### ❓ Question Management

Administrators can:

* Add questions
* Edit questions
* Delete questions
* Search questions
* Filter questions by domain
* Filter questions by difficulty
* Add reference answers
* Assign difficulty levels

---

### 👥 User Management

Administrators can:

* View registered users
* View user information
* Delete users

---

# 🔐 Role-Based Access Control

The application implements role-based authentication using **Spring Security**.

Two roles are supported:

```text
STUDENT
ADMIN
```

### Student

Students can:

* Access their dashboard
* Start interviews
* Submit answers
* View results
* View interview history
* Manage their profile

### Admin

Administrators can:

* Access admin dashboard
* Manage users
* Manage domains
* Manage questions
* View platform statistics

---

# 🗃️ Database

The application uses **MySQL** as its relational database.

Database:

```text
ai_interview
```

The database is automatically created when using the configured JDBC URL with:

```text
createDatabaseIfNotExist=true
```

Hibernate/JPA is used for database interaction.

---

# 🧩 Database Entities

The main entities include:

### User

Stores:

* ID
* Name
* Email
* Password
* Role
* College
* Department
* Graduation Year

---

### Domain

Stores interview domain information such as:

* Domain name
* Description
* Icon

---

### Question

Stores:

* Question text
* Reference answer
* Difficulty
* Domain

---

### InterviewSession

Stores:

* User
* Domain
* Score
* Start time
* End time
* Status
* Interview questions

---

### Answer

Stores the user's response and its evaluation.

---

# 🏗️ System Architecture

The application follows a layered Spring Boot architecture.

```text
                    ┌──────────────────────┐
                    │      Web Browser     │
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │ Thymeleaf Templates  │
                    │ HTML / CSS / JS      │
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │    Controllers       │
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │     Services         │
                    └──────────┬───────────┘
                               │
                    ┌──────────┴───────────┐
                    ▼                      ▼
          ┌─────────────────┐    ┌─────────────────┐
          │ Spring Data JPA │    │  Gemini AI API  │
          └────────┬────────┘    └────────┬────────┘
                   │                      │
                   ▼                      ▼
          ┌─────────────────┐    ┌─────────────────┐
          │      MySQL      │    │ AI Evaluation  │
          └─────────────────┘    └─────────────────┘
```

---

# 🛠️ Technology Stack

## Backend

* Java 17
* Spring Boot 3.5.15
* Spring MVC
* Spring Data JPA
* Spring Security
* Spring Validation
* Lombok

## Frontend

* HTML5
* CSS3
* JavaScript
* Thymeleaf
* Thymeleaf Spring Security Extras
* Bootstrap Icons / UI components

## Database

* MySQL
* Hibernate
* JPA

## AI

* Google Gemini API
* Gemini 1.5 Flash

## Build Tool

* Apache Maven
* Maven Wrapper

## Development Tools

* IntelliJ IDEA / Eclipse / VS Code
* MySQL Workbench
* Git
* GitHub

---

# 📁 Project Structure

```text
ai-interview-practice-platform/
│
├── .mvn/
│   └── wrapper/
│
├── src/
│   └── main/
│       │
│       ├── java/
│       │   └── com/
│       │       └── himachalam/
│       │           └── aiinterview/
│       │
│       │               ├── config/
│       │               │   ├── DataInitializer.java
│       │               │   ├── SecurityBeans.java
│       │               │   └── SecurityConfig.java
│       │               │
│       │               ├── controller/
│       │               │   ├── AdminController.java
│       │               │   ├── AuthController.java
│       │               │   ├── DashboardController.java
│       │               │   ├── HistoryController.java
│       │               │   ├── InterviewController.java
│       │               │   └── ProfileController.java
│       │               │
│       │               ├── dto/
│       │               │   ├── AdminStatsDto.java
│       │               │   ├── EvaluationResult.java
│       │               │   ├── InterviewSubmitDto.java
│       │               │   └── RegisterDto.java
│       │               │
│       │               ├── exception/
│       │               │   └── EmailAlreadyExistsException.java
│       │               │
│       │               ├── model/
│       │               │   ├── Answer.java
│       │               │   ├── Domain.java
│       │               │   ├── InterviewSession.java
│       │               │   ├── Question.java
│       │               │   └── User.java
│       │               │
│       │               ├── repository/
│       │               │   ├── AnswerRepository.java
│       │               │   ├── DomainRepository.java
│       │               │   ├── InterviewSessionRepository.java
│       │               │   ├── QuestionRepository.java
│       │               │   └── UserRepository.java
│       │               │
│       │               └── service/
│       │                   ├── AdminService.java
│       │                   ├── AdminServiceImpl.java
│       │                   ├── CustomUserDetailsService.java
│       │                   ├── DomainService.java
│       │                   ├── DomainServiceImpl.java
│       │                   ├── EvaluationService.java
│       │                   ├── InterviewService.java
│       │                   ├── InterviewServiceImpl.java
│       │                   ├── QuestionService.java
│       │                   ├── QuestionServiceImpl.java
│       │                   ├── UserService.java
│       │                   └── UserServiceImpl.java
│       │
│       └── resources/
│           │
│           ├── static/
│           │   ├── css/
│           │   │   └── style.css
│           │   │
│           │   └── js/
│           │       ├── charts.js
│           │       └── main.js
│           │
│           ├── templates/
│           │   ├── admin/
│           │   ├── fragments/
│           │   ├── history/
│           │   ├── interview/
│           │   ├── dashboard.html
│           │   ├── error.html
│           │   ├── login.html
│           │   └── register.html
│           │
│           └── application.properties
│
├── .env.example
├── .gitignore
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

---

# 🔄 Application Workflow

The basic student workflow is:

```text
Register
   ↓
Login
   ↓
Dashboard
   ↓
Select Interview Domain
   ↓
Start Interview
   ↓
10 Random Questions
   ↓
Submit Answers
   ↓
AI Evaluation
   │
   ├── Gemini API Available
   │          ↓
   │     AI Evaluation
   │
   └── Gemini API Unavailable
              ↓
       Keyword Evaluation
              ↓
          Result Page
              ↓
      View Feedback & Score
              ↓
       Interview History
```

---

# ⚙️ Requirements

Before running the project, install:

* Java 17 or later
* MySQL 8+
* Maven
* Git
* A Gemini API key for AI evaluation

Check Java:

```bash
java -version
```

Check Maven:

```bash
mvn -version
```

---

# 🗄️ Database Setup

Start MySQL and create the database:

```sql
CREATE DATABASE ai_interview;
```

The application is configured to use:

```text
Database: ai_interview
Username: root
```

Update the password in:

```text
src/main/resources/application.properties
```

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ai_interview?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

---

# 🤖 Gemini API Configuration

The project supports Google Gemini for AI-powered evaluation.

Create a Gemini API key and configure it as an environment variable.

### Windows PowerShell

```powershell
$env:GEMINI_API_KEY="YOUR_API_KEY"
```

### Windows Command Prompt

```cmd
set GEMINI_API_KEY=YOUR_API_KEY
```

The application reads the value using:

```properties
gemini.api.key=${GEMINI_API_KEY}
```

> ⚠️ **Never commit your real Gemini API key to GitHub.**

The repository includes:

```text
.env.example
```

as a template.

---

# 🚀 Running the Application

## 1. Clone the Repository

```bash
git clone https://github.com/Hacker-Hima/ai-interview-practice-platform.git
```

---

## 2. Navigate to the Project

```bash
cd ai-interview-practice-platform
```

---

## 3. Configure MySQL

Update your MySQL password in:

```text
src/main/resources/application.properties
```

---

## 4. Configure Gemini API

Set:

```text
GEMINI_API_KEY
```

as an environment variable.

---

## 5. Build the Project

Using Maven:

```bash
mvn clean install
```

Or using the Maven Wrapper:

### Windows

```bash
mvnw.cmd clean install
```

### Linux / macOS

```bash
./mvnw clean install
```

---

## 6. Run the Application

```bash
mvn spring-boot:run
```

Or:

```bash
mvnw.cmd spring-boot:run
```

The application will start on:

```text
http://localhost:8080
```

Open the URL in your browser.

---

# 👑 Default Admin Account

The application automatically creates an administrator account when the database is initialized.

```text
Email: admin@aiinterview.com
Password: admin123
Role: ADMIN
```

### Admin Dashboard

```text
http://localhost:8080/admin/dashboard
```

> ⚠️ For production use, change the default admin credentials and do not use these credentials on a public deployment.

---

# 🌱 Automatic Data Initialization

When the application starts, the `DataInitializer` component checks the database.

If required data is missing, it automatically creates:

```text
1 Admin User
7 Interview Domains
84 Interview Questions
```

The initializer is designed to avoid inserting duplicate domains and questions on every restart.

---

# 🔒 Security

The application uses **Spring Security 6** for authentication and authorization.

Security features include:

* BCrypt password hashing
* Login authentication
* Role-based authorization
* Protected admin routes
* Protected student routes
* Custom user details service
* Secure session handling
* Thymeleaf Spring Security integration

---

# 🧪 Testing

The project includes Spring Boot testing dependencies and Spring Security testing support.

Run tests using:

```bash
mvn test
```

Or:

```bash
mvnw.cmd test
```

---

# 📊 Interview Evaluation Logic

The platform has two evaluation modes.

## Mode 1 — Gemini AI

When the Gemini API is configured:

```text
Student Answer
      ↓
Gemini API
      ↓
Technical Evaluation
      ↓
Score + Feedback
      ↓
Strengths
      ↓
Weaknesses
      ↓
Recommended Topics
```

---

## Mode 2 — Keyword Fallback

When Gemini is unavailable:

```text
Reference Answer
       ↓
Keyword Extraction
       ↓
Student Answer
       ↓
Keyword Extraction
       ↓
Keyword Matching
       ↓
Percentage Score
       ↓
Basic Feedback
```

This makes the application resilient when external AI services are unavailable.

---

# 📌 Example Interview Domains

## ☕ Java

Topics include:

* JDK vs JRE vs JVM
* OOP principles
* `==` vs `.equals()`
* Collections
* Multithreading
* ArrayList vs LinkedList
* Java 8 features
* Java Memory Model
* Garbage Collection
* Design Patterns
* Generics
* Reflection

---

## 🧩 DSA

Topics include:

* Stack
* Queue
* BFS
* DFS
* Binary Search
* Bubble Sort
* BST
* Merge Sort
* Dynamic Programming
* Graphs
* Heap
* Quick Sort
* Time Complexity

---

## 🗄️ DBMS

Topics include:

* SQL
* Database normalization
* Transactions
* ACID properties
* Joins
* Indexing
* Transactions and concurrency
* Relational databases

---

## 💻 Operating Systems

Topics include:

* Processes
* Threads
* Scheduling
* Memory management
* Deadlocks
* Synchronization

---

## 🌐 Computer Networks

Topics include:

* OSI Model
* TCP/IP
* HTTP
* DNS
* Network security
* Protocols

---

## 🍃 Spring Boot

Topics include:

* Spring Boot
* Dependency Injection
* Spring Security
* Spring Data JPA
* REST APIs
* MVC architecture

---

## 👔 HR Interview

Topics include:

* Self introduction
* Strengths and weaknesses
* Career goals
* Teamwork
* Communication
* Behavioral questions

---

# 🎯 Project Objectives

The major objectives of this project are:

1. Provide students with an interactive interview practice environment.
2. Automate interview answer evaluation.
3. Provide meaningful feedback on technical answers.
4. Identify strengths and weaknesses.
5. Recommend topics for further preparation.
6. Maintain interview history and performance data.
7. Provide administrators with question and user management.
8. Demonstrate the integration of AI services with a Spring Boot application.

---

# 💡 Advantages

* Interactive interview practice
* AI-powered evaluation
* Automatic scoring
* Personalized feedback
* Multiple interview domains
* Different difficulty levels
* Interview history
* Role-based security
* Admin question management
* MySQL persistence
* AI fallback mechanism
* Responsive web interface

---

# 🔮 Future Enhancements

Possible future improvements include:

* [ ] Voice-based interviews
* [ ] Speech-to-text answers
* [ ] AI-generated interview questions
* [ ] Real-time conversational AI interviewer
* [ ] Resume-based interview generation
* [ ] Personalized interview difficulty
* [ ] Coding question execution
* [ ] Programming language selection
* [ ] Advanced performance analytics
* [ ] Interview leaderboard
* [ ] Email-based performance reports
* [ ] PDF interview reports
* [ ] More AI models
* [ ] Interview timer
* [ ] Webcam-based mock interview mode
* [ ] Deployment to cloud infrastructure

---

# 🐳 Docker Support

Dockerization can be added in future versions to simplify deployment of:

```text
Spring Boot Application
        +
      MySQL
```

A future Docker setup could provide:

```text
Browser
   ↓
Spring Boot Container
   ↓
MySQL Container
   ↓
Gemini API
```

---

# 📸 Screenshots

Screenshots can be added here to demonstrate the application's interface.

Recommended screenshots:

```text
docs/
├── login.png
├── register.png
├── dashboard.png
├── domain-selection.png
├── interview.png
├── result.png
├── history.png
├── admin-dashboard.png
├── question-management.png
└── user-management.png
```

Example:

```markdown
![Dashboard](docs/dashboard.png)
```

---

# 🔗 Repository

**GitHub Repository:**

https://github.com/Hacker-Hima/ai-interview-practice-platform

---

# 👨‍💻 Author

## Himachalam C

**B.E. Computer Science & Engineering Student**
National Engineering College, Kovilpatti

### Areas of Interest

* Java Development
* Spring Boot
* Full-Stack Development
* Artificial Intelligence
* Web Development
* Database Management
* Problem Solving
* Software Engineering

---

# 📬 Contact

### GitHub

https://github.com/Hacker-Hima

### LinkedIn

https://www.linkedin.com/in/himachalam-c-535153327/

### Portfolio

https://hacker-hima.github.io/Himachalam-portfolio/

---

# ⭐ Support

If you find this project useful or interesting, consider giving the repository a ⭐ on GitHub.

---

# 📜 License

This project was developed by **Himachalam C** for educational, academic, and portfolio purposes.

© 2026 Himachalam C. All rights reserved.

---

## ❤️ Final Note

The **AI Interview Practice Platform** demonstrates how modern web technologies, secure backend development, relational databases, and generative AI can be combined to build a practical interview preparation system.

> **Practice. Evaluate. Improve. Get Interview Ready. 🚀**
