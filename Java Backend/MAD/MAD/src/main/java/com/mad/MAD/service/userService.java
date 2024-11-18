package com.mad.MAD.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mad.MAD.model.UserModel;
import com.mad.MAD.repository.userRepo;

@Service
public class userService {

	@Autowired
	userRepo repo;
	
	public List<UserModel> findAll(){
		return repo.findAll();
	}
	
	public UserModel saveUser(UserModel user) {
        return repo.save(user);
    }
	
	public UserModel getuserData(String user_name, String password) {
		return (UserModel) repo.getByUserNameAndPassword(user_name, password);
	}
	
	public UserModel getUserDetails(String user_Id) {
		return repo.getUserByuserId(user_Id);
	}
	
	@Transactional
	public void saveUpdatedWieght(int weight, String user_id) {
		repo.updateWeight(weight, user_id);
	}
	
}
