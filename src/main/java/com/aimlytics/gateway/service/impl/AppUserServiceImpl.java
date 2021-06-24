package com.aimlytics.gateway.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.aimlytics.gateway.dto.AppUserDTO;
import com.aimlytics.gateway.dto.ChangePasswordDTO;
import com.aimlytics.gateway.dto.Response;
import com.aimlytics.gateway.exceptions.PasswordException;
import com.aimlytics.gateway.exceptions.RecordNotFoundException;
import com.aimlytics.gateway.model.AppUser;
import com.aimlytics.gateway.model.Status;
import com.aimlytics.gateway.repositories.AppRolesRepository;
import com.aimlytics.gateway.repositories.AppUserRepository;
import com.aimlytics.gateway.service.AppUserService;

import java.util.Locale;
import java.util.Optional;

import static com.aimlytics.gateway.constants.AppConstants.USER_NOT_FOUND_3101;
import static com.aimlytics.gateway.constants.AppConstants.WRONG_CURRENT_PASSWORD_4106;
import static com.aimlytics.gateway.constants.AppConstants.PSWD_UPDATED_1104;
import static com.aimlytics.gateway.constants.AppConstants.PIN_RESET_SUCCESS_1101;
import static com.aimlytics.gateway.constants.AppConstants.PINS_MISMATCH_4102;

@Slf4j
@Service
public class AppUserServiceImpl implements AppUserService {
	

	@Autowired
	private AppUserRepository appUserRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	@Qualifier("messageSource")
	private MessageSource messageSource;
	
	@Autowired
	private AppRolesRepository appRolesRepository;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Response<String> saveAppUser(AppUserDTO appUserDTO) {
		AppUser appUser = new AppUser(appUserDTO);
		populateRoles(appUser);
		appUser.setUserStatus(Status.ACTIVE);
		appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
		log.info("USER TYPE :: {}", appUser.getUserType());
		appUser = appUserRepository.save(appUser);
		return Response.<String>builder().body(appUser.getRefId()).build();
	}

	private void populateRoles(AppUser appUser) {
		appUser.setRoles(appRolesRepository.findByCodeIn(appUser.getUserType().getRoles()));
	}

	@Override
	public Response<Boolean> changePassword(String userName, ChangePasswordDTO changePasswordDTO) {
		AppUser appUser = getUserByUsername(userName);
		if (!passwordEncoder.matches(changePasswordDTO.getCurrentPassword(), appUser.getPassword())) {
			throw new PasswordException(messageSource.getMessage(WRONG_CURRENT_PASSWORD_4106, null, Locale.ENGLISH), WRONG_CURRENT_PASSWORD_4106);
		}
		if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
			return Response.<Boolean>builder().body(true)
			.messageCode(PINS_MISMATCH_4102)
			.message(messageSource.getMessage(PINS_MISMATCH_4102, null, Locale.ENGLISH)).build();
		}
		appUser.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
		appUserRepository.save(appUser);
		return Response.<Boolean>builder().body(true)
					.messageCode(PSWD_UPDATED_1104)
					.message(messageSource.getMessage(PSWD_UPDATED_1104, null, Locale.ENGLISH)).build();
	}

	@Override
	public Response<Boolean> resetPassword(ChangePasswordDTO changePasswordDTO) {
		if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
			return Response.<Boolean>builder().body(true)
			.messageCode(PINS_MISMATCH_4102)
			.message(messageSource.getMessage(PINS_MISMATCH_4102, null, Locale.ENGLISH)).build();
		}
		AppUser appUser = getUserByUsername(changePasswordDTO.getUserName());
		appUser.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
		appUserRepository.save(appUser);
		return Response.<Boolean>builder().body(true)
				.messageCode(PIN_RESET_SUCCESS_1101)
				.message(messageSource.getMessage(PIN_RESET_SUCCESS_1101, null, Locale.ENGLISH)).build();
	}

	@Override
	public Response<Boolean> updateAppUser(AppUserDTO appUserDTO, String username) {
		AppUser appUser = getUserByUsername(username);
		appUser.setFirstName(appUserDTO.getFirstName());
		appUser.setLastName(appUserDTO.getLastName());
		appUser.setEmail(appUserDTO.getEmail());
		appUserRepository.save(appUser);
		return Response.<Boolean>builder().body(true).build();
	}

	@Override
	public Response<Boolean> updateMobileNumber(AppUserDTO appUserDTO, String username) {
		AppUser appUser = appUserRepository.findByUserName(username).orElseThrow(RuntimeException::new);
		appUser.setMobileNumber(appUserDTO.getMobileNumber());
		appUser.setUserName(appUserDTO.getMobileNumber());
		appUserRepository.save(appUser);
		return Response.<Boolean>builder().body(true).build();
	}

	@Override
	public Response<Boolean> updateAdminUser(AppUserDTO appUserDTO, String username) {
		AppUser appUser = getUserByUsername(username);
		appUser.setFirstName(appUserDTO.getFirstName());
		appUser.setLastName(appUserDTO.getLastName());
		appUser.setUserStatus(appUserDTO.getUserStatus());
		appUser.setMobileNumber(appUserDTO.getMobileNumber());
		appUserRepository.save(appUser);
		return Response.<Boolean>builder().body(true).build();
	}

	private AppUser getUserByUsername(String username) {
		return appUserRepository.findByUserName(username)
				.orElseThrow(() -> new RecordNotFoundException(messageSource.getMessage(USER_NOT_FOUND_3101, null, Locale.ENGLISH), USER_NOT_FOUND_3101));
	}
	
	@Override
	public Response<Boolean> changeStatus(AppUserDTO appUserDTO) {
		AppUser appUser = this.getUserByUsername(appUserDTO.getUserName());
		appUser.setUserStatus(appUserDTO.getUserStatus());
		appUserRepository.save(appUser);
		return Response.<Boolean>builder().body(true).build();
	}

	@Override
	public Response<Boolean> deleteUser(String refId) {
		appUserRepository.deleteByRefId(refId);
		return Response.<Boolean>builder()
				.body(true)
				.message("user deleted successfully")
				.build();
	}

	@Override
	public Response<Boolean> updateStatusAndPassword(AppUserDTO appUserDTO) {
		AppUser appUser = getUserByUsername(appUserDTO.getUserName());
		appUser.setUserStatus(Status.ACTIVE);
		appUser.setPassword(passwordEncoder.encode(appUserDTO.getPassword()));
		appUserRepository.save(appUser);
		return Response.<Boolean>builder().body(true)
				.message("Password and status updated")
				.build();
	}

	@Override
	public void activeDeactive(String userUid) {
	 	Optional<AppUser> opAppUser =  appUserRepository.findByRefId(userUid);
	 	opAppUser.ifPresent(user -> {
	 		user.setUserStatus(user.getUserStatus().equals(Status.ACTIVE)?Status.INACTIVE:Status.ACTIVE);
	 		appUserRepository.save(user);
	 	});
	}
}
