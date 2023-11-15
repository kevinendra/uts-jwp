package com.uts.jwp.domain;

public class Instructors {
    private String nip;
    private String fullName;
    private String email;
    private String nophone;

    public Instructors(){

    }
    public String getnip(){
        return nip;
    }
    public void setnip(String nip){
        this.nip = nip;
    }
    public String getfullName(){
        return fullName;
    }
    public void setfullName(String fullName){
        this.fullName = fullName;
    }
    public String getemail(){
        return email;
    }
    public void setemail(String email){
        this.email = email;
    }
    public String getnophone(){
        return nophone;
    }
    public void setnophone(String nophone){
        this.nophone = nophone;
    }
}
