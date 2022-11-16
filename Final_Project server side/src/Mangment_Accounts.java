import java.util.HashMap;
public  class Mangment_Accounts {
	private  String account;
	private   String password;
	public   HashMap<String,String> accounts=new HashMap<String,String>();//Creating HashMap
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAccount() {
		return this.account;
	}
	public void setAccount(String account) {
		this.account=account;
	}
	public Mangment_Accounts() {}
	public HashMap<String, String> getAccounts() {
		return accounts;
	}
	public void setAccounts(HashMap<String, String> accounts) {
		this.accounts = accounts;
	}
	public boolean create(String Account, String Password) {
		try {
			if(!accounts.containsKey(Account)&&!accounts.containsKey(Password))
				this.accounts.put(Account, Password);
			return true;	

		} catch (Exception e) {
			System.out.println("this Accounts is already exists");
			return false;
		}
	}
	public boolean if_is_exists(String Account) {
		try {
			if(accounts.containsKey(Account)) {
				return true;
			}
		} catch (Exception e) {
			System.out.println("this Accounts is already exists");
			return false;
		}
		return false;
	}
	public static void main(String[] args) {

	}

}
