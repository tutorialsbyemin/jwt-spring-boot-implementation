# JWT Spring Boot implementation

### Step 1
#### Add following dependencies:
* [Spring Boot Starter Web](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web)
* [H2 database ](https://mvnrepository.com/artifact/com.h2database/h2/)
* [Spring Boot Starter Data JPA](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-jpa)
* [Json Web Token](https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt/)
* [Lombok](https://mvnrepository.com/artifact/org.projectlombok/lombok)
* [Spring Boot Starter Security](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-security)

<br/>


### Step 2
#### Add entity package and create following entities:
* DbUser
* DbRole

##### Some explanations
These classes are considered to create corresponding tables in the database.

<br/>


### Step 3
#### Add dto package and create following DTOs:
* DbUserDto
* DbRoleDto
* LoginRequestDto
* ResponseDto

##### Some explanations
DTO (data transfer object) is an object that carries data between processes. 
These DTOs are considered to carry data from server to client and vice versa.
They consume data from corresponding entity and send it to client.
It is not good practise to expose all fields of entity to a client. 
So DTO helps to specify which fields are intended to show.


<br/>


### Step 4
#### Add repository package and create following repositories:
* DbUserRepository
* DbRoleRepository

##### Some explanations
These interfaces extend JpaRepository interface provided by JPA that creates some useful methods behind the scene.
Those methods make it easier to create database queries.
For further information visit [documentation](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories).

######Example:
*findByUsername(String username)* method in UserRepository returns a user which has given username.


<br/>


### Step 5
#### Add service package and create following service:
* DbUserService

##### Note:
DbRoleService service has not been created in current project because there is nothing to do with it.
It is good practise to create a service for each entity in project.

##### Some explanations
This class provides functionality that is needed for main purpose. 
It may use repository methods and add some other features (validation, mapping, filtering, etc.) as well.
Currently, it uses methods from corresponding repository and maps (converts) return value to *DbUserDto*.


<br/>


### Step 6
#### Add and configure application.properties (.yaml) file
##### Some explanations
* **spring.h2.console.enabled** - enable/disable [h2 database console](http://localhost:8080/h2console) to track database changes with the help of user-friendly UI.
* **spring.datasource.username** - specifies username for *h2 database*
* **spring.datasource.password** - specifies password for *h2 database*
* **spring.datasource.url** - specifies url for *h2 database*


<br/>


### Step 7
#### Add and configure jwt.properties (.yaml) file
##### Some explanations
* **jwt.expiry.default=86400000** - keeps default expiration time for Jwt (A day).
* **jwt.expiry.remember=604800000** - keeps expiration time for using if *rememberMe* property is true for Jwt (A week).


<br/>


### Step 8
#### Create initial data for testing
**CommandLineRunner** bean is used to create initial data and save it in the database.
It is an interface used to indicate that a bean should run when it is contained within a SpringApplication.

##### Some explanations
* **encoder** - is an object of *BCryptPasswordEncoder* class which is used encode user password before saving it in database. 
###### Sequence:
* 3 users are created with username and password
* 2 roles are created with name
* Users and roles are saved in database
* Sets of roles are created to keep roles for each user
* roles are added to corresponding *Sets*
* Set of roles are assigned to coresponding users
* Users are saved in the database with refreshed data (added roles).


<br/>


###### Annotations:
* **@NoArgsConstructor** - Adds default constructor without any fields (from Lombok dependency)
* **@AllArgsConstructor** - Adds constructor with all fields (from Lombok dependency)
* **@Getter** - Adds getter for each field (from Lombok dependency)
* **@Setter** - Adds setter for each field (from Lombok dependency)
* **@Entity** - Makes db table according to corresponding class
* **@Id** - Makes corresponding field primary key of table
* **@GeneratedValue** - Specifies that a value will be automatically generated for that field
* **@Column** - Specifies mapped column for a corresponding field
* **@ManyToMany** - Specifies relation between corresponding field of current table and referenced table
* **@JoinTable** - Configures table specifications of relation
* **@JoinColumn** - Configures column specifications of relation
* **@Service** - Defines that corresponding class is considered to provide business functionality.
* **@Bean** - Creates a bean for corresponding class


<br/>

