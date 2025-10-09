package com.loglab.livlog.auth.oauth.dto;

import java.util.Map;

@SuppressWarnings("unchecked")
public class NaverOAuth2UserInfo extends OAuth2UserInfo {

    private final Map<String, Object> response;

    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
        this.response = (Map<String, Object>) attributes.get("response");
    }

    @Override
    public String getId() {
        return String.valueOf(response.get("id"));
    }

    @Override
    public String getName() {
        return String.valueOf(response.get("name"));
    }

    @Override
    public String getEmail() {
        return String.valueOf(response.get("email"));
    }

    @Override
    public String getImageUrl() {
        return String.valueOf(response.get("profile_image"));
    }
}
