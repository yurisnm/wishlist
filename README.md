# Wishlist Service
A simple wishlist service using Spring Boot and MongoDB.

The only 4 exitent features are:
### 1. Add product to wishlist (limited to 20)
e.g.:
```
curl --location --request POST 'localhost:8080/wishlist/add' \
--header 'Content-Type: application/json' \
--data-raw '{
"id":"1",
"name":"item 1"
}'
```
### 2. List all products on wishlist
```
curl --location --request GET 'localhost:8080/wishlist'
```
### 3. List specific product on wishlist
```
curl --location --request GET 'localhost:8080/wishlist/{id}'
```
### 4. Remove product from wishlist
```
curl --location --request DELETE 'localhost:8080/wishlist/remove/{id}'
```

## Prerequisites
Java 17 or newer

## Technologies Used
1. Spring Boot 2.6.3
2. Spring Data MongoDB 3.3.3
3. MongoDB Java Driver 3.12.7
3. JUnit 4.13.2
5. Rest-assured 4.4.0
6. Jackson JSON Parser 2.13.0

## Building the Project
To build the project, navigate to the root directory and run the following command:
```
mvn clean install
```
## Running the Application
To run the application, navigate to the root directory and run the following command:
```
mvn spring-boot:run
```

## Running the Tests
To run the tests, navigate to the root directory and run the following command:
```
mvn test
```
# Using DOCKER

## Generating image

Make sure you are in the root directory at the same place that your Dockerfile is.
```
docker build -t my-image-name .
```

## Running container
```
docker run -p 8080:8080 my-image-name
```

With this you are able to access the service on `http://localhost:8080`

# License
This project is licensed under the MIT License.