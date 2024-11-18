package com.mad.MAD.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mad.MAD.model.ResponseModel;
import com.mad.MAD.model.UserModel;
import com.mad.MAD.service.userService;

@RestController
@RequestMapping("/userDetails")
public class userController {

	@Autowired
	userService service;
	
	ResponseModel p;
	
//	------------------------Get All User----------------------------
	@GetMapping("/allUser")
	public ResponseEntity<Object> allUser() {
		ResponseModel p = new ResponseModel();
		
		System.out.println(service.findAll());
		List allUser = service.findAll();
		
		p.setCode("200");
		p.setMessage("ALL Users");
		p.setData(allUser);
		
		return ResponseEntity.ok(p);
	}
	
//	------------------------Save User------------------------------------
	@PostMapping("/saveUser")
    public String saveEmployee(@RequestBody UserModel user) {
		UUID uuid = UUID.randomUUID();
		String uuidAsString = uuid.toString();
		
		user.setUser_id(uuidAsString);
		
		System.out.println("User Data: " + user);
		
		try {
			service.saveUser(user);
	        return "Saved";
		} catch(Exception e) {
			e.printStackTrace();
			return "Failed";
		}
        
    }
	
//	--------------------------Get User Data----------------------------------
	@PostMapping("/loginUser/{user_name}/{password}")
	public UserModel getUserData(@PathVariable String user_name, @PathVariable String password) {
		UserModel data = service.getuserData(user_name, password);
		System.out.println("User Name: " + user_name);
		System.out.println("Password: " + password);
		
		System.out.println("Data Sent: " + data);
		try {
			return data;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
//	----------------------------Get User Data through userId-------------------------
	@PostMapping("/getUserDetails/{user_Id}")
	public UserModel getuserDetails(@PathVariable String user_Id) {
		System.out.println("User Id: " + user_Id.toString());
		
		try {
			UserModel data = service.getUserDetails(user_Id);
			return data;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
//	--------------------Update Weight-------------------------------
	@PostMapping("/updateWeight/{weight}/{user_id}")
	public String saveUpdatedWeight(@PathVariable int weight, @PathVariable String user_id) {
		System.out.println("Weight: " + weight);
		
		try {
			service.saveUpdatedWieght(weight, user_id);
			return "Saved Successful";
			
		} catch(Exception e) {
			e.printStackTrace();
			return "Failed";
		}
	}
}
