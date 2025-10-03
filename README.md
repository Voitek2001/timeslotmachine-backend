# TimeSlotMachine Backend

![stability-wip](https://img.shields.io/badge/Stability-WIP-lightgrey.svg)

WIP: more detailed description will be added later.

## Docker

1. Start the docker service if it hasn't been started yet:
   ```bash
   sudo systemctl start docker
   ```

2. Build:
   ```bash
   docker build -t tsm-backend-docker -f Dockerfile .
   ```

3. Run:
   ```bash
   docker run tsm-backend-docker
   ```

4. Get ip address:
   ```bash
   docker ps
   docker inspect CONTAINER_ID | grep IPAddress
   ```

5. Remove container:
   ```bash
   docker ps -a
   docker container rm CONTAINER_ID
   ```

6. Remove image:
   ```bash
   docker images
   docker rmi IMAGE_ID
   ```

## Links

### Postgres

1. PostgreSQL Sequences – the way of generating ids (autoincrement ids)
   - [PostgreSQL Tutorial](https://www.postgresqltutorial.com/postgresql-tutorial/postgresql-sequences/)
   - [Stackoverflow](https://stackoverflow.com/a/11828169/9200394)
2. [Datatypes](https://www.postgresql.org/docs/current/datatype.html)
   - [Date/Time Types](https://www.postgresql.org/docs/current/datatype-datetime.html)
   - [Geometric datatypes](https://www.postgresql.org/docs/current/datatype-geometric.html)
3. [Deferrable Constraints in PostgreSQL](https://emmer.dev/blog/deferrable-constraints-in-postgresql/)
4. [Generated Columns](https://www.postgresql.org/docs/current/ddl-generated-columns.html)
5. [Postgresql: multicolumn indexes vs single column index](https://stackoverflow.com/q/49776856/9200394)

### Spring/JPA/Hibernate/Jackson

1. [Spring Data JPA Reference](https://docs.spring.io/spring-data/jpa/reference/jpa.html)
2. [Spring Boot with PostgreSQL: A Step-by-Step Guide](https://talesofdancingcurls.medium.com/spring-boot-with-postgresql-a-step-by-step-guide-c451848f0184)
3. [JPA/Hibernate Bidirectional Lazy Loading Done Right](https://medium.com/monstar-lab-bangladesh-engineering/jpa-hibernate-bidirectional-lazy-loading-done-right-65eda6426d64)
4. [JPA implementation patterns: Lazy loading](https://xebia.com/blog/jpa-implementation-patterns-lazy-loading/)
5. [JPA Attribute Converters](https://www.baeldung.com/jpa-attribute-converters)
6. [Lazy loading and Jackson serialization](https://stackoverflow.com/a/54415243/9200394)
7. [Jackson - Serializing a value only if a condition met, ignore nulls](https://stackoverflow.com/q/54392686/9200394)
8. [Spring Docs - Jackson JSON](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-methods/jackson.html)
9. [Jackson Annotations](https://www.baeldung.com/jackson-annotations)
10. [Jackson enum Serializing and DeSerializer](https://stackoverflow.com/q/12468764/9200394)
11. [Composite Primary Keys in JPA](https://www.baeldung.com/jpa-composite-primary-keys)
12. [How to create a composite primary key which contains a `@ManyToOne` attribute as an `@EmbeddedId` in JPA?](https://stackoverflow.com/a/7196183/9200394)

### Kotlin

1. Annotation Processing – it will allow us to reduce the boilerplate code, i.e.,
   replace
   ```kotlin
   @Id
   @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
   var id: EID? = null
   ```
   with just a single annotation
   ```kotlin
   @EntityId
   var id: EID? = null
   ```

   - [Kt. Academy - kapt](https://kt.academy/article/ak-annotation-processing)
   - [Kotlin Docs - KSP Overview](https://kotlinlang.org/docs/ksp-overview.html)
   - [The Guide To Your First Annotation Processor with KSP](https://proandroiddev.com/the-guide-to-your-first-annotation-processor-with-ksp-and-becoming-a-kotlin-artist-4e5d13f171e6)


### Database setup

To configure the database of the application, you need to set the following properties in the `application.properties` file:

1. `spring.datasource.url`: The URL of your database.
2. `spring.datasource.username`: The username for authenticating with your database.
3. `spring.datasource.password`: The password associated with username.

### Setting up a local database

The following steps enable setting up a local database. This setup is crucial if the system is intended to support a large number of users, as the database is often the bottleneck for performance:

1. Install PostgreSQL on your machine (we recommend visiting the official PostgreSQL website to proper version of database for your operation system)
2. Create a user named `postgres` (this user should exist by default) and set a password.
3. In our project, we also installed pgAdmin. This is an application for managing PostgreSQL databases. While not necessary, it significantly simplify our work. The following steps require installing pgAdmin.
4. Connect to the local database using pgAdmin, providing the `postgress` user credentials defined in step 2.
5. Load the database scripts into the local database via the console. In our project, the scripts are located in the `/EngineeringThesis/backend/dbscripts/public` directory. Make sure to execute them in correct order.
6. Configure the database connection and authentication details in the `application.properties` file, located in the `resources` directory on the backend.
