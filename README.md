# Mini-Git in Java

A simplified version control system implemented in Java, inspired by Git.  
This project demonstrates object-oriented design, data structures, and basic version control operations.

---

## 🛠️ Features
- **Repository:** Stores commits with unique IDs, timestamps, and messages. Supports operations like `commit`, `drop`, `synchronize`, and viewing commit history.  
- **Client:** Provides a console interface to create and manage repositories and perform all Mini-Git operations interactively.  
- **Commit:** Inner class of `Repository` representing individual commits, storing message, timestamp, ID, and a reference to the previous commit.

---

## 🎯 Design & Implementation
- `Repository` maintains the history of commits as a linked structure (`head` pointing to the most recent commit).  
- `Commit` objects store metadata and link to previous commits to maintain chronological history.  
- `Client` allows users to interactively create repositories, make commits, view history, drop commits, and synchronize repositories.  
- Implements error checking for invalid input, duplicate repository creation, and invalid commit operations.  

---

## 🧩 Skills Practiced
- Object-oriented design and encapsulation in Java  
- Implementing version control functionality and linked data structures  
- Handling user input and exceptions  
- Console-based application development

---

## 🔗 Usage
1. Clone the repository:
```bash
git clone https://github.com/amberli/mini-git.git
```
2. Open the project in your IDE (Visual Studio Code, IntelliJ, etc.).
3. Run the Client class to interact with Mini-Git in the console.
4. Follow the on-screen instructions to create repositories, commit changes, view history, drop commits, or synchronize repositories.

---

## 📂 Repository Structure
The project is organized as follows:

```text
mini-git/
├── src/
│   ├── Client.java        # Main program for running and interacting with Mini-Git
│   ├── Repository.java    # Handles repository state, commits, and operations
│   └── Testing.java       # Contains test cases to verify Mini-Git functionality
├── README.md              # Project overview and instructions
└── .gitignore             # Ignore compiled files and IDE artifacts

```
