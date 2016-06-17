package net.smartcoder.shack.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.smartcoder.shack.dao.UserDao;
import net.smartcoder.shack.model.User;

@Service("userService")
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDao dao;

	@Override
	public User findById(long id) {
		return dao.findById(id);
	}

	@Override
	public User findByUsername(String username) {
		return dao.findByUsername(username);
	}

}
