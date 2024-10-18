package Service;
import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    AccountDAO accountDAO = new AccountDAO();

    public Account registerAccount(Account account) throws Exception{
        try{
            if (account.getUsername() == null || account.getPassword() == null || account.getUsername().trim() == "" || account.getPassword().length() < 4) {
                throw new IllegalArgumentException("Username not  empty.");
            } else {
                return accountDAO.createAccount(account);
            }
        }catch (Exception e){
            throw new IllegalArgumentException("Account create fail");  
        }
    }

    public Account login(String username, String password) throws Exception{
        
        return accountDAO.login(username, password);
    }
    
    public Account getAccountById(int accountId) throws Exception{
        return accountDAO.findAccountById(accountId);
    }

}
