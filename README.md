# shorty-back - URL Shortening Service

shorty-back is a backend service that provides URL shortening capabilities. It uses Spring Boot for the backend and Redis as the data store. The service offers creating custom short URLs, retrieving original URLs using the short version, updating URLs, and deleting them.

## Features:

- **Shorten URLs:** Create a custom or randomized shortened URL.
- **Retrieve Original URLs:** Using the shortened URL, retrieve the original one.
- **Update URLs:** Change the original URL associated with a shortened one.
- **Delete URLs:** Remove a shortened URL and its associated original URL from the system.

## System Requirements:

- JDK 11 or above
- Maven
- Docker (for Redis setup)
- Redis

## Setting up Redis with Docker:

To run this service, you'll need Redis. The easiest way to get Redis up and running is using Docker.

### Pre-requisites:
1. Docker installed on your machine. If you haven't installed Docker, you can find the installation instructions for various platforms on the [official Docker documentation](https://docs.docker.com/get-docker/).

### Steps to Run Redis in Docker:

1. **Pull the Redis Image from Docker Hub**  
   Fetch the official Redis image.
   ```bash
   docker pull redis
   ```

2. **Start a Redis Instance**  
   Run a Redis instance named `shorty-back-redis`. The default port `6379` will be exposed to the host.
   ```bash
   docker run --name shorty-back-redis -p 6379:6379 -d redis
   ```

3. **Check if Redis is Running**  
   Ensure `shorty-back-redis` is in the list of running containers.
   ```bash
   docker ps
   ```

## Getting Started:

1. **Clone the Repository**
   ```bash
   git clone https://github.com/mattempyre/shorty-back.git
   cd shorty-back
   ```

2. **Build the Project with Maven**
   ```bash
   mvn clean install
   ```

3. **Run the Service**
   ```bash
   java -jar target/shorty-back-0.0.1-SNAPSHOT.jar
   ```

Your service should now be running on `http://localhost:9000/`.

## Testing the Service:

You can use tools like `curl`, `Postman`, or `Insomnia` to test the endpoints. A basic guide for using Insomnia:

- To **create a shortened URL**: Make a `POST` request to `http://localhost:9000/api/url/create` with JSON payload `{ "longUrl": "https://example.com", "customShortUrl": "myShortUrl" }`.
  
- To **retrieve the original URL** using a shortened URL: Make a `GET` request to `http://localhost:9000/myShortUrl`.

And so on for other operations.