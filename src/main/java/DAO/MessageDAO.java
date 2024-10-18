package DAO;
import Model.Message;
import Util.ConnectionUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Model.Account;

public class MessageDAO {
    Connection conn = ConnectionUtil.getConnection();
    AccountDAO accountDAO = new AccountDAO();

    public Message createMessage(Message message) throws SQLException{
        try{
            Account user = accountDAO.findAccountById(message.getPosted_by());
            if (user == null) {
                throw new IllegalArgumentException("User does not exist");
            }
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            st.setInt(1, message.getPosted_by());
            st.setString(2, message.getMessage_text());
            st.setLong(3, message.getTime_posted_epoch());

            int rowsAffected = st.executeUpdate();
            if(rowsAffected == 0){
                throw new SQLException("Message not created");
            } 

            ResultSet generatedKeys = st.getGeneratedKeys();
            if (generatedKeys.next()) {
                int message_id = (generatedKeys.getInt(1));
                return new Message(message_id,message.getPosted_by(),message.getMessage_text(),message.getTime_posted_epoch());
            }  
        }catch(Exception e){
            throw new IllegalArgumentException("Message does not created");
        }
        return null;
    }
    public List<Message> getAllMessages() throws SQLException{
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM message";
        PreparedStatement st= conn.prepareStatement(sql);
        ResultSet resultSet = st.executeQuery();
        while (resultSet.next()) {
            Message message = new Message(
                resultSet.getInt("message_id"),
                resultSet.getInt("posted_by"),
                resultSet.getString("message_text"),
                resultSet.getLong("time_posted_epoch")
            );
            messages.add(message);
        }
        return messages;
    }
     

    public Message getMessageById(int messageId){
        try{
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement st= conn.prepareStatement(sql);
            st.setInt(1, messageId);
            ResultSet resultSet = st.executeQuery();
            if (resultSet.next()) {
                return new Message(
                    resultSet.getInt("message_id"),
                    resultSet.getInt("posted_by"),
                    resultSet.getString("message_text"),
                    resultSet.getLong("time_posted_epoch")
                );
            }
        }catch(Exception e){
            throw new IllegalArgumentException("Message_id not found");
        }
        return null;
    }

    public Message deleteMessageById(int messageId){
        try{
            Message deletedMessage = getMessageById(messageId);
            String sql = "DELETE FROM message WHERE message_id = ?";
            PreparedStatement st  = conn.prepareStatement(sql);
            st.setInt(1, messageId);
            int affectedRow = st.executeUpdate();
            if(affectedRow > 0){
                return deletedMessage;
            }
           
        }catch (Exception e){
            throw new IllegalArgumentException("Message not deleted");
        }
        return null;
        
    }

    public Message updateMessageText(int messageId, String newMessageText){
        try{
            Message updatedMessage = null;
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            System.out.println(sql);
            PreparedStatement st  = conn.prepareStatement(sql);
            st.setString(1, newMessageText);
            st.setInt(2, messageId);
            int rowsUpdated = st.executeUpdate();
            if (rowsUpdated == 1) {
                updatedMessage = getMessageById(messageId); 
                return updatedMessage;   
            }
        }catch(Exception e){
            throw new IllegalArgumentException("Message not updated"); 
        }
        return null;
    }

    public List<Message> getMessagesByUser(int accountId) throws SQLException{
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM message WHERE posted_by = ?";
        PreparedStatement st= conn.prepareStatement(sql);
        st.setInt(1, accountId);

        ResultSet resultSet = st.executeQuery();
        while (resultSet.next()) {
            Message message = new Message(
                    resultSet.getInt("message_id"),
                    resultSet.getInt("posted_by"),
                    resultSet.getString("message_text"),
                    resultSet.getLong("time_posted_epoch")
            );
            messages.add(message);
        }
        return messages;
    }
}

