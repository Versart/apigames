# apigames
API RESTful utilizando Spring Boot que gerencia produtoras de games.
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
* [Prometheus](https://prometheus.io/)
* [Jib](https://github.com/GoogleContainerTools/jib)
* [GitHub Actions](https://github.com/features/actions)
* [Grafana](https://grafana.com/)
* [Modelmapper](http://modelmapper.org/)
* [MySQL](https://www.mysql.com/)

## Como executar
1. Clonar o repositório:
```
git clone https://github.com/Versart/apigames
```
2. Entrar no diretório clonado e executar o comando:
```
docker compose up
```
3. A aplicação poderá ser acessada em [localhost:8080](http://localhost:8080/swagger-ui.html)
## API Endpoints
Para fazer requisições, é necessário primeiramente se registrar, e depois fazer login para receber um token, este deve ser enviado em todas requisições.
Para maiores informações dos endpoints, você poderá acessar a documentação em: [localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
### Authentication
- POST /auth/register
```
Example Request
{
  "login": "maria",
  "password": "12345678",
  "role": "ADMIN"
}
```
```
Example Response
{
  "login": "maria",
  "dataCriacao": "2023-09-04T23:08:06.982Z"
}
```
- POST /auth/login
```
Example Request
{
  "login": "maria",
  "password": "12345678"
}
```
```
Example Response
eyJhbGciOiJIUzI1NiIsInR5cCI.............................................
```
### Company 
- POST /companies
```
  Example Request
  {
    "name" : "Nintendo",
    "dateOfFoundation" : "1889-09-23"
  }
```
```
 Example Response
 {
    "id": 1,
    "name": "Nintendo",
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
          "name": "Nintendo",
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
    "name": "Nintendo",
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
        "name": "Nintendo",
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
  "name": "Nintendo 2",
  "dateOfFoundation": "1889-09-23"
}

```
```
Example Response
{
  "id": 1,
  "name": "Nintendo 2",
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
### Game
- POST /games
```
Example Request
{
  "name": "Contra ",
  "category": "ACTION",
  "companyId": 2
}
```
```
Example Response
{
  "name": "Contra ",
  "companyName": "Nintendo",
  "_links": {
    "self": {
      "href": "http://localhost:8080/games/1"
    },
    "All Games": {
      "href": "http://localhost:8080/games"
    }
  }
}
```
- GET /games
```
Example Request
/games
```
```
{
  "_embedded": {
    "gameResponseDTOList": [
      {
        "name": "Contra ",
        "companyName": "Nintendo",
        "_links": {
          "self": {
            "href": "http://localhost:8080/games/1"
          },
          "All Games": {
            "href": "http://localhost:8080/games"
          }
        }
      }
    ]
  },
  "_links": {
    "self": {
      "href": "http://localhost:8080/games"
    }
  }
}
```
- GET /games/{id}
```
Example Request
/games/1
```
```
Example Response
{
  "name": "Contra ",
  "companyName": "Nintendo",
  "_links": {
    "self": {
      "href": "http://localhost:8080/games/1"
    },
    "All Games": {
      "href": "http://localhost:8080/games"
    }
  }
}
```
- PUT /games/{id}
```
Example Request
/companies/1
{
  "name": "Contra 2",
  "category": "ACTION",
  "companyId": 2
}
```
```
{
  "name": "Contra 2",
  "companyName": "Nintendo",
  "_links": {
    "self": {
      "href": "http://localhost:8080/games/1"
    },
    "All Games": {
      "href": "http://localhost:8080/games"
    }
  }
}
```
- DELETE /games/{id}
```
Example Request
/games/1
```

