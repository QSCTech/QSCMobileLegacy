package com.myqsc.qscmobile2.support.database.structure;

public class UserIDStructure {
	public String uid = "";
	public String pwd = "";
	public Integer select = 0, cancle = 0;
	
	public UserIDStructure(){
	}
	
	public UserIDStructure(String uid, String pwd, Integer select){
		this.uid = uid;
		this.pwd = pwd;
		this.select = select;
		this.cancle = 0; 
	}
}
