# Project Management application
#### Spring-Boot-REST-API

* [Running the application with Docker](#running-the-application-with-docker)
* [Authentication](#authentication)

 |         [Users](#users)           |          [Teams](#teams)          |          [Projects](#projects)          |            [Tasks](#tasks)        |          [Worklogs](#worklogs)          |
 |:---------------------------------:|:---------------------------------:|:---------------------------------------:|:---------------------------------:|:---------------------------------------:|
 | [create user](#create-user)       | [create team](#create-team)       | [create project](#create-project)       | [create task](#create-task)       | [create worklog](#create-worklog)       |
 | [get all users](#get-all-users)   | [get all teams](#get-all-teams)   | [get all projects](#get-all-projects)   | [get all tasks](#get-all-tasks)   | [get all worklogs](#get-all-worklogs)   |
 | [edit user](#edit-user)           | [edit team](#edit-team)           | [edit project](#edit-project)           | [edit task](#edit-task)           | [edit worklog](#edit-worklog)           |
 | [get user](#get-user-by-id)       | [get team](#get-team-by-id)       | [get project](#get-project-by-id)       | [get task](#get-task-by-id)       | [get worklog](#get-all-worklogs)        |
 | [delete user](#delete-user-by-id) | [delete team](#delete-team-by-id) | [delete project](#delete-project-by-id) | [delete task](#delete-task-by-id) | [delete worklog](#delete-worklog-by-id) |    

## Running the application with Docker
### Step 1. Pull the docker image for the app
```bash
docker pull xpresser/spring-boot    
```

### Step 2. Pull the docker image for the database
```
docker pull xpresser/oracle
```

### Step 3. Start the application with
```
docker-compose up
```

## Postman Requests
## Authentication
[Spring API](#spring-boot-rest-api)

Authenticate by hitting the '/users/login' endpoint, providing valid user credentials you will be granted  a Bearer Json Web Token.

You should provide that token at every request to the API (as Authorization header)
- *Request*
```bash
POST localhost:8080/users/login
Content-Type: application/json

{
    "username": "admin",
    "password": "password"
}
```
- *Response*
```bash
{
    "token": "Bearer eyJhbGciOiJIUzI1NiJ9
                    .eyJpZCI6Nywic3ViIjoiYWRtaW4iLCJhZG1pbiI6dHJ1ZSwiZXhwIjoxNTg4ODUxNzU0fQ
                    .OauJeEda5iltzv6g2Y5gvefHGPK-PO2gy9dlUfqfgmc"
}
```

## Users
[Spring API](#spring-boot-rest-api)

##### **Create user**

- *Request*
```bash
POST localhost:8080/users HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Cache-Control: no-cache
Postman-Token: Bearer eyJhbGciOiJIUzI1NiJ9
                     .eyJpZCI6Nywic3ViIjoiYWRtaW4iLCJhZG1pbiI6dHJ1ZSwiZXhwIjoxNTg4ODUxNzU0fQ
                     .OauJeEda5iltzv6g2Y5gvefHGPK-PO2gy9dlUfqfgmc

{
    "username": "test",
    "password": "testpass",
    "firstName": "t",
    "lastName": "t",
    "admin": false
}
```
- *Response*
```bash
{
    "id": 2,
    "username": "test",
    "password": "$2a$10$W7nJNtuRgo7l7xzPKP71JOTNRRwp/CpCZB/4AOuOGzAF1BQ942AVq",
    "firstName": "t",
    "lastName": "t",
    "dateCreated": "2020-05-12T00:14:56.2935314",
    "dateEdited": "2020-05-12T00:14:56.2935314",
    "creatorID": 1,
    "editorID": 1,
    "teams": null,
    "admin": false
}
```

##### **Get all users**

- *Request*
```bash
GET localhost:8080/users HTTP/1.1
Host: localhost:8080
Content-type: application/json
Cache-Control: no-cache
Postman-Token: Bearer eyJhbGciOiJIUzI1NiJ9
                     .eyJpZCI6Nywic3ViIjoiYWRtaW4iLCJhZG1pbiI6dHJ1ZSwiZXhwIjoxNTg5MDk3OTUzfQ
                     .0iBE9esgbbuFJh94DF9UjPlEZzk6vhlOvnQ744Q6tGY
```
- *Response*
```bash
[
    {
        "id": 1,
        "username": "admin",
        "password": "password",
        "firstName": "Administrator",
        "lastName": "Administrator"
        "dateCreated": "2020-04-21T22:03:37.41797",
        "dateEdited": "2020-04-21T22:05:07.610121",
        "creatorID": 0,
        "editorID": 0
        "teams": [
            "Team 1"
        ],
        "admin": true
    },
    {
        "id": 2,
        "username": "test",
        "password": "testpass",
        "firstName": "t",
        "lastName": "t",
        "admin": false,
        "dateCreated": "2020-05-12T00:14:56.2935314",
        "dateEdited": "2020-05-12T00:14:56.2935314",
        "creatorID": 1,
        "editorID": 1
        "teams": [],
        "admin": false
    }
]
```

##### **Edit user**

- *Request*
```bash
PUT localhost:8080/users/2 HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Cache-Control: no-cache
Postman-Token: Bearer eyJhbGciOiJIUzI1NiJ9
                     .eyJpZCI6Nywic3ViIjoiYWRtaW4iLCJhZG1pbiI6dHJ1ZSwiZXhwIjoxNTg4ODUxNzU0fQ
                     .OauJeEda5iltzv6g2Y5gvefHGPK-PO2gy9dlUfqfgmc

{
    "username": "test username",
    "password": "test password",
    "firstName": "Testing",
    "lastName": "Testing",
    "admin": true
}
```
- *Response*
```bash
{
    "id": 2,
    "username": "test username",
    "password": "test password",
    "firstName": "Testing",
    "lastName": "Testing",
    "dateCreated": "2020-05-12T00:14:56.293531",
    "dateEdited": "2020-05-12T00:28:22.3558958",
    "creatorID": 1,
    "editorID": 1,
    "teams": [],
    "admin": true
}
```

##### **Get user by id**

- *Request*
```bash
GET localhost:8080/users/2 HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Cache-Control: no-cache
Postman-Token: Bearer eyJhbGciOiJIUzI1NiJ9
                     .eyJpZCI6Nywic3ViIjoiYWRtaW4iLCJhZG1pbiI6dHJ1ZSwiZXhwIjoxNTg4ODUxNzU0fQ
                     .OauJeEda5iltzv6g2Y5gvefHGPK-PO2gy9dlUfqfgmc
```
- *Response*
```bash
{
    "id": 2,
    "username": "test username",
    "firstName": "Testing",
    "lastName": "Testing",
    "dateCreated": "2020-05-12T00:14:56.293531",
    "dateEdited": "2020-05-12T00:28:22.355896",
    "creatorID": 1,
    "editorID": 1,
    "password": null,
    "teams": [],
    "admin": true
}
```

##### **Delete user by id**
```
DELETE localhost:8080/users/2 HTTP/1.1
Host: localhost:8080
Content-Type:application/json
Cache-Control: no-cache
Postman-Token: Bearer eyJhbGciOiJIUzI1NiJ9
                     .eyJpZCI6Nywic3ViIjoiYWRtaW4iLCJhZG1pbiI6dHJ1ZSwiZXhwIjoxNTg4ODUxNzU0fQ
                     .OauJeEda5iltzv6g2Y5gvefHGPK-PO2gy9dlUfqfgmc
```

## Teams
[Spring API](#spring-boot-rest-api)

##### **Create team**

- *Request*
```bash
POST localhost:8080/teams HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Cache-Control: no-cache
Postman-Token: Bearer eyJhbGciOiJIUzI1NiJ9
                     .eyJpZCI6Nywic3ViIjoiYWRtaW4iLCJhZG1pbiI6dHJ1ZSwiZXhwIjoxNTg4ODUxNzU0fQ
                     .OauJeEda5iltzv6g2Y5gvefHGPK-PO2gy9dlUfqfgmc

{
    "title": "Team 1",
    "description": "very cool team"
}
```
- *Response*
```bash
{
    "id": 1,
    "title": "Team 1",
    "dateCreated": "2020-05-12T00:40:12.9454652",
    "dateEdited": "2020-05-12T00:40:12.9454652",
    "creatorID": 1,
    "editorID": 1,
    "users": null,
    "projects": null
}    
```

**Assign user to team**

- *Request*
```
PUT localhost:8080/teams/1/users/1 HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Cache-Control: no-cache
Postman-Token: Bearer eyJhbGciOiJIUzI1NiJ9
                     .eyJpZCI6Nywic3ViIjoiYWRtaW4iLCJhZG1pbiI6dHJ1ZSwiZXhwIjoxNTg4ODUxNzU0fQ
                     .OauJeEda5iltzv6g2Y5gvefHGPK-PO2gy9dlUfqfgmc
```

##### **Get all teams**

- *Request*
```bash
GET localhost:8080/teams HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Cache-Control: no-cache
Postman-Token: Bearer eyJhbGciOiJIUzI1NiJ9
                     .eyJpZCI6Nywic3ViIjoiYWRtaW4iLCJhZG1pbiI6dHJ1ZSwiZXhwIjoxNTg4ODUxNzU0fQ
                     .OauJeEda5iltzv6g2Y5gvefHGPK-PO2gy9dlUfqfgmc
```
- *Response*
```bash
[
    {
         "id": 1,
         "title": "Team 1",
         "users": [
            1
         ],
         "projects": [
            1
         ]         
         "dateCreated": "2020-05-12T00:40:12.9454652",
         "dateEdited": "2020-05-12T00:40:12.9454652",
         "creatorID": 1,
         "editorID": 1,
    }  
]
```

##### **Edit team**

- *Request* 
```bash
PUT localhost:8080/teams/1 HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Cache-Control: no-cache
Postman-Token: Bearer eyJhbGciOiJIUzI1NiJ9
                     .eyJpZCI6Nywic3ViIjoiYWRtaW4iLCJhZG1pbiI6dHJ1ZSwiZXhwIjoxNTg4ODUxNzU0fQ
                     .OauJeEda5iltzv6g2Y5gvefHGPK-PO2gy9dlUfqfgmc

{
    "title": "Freakin Awesomes",
    "description": "best team ever"
}
```
- *Response*
```bash
{
    "id": 1,
    "title": "Freakin Awesomes",
    "users": [
        1
    ],
    "projects": [
        1
    ]
    "dateCreated": "2020-05-12T00:40:12.945465",
    "dateEdited": "2020-05-12T00:43:44.6515469",
    "creatorID": 1,
    "editorID": 1,
}
```

##### **Get team by id**

- *Request*
```bash
GET localhost:8080/teams/1 HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Cache-Control: no-cache
Postman-Token: Bearer eyJhbGciOiJIUzI1NiJ9
                     .eyJpZCI6Nywic3ViIjoiYWRtaW4iLCJhZG1pbiI6dHJ1ZSwiZXhwIjoxNTg4ODUxNzU0fQ
                     .OauJeEda5iltzv6g2Y5gvefHGPK-PO2gy9dlUfqfgmc
```
- *Response*
```bash
{
    "id": 1,
    "title": "Freakin Awesomes",
    "users": [
        1
    ],
    "projects": [
        1
    ],
    "dateCreated": "2020-05-12T00:40:12.945465",
    "dateEdited": "2020-05-12T00:43:44.651547",
    "creatorID": 1,
    "editorID": 1
}
```

##### **Delete team by id**
```
DELETE localhost:8080/teams/1 HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Cache-Control: no-cache
Postman-Token: Bearer eyJhbGciOiJIUzI1NiJ9
                     .eyJpZCI6Nywic3ViIjoiYWRtaW4iLCJhZG1pbiI6dHJ1ZSwiZXhwIjoxNTg4ODUxNzU0fQ
                     .OauJeEda5iltzv6g2Y5gvefHGPK-PO2gy9dlUfqfgmc  
```

## Projects
[Spring API](#spring-boot-rest-api)

##### **Create project**

- *Request*
```bash
POST localhost:8080/projects HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Cache-Control: no-cache
Postman-Token: Bearer eyJhbGciOiJIUzI1NiJ9
                     .eyJpZCI6Nywic3ViIjoiYWRtaW4iLCJhZG1pbiI6dHJ1ZSwiZXhwIjoxNTg4ODUxNzU0fQ
                     .OauJeEda5iltzv6g2Y5gvefHGPK-PO2gy9dlUfqfgmc

{
    "title": "My First Project",
    "description": "testing project"
}
```
- *Response*
```bash
{
    "id": 1,
    "title": "My First Project",
    "description": "testing project",
    "dateCreated": "2020-05-12T01:09:33.4794558",
    "dateEdited": "2020-05-12T01:09:33.4794558",
    "creatorID": 1,
    "editorID": 1,
    "tasks": null,
    "teams": null
}
```

##### **Get all projects**

- *Request*
```bash
GET localhost:8080/projects HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Cache-Control: no-cache
Postman-Token: Bearer eyJhbGciOiJIUzI1NiJ9
                     .eyJpZCI6Nywic3ViIjoiYWRtaW4iLCJhZG1pbiI6dHJ1ZSwiZXhwIjoxNTg4ODUxNzU0fQ
                     .OauJeEda5iltzv6g2Y5gvefHGPK-PO2gy9dlUfqfgmc
```
- *Response*
```bash
[
    {
        "id": 1,
        "title": "My First Project",
        "description": "testing project",
        "tasks": [
            1
        ]
        "teams": [
            "Team 1"
        ]
        "dateCreated": "2020-05-12T01:09:33.479456",
        "dateEdited": "2020-05-12T01:09:33.479456",
        "creatorID": 1,
        "editorID": 1
    }
]
```

##### **Edit project**

- *Request*
```bash
PUT localhost:8080/projects/1 HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Cache-Control: no-cache
Postman-Token: Bearer eyJhbGciOiJIUzI1NiJ9
                     .eyJpZCI6Nywic3ViIjoiYWRtaW4iLCJhZG1pbiI6dHJ1ZSwiZXhwIjoxNTg4ODUxNzU0fQ
                     .OauJeEda5iltzv6g2Y5gvefHGPK-PO2gy9dlUfqfgmc

{
    "title": "To Do Application",
    "description": "project about rest api"
}
```
- *Response*
```bash
{
    "id": 1,
    "title": "To Do Application",
    "description": "project about rest api",
    "dateCreated": "2020-05-12T01:09:33.479456",
    "dateEdited": "2020-05-12T01:16:49.1196359",
    "creatorID": 1,
    "editorID": 1,
    "tasks": [
        1
    ],
    "teams": [
        "Team 1"
    ]
}
```

##### **Get project by id**

- *Request*
```bash
GET localhost:8080/projects/1 HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Cache-Control: no-cache
Postman-Token: Bearer eyJhbGciOiJIUzI1NiJ9
                     .eyJpZCI6Nywic3ViIjoiYWRtaW4iLCJhZG1pbiI6dHJ1ZSwiZXhwIjoxNTg4ODUxNzU0fQ
                     .OauJeEda5iltzv6g2Y5gvefHGPK-PO2gy9dlUfqfgmc
```
- *Response*
```bash
{
    "id": 1,
    "title": "To Do Application",
    "description": "project about rest api",
    "tasks": [
        1
    ],
    "teams": [
        "Team 1"
    ],
    "dateCreated": "2020-05-12T01:09:33.479456",
    "dateEdited": "2020-05-12T01:16:49.119636",
    "creatorID": 1,
    "editorID": 1
}
```

**Assign team to project**
- *Request*
```
PUT localhost:8080/projects/1/teams/1 HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Cache-Control: no-cache
Postman-Token: Bearer eyJhbGciOiJIUzI1NiJ9
                     .eyJpZCI6Nywic3ViIjoiYWRtaW4iLCJhZG1pbiI6dHJ1ZSwiZXhwIjoxNTg4ODUxNzU0fQ
                     .OauJeEda5iltzv6g2Y5gvefHGPK-PO2gy9dlUfqfgmc
```

##### **Delete project by id**
```
DELETE localhost:8080/projects/1 HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Cache-Control: no-cache
Postman-Token: Bearer eyJhbGciOiJIUzI1NiJ9
                     .eyJpZCI6Nywic3ViIjoiYWRtaW4iLCJhZG1pbiI6dHJ1ZSwiZXhwIjoxNTg4ODUxNzU0fQ
                     .OauJeEda5iltzv6g2Y5gvefHGPK-PO2gy9dlUfqfgmc
```

## Tasks
[Spring API](#spring-boot-rest-api)

##### **Create task**

- *Request*
```bash
POST localhost:8080/projects/1/tasks HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Cache-Control: no-cache
Postman-Token: Bearer eyJhbGciOiJIUzI1NiJ9
                     .eyJpZCI6Nywic3ViIjoiYWRtaW4iLCJhZG1pbiI6dHJ1ZSwiZXhwIjoxNTg4ODUxNzU0fQ
                     .OauJeEda5iltzv6g2Y5gvefHGPK-PO2gy9dlUfqfgmc

{
    "title": "My First Task",
    "description": "project about rest api",
    "status": 1
}
```
- *Response*
```bash
{
    "id": 1,
    "title": "My First Task",
    "description": "project about rest api",
    "status": true,
    "dateCreated": "2020-05-12T01:20:31.4160882",
    "dateEdited": "2020-05-12T01:20:31.4160882",
    "creatorID": 1,
    "editorID": 1,
    "projectID": 1,
    "assigneeID": 1
}
```

##### **Get all tasks**

- *Request*
```bash
GET localhost:8080/projects/1/tasks HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Cache-Control: no-cache
Postman-Token: Bearer eyJhbGciOiJIUzI1NiJ9
                     .eyJpZCI6Nywic3ViIjoiYWRtaW4iLCJhZG1pbiI6dHJ1ZSwiZXhwIjoxNTg4ODUxNzU0fQ
                     .OauJeEda5iltzv6g2Y5gvefHGPK-PO2gy9dlUfqfgmc
```
- *Response*
```bash
[
    {
        "id": 1,
        "title": "My First Task",
        "description": "project about rest api",
        "status": true,
        "dateCreated": "2020-05-12T01:20:31.416088",
        "dateEdited": "2020-05-12T01:20:31.416088",
        "creatorID": 1,
        "editorID": 1,
        "projectID": 1,
        "assigneeID": 1
    }
]
```

##### **Edit task**

- *Request*
```bash
PUT localhost:8080/projects/1/tasks/1 HTTP/1.1
POST /teams HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Cache-Control: no-cache
Postman-Token: Bearer eyJhbGciOiJIUzI1NiJ9
                     .eyJpZCI6Nywic3ViIjoiYWRtaW4iLCJhZG1pbiI6dHJ1ZSwiZXhwIjoxNTg4ODUxNzU0fQ
                     .OauJeEda5iltzv6g2Y5gvefHGPK-PO2gy9dlUfqfgmc

{
    "title": "My_First_Task",
    "description": "Project About Rest API",
    "status": true,
    "dateCreated": "2020-05-12T01:20:31.416088",
    "dateEdited": "2020-05-12T01:26:16.5035945",
    "creatorID": 1,
    "editorID": 1,
    "projectID": 1,
    "assigneeID": 1
}
```
- *Response*
```bash
{
    "id": 1,
    "title": "My_First_Task",
    "description": "Project About Rest API",
    "status": true,
    "dateCreated": "2020-05-12T01:20:31.416088",
    "dateEdited": "2020-05-12T01:26:16.5035945",
    "creatorID": 1,
    "editorID": 1,
    "projectID": 1,
    "assigneeID": 1
}
```

##### **Get task by id**

- *Request*
```bash
GET localhost:8080/projects/1/tasks/1 HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Cache-Control: no-cache
Postman-Token: Bearer eyJhbGciOiJIUzI1NiJ9
                     .eyJpZCI6Nywic3ViIjoiYWRtaW4iLCJhZG1pbiI6dHJ1ZSwiZXhwIjoxNTg4ODUxNzU0fQ
                     .OauJeEda5iltzv6g2Y5gvefHGPK-PO2gy9dlUfqfgmc
```
- *Response*
```bash
{
    "id": 1,
    "title": "My_First_Task",
    "description": "Project About Rest API",
    "status": true,
    "dateCreated": "2020-05-12T01:20:31.416088",
    "dateEdited": "2020-05-12T01:26:16.503595",
    "creatorID": 1,
    "editorID": 1,
    "projectID": 1,
    "assigneeID": 1
}
```

##### **Delete task by id**
```
DELETE localhost:8080/projects/1/tasks/1 HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Cache-Control: no-cache
Postman-Token: Bearer eyJhbGciOiJIUzI1NiJ9
                     .eyJpZCI6Nywic3ViIjoiYWRtaW4iLCJhZG1pbiI6dHJ1ZSwiZXhwIjoxNTg4ODUxNzU0fQ
                     .OauJeEda5iltzv6g2Y5gvefHGPK-PO2gy9dlUfqfgmc
```

## Worklogs
[Spring API](#spring-boot-rest-api)

##### **Create worklog**

- *Request*
```bash
POST localhost:8080/projects/1/tasks/1/worklogs HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Cache-Control: no-cache
Postman-Token: Bearer eyJhbGciOiJIUzI1NiJ9
                     .eyJpZCI6Nywic3ViIjoiYWRtaW4iLCJhZG1pbiI6dHJ1ZSwiZXhwIjoxNTg4ODUxNzU0fQ
                     .OauJeEda5iltzv6g2Y5gvefHGPK-PO2gy9dlUfqfgmc

{
    "id": 1,
    "taskID": 1,
    "userID": 1,
    "hoursSpent": 4,
    "dateWorked": "2020-04-05"
}
```
- *Response*
```bash
{
    "id": 1,
    "taskID": 1,
    "userID": 1,
    "hoursSpent": 4,
    "dateWorked": "2020-04-05"
}
```

##### **Get all worklogs**

- *Request*
```bash
GET localhost:8080/projects/1/tasks/1/worklogs HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Cache-Control: no-cache
Postman-Token: Bearer eyJhbGciOiJIUzI1NiJ9
                     .eyJpZCI6Nywic3ViIjoiYWRtaW4iLCJhZG1pbiI6dHJ1ZSwiZXhwIjoxNTg4ODUxNzU0fQ
                     .OauJeEda5iltzv6g2Y5gvefHGPK-PO2gy9dlUfqfgmc
```
- *Response*
```bash
[
    {
        "id": 1,
        "taskID": 1,
        "userID": 1,
        "hoursSpent": 4,
        "dateWorked": "2020-04-05"
    }
]
```

##### **Edit worklog**

- *Request*
```bash
PUT localhost:8080/projects/1/tasks/1/worklogs/1 HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Cache-Control: no-cache
Postman-Token: Bearer eyJhbGciOiJIUzI1NiJ9
                     .eyJpZCI6Nywic3ViIjoiYWRtaW4iLCJhZG1pbiI6dHJ1ZSwiZXhwIjoxNTg4ODUxNzU0fQ
                     .OauJeEda5iltzv6g2Y5gvefHGPK-PO2gy9dlUfqfgmc

{
	"taskId": 1,
	"userId": 1,
	"hoursSpent": 8,
	"dateWorked": "2020-04-05"
}
```
- *Response*
```bash
{
	"taskId": 1,
	"userId": 1,
	"hoursSpent": 8,
	"dateWorked": "2020-04-05"
}
```

##### **Get worklog by id**

- *Request*
```bash
GET localhost:8080/projects/1/tasks/1/worklogs/1 HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Cache-Control: no-cache
Postman-Token: Bearer eyJhbGciOiJIUzI1NiJ9
                     .eyJpZCI6Nywic3ViIjoiYWRtaW4iLCJhZG1pbiI6dHJ1ZSwiZXhwIjoxNTg4ODUxNzU0fQ
                     .OauJeEda5iltzv6g2Y5gvefHGPK-PO2gy9dlUfqfgmc
```
- *Response*
```bash
{
	"taskId": 1,
	"userId": 1,
	"hoursSpent": 8,
	"dateWorked": "2020-04-05"
}
```

##### **Delete worklog by id**
```
DELETE localhost:8080/projects/1/tasks/worklogs/1 HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Cache-Control: no-cache
Postman-Token: Bearer eyJhbGciOiJIUzI1NiJ9
                     .eyJpZCI6Nywic3ViIjoiYWRtaW4iLCJhZG1pbiI6dHJ1ZSwiZXhwIjoxNTg4ODUxNzU0fQ
                     .OauJeEda5iltzv6g2Y5gvefHGPK-PO2gy9dlUfqfgmc
```