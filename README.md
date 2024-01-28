# VRC

# Branching Strategy 

## Master Branch  🏆  🚀 

The master branch is the main branch of the project. It represents the stable, production-ready version of the code.

---

## Feature Branches  🧩
For each new feature or task, a dedicated feature branch should be created. Feature branches are essential for developing and isolating changes related to specific features or tasks.

### Creating a Feature Branch 🛠️🧩
From the master branch, create a feature branch using the following naming convention:

```bash
master/<feature name>
```
For example : 

```bash
master/Authentication
```
---

## Task Branches 📋
For each task within a feature, create a task branch from the corresponding feature branch. Avoid pushing directly to the feature branch; instead, use the task branch. Follow the naming convention:

```bash
git checkout -b task/your-name/description-For-Task
```
Replace "your-name" with your name or identifier like **`Marwan, Islam, Rana, Emil, Fady`**, and provide a brief of task  "description-for-task."

```bash
 task/marwan/forget-password
```
---

# Pull Request (PR) Strategy 🔄 🚀
1-Create a pull request from the task branch to the feature branch. A team member should review the pull request. 📤📋🚀🧩

2-After approval, create a pull request from the feature branch to the master branch. Again, ensure that a team member reviews the pull request before merging. 📤🧩🚀🏆

---

## Technologies Used

Here is a list of technologies and tools used in this project:

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)
![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)
![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)
[![Maven](https://badgen.net/badge/icon/maven?icon=maven&label)](https://https://maven.apache.org/)
![Git](https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white)
