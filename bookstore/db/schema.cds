namespace sap.capire.bookstore;

    using { Currency, cuid, managed }      from '@sap/cds/common';
    using { sap.capire.products.Products } from '@sap/capire-products';

    entity Books as projection on Products; extend Products with {
        // Note: we map Books to Products to allow reusing AdminService as is
        author : Association to Authors;
    }

    entity Authors : cuid {
        firstname : String(111);
        lastname  : String(111);
        books     : Association to many Books on books.author = $self;
    }

    // This entities is annotated with @Capabilities.Updatable: false.
    // It means that they cannot be updated, only created and deleted.
    @Capabilities.Updatable: false
    entity Orders : cuid, managed {
        items    : Composition of many OrderItems on items.parent = $self;

        // The total elemen is @readonly so value of this element cannot be set by a client.
        // The value is calculated by custom code.
        total    : Decimal(9,2) @readonly;
        currency : Currency;
    }

    @Capabilities.Updatable: false
    entity OrderItems : cuid {
        parent    : Association to Orders not null;
        book_ID   : UUID;
        amount    : Integer;
        netAmount : Decimal(9,2) @readonly;
    }