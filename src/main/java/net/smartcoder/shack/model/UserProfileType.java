package net.smartcoder.shack.model;

import java.io.Serializable;

public enum UserProfileType implements Serializable {
	
	USER("User"),
	SELLER("Seller"),
	ADMIN("Admin");
	
	String userProfileType;
	
	private UserProfileType(String userProfileType) {
		this.userProfileType = userProfileType;
	}

	public String getUserProfileType() {
		return userProfileType;
	}

}
