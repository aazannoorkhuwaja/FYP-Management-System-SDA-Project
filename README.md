# FYP Management System (Monolithic Serenity)

[![Course](https://img.shields.io/badge/Course-Software%20Design%20%26%20Analysis-blue)](https://pwr.nu.edu.pk/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Language-Java-orange)](https://www.java.com/)

A comprehensive Final Year Project (FYP) management platform built with Java Swing, following the **Monolithic Serenity** design philosophy. This project was developed as part of the Software Design & Analysis (SDA) course at FAST-NUCES Peshawar.

## 👥 Contributors
- **Aazan Noor Khuwaja** (24P-0706)
- **Munesh Kumar** (24P-0635)
- **Ahmed Asim** (24P-0740)

---

## ✨ Features

### 🚀 Core Functionalities
- **UC-01: Proposal Management**: Students can submit FYP proposals with duplicate title checking and domain classification.
- **UC-02: Supervision Workflow**: Seamless request/response system between students and supervisors.
- **UC-04: Peer Review System**: Private rating system for group members to ensure fair contribution assessment.
- **Unified Dashboard**: Personalized interfaces for Students, Supervisors, and Administrators.

### 🎨 Design & Architecture
- **MVC Pattern**: Strict separation of concerns between data models, Swing views, and controllers.
- **Singleton Pattern**: Centralized data management via `Database.java`.
- **Inheritance Hierarchy**: Robust class structure with `Student`, `Supervisor`, and `Admin` extending a base `User` class.
- **Monolithic Serenity UI**: A custom, high-craft dark-themed UI built using native Java Swing components.

---

## 📂 Project Structure

```text
├── src/               # Java source files (Model, View, Controller)
├── docs/              # Project documentation and assignment details
│   ├── assignment-context/  # Original assignment PDFs and requirements
│   └── legacy/        # Data from previous project iterations
├── data/              # Persistent data storage (database.dat)
├── design/            # UI/UX design assets and mockups
└── README.md          # Project overview
```

---

## 🛠️ How to Run

### Prerequisites
- Java Development Kit (JDK) 8 or higher.

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/aazannoorkhuwaja/FYP-Management-System-SDA-Project.git
   cd FYP-Management-System-SDA-Project
   ```
2. Compile the source code:
   ```bash
   javac -d bin src/Main.java src/model/*.java src/view/*.java src/controller/*.java
   ```
3. Run the application:
   ```bash
   java -cp bin Main
   ```

---

## 🔐 Demo Credentials

| Role | Email | Password |
|------|-------|----------|
| **Student** | `aazan@fast.edu` | `pass123` |
| **Supervisor** | `umer@fast.edu` | `pass123` |
| **Admin** | `admin@fast.edu` | `admin123` |

---

## 📄 License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
