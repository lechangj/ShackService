package net.smartcoder.shack.service;

import net.smartcoder.shack.model.User;

public interface UserService {
	
	User findById(long id);
	
	User findByUsername(String username);

}
