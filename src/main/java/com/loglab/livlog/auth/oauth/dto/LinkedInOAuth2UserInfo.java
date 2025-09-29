package com.loglab.livlog.auth.oauth.dto;

import java.util.Map;

public class LinkedInOAuth2UserInfo extends OAuth2UserInfo {

    public LinkedInOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);

    }

    @Override
    public String getId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getName() {
        return String.valueOf(attributes.get("localizedFirstName")) +
                " " + String.valueOf(attributes.get("localizedLastName"));
    }

    @Override
    public String getEmail() {
        return String.valueOf(attributes.get("emailAddress"));
    }

    @Override
    public String getImageUrl() {
        return String.valueOf(attributes.get("profilePicture"));
    }
}
