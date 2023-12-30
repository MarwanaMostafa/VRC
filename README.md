# VRC

---
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


