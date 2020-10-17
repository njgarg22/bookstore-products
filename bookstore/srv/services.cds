    using { sap.capire.bookstore as db } from '../db/schema';

    // Define Books Service
    // The `BooksService` is used to provide a read-only view on the Books and Authors data. 
    // Modifications of these entities isn’t possible via this service.
    service BooksService {
        @readonly entity Books as projection   on db.Books { *, category as genre } excluding { category, createdBy, createdAt, modifiedBy, modifiedAt };
        @readonly entity Authors as projection on db.Authors;
    }

    // Define Orders Service
    // The `OrdersService` allows to view, create, and delete orders. 
    service OrdersService {
        entity Orders as projection on db.Orders;
        entity OrderItems as projection on db.OrderItems;
    }

    // Reuse Admin Service
    // Note that we’ve added the Authors entity to it. It can be used to create, update, and delete products and authors.
    using { AdminService } from '@sap/capire-products';
    extend service AdminService with {
        entity Authors as projection on db.Authors;
    }