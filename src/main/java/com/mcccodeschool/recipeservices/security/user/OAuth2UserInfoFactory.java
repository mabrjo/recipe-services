package com.mcccodeschool.recipeservices.security.user;

import com.mcccodeschool.recipeservices.exception.OAuth2Exception;
import com.mcccodeschool.recipeservices.security.user.OAuth2UserInfo;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String id, Map<String, Object> attributes) {
        if (id.equalsIgnoreCase("google")){
            return new GoogleOAuth2UserInfo(attributes);
        } else if (id.equalsIgnoreCase("github")){
            return new GithubOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2Exception("Try logging in with Google or Github!");
        }

    }

}
