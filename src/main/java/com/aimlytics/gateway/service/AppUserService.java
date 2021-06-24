package com.aimlytics.gateway.service;

import com.aimlytics.gateway.dto.AppUserDTO;
import com.aimlytics.gateway.dto.ChangePasswordDTO;
import com.aimlytics.gateway.dto.Response;

public interface AppUserService {

	Response<String> saveAppUser(AppUserDTO appUserDTO);
    
    Response<Boolean> changePassword(String userName, ChangePasswordDTO changePasswordDTO);

	Response<Boolean> resetPassword(ChangePasswordDTO changePasswordDTO);

	Response<Boolean> updateAppUser(AppUserDTO appUserDTO, String username);
	
	Response<Boolean> updateMobileNumber(AppUserDTO appUserDTO, String username);
	
	Response<Boolean> updateAdminUser(AppUserDTO appUserDTO, String username);
	
	Response<Boolean> changeStatus(AppUserDTO appUserDTO);

	Response<Boolean> deleteUser(String refId);

	Response<Boolean> updateStatusAndPassword(AppUserDTO appUserDTO);

	void activeDeactive(String userUid);
}
