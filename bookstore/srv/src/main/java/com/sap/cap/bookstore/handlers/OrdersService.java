package com.sap.cap.bookstore.handlers;

import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.ServiceName;

import org.springframework.stereotype.Component;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import com.sap.cds.ql.Select;
import com.sap.cds.ql.Update;
import com.sap.cds.ql.cqn.CqnSelect;
import com.sap.cds.ql.cqn.CqnUpdate;
import com.sap.cds.services.ErrorStatuses;
import com.sap.cds.services.ServiceException;
import com.sap.cds.services.cds.CdsService;
import com.sap.cds.services.handler.annotations.Before;
import com.sap.cds.services.persistence.PersistenceService;

import cds.gen.ordersservice.OrderItems;
import cds.gen.ordersservice.Orders;
import cds.gen.sap.capire.bookstore.Books;
import cds.gen.sap.capire.bookstore.Books_;
import java.math.BigDecimal;
import cds.gen.sap.capire.bookstore.OrderItems_;

import com.sap.cds.services.handler.annotations.After;

@Component
@ServiceName("OrdersService")
public class OrdersService implements EventHandler {

    @Autowired
    PersistenceService db;

    @Before(event = CdsService.EVENT_CREATE, entity = "OrdersService.OrderItems")
    public void validateBookAndDecreaseStock(List<OrderItems> items) {
        for (OrderItems item : items) {
            String bookId = item.getBookId();
            Integer amount = item.getAmount();

            // check if the book that should be ordered is existing
            CqnSelect sel = Select.from(Books_.class).columns(b -> b.stock()).where(b -> b.ID().eq(bookId));
            Books book = db.run(sel).first(Books.class)
                    .orElseThrow(() -> new ServiceException(ErrorStatuses.NOT_FOUND, "Book does not exist"));

            // check if order could be fulfilled
            int stock = book.getStock();
            if (stock < amount) {
                throw new ServiceException(ErrorStatuses.BAD_REQUEST, "Not enough books on stock");
            }

            // update the book with the new stock, means minus the order amount
            book.setStock(stock - amount);
            CqnUpdate update = Update.entity(Books_.class).data(book).where(b -> b.ID().eq(bookId));
            db.run(update);
        }
    }

    @Before(event = CdsService.EVENT_CREATE, entity = "OrdersService.Orders")
    public void validateBookAndDecreaseStockViaOrders(List<Orders> orders) {
        for (Orders order : orders) {
            if (order.getItems() != null) {
                validateBookAndDecreaseStock(order.getItems());
            }
        }
    }
}