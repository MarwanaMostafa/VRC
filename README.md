# VRC

## Motivation 🌟

**The Power of Imagination: Unlocking Dreams and Enhancing Learning**

Imagination is one of the greatest gifts that humanity can enjoy. It not only enriches our lives but also plays a pivotal role in realizing our dreams. 🚀

In various fields, imagination proves to be a valuable asset. In interactive education, it fosters student engagement by allowing them to visualize the knowledge imparted, such as narrating the story of Saturn or picturing the sun with their own eyes. 👩‍🏫🌌

Imagination is also a tool for efficiency, be it in conceptualizing the construction and preparation of a house or contributing to architectural design. It extends to diverse professions, with medical practitioners honing their skills through imaginative training. 👩‍⚕️🌡️

Similarly, for drivers, imagination enhances their training, contributing to safer and more skilled driving practices. 🚗

Crucially, imagination serves as a conduit for the transmission of ideas and civilizations. By integrating education with technology, we aim for a brilliant future. This endeavor fuels our motivation to embark on this project. 🌐🚀

---
## Objective
Virtual Room Creator has been designed to provide an immersive and innovative educational experience. It allows users to dynamically visualize and interact with various educational scenarios, making the learning process more engaging and memorable. We are working on a user-friendly 3D interface, empowering users to effortlessly create and customize their virtual environments according to their educational needs. 🌐✨

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

---
## Features 🚀

1. 📝 **User Sign Up**: Users can sign up for an account.

2. 🔐 **User Login**: Users can log in to their accounts.

3. 🔑 **Password Management**:
    - 📬 **Forget Password**: Users can initiate the forget password process.
    - 🔐 **Set New Password**: Users can set a new password after the forget password process.

4. 🏠 **Room Operations**:
    - 🆕 **Create New Room**: Users can create a new room.
    - ➕ **Add Collaborator**: Users can add collaborators to a room.
    - 🌐 **Share Public Room**: Users can share a public room with anyone.
    - 🔄 **Get Specific Room**: Users can retrieve information about a specific room.
    - 📋 **Get All Rooms**: Users can retrieve information about all rooms.
    - 🔄 **Update Room**: Users can update room details.

5. 👥 **Collaborator Management**:
    - 🔄 **Get All Collaborators**: Users can retrieve all collaborators for a specific room.
    - ❌ **Delete Collaborator**: Users can delete a collaborator from a room.

6. 🖼️ **Object Operations**:
    - ➕ **Insert Object**: Users can insert objects into a room.

---
## Demo
![Untitled design (1).gif](images%2FUntitled%20design%20%281%29.gif)

![Untitled design (2).gif](images%2FUntitled%20design%20%281%29.gif)
---
## System Architecture 
![ezgif.com-crop (1).gif](images%2Fezgif.com-crop%20%281%29.gif)
---
## Caching

This project utilizes Redis Cache for efficient data storage and retrieval. Redis is an in-memory data structure store that can be used as a caching mechanism to enhance performance and reduce database load. 🚀

### Key Benefits of Redis Cache:

1. **Faster Data Retrieval**: Redis stores data in-memory, allowing for quick access and retrieval of frequently accessed information. 🏎️

2. **Reduced Database Load**: By caching frequently accessed data, the load on the main database is reduced, leading to improved overall system performance. ⚖️

3. **Real-time Updates**: The cache is configured for real-time updates, ensuring that the data remains synchronized with the latest changes. 

---
<details open> 
  <summary><strong style="font-size: x-large;">Folder Structure 📁</strong></summary>

