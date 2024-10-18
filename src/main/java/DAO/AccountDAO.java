package DAO;
import Model.Account;
import Util.ConnectionUtil;
import java.sql.*;

public class AccountDAO {

   Connection conn = ConnectionUtil.getConnection();

    public Account createAccount(Account account) throws SQLException{

        try{
            String checkSql = "SELECT * FROM account WHERE username = ?";
            PreparedStatement checkStatement = conn.prepareStatement(checkSql);
            checkStatement.setString(1, account.getUsername());
            ResultSet checkResult = checkStatement.executeQuery();   
            if (checkResult.next() && checkResult.getInt(1) > 0) {
                throw new IllegalArgumentException("Username already exists.");
            }
           
            String sql = "INSERT INTO account(username,password) VALUES(?,?)";

            PreparedStatement st = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

            st.setString(1, account.getUsername());
            st.setString(2,account.getPassword()); 

            int affectedRows = st.executeUpdate();
          
            if (affectedRows == 0) {
                throw new SQLException("Account created fail");
            }

            ResultSet generatedKeys = st.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generated_accountId = generatedKeys.getInt(1);
                return new Account(generated_accountId,account.getUsername(),account.getPassword());
            }
        }catch (SQLException e){
            throw new SQLException("Account Created Fial");
        }
        return null;
    }


   public Account login(String username, String password) throws SQLException{
    Account account = null;
    String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, username);
        statement.setString(2, password);
        
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            account = new Account(
                resultSet.getInt("account_id"),
                resultSet.getString("username"),
                resultSet.getString("password")
            );
        }
    return account;
   }

  public Account findAccountById(int accountId) throws SQLException {
    String sql = "SELECT * FROM account WHERE account_id = ?";
    PreparedStatement st = conn.prepareStatement(sql);
    st.setInt(1, accountId);
    ResultSet resultSet = st.executeQuery();

    if (resultSet.next()) {
        return new Account(
            resultSet.getInt("account_id"),
            resultSet.getString("username"),
            resultSet.getString("password")
        );
    } else {
        return null;
    }
  }

}
