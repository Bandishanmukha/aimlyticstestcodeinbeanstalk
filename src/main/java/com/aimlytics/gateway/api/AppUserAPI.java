package com.aimlytics.gateway.api;

import javax.validation.Valid;

import com.aimlytics.gateway.validation.group.OnReset;
import com.aimlytics.gateway.validation.group.OnResetByAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.aimlytics.gateway.dto.AppUserDTO;
import com.aimlytics.gateway.dto.ChangePasswordDTO;
import com.aimlytics.gateway.dto.Response;
import com.aimlytics.gateway.service.AppUserService;

@RestController
@RequestMapping("user")
public class AppUserAPI {

	private static final Logger logger = LoggerFactory.getLogger(AppUserAPI.class);

	@Autowired
	private AppUserService appUserService;

	@PostMapping(value = "create")
	public ResponseEntity<Response<String>> saveAppUser(@RequestBody AppUserDTO appUserDTO) {
		return new ResponseEntity<>(appUserService.saveAppUser(appUserDTO), HttpStatus.CREATED);
	}
	
	@PostMapping(value = "changePassword")
	public ResponseEntity<Response<Boolean>> changePassword(@RequestBody @Valid ChangePasswordDTO changePasswordDTO,
			@AuthenticationPrincipal String user) {
		return new ResponseEntity<>(appUserService.changePassword(user, changePasswordDTO),
				HttpStatus.OK);

	}

	@PostMapping(value = "resetPassword")
	public ResponseEntity<Response<Boolean>> resetPassword(@RequestBody @Validated({OnReset.class}) ChangePasswordDTO changePasswordDTO,
			@AuthenticationPrincipal String user) {
		logger.info("User Name while reset password :: {}", user);
		changePasswordDTO.setUserName(user);
		return new ResponseEntity<>(appUserService.resetPassword(changePasswordDTO),
				HttpStatus.OK);
	}
	
	@PutMapping(value = "updateInfo")
	public ResponseEntity<Response<Boolean>> updateAppUser(@RequestBody AppUserDTO appUserDTO, @AuthenticationPrincipal String user) {
		return new ResponseEntity<>(appUserService.updateAppUser(appUserDTO, user), HttpStatus.OK);
	}
	
	@PutMapping(value = "updateMobileNo")
	public ResponseEntity<Response<Boolean>> updateAppUserMobileNo(@RequestBody AppUserDTO appUserDTO, @AuthenticationPrincipal String user) {
		return new ResponseEntity<>(appUserService.updateMobileNumber(appUserDTO, user), HttpStatus.OK);
	}
	
	@PutMapping(value = "updateAdmin")
	public ResponseEntity<Response<Boolean>> updateAdminUser(@RequestBody AppUserDTO appUserDTO, @AuthenticationPrincipal String user) {
		return new ResponseEntity<>(appUserService.updateAdminUser(appUserDTO, user), HttpStatus.OK);
	}
	
	@PutMapping(value = "changeStatus")
	public ResponseEntity<Response<Boolean>> changeAppUserStatus(@RequestBody AppUserDTO appUserDTO) {
		return new ResponseEntity<>(appUserService.changeStatus(appUserDTO), HttpStatus.OK);
	}
	
	@PostMapping(value = "status/{userUid}")
	public Response<Boolean> activeDeactive(@PathVariable("userUid") String userUid) {
		appUserService.activeDeactive(userUid);
		return Response.<Boolean>builder().body(true).build();
	}

	@DeleteMapping("{refId}")
	public ResponseEntity<Response<Boolean>> deleteUser(@PathVariable String refId) {
		return ResponseEntity.ok(appUserService.deleteUser(refId));
	}

	@PutMapping("reset/password/byAdmin")
	public ResponseEntity<Response<Boolean>> resetPswd(@RequestBody @Validated({OnResetByAdmin.class}) ChangePasswordDTO changePasswordDTO) {
		return ResponseEntity.ok(this.appUserService.resetPassword(changePasswordDTO));
	}

	@PutMapping("update/statusAndPassword")
	public ResponseEntity<Response<Boolean>> updateStatusAndPassword(@RequestBody AppUserDTO dto) {
		return ResponseEntity.ok(this.appUserService.updateStatusAndPassword(dto));
	}
}
