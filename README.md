Exercise
-----
Design and implement POC of api for accounts and money exchanges PLN->USD , USD->PLN
 
 Run application
-----
From project directory: 

```bash
mvn spring-boot:run
```

Swagger
----

```bash
http://localhost:8080/swagger-ui.html
```
 
Main goals
-----
- Eventsourced Account domain model, non-anemic without setters and getters, and with business logic

Please note, instead of getters there are two methods providing state of Account object: 

```java
public AccountData state()    
public AccountNo accountNo()
```
- Promoting objects in domain layer
- Using “Currency and Money” as value object for money (https://martinfowler.com/eaaCatalog/money.html)
- Inspired by "Implementing Domain-Driven Design" (https://github.com/VaughnVernon/IDDD_Samples) 
- Application centric architecture, promoting separation of concerns with :
    - CQRS aware application layer - with application services responsible for modeling use cases
    - Domain layer - object oriented , non anemic (no setter/getters) with entities , events and value objects
    - Infrastructure layer  - partly implemented
    - Interfaces layer(web) - partly implemented
- Using Spring and SpringBoot
- Tests 


Please note 
----
- It's only POC. The goal of this project was present way of work with code and with daily programmer challenges
- Different approaches with name of tests are intentional
- Using double for transfer amount in Dto's is intentional, in production-ready solution BigDecimal should be used. 
  All Monetary calculation are done by javax.money.MonetaryAmount abstract concept
- Negative amount not handled
