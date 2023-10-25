package Service;

import java.util.List;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    /**
     * @return all accounts
     */
    public List<Account> getAllAccounts(){
        return accountDAO.getAllAccounts();
    }

    /**
     * 
     * @param id
     * @return an account identified by its id
     */
    public Account getAccountById(int id){
        return accountDAO.getAccountById(id);
    }

    /**
     * @param account
     * @return the persisted account if it meets the conditions, null if not
     */
    public Account addAccount(Account account){
        if (!account.getUsername().equals("") && 
        account.getPassword().length() > 4 
        && !accountDAO.getAllAccounts().contains(account)){
           return accountDAO.insertAccount(account);
        }
        return null;
    }

    /**
     * 
     * @param account (with only a username and password)
     * @return account, with an account id, username, and password
     */
    public Account loginAccount(Account account){
        Account loggedInAccount = accountDAO.loginAccount(account);
        if (accountDAO.getAllAccounts().contains(loggedInAccount)){
            return loggedInAccount;
        }
        return null;
    }
}
