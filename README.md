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
* [Spring HATEOAS](https://spring.io/projects/spring-hateoas)
* [Spring Security](https://spring.io/projects/spring-security)

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
    "id": 1,
    "name": "Nintendoow",
    "dateOfFoundation": "1889-09-23",
    "_links": {
      "self": {
        "href": "http://localhost:8080/companies/1"
      }
    }
 }
```
- GET /companies
```
Example Response
{
    "_embedded": {
      "companyResponseList": [
        {
          "id": 1,
          "name": "Nintendoow",
          "dateOfFoundation": "1889-09-23",
          "_links": {
            "self": {
              "href": "http://localhost:8080/companies/1"
            }
          }
        }
      ]
    },
    "_links": {
      "self": {
        "href": "http://localhost:8080/companies?page=0&size=20"
      }
    },
    "page": {
      "size": 20,
      "totalElements": 1,
      "totalPages": 1,
      "number": 0
    }
}
```

- GET /companies/{id}
```
Example Request
/companies/1
```
```
Example Response
{
    "id": 1,
    "name": "Nintendoow",
    "dateOfFoundation": "1889-09-23",
    "_links": {
      "self": {
        "href": "http://localhost:8080/companies/1"
      }
    }
}

```
- GET /companies/find?name=?
```
Example Request
/companies/find?name=nint
```
```
Example Response
{
  "_embedded": {
    "companyResponseList": [
      {
        "id": 1,
        "name": "Nintendoow",
        "dateOfFoundation": "1889-09-23",
        "_links": {
          "self": {
            "href": "http://localhost:8080/companies/1"
          }
        }
      }
    ]
  }
}  
```
- PUT /companies/{id}
```
Example Request
/companies/1
{
  "name": "Nintendoow 2",
  "dateOfFoundation": "1889-09-23"
}

```
```
Example Response
{
  "id": 1,
  "name": "Nintendoow 2",
  "dateOfFoundation": "1889-09-23",
  "_links": {
    "self": {
      "href": "http://localhost:8080/companies/1"
    }
  }
}
```
- DELETE /companies/{id}
```
Example Request
/companies/1
```
