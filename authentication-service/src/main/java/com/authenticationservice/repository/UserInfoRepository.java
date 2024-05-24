package com.authenticationservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.authenticationservice.entity.UserInfo;

import jakarta.transaction.Transactional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Integer>{
	
	Optional<UserInfo>findByUsername(String username);
	
	@Transactional
	@Modifying
	@Query("Update UserInfo a Set a.token = :#{#userInfo.token} where a.username = :#{#userInfo.username}")
	void updateTokenByUserInfo(@Param("userInfo") UserInfo userInfo);
	


}
