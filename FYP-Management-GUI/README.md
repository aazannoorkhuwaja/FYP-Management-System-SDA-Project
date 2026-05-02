# FYP Management Platform - Java GUI

**Course:** Software Design & Analysis (FAST-NUCES Peshawar)
**Group Members:**
- Aazan Noor (24P-0706)
- Munesh Kumar (24P-0635)
- Ahmed Asim (24P-0740)

## Compile & Run

```bash
cd src
javac Main.java model/*.java view/*.java controller/*.java
java Main
```

Or on Windows:
```cmd
cd src
javac Main.java model\*.java view\*.java controller\*.java
java Main
```

## Implemented Use Cases (2-3 Core)
1. **UC-01: Submit FYP Proposal** — Student submits title/domain/abstract with duplicate title checking.
2. **UC-02: Send Supervision Request** — Student sends formal request to supervisor; supervisor accepts/declines with reason.
3. **UC-04: Submit Peer Review** — Group members rate each other privately (hidden from other students, visible to supervisor/admin conceptually).

## Design Patterns Used
- **MVC (Model-View-Controller):** `model/` for entities, `view/` for Swing GUI, `controller/` for wiring (integrated within view event listeners for simplicity in this scope).
- **Singleton:** `Database.java` ensures one shared data instance across all screens.
- **Inheritance:** `Student`, `Supervisor`, `Admin` extend abstract `User` per class diagram.

## Demo Accounts
| Role | Email | Password |
|------|-------|----------|
| Student | aazan@fast.edu | pass123 |
| Student | munesh@fast.edu | pass123 |
| Student | ahmed@fast.edu | pass123 |
| Supervisor | umer@fast.edu | pass123 |
| Admin | admin@fast.edu | admin123 |

## Notes
- Data persists via Java serialization (`data/database.dat`).
- Error handling uses `JOptionPane` dialogs for invalid input and missing data.
- Only 3 core use cases are fully implemented to meet assignment scope; all other classes from the class diagram are present as stubs to show design-to-code mapping.
