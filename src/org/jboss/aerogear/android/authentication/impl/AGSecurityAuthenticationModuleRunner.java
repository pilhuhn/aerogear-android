/**
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.aerogear.android.authentication.impl;

import android.util.Log;
import com.google.gson.JsonObject;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import org.jboss.aerogear.android.Provider;
import org.jboss.aerogear.android.authentication.AuthenticationConfig;
import org.jboss.aerogear.android.http.HeaderAndBody;
import org.jboss.aerogear.android.http.HttpProvider;
import org.jboss.aerogear.android.impl.core.HttpProviderFactory;
import org.jboss.aerogear.android.impl.util.UrlUtils;
import org.json.JSONObject;

class AGSecurityAuthenticationModuleRunner {

    private static final String TAG = AGSecurityAuthenticationModuleRunner.class.getSimpleName();
    private final Provider<HttpProvider> httpProviderFactory = new HttpProviderFactory();
    private final URL baseURL;
    private final String loginEndpoint;
    private final URL loginURL;
    private final String logoutEndpoint;
    private final URL logoutURL;
    private final String enrollEndpoint;
    private final URL enrollURL;
    private final Integer timeout;

    /**
     * @param baseURL
     * @param config
     * @throws IllegalArgumentException if an endpoint can not be appended to
     * baseURL
     */
    public AGSecurityAuthenticationModuleRunner(URL baseURL, AuthenticationConfig config) {
        this.baseURL = baseURL;
        this.loginEndpoint = config.getLoginEndpoint();
        this.logoutEndpoint = config.getLogoutEndpoint();
        this.enrollEndpoint = config.getEnrollEndpoint();

        this.loginURL = UrlUtils.appendToBaseURL(baseURL, loginEndpoint);
        this.logoutURL = UrlUtils.appendToBaseURL(baseURL, logoutEndpoint);
        this.enrollURL = UrlUtils.appendToBaseURL(baseURL, enrollEndpoint);

        this.timeout = config.getTimeout();

    }

    public HeaderAndBody onEnroll(final Map<String, String> userData) {
        HttpProvider provider = httpProviderFactory.get(enrollURL, timeout);
        String enrollData = new JSONObject(userData).toString();
        return provider.post(enrollData);
    }

    public HeaderAndBody onLogin(final String username, final String password) {
        HttpProvider provider = httpProviderFactory.get(loginURL, timeout);
        String loginData = buildLoginData(username, password);
        return provider.post(loginData);
    }

    public void onLogout() {
        HttpProvider provider = httpProviderFactory.get(logoutURL, timeout);
        provider.post("");
    }

    public URL getBaseURL() {
        return baseURL;
    }

    public String getLoginEndpoint() {
        return loginEndpoint;
    }

    public String getLogoutEndpoint() {
        return logoutEndpoint;
    }

    public String getEnrollEndpoint() {
        return enrollEndpoint;
    }

    private String buildLoginData(String username, String password) {
        JsonObject response = new JsonObject();
        response.addProperty("username", username);
        response.addProperty("password", password);
        return response.toString();
    }
}
