package com.tutv.android.domain;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class User {
	@PrimaryKey
	private int id;

	private String mail;

	private boolean admin;

	private String avatar;

	private boolean banned;

	private String userName;

	public void setMail(String mail){
		this.mail = mail;
	}

	public String getMail(){
		return mail;
	}

	public void setAdmin(boolean admin){
		this.admin = admin;
	}

	public boolean isAdmin(){
		return admin;
	}

	public void setAvatar(String avatar){
		this.avatar = avatar;
	}

	public String getAvatar(){
		return avatar;
	}

	public void setBanned(boolean banned){
		this.banned = banned;
	}

	public boolean isBanned(){
		return banned;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setUserName(String userName){
		this.userName = userName;
	}

	public String getUserName(){
		return userName;
	}
}
