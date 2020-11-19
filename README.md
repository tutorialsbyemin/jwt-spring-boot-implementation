# JWT Spring Boot implementation

***If you are not familiar with JWT, I would highly recommend you to visit [Introduction to JWT](https://jwt.io/introduction/) page.***
***This project is intended to IMPLEMENT JWT into Spring Boot Application.***


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

These classes are intended to create corresponding tables in the database.


<br/>


### Step 3
#### Add dto package and create following DTOs:
* DbUserDto
* DbRoleDto
* LoginRequestDto
* ResponseDto

DTO (data transfer object) is an object that carries data between processes. 
These DTOs are intended to carry data from server to client and vice versa.
They consume data from corresponding entity and send it to client.
It is not good practise to expose all fields of entity to a client. 
So DTO helps to specify which fields are supposed to show.


<br/>


### Step 4
#### Add repository package and create following repositories:
* DbUserRepository
* DbRoleRepository

These interfaces extend JpaRepository interface provided by JPA that creates some useful methods behind the scene.
Those methods make it easier to create SQL queries.
For further information visit [documentation](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories).

###### Example:
*findByUsername(String username)* method in UserRepository returns a user which has given username.


<br/>


### Step 5
#### Add service package and create following service:
* DbUserService

This class provides functionality that is needed for main purpose. 
It may use repository methods and add some other features (validation, mapping, filtering, etc.) as well.
Currently, it uses methods from corresponding repository and maps (converts) return value to *DbUserDto*.

##### Note:
DbRoleService service has not been created in current project because there is nothing to do with it.
It is good practise to create a service for each entity in project.



<br/>


### Step 6
#### Add and configure application.properties (.yaml) file
* **spring.h2.console.enabled** - enable/disable [h2 database console](http://localhost:8080/h2console) to track database changes with the help of user-friendly UI.
* **spring.datasource.username** - specifies username for *h2 database*
* **spring.datasource.password** - specifies password for *h2 database*
* **spring.datasource.url** - specifies url for *h2 database*


<br/>


### Step 7
#### Add and configure jwt.properties (.yaml) file
##### Some explanations
* **jwt.expiry.default** - stores default expiration time for Jwt (A day)
* **jwt.expiry.remember** - stores expiration time for usage if *rememberMe* property is true for Jwt


<br/>


### Step 8
#### Create initial data for testing
**CommandLineRunner** bean is used to create initial data and save it into the database.
It is an interface used to indicate that a bean should run when it is contained within a SpringApplication.

##### Instance
* **encoder** - is an object of *BCryptPasswordEncoder* class which is used encode user password before saving it in database. 
###### Sequence:
* 3 users are created with username and password
* 2 roles are created with name
* Users and roles are saved in database
* Sets of roles are created to store roles for each user
* roles are added to corresponding *Sets*
* Set of roles are assigned to coresponding users
* Users are saved in the database with refreshed data (added roles).


<br/>


### Step 9
#### Add security package that contains all security-related classes


<br/>

### Step 10
#### Create Const class
This class stores constants which are used in different places.


<br/>

### Step 11
#### Create MUserDetails class
This class is intended to be specific *UserDetails* object for current project (**M** stands for modified, and it is up to you how to name this class).
It implements UserDetails interface demonstrated by *Spring Security*.
UserDetails is a specific interface storing user information which is later encapsulated into *Authentication* objects.
We may store any fields of *DbUser* entity here but minimum requirment is to have *username*, *password* and *roles*.
Roles field must be collection of any object that extends from *GrantedAuthority* interface.
*UserDetails* interface has some methods to implement. They should return corresponding values according to our *MUserDetails*.

##### Note
*Name* fields of roles of *DbUser* entity are fetched and mapped to GrantedAuthority for being able to be stored in *MUserDetails* object at initialization.


<br/>


### Step 12
#### Create MUserDetailsService class
This class is intended to be specific *UserDetailsService* object for current project (**M** stands for modified, and it is up to you how to name this class).
It implements UserDetailsService interface demonstrated by *Spring Security*.
This interface is used to retrieve user-related data from database.
[Learn more](https://www.baeldung.com/spring-security-authentication-with-a-database)

##### Methods
* **map** - Accepts user data from the database as a parameter *(DbUserDto)* and creates *MUserDetails* object based on this data.
* **loadByUsername** - Comes from *UserDetailsService* and is used to fetch user from the database with given username and create UserDetails (with the help of *map* method). If there is no user with given username in the database:
    * Message is logged about it
    * UsernameNotFoundException is thrown 
* **loadUserById** - Is similar to *loadUserByUsername* but fetches user by its id instead of username.


<br/>


### Step 13
#### Create JwtService class
##### Fields
* **secret** - stores secret key which is used to generate token
* **expiryDefault** - stores default expiry date which is used for generating token (One day here)
* **expiryRememberMe** - stores alternative expiry date for generating token (One week here) 

##### Methods
* **generateToken** 
    * Accepts *userId* and *rememberMe* values as a parameter from client
    * Creates variable which stores current time value *(now)*
    * Creates variable which stores expiration time according to value of *rememberMe* parameter *(delta)*

    * Builds JWT
    * Sets subject of JWT *(userId)*
    * Sets creation time of JWT *(now)*
    * Sets expiration of JWT *(now + expiration time)*
    * Signs the constructed JWT using the specified algorithm with the specified key *(secret)*
    * Serializes JWT to a compact
    
    
* **extractToken**
    * Accepts HttpServletRequest as a parameter
    * Gets header with predefined name *(Const.HEADER)* from request
    * Filters this header to be sure it starts with predefined prefix *(Const.PREFIX)*
    * Removes prefix to get actual token
    
* **parseTokenToClaims**
    * Accepts token (JWT) as a parameter
    * Tries to parse this token and get Claims instance from it
    * Logs actual messages for different invalid cases
    
 * **getSubjectFromClaims**
    * extracts subject from Claims instance of provided token (JWT)
    
    
<br/>


### Step 14
#### Create JwtFilter class
This class is intended to be filter for *Spring Security*.
It extends *OncePerRequestFilter* class in order to be an implementable filter.

##### Fields
* JwtService
* MUserDetailsService

##### Method
* **doInternalFilter** - Is demonstrated by OncePerRequestFilter class and used to authorize user with provided JWT. 
    * Accepts *HttpServletRequest, HttpServletResponse and FilterChain* as parameters.
    * Extracts token from request *(using extractToken method of JwtService)*
    * Parses token to Claims instance *(using parseTokenToClaims method of JwtService)*
    * Extracts subject from Claims instance *(using getSubjectFromClaims method of JwtService)*
    * Parses subject (userId here) to long
    * Finds user by id *(using loadUserById method of MUserDetailsService)*
    * Creates *UsernamePasswordAuthenticationToken* with details of found user.
    * Does filtering process
    * Logs exception message if something goes wrong
<br/>

### Step 15
#### Create SecurityConfiguration class
This class is intended to be Configuration for Spring Security.
It extends *WebSecurityConfigurerAdapter* class in order to be considered as a configuration.

##### Field
* JwtFilter


##### Methods
* **configure** - Is demonstrated by WebSecurityConfigurerAdapter class and used to configure security as needed. 
    * Accepts HttpSecurity as a parameter
    * disables csrf  ([What is CSRF?](https://portswigger.net/web-security/csrf))
    * tells *Spring Security* not to create or use any session
    * configures access to different URLs (*permitAll()* means all users can access, *authenticated()* means only authenticated users can access)
    * adds Jwt filter to configuration
    * disables X-Frame-Options ([What is X-Frame-Options?](https://docs.spring.io/spring-security/site/docs/3.2.0.CI-SNAPSHOT/reference/html/headers.html#headers-frame-options))

* **authenticationManagerBean** - creates Bean for AuthenticationManager object
* **passwordEncoder** - creates Bean for BCryptPasswordEncoder object


<br/>


### Step 16
#### Create AuthService class
This class is intended to provide login functionality for project.

##### Fields
* AuthenticationManager
* JwtService

##### Method
* **login** 
    * Accepts *username, password and rememberMe* values from client as parameters
    * Creates an instance of Authentication object with provided username and password
    * Generates token with data of created Authentication object
    * Returns generated token as a String


<br/>


### Step 17
#### Create AuthController class
This class is intended to handle client requests for authentication.

##### Field
* **AuthService**

##### Method
* **login** - returns generated token (error message if something goes wrong) as a ResponseEntity. 


<br/>


**If token is generated successfully, it can be used to access any URL which requires authentication.**
**It is enough to add  *token* header to request.**


<br/>


#### Annotations:
* **@AllArgsConstructor** - Adds constructor with all fields (from Lombok dependency)
* **@Bean** - Creates a bean for corresponding class
* **@Column** - Specifies mapped column for a corresponding field
* **@Component** - Indicates corresponding class as a candidate for auto-detection when using annotation-based configuration and classpath scanning
* **@Configuration** - Defines that corresponding class is considered to be configuration bean
* **@EnableWebSecurity** - Defines that corresponding class is considered to have security configuration
* **@Entity** - Creates db table according to corresponding class
* **@GeneratedValue** - Specifies that a value will be automatically generated for that field
* **@Getter** - Adds getter for all fields (from Lombok dependency)
* **@Id** - Makes corresponding field primary key of table
* **@JoinColumn** - Configures column specifications of relation
* **@JoinTable** - Configures table specifications of relation
* **@Log4j2** - Allows logging messages in different log levels (from Lombok dependency)
* **@ManyToMany** - Specifies relation between corresponding field of current table and referenced table
* **@NoArgsConstructor** - Adds default constructor without any fields (from Lombok dependency)
* **@PropertySource** - Assigns properties file to corresponding object (*classpath:* points to resources folder)
* **@Service** - Defines that corresponding class is considered to provide business functionality
* **@Setter** - Adds setter for all fields (from Lombok dependency)
* **@Value** - Assigns value to corresponding field
* **PostMapping** - Indicates that annotated class or method will be responsible to respond POST requests sent to corresponding URL
* **RequestMapping** - Indicates that annotated class or method will be responsible to respond requests sent to corresponding URL
* **RestController** - Is used to create RESTful web services using Spring MVC. Spring RestController takes care of mapping request data to the defined request handler method.<br/>



### Bonus
Custom exception *(InvalidTokenGenerationException)* has been created to use in case which something goes wrong while token generation.
And it is handled in *(JwtExceptionHandler)* class.