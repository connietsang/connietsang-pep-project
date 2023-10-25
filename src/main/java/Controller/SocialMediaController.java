package Controller;

import Service.AccountService;
import Service.MessageService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create(); 

        app.post("register", this::postAccountHandler);
        app.post("login", this::postLoginHandler);
        app.post("messages", this::postMessagesHandler);
        app.get("messages", this::getMessagesHandler);
        app.get("messages/{message_id}", this::getMessageByIdHandler);
        app.delete("messages/{message_id}", this::deleteMessageHandler);
        app.patch("messages/{message_id}", this::patchMessagesHandler);
        app.get("accounts/{account_id}/messages", this::getMessagesFromAccountHandler);

        return app;
    }

    /**
     * Handler to post (register) a new account.
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     */
    private void postAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        if (addedAccount != null){
            ctx.json(addedAccount);
        }else{
            ctx.status(400);
        }
    }

    /**
     * Handler to login to an account.
     * @param ctx
     * @throws JsonProcessingException
     */
    private void postLoginHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account loggedInAccount = accountService.loginAccount(account);
        if (loggedInAccount != null){
            ctx.json(loggedInAccount);
        }else{
            ctx.status(401);
        }
    }

    /**
     * Handler to create and post a new message.
     * @param ctx
     * @throws JsonProcessingException
     */
    private void postMessagesHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message addedMessage = messageService.addMessage(message);
        if (addedMessage != null && 
        accountService.getAccountById(message.getPosted_by()) != null){
            ctx.json(addedMessage);
        }else{
            ctx.status(400);
        }

    }

    /**
     * Handler to retrieve all messages.
     * @param ctx
     */
    private void getMessagesHandler(Context ctx){
        ctx.json(messageService.getAllMessages());
    }

    /**
     * Handler to retrieve a message identified by its message id. 
     * @param ctx
     * @throws NumberFormatException
     */
    private void getMessageByIdHandler(Context ctx)throws NumberFormatException{
        try{
            int id = Integer.parseInt(ctx.pathParam("message_id"));
            if (messageService.getMessage(id) != null){
                ctx.json(messageService.getMessage(id));
            }else{
                ctx.status(200);
            }
        } catch (NumberFormatException e){
            System.out.println("not an integer");
        }
    }

    /**
     * Handler to delete a message identified by its message id.
     * @param ctx
     * @throws JsonProcessingException
     */
    private void deleteMessageHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Integer id = Integer.parseInt(ctx.pathParam("message_id"));
        if (id != null){
            Message messageToDelete = messageService.getMessage(id.intValue());
            if (messageToDelete != null){
                ctx.json(mapper.writeValueAsString(messageToDelete));
            }else{
                 ctx.json("");
            }
        }
        ctx.status(200);
    } 
    
    /**
     * Handler to update the text of a message identified by its message id.
     * @param ctx
     * @throws JsonProcessingException
     */
    private void patchMessagesHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Integer id = Integer.parseInt(ctx.pathParam("message_id"));
        Message newMessage = mapper.readValue(ctx.body(), Message.class);

        if (newMessage != null && id != null && messageService.doesMessageExist(id.intValue()) 
        && !newMessage.getMessage_text().equals("") && newMessage.getMessage_text().length() < 255){
            Message updatedMessage = messageService.updateMessage(newMessage, id.intValue());
            ctx.json(mapper.writeValueAsString(updatedMessage));
            ctx.status(200);
        }else{
            ctx.status(400);
        }

    }

    /**
     * Handler to retrieve all messages from an account specified by its account id. 
     * @param ctx
     */
    private void getMessagesFromAccountHandler (Context ctx){
        int id = Integer.parseInt(ctx.pathParam("account_id"));
        ctx.json(messageService.getMessagesFromAccount(id));
    }


}