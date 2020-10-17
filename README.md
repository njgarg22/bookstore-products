# products-service

## CAP

1. The [CAP Java SDK](https://cap.cloud.sap/docs/java/) is able to tightly integrate with Spring Boot, which provides numerous features out of the box. This means, Spring Boot will be your runtime container.

1. CAP applications use [Core Data Services](https://cap.cloud.sap/docs/cds/) (CDS) to describe:
    * Data structures by using [entity definitions](https://cap.cloud.sap/docs/cds/cdl)
    * How data structures are consumed by using [service definitions](https://cap.cloud.sap/docs/cds/cdl#services)

1. The initialization of the CAP Java runtime is done by Spring automatically, based on the dependencies defined in the `pom.xml`.

## Folder structure

1. The `db` folder stores database-related artifacts.
1. The `srv` folder stores your Java application.
1. The `srv/admin-service.cds` defines a simple service, which also defines its own entity. In more complex applications, services usually expose projections on entities defined in the data model.
1. The `srv/src/main/resources/edmx` is the default path, where CAP Java runtime looks for the model definitions.
1. The `srv/src/main/java/com/sap/cap/productsservice/Application.java` is the startup class for the Spring Boot container and contains a `main` method.

## Build and Run the application

1. Compile model definition: `mvn clean install`. After running this command, some files are generated and added to the `srv/src/main/resources/edmx` folder.
1. Start the application: `mvn clean spring-boot:run`. Inspect oData metadata at http://localhost:8080/odata/v4/AdminService/$metadata.
