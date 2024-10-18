package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Service.AccountService;
import Service.MessageService;
import Model.Account;
import Model.Message;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);

        // user related request ex.registration,login and accounts

        app.post("/register", this::registerAccount);
        app.post("/login", this::login);

        //message related requests

        app.post("/messages", this::createMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageById);
        app.delete("/messages/{message_id}", this::deleteMessage);
        app.patch("/messages/{message_id}", this::updateMessage);
        app.get("/accounts/{account_id}/messages", this::getMessagesByUserId);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    // users related request ex.registration,login

    AccountService accountService = new AccountService();
    Account account = new Account();

    public void registerAccount(Context ctx){
        try{
            ObjectMapper mapper = new ObjectMapper();
            Account account = mapper.readValue(ctx.body(), Account.class);
            Account createdAccount = accountService.registerAccount(account);

            if(createdAccount == null){
                ctx.status(400);
            }else{
                ctx.json(mapper.writeValueAsString(createdAccount)).status(200);
            } 
        }catch(Exception e){
            ctx.status(400);
        }
             
    }

    public void login(Context ctx) throws Exception{
        Account account = ctx.bodyAsClass(Account.class);
        Account loggedInAccount = accountService.login(account.getUsername(), account.getPassword());
        if(loggedInAccount != null){
            ctx.json(loggedInAccount).status(200);
        }else{
            ctx.status(401);
        }

    }
    


     //message  requeests

     MessageService messageService = new MessageService();

     public void createMessage(Context ctx){
        try{
            Message message = ctx.bodyAsClass(Message.class);
            Message createdMessage = messageService.createMessage(message);
            
            if (createdMessage == null) {
                ctx.status(400);
            } else {
                ctx.json(createdMessage).status(200);
            }
        }catch(Exception e){
            ctx.status(400);
        }
        
    }


    public void getAllMessages(Context ctx) throws Exception{
        List<Message> messages = messageService.getAllMessages(account);
        ctx.json(messages).status(200);
    }

    
    public void getMessageById(Context ctx){
        try{
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            Message message = messageService.getMessageById(messageId);

            if (message == null) {
                ctx.status(200);
            } else {
                ctx.json(message).status(200);
            }
        }catch(Exception e){
            ctx.status(400);
        }
        
    }

    public void deleteMessage(Context ctx){
        try{
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            Message deletedMessage = messageService.deleteMessagById(messageId);
            
            if (deletedMessage == null) {
                ctx.status(200); 
            } else {
                ctx.json(deletedMessage).status(200);
            }
        }catch(Exception e){
            ctx.status(400);
        }
        
    }

    public void updateMessage(Context ctx) {
        try{
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            String newMessageText = ctx.bodyAsClass(Message.class).getMessage_text();
            Message updatedMessage = messageService.updateMessageText(messageId, newMessageText);
            if (updatedMessage == null) {
                ctx.status(400); 
            } else {
                ctx.json(updatedMessage).status(200);
            }

        }catch(Exception e){
            ctx.status(400);
        }
         
    }

    public void getMessagesByUserId(Context ctx) throws Exception{
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getMessagesByUser(accountId);
        if(messages != null){
            ctx.json(messages).status(200);
        }else{
            ctx.status(400);
        }
        
    }



}