package net.smartcoder.shack.dao;

import java.util.List;

import net.smartcoder.shack.model.UserProfile;

public interface UserProfileDao {
	
	List<UserProfile> findAll();
	
	UserProfile findByType(String type);
	
	UserProfile findById(long id);

}