```
|   .gitignore
|   mvnw
|   mvnw.cmd
|   polyPizzaObjects.json
|   pom.xml
|   README.md
|   tree_structure.txt
|   VRC.iml
|
+---.mvn
|   \---wrapper
|           maven-wrapper.jar
|           maven-wrapper.properties
|           
+---src
   +---main
   |   +---java
   |   |   \---com
   |   |       \---example
   |   |           \---vrc
   |   |               |   VrcApplication.java
   |   |               |   
   |   |               +---assets
   |   |               |   +---configs
   |   |               |   |       AssetsSwaggerConfig.java
   |   |               |   |       
   |   |               |   +---controllers
   |   |               |   |       AssetsController.java
   |   |               |   |       
   |   |               |   \---services
   |   |               |       |   HdrisAssetsService.java
   |   |               |       |   ModelsAssetsService.java
   |   |               |       |   
   |   |               |       +---jsonParser
   |   |               |       |       JsonParser.java
   |   |               |       |       
   |   |               |       +---objects
   |   |               |       |       ObjectsList.java
   |   |               |       |       PolyHavenHdrisObjectsList.java
   |   |               |       |       PolyHavenModelsObjectsList.java
   |   |               |       |       PolyHavenObjectsList.java
   |   |               |       |       PolyPizzaObjectsList.java
   |   |               |       |       
   |   |               |       \---singleObject
   |   |               |               Object.java
   |   |               |               PolyHavenHdriObject.java
   |   |               |               PolyHavenModelObject.java
   |   |               |               PolyPizzaModelObject.java
   |   |               |               
   |   |               +---authentication
   |   |               |   +---common
   |   |               |   |   \---documentation
   |   |               |   |           DocConstant.java
   |   |               |   |           
   |   |               |   +---configs
   |   |               |   |       AuthenticationSwaggerConfig.java
   |   |               |   |       CacheConfig.java
   |   |               |   |       EmailConfig.java
   |   |               |   |       SpringSecurityConfig.java
   |   |               |   |       
   |   |               |   +---controllers
   |   |               |   |       AuthController.java
   |   |               |   |       ContactUsController.java
   |   |               |   |       
   |   |               |   +---DTOs
   |   |               |   |       ContactUsDTO.java
   |   |               |   |       ResetPasswordData.java
   |   |               |   |       RUserCredentials.java
   |   |               |   |       UserDTO.java
   |   |               |   |       UserWithoutPasswordDTO.java
   |   |               |   |       
   |   |               |   +---Exception
   |   |               |   |       PasswordMismatchException.java
   |   |               |   |       
   |   |               |   +---mappers
   |   |               |   |       ContactUsMapper.java
   |   |               |   |       UserMapper.java
   |   |               |   |       UserWithoutPasswordMapper.java
   |   |               |   |       
   |   |               |   +---models
   |   |               |   |       ContactUs.java
   |   |               |   |       UserEntity.java
   |   |               |   |       
   |   |               |   +---repositories
   |   |               |   |       ContactUsRepository.java
   |   |               |   |       UserRepository.java
   |   |               |   |       
   |   |               |   +---services
   |   |               |   |   |   AuthService.java
   |   |               |   |   |   ContactUsService.java
   |   |               |   |   |   UserService.java
   |   |               |   |   |   
   |   |               |   |   \---impl
   |   |               |   |           AuthServiceImpl.java
   |   |               |   |           ContactUsServiceImpl.java
   |   |               |   |           UserServiceImpl.java
   |   |               |   |           
   |   |               |   +---swagger
   |   |               |   |       ApiFullResponseAutoLogin.java
   |   |               |   |       ApiFullResponseCreateComplaint.java
   |   |               |   |       ApiFullResponseForgetPassword.java
   |   |               |   |       ApiFullResponseLogin.java
   |   |               |   |       ApiFullResponseSetPassword.java
   |   |               |   |       ApiFullResponseSignUp.java
   |   |               |   |       
   |   |               |   \---utilities
   |   |               |           EmailUtil.java
   |   |               |           JwtRequestFilter.java
   |   |               |           JwtUtil.java
   |   |               |           UserPasswordEncryption.java
   |   |               |           
   |   |               +---rooms
   |   |               |   +---common
   |   |               |   |   \---documentation
   |   |               |   |           DocConstant.java
   |   |               |   |           
   |   |               |   +---configs
   |   |               |   |       RoomSwaggerConfig.java
   |   |               |   |       UserRoomChannelInterceptor.java
   |   |               |   |       WebSocketConfig.java
   |   |               |   |       
   |   |               |   +---controllers
   |   |               |   |       RoomController.java
   |   |               |   |       
   |   |               |   +---DTOs
   |   |               |   |       RoomDTO.java
   |   |               |   |       RoomWithoutUserDTO.java
   |   |               |   |       SharedRoomDTO.java
   |   |               |   |       
   |   |               |   +---mappers
   |   |               |   |       RoomMapper.java
   |   |               |   |       RoomWithoutUserMapper.java
   |   |               |   |       SharedRoomMapper.java
   |   |               |   |       
   |   |               |   +---models
   |   |               |   |       RoomEntity.java
   |   |               |   |       SharedRoomEntity.java
   |   |               |   |       
   |   |               |   +---repositories
   |   |               |   |       RoomRepository.java
   |   |               |   |       SharedRoomRepository.java
   |   |               |   |       
   |   |               |   +---services
   |   |               |   |   |   RoomService.java
   |   |               |   |   |   
   |   |               |   |   \---impl
   |   |               |   |           RoomServiceImpl.java
   |   |               |   |           
   |   |               |   \---swagger
   |   |               |           ApiFullResponseAddCollaborator.java
   |   |               |           ApiFullResponseCreate.java
   |   |               |           ApiFullResponseDeleteCollaborator.java
   |   |               |           ApiFullResponseGetAllCollaborators.java
   |   |               |           ApiFullResponseGetRoomByID.java
   |   |               |           ApiFullResponseGetRooms.java
   |   |               |           ApiFullResponseGetSharedRoom.java
   |   |               |           ApiFullResponseGetSharedRooms.java
   |   |               |           ApiFullResponseUpdateRoomByID.java
   |   |               |           
   |   |               \---shared
   |   |                   \---utilities
   |   |                           UserInputsValidator.java
   |   |                           
   |   \---resources
   |           application.properties
   |           
   \---test
       \---java
           \---com
               \---example
                   \---vrc
                       |   VrcApplicationTests.java
                       |   
                       \---rooms
                           +---controllers
                           |       RoomControllerTest.java
                           |       
                           \---services
                                   RoomServiceTest.java
                                   


```
</details>

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
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![JUnit](https://img.shields.io/badge/JUnit-25A162?style=for-the-badge&logo=junit5&logoColor=white) 
![Redis](https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)
---
