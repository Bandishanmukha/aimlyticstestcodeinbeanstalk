package com.aimlytics.gateway.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "APP_ROLES")
public class AppRoles {
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "NAME")
	private String name;
	@Column(name = "CODE")
	private String code;
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "ROLES_PERMISSIONS",  joinColumns = @JoinColumn( name="ROLE_ID"),
    inverseJoinColumns = @JoinColumn( name="PERMISSION_ID", unique = false) )
	private List<AppPermissions> permissions;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public List<AppPermissions> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<AppPermissions> permissions) {
		this.permissions = permissions;
	}
	
	
	

}
