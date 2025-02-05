# Vocal Pad
# Journaling App (Like Google Keep)

A journaling application built with Java, Spring Boot, Maven, and MongoDB Atlas.
This app allows users to register, authenticate using basic authentication with bcrypt password encoding,
and manage their personal notes. The app also integrates with an external API (Eleven Labs) to convert notes to voice,
with the ability to download and listen to the voice files.

Project is live at:

 ```bash
https://vocalpad-cf3e33b475f4.herokuapp.com/
```

## Features

- **User Registration and Authentication**
    - Users can register and authenticate using a secure password hashed with bcrypt.
    - Basic authentication is used for secure access to the app.

- **User Account Management**
    - Users can update their personal details.
    - Users can change their password.
    - Users can delete their account.

- **Note Management**
    - Users can write, update, and delete their own notes.
    - Notes are stored securely in MongoDB Atlas.

- **Voice Conversion**
    - Integrated with the Eleven Labs API to convert notes to voice.
    - Users can download and listen to the audio version of their notes.

- **Security**
    - Implemented Spring Security to secure all API endpoints.
    - Only authenticated users can access, update, or delete their notes.

## Technologies Used

- **Java** 		        – The primary programming language.
- **Spring Boot** 	    – Framework for building the RESTful APIs.
- **Maven** 	        – Project management and dependency management tool.
- **MongoDB Atlas** 	– Cloud-based NoSQL database to store user data and notes.
- **Spring Security**   – Provides security to the application with authentication and authorization.
- **Eleven Labs API**   – External API used to convert text to speech.

## Prerequisites

Before you start, make sure you have the following installed:

- Java 22
- Maven
- MongoDB Atlas account (or local MongoDB instance)
- Spring Boot setup

## Getting Started

1. Clone the repository:

    ```bash
    git clone https://github.com/chandkavathepratik/vocal-pad.git
    ```

2. Navigate to the project directory:

    ```bash
    cd vocal-pad
    ```

3. Set up your MongoDB Atlas connection:

    - Go to [MongoDB Atlas](https://www.mongodb.com/cloud/atlas) and create a free cluster.
    - Get the connection URI and update the `application.properties` file in the `src/main/resources` folder with your MongoDB URI.

4. Install the required dependencies:

    ```bash
    mvn clean install
    ```

5. Run the application:

    ```bash
    mvn spring-boot:run
    ```

6. The app will be available at `http://localhost:8080`, which is redirected to swagger ui.
7. Swagger-UI API `http://localhost:8080/swagger-ui/index.html`.
8. Added a logback.xml file for logging. (I didn't like format so turned it off by removing its extension if you want to log add .xml extension to file)
9. Check `application.properties` file in the `src/main/resources` folder and replace the file content with your details.

## Endpoints

Public open endpoints:
- **GET**   /
  – Redirected the apps context path / to the swaggers home page.
  – Redirects "/" to "/swagger-ui/index.html"

- **POST**   /signup
  – Register a new user.

- **POST**   /login                         
  – Login

- **PUT**    /forgot-password
  – Sends email to user registered email id with a reset token to reset password.

- **POST**    /reset-password
  – Takes username, reset token and new password to reset password.

Manage Notes:
- **POST**   /notes                	        
  – Create a new note.

- **GET**    /notes                	        
  – Get all notes for the authenticated user.

- **GET**    /notes/{noteId}      		    
  – Get a single note.

- **PUT**    /notes/{noteId}      		    
  – Update an existing note.

- **DELETE** /notes/{noteId}      		    
  – Delete a note.

- **GET**    /notes/search/{text}           
  – Get notes of matching text in title and content field.

- **GET**    /notes/trash                         
  -Get trash.

- **DELETE** /notes/empty-trash                         
  -Empty trash.

- **GET**    /notes/archives                
  -Get archived notes

- **PUT**    /notes/archives{noteId}        
  -Archive note

- **PUT**    /notes/unarchive{noteId}                         
  -Unarchive note.

Manage User Account
- **GET**    /user                     		
  – Get user details.

- **PUT**    /user    	              		
  – Change user password.

- **DELETE** /user                      	
  – Delete user account.

Text to Voice Conversion Endpoints:
- **GET**    /notes/download/{noteId}
  – Convert note to voice and download the audio file.

- **GET**    /listen/{noteId}           	
  – Convert note to voice and stream the audio file.


## Security

- **Basic Authentication** is used for user login.
- Passwords are stored securely using **Bcrypt** hashing.
- All sensitive endpoints are protected by **Spring Security**.
- Implemented user roles and permissions for better access control.

## Future Improvements

- Add JWT Authentication for stateless user sessions and Oauth.(I will implement in future)
- Enhance the UI and provide a web or mobile frontend.

## Acknowledgements

- Thanks to [Eleven Labs](https://www.elevenlabs.io) for the amazing text-to-speech API.
