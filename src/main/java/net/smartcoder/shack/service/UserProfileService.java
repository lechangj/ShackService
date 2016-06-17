package net.smartcoder.shack.service;

import java.util.List;

import net.smartcoder.shack.model.UserProfile;

public interface UserProfileService {
 
    UserProfile findById(long id);
 
    UserProfile findByType(String type);
     
    List<UserProfile> findAll();

}
