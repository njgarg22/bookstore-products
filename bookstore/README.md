# bookstore

## Re-use the `product-service`

1. As the `product-service` should be reused for the bookstore, we need to add a dependency between those two projects. 
Reusable models can be published as NPM modules and imported through dependencies in the `package.json` of a project.

1. First, we need to simulate a release of the `product-service` module, and consume this release in the bookstore application.

1. Install the reusable service project as npm dependency by running `npm install $(npm pack ../products-service -s)`.

1. `npm pack` creates a tarball from the `products-service`, which is then directly used as a dependency in the bookstore application.

1. You’ll find a `sap-capire-products-1.0.0.tgz` in the root folder of the bookstore project, which is the tarball file of the `products-service` project

1. Install all other packages and simplify the overall dependency structure by running: `npm install && npm dedupe`

1. If you open the `package.json` of your bookstore project, you’ll see a dependency to `@sap/capire-products`.

    ```json
    "dependencies": {
        "@sap/capire-products": "file:sap-capire-products-1.0.0.tgz"
    }
    ```

## Deploy bookstore domain model

1. The name of the CSV inside `db/data` has to match the pattern [namespace]-[entity name] exactly otherwise the file will be ignored.

1. To install sqlite3 node packages, run `npm install -g --save-dev sqlite3`. We need SQLite Node.js packages because the CDS CLI (for instance to deploy artefacts to the database) is based on Node.js.

1. To initialize the bookstore database with the defined domain model and sample data, run:

    ```console
    $ cds deploy --to sqlite

     > filling sap.capire.bookstore.Authors from db/data/sap.capire.bookstore-Authors.csv 
     > filling sap.capire.bookstore.Books from db/data/sap.capire.bookstore-Books.csv 
     > filling sap.capire.products.Products_texts from db/data/sap.capire.bookstore-Books_texts.csv 
     > filling sap.capire.products.Categories from db/data/sap.capire.products-Categories.csv 
    /> successfully deployed to ./sqlite.db

    /> updated ./package.json
    ```
