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
    * The annotation `@ServiceName` specifies the default service name all event handler methods apply to. Here this is `OldAdminService`, as this was also the name when defining the service in the CDS model.
    * Event handler methods get an event-specific event context parameter, which provides access to the input parameters of the event and the ability to set the result. For example, `CreateEventContext` context parameter is specific to the extended `CREATE` event.

## Understanding CDS files

1. Let's take a sample CDS file:

    ```
    namespace sap.capire.products;

    using { Currency, cuid, managed, sap.common.CodeList } from '@sap/cds/common';

    entity Products : cuid, managed {
        title    : localized String(111);
        descr    : localized String(1111);
        stock    : Integer;
        price    : Decimal(9,2);
        currency : Currency;
        category : Association to Categories;
    }

    entity Categories : CodeList {
        key ID   : Integer;
        parent   : Association to Categories;
        children : Composition of many Categories on children.parent = $self;
    }
    ```

1. The above domain model defines two entities: `Products` and `Categories`.

1. It imports various common definitions from the `@sap/cds/common` package (a globally available reuse package): `Currency`, `cuid`, `managed` and `CodeList`.

1. In addition, the domain model uses the CDS keywords `localized`, `Association`, and `Composition`. 

1. Let’s explain these imports and keywords in more detail:

### The localized Keyword

The `localized` keyword can be used to mark elements, which require translation. The ability to store translations for different languages and to store a default fallback translation is automatically handled by CDS for you.

### Associations and Compositions

1. Associations and compositions can be used to define relationships between entities. They often allow you to define these relationships without explicitly working with foreign keys.

1. While associations define a rather loose coupling between the entities, compositions define a containment relationship. Compositions can also be thought of as defining deep structures. You can perform `deep inserts` and `upserts` along these structures.

1. In above domain model, the `Categories` entities define a `parent` and `children` element. This enables a hierarchy of categories. The children of a category are modelled as a composition. A category with all of its children defines a deep nested structure. Deleting a category would automatically delete all of its children. However, the parent of a category is modelled as an association. Deleting a category obviously shouldn’t delete its parent.

### The cuid and managed Aspects

1. Both `cuid` and `managed` are `aspects`. Aspects extend an entity with additional elements. The `cuid` aspect adds a `key` element `ID` of type `UUID` to the entity.

1. The `managed` aspect adds four additional elements to the entity. These capture the time of the creation and last update of the entity, and the user, which performed the creation and last update.

### The CodeList Aspect and the Currency Type

1. `CodeLists` can be used to store global, translatable definitions based on codes, such as currencies, countries, or languages. Especially for UIs, a `CodeList` can be useful to provide a value help for certain input fields.

1. The `Currency` definition is a type. It defines an association to a `Currencies` entity. The `Currencies` entity is based on ISO 4217 and uses three-letter alpha codes as keys such as `EUR` or `USD` and provides the possibility to store the corresponding currency symbol such as `€` or `$`.

## Folder structure

1. The `db` folder stores database-related artifacts.
When you define a service, you define its own entity within the service itself. 
But when modeling with CDS the best practice is to separate services from the domain model.
Therefore, you will define the domain model in the db folder of your CAP application.
1. The `db/schema.cds` defines the complete domain model that is used by the products service
1. The `srv` folder stores your Java application.
1. The `srv/old-admin-service.cds` defines a simple service, which also defines its own entity. In more complex applications, services usually expose projections on entities defined in the data model.
1. The `srv/src/main/resources/edmx` is the default path, where CAP Java runtime looks for the model definitions.
1. The `srv/src/main/java/com/sap/cap/productsservice/Application.java` is the startup class for the Spring Boot container and contains a `main` method.
1. The `srv/src/main/java/com/sap/cap/productsservice/handlers/OldAdminService.java` is the Java class for custom event handler.

## Build and Run the application

1. Compile model definition: `mvn clean install`. After running this command, some files are generated and added to the `srv/src/main/resources/edmx` folder.
1. Start the application: `mvn clean spring-boot:run`. Inspect oData metadata at http://localhost:8080/odata/v4/OldAdminService/$metadata.

## API's

1. Get the entity `Products` of the service `OldAdminService`: GET http://localhost:8080/odata/v4/OldAdminService/Products

1. This `POST` request causes an OData Insert on the entity `Products` of the service `OldAdminService`:

   ```json
   POST http://localhost:8080/odata/v4/OldAdminService/Products
   
   {
       "ID": 42,
       "title": "My Tutorial Product",
       "descr": "You are doing an awesome job!"
   }
   ```
