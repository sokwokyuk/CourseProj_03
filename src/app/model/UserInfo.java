package app.model;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
 
public class UserInfo {

	private final StringProperty accountName;
	private final StringProperty userID;
	private final StringProperty password;
	private final StringProperty remarks;

	public UserInfo() {
		this(null, null);
	}

	public UserInfo(String accountName, String userID) {
		this.accountName = new SimpleStringProperty(accountName);
		this.userID = new SimpleStringProperty(userID);
		this.password = new SimpleStringProperty("password");
		this.remarks = new SimpleStringProperty("remarks");
	}

	public String getAccountName() {
		return accountName.get();
	}

	public void setAccountName(String accountName) {
		this.accountName.set(accountName);
	}

	public StringProperty accountNameProperty() {
		return accountName;
	}

	public String getUserID() {
		return userID.get();
	}

	public void setUserID(String userID) {
		this.userID.set(userID);
	}

	public StringProperty userIDProperty() {
		return userID;
	}

	public String getPassword() {
		return password.get();
	}

	public void setPassword(String password) {
		this.password.set(password);
	}

	public StringProperty passwordProperty() {
		return password;
	}

	public String getRemarks() {
		return remarks.get();
	}

	public void setRemarks(String remarks) {
		this.remarks.set(remarks);
	}

	public StringProperty remarksProperty() {
		return remarks;
	}

}