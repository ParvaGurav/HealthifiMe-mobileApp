package com.mad.MAD.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mad.MAD.model.UserModel;

@Repository
public interface userRepo extends JpaRepository<UserModel, Integer> {
	
	@Query(value = "SELECT * FROM user WHERE user_name = ?1 AND password = ?2", nativeQuery = true)
	UserModel getByUserNameAndPassword(String user_name, String password);
	
	@Query(value = "select * from user where user_id = ?1", nativeQuery = true)
	UserModel getUserByuserId(String user_Id);
	
	@Modifying
    @Query(value = "update user set weight = ?1 where user_id = ?2", nativeQuery = true)
    void updateWeight(int weight, String userId);

}
