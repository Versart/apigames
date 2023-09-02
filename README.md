# apigames
API Rest Utilizando Spring boot e o MYSQL

## Tecnologias utilizadas:
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/3.0.2/reference/htmlsingle/#using.devtools)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.0.2/reference/htmlsingle/#web)
* [Flyway](https://flywaydb.org/)
* [Validation](https://docs.spring.io/spring-boot/docs/3.0.2/reference/htmlsingle/#io.validation)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.0.2/reference/htmlsingle/#data.sql.jpa-and-spring-data)
* [Java JWT](https://github.com/auth0/java-jwt)
* [Docker](https://www.docker.com/)
* [Spring Doc OpenAPI 3](https://springdoc.org/)

## API Endpoints

### Company 
- POST /companies
```
  Example Request
  {
    "name" : "Nintendoow",
    "dateOfFoundation" : "1889-09-23"
  }
```
```
 Example Response
  {
    "id": 1
    "name" : "Nintendoow",
    "dateOfFoundation" : "1889-09-23"
  }

```
