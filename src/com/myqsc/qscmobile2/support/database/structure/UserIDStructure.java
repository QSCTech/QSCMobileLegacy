package com.myqsc.qscmobile2.support.database.structure;

public class UserIDStructure {
	public String uid;
	public String pwd;
	public Integer select;
	
	public UserIDStructure(){
		
	}
	
	public UserIDStructure(String uid, String pwd, Integer select){
		this.uid = uid;
		this.pwd = pwd;
		this.select = select;
	}
}
