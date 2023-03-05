# Spring JDBC Library for Bookstore
This project is a Java application that uses Spring JDBC and a relational database to implement a simple bookstore. The project provides tables for authors, books, and genres and supports many-to-one and many-to-many relationships between them. The interface is executed on Spring Shell, and it includes CRUD operations for books and operations for authors and genres as well.

## Prerequisites
Before running the application, you will need the following:
* Java 8 or higher
* Maven
* An instance of a relational database (either H2 or a real relational database)

## Installation
To install and run the application, follow these steps:
* Clone the repository to your local machine.
* Navigate to the project's root directory.
* Run mvn clean install to compile the project and create the JAR file.
* Run java -jar target/bookstore-0.0.1-SNAPSHOT.jar to start the application.

## Usage
Once the application is running, you can interact with it using Spring Shell. The following commands are available:
* book add: Adds a new book to the database.
* book update: Updates an existing book in the database.
* book delete: Deletes a book from the database.
* book find: Finds a book by ID or title.
* author add: Adds a new author to the database.
* author update: Updates an existing author in the database.
* author delete: Deletes an author from the database.
* author find: Finds an author by ID or name.
* genre add: Adds a new genre to the database.
* genre update: Updates an existing genre in the database.
* genre delete: Deletes a genre from the database.
* genre find: Finds a genre by ID or name.

## Database Scripts
The project includes scripts for creating the database tables and populating them with sample data. These scripts are executed automatically when the application starts up using Spring Boot's spring-boot-starter-jdbc library.

## Testing
The project includes tests for all DAO methods and the book service. The tests use JUnit and Mockito and do not use inheritance. To run the tests, execute the following command from the project's root directory:
```shell
mvn test
```

## License
This project is licensed under the MIT License. See the LICENSE file for details.

## Conclusion
This project demonstrates how to use Spring JDBC and a relational database to implement a simple bookstore with tables for authors, books, and genres. By using Spring Shell, users can easily interact with the application and perform CRUD operations on books, authors, and genres. The project also includes tests for all DAO methods and the book service, making it easy to verify the correctness of the application's functionality.
