# Shorty-Back

Shorty-Back is a URL shortening service that allows you to create and manage short URLs for your long web addresses. This service is built with Spring Boot and uses Redis as a data store.

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

### Running Redis in Docker

To use Redis as the data store for Shorty-Back, you can run it in a Docker container. If you don't have Docker installed, you can download it [here](https://www.docker.com/get-started).

To run Redis in a Docker container, execute the following command:

```bash
docker run -d --name shorty-redis -p 6379:6379 redis
```

This will start a Redis container named "shorty-redis" and expose it on port 6379.

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