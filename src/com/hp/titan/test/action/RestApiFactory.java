package com.hp.titan.test.action;

import java.net.URI;
import java.net.URISyntaxException;

import com.hp.titan.common.constants.TitanContent;
import com.rallydev.rest.RallyRestApi;

public class RestApiFactory {

    //Specify your Rally server
    private static final String SERVER = TitanContent.CASE_DEFECT_PARA_RALLY_URL;

    //Specify your WSAPI version
    private static final String WSAPI_VERSION = TitanContent.CASE_DEFECT_PARA_RALLY_VERSION;

    //Specify your Rally username
    private static final String USERNAME = TitanContent.CASE_DEFECT_PARA_RALLY_USERNAME;

    //Specify your Rally password
    private static final String PASSWORD = TitanContent.CASE_DEFECT_PARA_RALLY_PWD;

    //If using a proxy specify full url, like http://my.proxy.com:8000
    private static final String PROXY_SERVER = TitanContent.CASE_DEFECT_PARA_RALLY_PROXY;

    //If using an authenticated proxy server specify the username and password
    private static final String PROXY_USERNAME = null;
    private static final String PROXY_PASSWORD = null;

    public RallyRestApi getRestApi() throws URISyntaxException {
        RallyRestApi restApi = new RallyRestApi(new URI(SERVER), USERNAME, PASSWORD);
        if (PROXY_SERVER != null) {
            URI uri = new URI(PROXY_SERVER);
            if (PROXY_USERNAME != null) {
                restApi.setProxy(uri, PROXY_USERNAME, PROXY_PASSWORD);
            } else {
                restApi.setProxy(uri);
            }
        }

        restApi.setWsapiVersion(WSAPI_VERSION);

        return restApi;
    }
}
