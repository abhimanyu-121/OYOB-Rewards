package com.oyob.controller.model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2777769375200066195L;
	private int id;
	private int client_id;
	private int domain_id;
	private String type;
	private String username;
	private String email;
	private String first_name;
	private String last_name;
	private String state;
	private String country;
	private String mobile;
	private String card_ext;
	private String client_name;
	private String newsletter;
	private Bitmap clientBanner;
	private Bitmap myMembershipCard;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getClient_id() {
		return client_id;
	}
	public void setClient_id(int client_id) {
		this.client_id = client_id;
	}
	public int getDomain_id() {
		return domain_id;
	}
	public void setDomain_id(int domain_id) {
		this.domain_id = domain_id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getCard_ext() {
		return card_ext;
	}
	public void setCard_ext(String card_ext) {
		this.card_ext = card_ext;
	}
	public String getClient_name() {
		return client_name;
	}
	public void setClient_name(String client_name) {
		this.client_name = client_name;
	}
	public String getNewsletter() {
		return newsletter;
	}
	public void setNewsletter(String newsletter) {
		this.newsletter = newsletter;
	}
	public Bitmap getClientBanner() {
		return clientBanner;
	}
	public void setClientBanner(Bitmap clientBanner) {
		this.clientBanner = clientBanner;
	}
	public Bitmap getMyMembershipCard() {
		return myMembershipCard;
	}
	public void setMyMembershipCard(Bitmap myMembershipCard) {
		this.myMembershipCard = myMembershipCard;
	}
	
}
