package com.aimlytics.gateway.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.groups.Default;

import com.aimlytics.gateway.validation.group.OnReset;
import com.aimlytics.gateway.validation.group.OnResetByAdmin;

import static com.aimlytics.gateway.constants.AppConstants.*;

public class ChangePasswordDTO {

	@NotBlank(message = CURRENT_PIN_REQUIRED_4107)
	private String currentPassword;
	@NotBlank(message = NEW_PIN_REQUIRED_4103, groups = {Default.class, OnReset.class, OnResetByAdmin.class})
	private String newPassword;
	@NotBlank(message = CONFIRM_PIN_REQUIRED_4104, groups = {Default.class, OnReset.class, OnResetByAdmin.class})
	private String confirmPassword;
	@NotBlank(message = USERNAME_REQUIRED_4108, groups = {OnResetByAdmin.class})
	private String userName;

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public boolean checkNewAndConfirmPasswordOrSame() {
		return this.newPassword.equals(confirmPassword);
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
