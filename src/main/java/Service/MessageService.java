package Service;

import DAO.MessageDAO;
import java.util.List;
import Model.Message;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }

    public boolean doesMessageExist(int id){
        return (messageDAO.getMessage(id) != null);
    }
    public Message addMessage(Message message){
        if (!message.getMessage_text().equals("") && message.getMessage_text().length() < 255)
            return messageDAO.insertMessage(message);
        return null;
    }

    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    public Message getMessage(int id){
        return messageDAO.getMessage(id);
    }
    
    public Message deleteMessage(int id){
        return messageDAO.deleteMessage(id);
    }

    public Message updateMessage(Message newMessage, int id){
        return messageDAO.updateMessage(newMessage, id);
    }
    
    public List<Message> getMessagesFromAccount(int id){
        return messageDAO.getMessagesFromAccount(id);
    }
}
