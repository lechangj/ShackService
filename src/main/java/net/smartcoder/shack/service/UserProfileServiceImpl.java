package net.smartcoder.shack.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.smartcoder.shack.dao.UserProfileDao;
import net.smartcoder.shack.model.UserProfile;

@Service("userProfileService")
@Transactional
public class UserProfileServiceImpl implements UserProfileService {
	
	@Autowired
	UserProfileDao dao;

	@Override
	public UserProfile findById(long id) {
		return dao.findById(id);
	}

	@Override
	public UserProfile findByType(String type) {
		return dao.findByType(type);
	}

	@Override
	public List<UserProfile> findAll() {
		return dao.findAll();
	}

}
