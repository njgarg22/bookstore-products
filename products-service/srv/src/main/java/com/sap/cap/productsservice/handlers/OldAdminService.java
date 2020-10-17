 package com.sap.cap.productsservice.handlers;

 import java.util.HashMap;
 import java.util.Map;

 import org.springframework.stereotype.Component;

 import com.sap.cds.services.cds.CdsCreateEventContext;
 import com.sap.cds.services.cds.CdsReadEventContext;
 import com.sap.cds.services.cds.CdsService;
 import com.sap.cds.services.handler.EventHandler;
 import com.sap.cds.services.handler.annotations.On;
 import com.sap.cds.services.handler.annotations.ServiceName;

// Java class for custom event handler
 @Component
 @ServiceName("OldAdminService")
 public class OldAdminService implements EventHandler {

     private Map<Object, Map<String, Object>> products = new HashMap<>();

     /*
     Let’s look at the CdsCreateEventContext context parameter.
     The event we’re extending is the CREATE event.
     The type of the context variable is specific to this extended CREATE event.
     The onCreate method returns void, as the result is set by running: context.setResult().
     @On annotation replaces the default handling of an event that is provided by the CAP Java runtime.
     */
     @On(event = CdsService.EVENT_CREATE, entity = "OldAdminService.Products")
     public void onCreate(CdsCreateEventContext context) {
         context.getCqn().entries().forEach(e -> products.put(e.get("ID"), e));
         context.setResult(context.getCqn().entries());
     }

     @On(event = CdsService.EVENT_READ, entity = "OldAdminService.Products")
     public void onRead(CdsReadEventContext context) {
         context.setResult(products.values());
     }

 }