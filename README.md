# Shorty-Back

Shorty-Back is a URL-shortening service that allows you to create and manage short URLs for your long web addresses. This service is built with Spring Boot and uses Redis as a data store.

For the front-end code and additional details [Shorty Frontend Repository](https://github.com/mattempyre/shorty-front)

## Table of Contents

- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Running the Service](#running-the-service)
  - [Running Redis in Docker](#running-redis-in-docker)
- [Usage](#usage)
  - [Creating a Short URL](#creating-a-short-url)
  - [Accessing a Shortened URL](#accessing-a-shortened-url)
  - [Updating a URL](#updating-a-url)
  - [Deleting a URL](#deleting-a-url)
  - [Listing All Shortened URLs](#listing-all-shortened-urls)
- [Testing](#testing)

- [Api Documentation](#api)
## Getting Started

### Prerequisites

Before you can run the Shorty-Back service, make sure you have the following prerequisites installed:

- [Java Development Kit (JDK)](https://openjdk.java.net/)
- [Maven](https://maven.apache.org/)

### Running the Service

1. Clone the repository to your local machine:

   ```bash
   git clone https://github.com/mattempyre/Shorty-Back.git
   ```

2. Navigate to the project directory:

   ```bash
   cd Shorty-Back
   ```

3. Build the project using Maven:

   ```bash
   mvn clean install
   ```

4. Run the service:

   ```bash
   mvn spring-boot:run
   ```

The Shorty-Back service should now be running on `http://localhost:9000`. You can access the service's API endpoints using tools like [Insomnia](https://insomnia.rest/) or [Postman](https://www.postman.com/).


## Run a Redis Server in Docker

To set up and run a Redis server locally with Docker, follow these steps:

1. **Install Docker:** If you haven't already, download and install Docker for your specific operating system. You can find the installation instructions for various platforms on the [Docker website](https://docs.docker.com/get-docker/).

2. **Open a Terminal (Command Prompt):** Open a terminal or command prompt on your local machine.

3. **Pull the Redis Docker Image:**

   Run the following command to pull the official Redis Docker image from Docker Hub:

   ```bash
   docker pull redis
   ```

   This command will download the latest Redis image to your local machine.

4. **Run a Redis Container:**

   Once the Redis image is downloaded, you can start a Redis container by running the following command:

   ```bash
   docker run -d --name shorty-redis -p 6379:6379 redis
   ```

   - `-d`: This flag runs the container in detached mode, which means it runs in the background.
   - `-p 6379:6379`: This flag maps port 6379 inside the container to port 6379 on your local machine. This is the default Redis port.
   - `--name redis-server`: This assigns the name "redis-server" to the running container.
   - `redis`: This is the name of the Docker image we pulled earlier.

5. **Verify Redis Container Status:**

   You can check if the Redis container is running by executing:

   ```bash
   docker ps
   ```

   This command lists all the running containers. You should see the "redis-server" container in the list.

6. **Access the Redis Server:**

   You can access the Redis server using a Redis client or by running commands inside the container. To access it using a Redis client, you can install the Redis CLI tool on your local machine.

   - **Install Redis CLI on Linux/Mac:**

     ```bash
     sudo apt-get install redis-tools   # On Ubuntu/Debian
     ```

     -OR-

     ```bash
     brew install redis                # On macOS with Homebrew
     ```

   - **Install Redis CLI on Windows:**

     You can download the Windows version of the Redis CLI from the [official GitHub repository](https://github.com/microsoftarchive/redis/releases).

7. **Connect to the Redis Server:**

   You can connect to the Redis server using the Redis CLI with the following command:

   ```bash
   redis-cli
   ```


## Usage

### Creating a Short URL

To create a short URL, send a POST request to the `/api/url/create` endpoint with the long URL you want to shorten. You can also provide a custom short URL as an option.

Example:

```http
POST http://localhost:9000/api/url/create
Content-Type: application/json

{
  "longUrl": "https://example.com",
  "customShortUrl": "custom123"
}
```

### Accessing a Shortened URL

You can access a shortened URL by entering it in your web browser or making a GET request to `http://localhost:9000/shortUrl`, where "shortUrl" is the generated or custom short URL.

Example:

```http
GET http://localhost:9000/custom123
```

### Updating a URL

To update a URL, send a PUT request to the `/api/url/update` endpoint with the short URL and the new long URL.

Example:

```http
PUT http://localhost:9000/api/url/update
Content-Type: application/json

{
  "shortUrl": "custom123",
  "newLongUrl": "https://updated-example.com"
}
```

### Deleting a URL

To delete a URL, send a DELETE request to the `/api/url/delete/{shortUrl}` endpoint, where "shortUrl" is the short URL you want to delete.

Example:

```http
DELETE http://localhost:9000/api/url/delete/custom123
```

### Listing All Shortened URLs

You can retrieve a list of all shortened URLs by making a GET request to the `/api/url/all` endpoint.

Example:

```http
GET http://localhost:9000/api/url/all
```

## Testing

You can test the Shorty-Back service using tools like [Insomnia](https://insomnia.rest/) or [Postman](https://www.postman.com/). Here are some test scenarios to try:

Certainly, here are some example testing commands for Insomnia and Postman that you can use to test your Shorty-Back service:

### Create a Short URL
#### Request:
- **Method:** POST
- **URL:** `http://localhost:9000/api/url/create`
- **Headers:** `Content-Type: application/json`
- **Body:** JSON
    ```json
    {
        "longUrl": "https://www.example.com",
        "customShortUrl": "custom123" // Optional: You can provide a custom short URL
    }
    ```
#### Expected Response:
- **Status Code:** 200 OK
- **Body:**
    ```json
    {
        "shortUrl": "custom123" // The short URL you provided or generated
    }
    ```

### Get Long URL from Short URL
#### Request:
- **Method:** GET
- **URL:** `http://localhost:9000/custom123` (Replace `custom123` with the actual short URL)
#### Expected Response:
- **Status Code:** 200 OK
- **Body:**
    ```json
    {
        "redirectUrl": "https://www.example.com" // The long URL associated with the short URL
    }
    ```

### Update a Short URL
#### Request:
- **Method:** PUT
- **URL:** `http://localhost:9000/api/url/update`
- **Headers:** `Content-Type: application/json`
- **Body:** JSON
    ```json
    {
        "shortUrl": "custom123", // The short URL you want to update
        "newLongUrl": "https://www.newexample.com" // The new long URL
    }
    ```
#### Expected Response:
- **Status Code:** 200 OK
- **Body:**
    ```json
    {
        "message": "URL updated successfully."
    }
    ```

### Delete a Short URL
#### Request:
- **Method:** DELETE
- **URL:** `http://localhost:9000/api/url/delete/custom123` (Replace `custom123` with the actual short URL you want to delete)
#### Expected Response:
- **Status Code:** 200 OK
- **Body:**
    ```json
    {
        "message": "URL with short URL: custom123 has been deleted."
    }
    ```

### Get a List of All Shortened URLs
#### Request:
- **Method:** GET
- **URL:** `http://localhost:9000/api/url/all`
#### Expected Response:
- **Status Code:** 200 OK
- **Body:** A list of all shortened URLs in JSON format.on.

## API
# API Documentation for ShortyBack

ShortyBack is a URL shortening service that allows users to create short URLs for long web addresses. This API documentation provides details on how to use the ShortyBack API for various URL shortening operations.

## Table of Contents
- [API Endpoints](#api-endpoints)
  - [Create a Short URL](#create-a-short-url)
  - [Retrieve the Original URL](#retrieve-the-original-url)
  - [Increment Click Count](#increment-click-count)
  - [Update a URL](#update-a-url)
  - [Delete a URL](#delete-a-url)
  - [Get All URLs with Click Counts](#get-all-urls-with-click-counts)
- [Global Exception Handling](#global-exception-handling)
- [Data Models](#data-models)

## Introduction

ShortyBack is a Spring Boot-based URL shortening service that utilizes Redis as its data store. It offers the following functionality through its API:

- Create a short URL for a given long URL.
- Retrieve the original long URL associated with a short URL.
- Increment the click count of a short URL.
- Update the long URL associated with a short URL.
- Delete a short URL and its associated mapping.
- Get a list of all URLs with their associated click counts.

## API Endpoints

### Create a Short URL

**Endpoint:** `/api/url/create`

**HTTP Method:** `POST`

**Request Body:**
```json
{
  "longUrl": "https://example.com",
  "customShortUrl": "custom"
}
```

- `longUrl` (string, required): The original long URL.
- `customShortUrl` (string, optional): A custom short URL. If not provided, a random short URL will be generated.

**Response:**
```json
{
  "shortUrl": "custom",
  "longUrl": "https://example.com",
  "clickCount": 0,
  "message": "Short URL created successfully."
}
```

- `shortUrl` (string): The short URL generated or provided.
- `longUrl` (string): The original long URL.
- `clickCount` (integer): The initial click count (0).
- `message` (string): A success message.

**Error Response:**
```json
{
  "error": "Short URL already exists. Please choose another"
}
```

- `error` (string): Error message indicating that the custom short URL already exists.

### Retrieve the Original URL

**Endpoint:** `GET /{shortUrl}`

- `{shortUrl}` (string, required): The short URL to lookup and redirect to its corresponding long URL.

**Response:** The API returns an HTTP 302 Found response, redirecting the client to the original long URL associated with the short URL. The click count for the short URL is also incremented.

**Error Response:**

- If the short URL isn't found in the system, the API returns an HTTP 404 Not Found response.

### Increment Click Count

**Endpoint:** `/api/url/clickCount/{shortUrl}`

**HTTP Method:** `GET`

- `{shortUrl}` (string, required): The short URL for which you want to retrieve the click count.

**Response:**
```json
{
  "clickCount": 5
}
```

- `clickCount` (integer): The click count for the specified short URL.

**Error Response:**
```json
{
  "error": "Short URL not found."
}
```

- `error` (string): Error message indicating that the short URL was not found.

### Update a URL

**Endpoint:** `/api/url/update`

**HTTP Method:** `PUT`

**Request Body:**
```json
{
  "shortUrl": "custom",
  "newLongUrl": "https://newexample.com"
}
```

- `shortUrl` (string, required): The short URL to update.
- `newLongUrl` (string, required): The new long URL to associate with the short URL.

**Response:**
```json
{
  "message": "URL updated successfully."
}
```

- `message` (string): Success message indicating that the URL was updated successfully.

**Error Response:**
```json
{
  "error": "Short URL not found. Cannot update."
}
```

- `error` (string): Error message indicating that the short URL was not found.

### Delete a URL

**Endpoint:** `/api/url/delete/{shortUrl}`

**HTTP Method:** `DELETE`

- `{shortUrl}` (string, required): The short URL to delete.

**Response:**
```json
{
  "message": "URL with short URL: custom has been deleted."
}
```

- `message` (string): Success message indicating that the URL with the specified short URL has been deleted.

**Error Response:**
```json
{
  "error": "Short URL not found. Cannot delete."
}
```

- `error` (string): Error message indicating that the short URL was not found.

### Get All URLs with Click Counts

**Endpoint:** `/api/url/all`

**HTTP Method:** `GET`

**Response:**
```json
[
  {
    "longUrl": "https://example.com",
    "shortUrl": "custom",
    "clickCount": 5
  },
  {
    "longUrl": "https://anotherexample.com",
    "shortUrl": "another",
    "clickCount": 3
  }
]
```

- An array of objects containing:
  - `longUrl` (string): The original long URL.
  - `shortUrl` (string): The short URL.
  - `clickCount` (integer): The click count for the short URL.

## Global Exception Handling

The ShortyBack API employs global exception handling to provide consistent error responses. Two types of exceptions are handled:

1. `UrlException`: Custom exception for URL-related errors.
2. `IllegalArgumentException`: Exception for invalid input data.

Responses for these exceptions include error messages and HTTP status codes:

- `UrlException`: HTTP status code `400 Bad Request`.
- `IllegalArgumentException`: HTTP status code `400 Bad Request`.

## Data Models

### URL Entity

The `Url` entity represents a URL mapping and is stored in a Redis database. It contains the following attributes:

- `longUrl` (string): The original long URL.
- `shortUrl` (string): The shortened URL representation.
- `clickCount` (integer): The click count for the short URL.

### URL Request DTO

#### Create URL Request DTO

The `UrlRequest` DTO is used for creating short URLs and contains the following attributes:

- `longUrl` (string): The original long URL.
- `customShortUrl` (string, optional): A custom short URL. If not provided, a random short URL will be generated.

#### Update URL Request DTO

The `UrlUpdateRequest` DTO is used for updating the long URL associated with a short URL and contains the following attributes:

- `shortUrl` (string): The short URL to update.
- `newLongUrl` (string): The new long URL to associate with the short URL.

This documentation outlines the endpoints, request and response formats, and error handling for the ShortyBack API. Please refer to the corresponding sections for more information on

 how to use the API for URL shortening and management.
