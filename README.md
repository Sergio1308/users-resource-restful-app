# users-resource-restful-app

A simple RESTful API based on the Spring Boot web app: a controller responsible for the resource named Users, implemented according to the best practices. It allows to perform various operations related to user management, including user creation, updating user details, deleting users, and searching for users by birth date range.

# Navigation
* [Features](#features)
* [Getting Started](#getting-started)
  * [Installation](#installation)
* [Usage](#usage)
  * [Creating User](#creating-user)
  * [Updating User](#updating-user)
  * [Deleting User](#deleting-user)
  * [Searching for Users](#searching-for-users)
* [Testing](#testing)

# Features
* Create a user with validation for age (configurable).
* Update user details either partially or for all fields.
* Delete user.
* Search for users within a specified birth date range (with the validation which checks that “From” is less than “To”). Returns a list of objects.
* Comprehensive unit tests for ensuring functionality and reliability.
* Code has error handling for REST.
* JSON format for API responses.
* Use of database is not necessary. The data persistence layer is not required.

# Getting started
To run this project, you need to have the following software installed on your machine:
* Java Development Kit (JDK)
* Maven (for building the project)

## Installation
Clone this repository to your local machine using the following command:
```
git clone https://github.com/your-username/user-management-api.git
```
Navigate to the project directory:
```
cd users-resource-restful-app
```
Build the project using Maven:
```
mvn clean install
```
# Usage
API provides various endpoints to interact with the system. Below are examples of how to use the API:
## Creating User
### Request 
```
POST /users
Content-Type: application/json

{
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "birthDate": "1990-01-15"
}
```
### Response (Success)
```
HTTP 201 Created

{
  "id": 1,
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "birthDate": "1990-01-15",
  "address": null,
  "phoneNumber": null
}
```
### Response (Validation Error)
```
HTTP 400 Bad Request

{
  "title": "BAD_REQUEST",
  "status": 400,
  "timestamp": "2023-10-04T12:00:00",
  "message": "Validation failed",
  "detail": "Age must be greater than 18",
  "errors": [
    "Age must be greater than 18"
  ],
  "instance": "/users"
}
```
## Updating User
### Request 
```
PUT /users/1
Content-Type: application/json

{
  "email": "updated@example.com",
  "firstName": "Updated",
  "lastName": "User",
  "birthDate": "1990-01-01"
}
```
### Response (Success)
```
HTTP 200 OK

{
  "id": 1,
  "email": "updated@example.com",
  "firstName": "Updated",
  "lastName": "User",
  "birthDate": "1990-01-01",
  "address": null,
  "phoneNumber": null
}
```
### Response (Validation Error)
```
HTTP 400 Bad Request

{
  "title": "BAD_REQUEST",
  "status": 400,
  "timestamp": "2023-10-04T12:00:00",
  "message": "Validation failed",
  "detail": "Age must be greater than 18",
  "errors": [
    "Age must be greater than 18"
  ],
  "instance": "/users/1"
}
```
## Deleting User
### Request 
```
DELETE /users/1
```
### Response (Success)
```
HTTP 202 Accepted

"User 1 deleted successfully"
```
### Response (User Not Found)
```
HTTP 404 Not Found

{
  "title": "NOT_FOUND",
  "status": 404,
  "timestamp": "2023-10-04T12:00:00",
  "message": "Resource not found",
  "detail": "User with ID 1 not found",
  "instance": "/users/1"
}
```
## Searching for Users
### Request 
```
GET /users/search?from=1990-01-01&to=2000-12-31
```
### Response (Success)
```
HTTP 200 OK

[
  {
    "id": 2,
    "email": "user2@example.com",
    "firstName": "Alice",
    "lastName": "Smith",
    "birthDate": "1995-06-10",
    "address": null,
    "phoneNumber": null
  },
  {
    "id": 3,
    "email": "user3@example.com",
    "firstName": "Bob",
    "lastName": "Johnson",
    "birthDate": "1998-11-20",
    "address": null,
    "phoneNumber": null
  }
]
```
### Response (Validation Error - "from" not less than "to")
```
HTTP 400 Bad Request

{
  "title": "BAD_REQUEST",
  "status": 400,
  "timestamp": "2023-10-04T12:00:00",
  "message": "Validation failed",
  "detail": "Parameter 'from' must be less than 'to'",
  "errors": [
    "Parameter 'from' must be less than 'to'"
  ],
  "instance": "/users/search"
}
```
# Testing
Unit tests for the application can be executed using Maven. Run the following command from the project root directory:
```
mvn test
```
