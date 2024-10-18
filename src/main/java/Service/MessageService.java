package Service;

import DAO.MessageDAO;
import Model.Message;
import Model.Account;
import java.util.List;

public class MessageService {
    // Account account = new Account();
    MessageDAO messageDAO = new MessageDAO();

   
    public Message createMessage(Message message) throws Exception{
        try{
            if (message.getMessage_text() == null || message.getMessage_text().trim() == "" || message.getMessage_text().length() > 255) {
                throw new IllegalArgumentException("Message text not empty");
            }else{
                return messageDAO.createMessage(message);
            }
        }catch(Exception e){
            throw new IllegalArgumentException("Message create fail");
        }

        
    }

  
    public List<Message> getAllMessages(Account account) throws Exception{
        return messageDAO.getAllMessages();
    }

  
    public Message getMessageById(int messageId){
        try{
            return messageDAO.getMessageById(messageId);
        }catch(Exception e){
            throw new IllegalArgumentException("Message_id not found");
        }
        
    }

    
    public Message updateMessageText(int messageId, String newMessageText){ 
        try{
            if (newMessageText == null  || newMessageText.length() > 255 || newMessageText.trim() == "") {
                throw new IllegalArgumentException("Messagetext not be empty");
            }else {
                return messageDAO.updateMessageText(messageId, newMessageText);
            }
        }catch (Exception e){
            throw new IllegalArgumentException("update message fail");
        }
    }

    
    public Message deleteMessagById(int messageId){
        try{
            return messageDAO.deleteMessageById(messageId);
        }catch(Exception e){
            throw new IllegalArgumentException("message not deleted");
        }
        
    }

    public List<Message> getMessagesByUser(int accountId) throws Exception{
        return messageDAO.getMessagesByUser(accountId);
    }
  }
