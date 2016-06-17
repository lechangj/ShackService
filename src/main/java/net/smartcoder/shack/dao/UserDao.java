package net.smartcoder.shack.dao;

import net.smartcoder.shack.model.User;

public interface UserDao {
	
	User findById(long id);
	
	User findByUsername(String username);

}
