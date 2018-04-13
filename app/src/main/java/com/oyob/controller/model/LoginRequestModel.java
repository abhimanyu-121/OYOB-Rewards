package com.oyob.controller.model;

/**
 * Created by Trush on 01/03/16.
 */
public class LoginRequestModel {


    NameModel name;
    FacebookModel facebook;
    TwitterModel twitter;
    Instagram Instagram;
    MailModel mail;
    String referralCode;
    String loginType;

    public NameModel getName() {
        return name;
    }

    public void setName(NameModel name) {
        this.name = name;
    }

    public FacebookModel getFacebook() {
        return facebook;
    }

    public void setFacebook(FacebookModel facebook) {
        this.facebook = facebook;
    }

    public TwitterModel getTwitter() {
        return twitter;
    }

    public void setTwitter(TwitterModel twitter) {
        this.twitter = twitter;
    }

    public LoginRequestModel.Instagram getInstagram() {
        return Instagram;
    }

    public void setInstagram(LoginRequestModel.Instagram instagram) {
        Instagram = instagram;
    }

    public MailModel getMail() {
        return mail;
    }

    public void setMail(MailModel mail) {
        this.mail = mail;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }







    public class Instagram{
        String InstagramId;
        String InstagramToken;
        String email;
        String username;

        public String getInstagramId() {
            return InstagramId;
        }

        public void setInstagramId(String instagramId) {
            InstagramId = instagramId;
        }

        public String getInstagramToken() {
            return InstagramToken;
        }

        public void setInstagramToken(String instagramToken) {
            InstagramToken = instagramToken;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }




}
