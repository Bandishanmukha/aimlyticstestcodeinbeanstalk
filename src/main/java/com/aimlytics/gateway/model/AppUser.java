package com.aimlytics.gateway.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.aimlytics.gateway.dto.AppUserDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "APP_USER")
public class AppUser {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "FIRST_NAME")
	private String firstName;
	@Column(name = "LASTNAME")
	private String lastName;
	@Column(name = "MOBILE_NUMBER")
	private String mobileNumber;
	@Column(name = "EMAIL")
	private String email;
	@Column(name = "USER_NAME", nullable = false, unique = true)
	private String userName;
	@Column(name = "PASSWORD")
	private String password;
	@Column(name = "ENABLED")
	private boolean enabled;
	@Column(name = "USER_STATUS")
	@Enumerated(EnumType.STRING)
	private Status userStatus;
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "APP_USER_ROLES", joinColumns = @JoinColumn(name = "USER_ID"), inverseJoinColumns = @JoinColumn(name = "ROLE_ID",unique = false ))
	private List<AppRoles> roles;
	@Column(name = "REF_ID", unique = true, nullable = false, updatable = false)
	private String refId;
	@Column(name = "LOGIN_ATTEMPTS")
	private int loginAttempts;
	@Column(name = "USER_TYPE")
	@Enumerated(EnumType.STRING)
	private UserType userType;

	public AppUser() {

	}

	public AppUser(AppUserDTO appUserDTO) {
		this.firstName = appUserDTO.getFirstName();
		this.lastName = appUserDTO.getLastName();
		this.mobileNumber = appUserDTO.getMobileNumber();
		this.email = appUserDTO.getEmail();
		this.userName = appUserDTO.getUserName();
		this.password = appUserDTO.getPassword();
		this.enabled = appUserDTO.isEnabled();
		this.userStatus = appUserDTO.getUserStatus();
		this.userType = appUserDTO.getUserType();
	}

	@PrePersist
	public void prePersist() {
		this.refId = UUID.randomUUID().toString();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Status getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(Status userStatus) {
		this.userStatus = userStatus;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public int getLoginAttempts() {
		return loginAttempts;
	}

	public void setLoginAttempts(int loginAttempts) {
		this.loginAttempts = loginAttempts;
	}

	@Transient
	public String fullName() {
		return this.lastName != null ? this.lastName : "" + this.firstName;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	@JsonIgnore
	public List<String> buildPermissions() {
		if (null != this.getRoles()) {
			return this.getRoles().stream().flatMap(role -> role.getPermissions().stream()).map(AppPermissions::getName)
					.collect(Collectors.toList());
		} else {
			return new ArrayList<>(0);
		}
	}

	public List<AppRoles> getRoles() {
		return roles;
	}

	public void setRoles(List<AppRoles> roles) {
		this.roles = roles;
	}
}
