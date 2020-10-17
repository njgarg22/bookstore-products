# products-service

## CAP

1. The [CAP Java SDK](https://cap.cloud.sap/docs/java/) is able to tightly integrate with Spring Boot, which provides numerous features out of the box. This means, Spring Boot will be your runtime container.

1. CAP applications use [Core Data Services](https://cap.cloud.sap/docs/cds/) (CDS) to describe:
    * Data structures by using [entity definitions](https://cap.cloud.sap/docs/cds/cdl)
    * How data structures are consumed by using [service definitions](https://cap.cloud.sap/docs/cds/cdl#services)

1. The initialization of the CAP Java runtime is done by Spring automatically, based on the dependencies defined in the `pom.xml`.

1. [CDS Query Notation](https://cap.cloud.sap/docs/cds/cqn) (CQN) is the common language in CAP to run queries against services. It can be used to talk to the services defined by your model, but also remote services, such as the database.

1. The event handler uses the following APIs, which are available for service providers in CAP Java:
    * [Event handler classes](https://cap.cloud.sap/docs/java/provisioning-api) have to implement the _marker interface_ `EventHandler` and register themselves as Spring Beans (`@Component`). The marker interface is important, because it enables the CAP Java runtime to identify these classes among all Spring Beans.
    * `Event handler methods` are registered with `@On`, `@Before`, or `@After` annotations. Every event, such as an entity creation, runs through these [three phases](https://cap.cloud.sap/docs/java/provisioning-api#phases). Each phase has a slightly different semantic.
    * The annotation `@ServiceName` specifies the default service name all event handler methods apply to. Here this is `AdminService`, as this was also the name when defining the service in the CDS model.
    * Event handler methods get an event-specific event context parameter, which provides access to the input parameters of the event and the ability to set the result. For example, `CreateEventContext` context parameter is specific to the extended `CREATE` event.

## Folder structure

1. The `db` folder stores database-related artifacts.
1. The `srv` folder stores your Java application.
1. The `srv/admin-service.cds` defines a simple service, which also defines its own entity. In more complex applications, services usually expose projections on entities defined in the data model.
1. The `srv/src/main/resources/edmx` is the default path, where CAP Java runtime looks for the model definitions.
1. The `srv/src/main/java/com/sap/cap/productsservice/Application.java` is the startup class for the Spring Boot container and contains a `main` method.
1. The `srv/src/main/java/com/sap/cap/productsservice/handlers/AdminService.java` is the Java class for custom event handler.

## Build and Run the application

1. Compile model definition: `mvn clean install`. After running this command, some files are generated and added to the `srv/src/main/resources/edmx` folder.
1. Start the application: `mvn clean spring-boot:run`. Inspect oData metadata at http://localhost:8080/odata/v4/AdminService/$metadata.

## API's

1. Get the entity `Products` of the service `AdminService`: GET http://localhost:8080/odata/v4/AdminService/Products

1. This `POST` request causes an OData Insert on the entity `Products` of the service `AdminService`:

   ```json
   POST http://localhost:8080/odata/v4/AdminService/Products
   
   {
       "ID": 42,
       "title": "My Tutorial Product",
       "descr": "You are doing an awesome job!"
   }
   ```
