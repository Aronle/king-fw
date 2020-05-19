package com.mars.fw.security.authorization.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Component
public class AccessMatchImpl implements AccessMatch {

    private static final String ROOT_URI = "/";
    private static final String INDEX_URI = "/index.html";
    private static final String LOGIN_URI = "/login.html";
    private static final String SWAGGER_UI_HTML = "/swagger-ui.html";


    private List<String> accessList = new ArrayList<String>() {
        {
            add("/hx/login");
            add("/shop/login");
            add("/hx/loginDev");
            add("/hx/open/api/getSign");
            add("/index.html");
            add("/manifest.json");
            add("/robots.txt");
            add("/service-worker.js");
            add("/precache-manifest.eea302037a9c2783bdf341d6c2dd2ca2.js");
            add(ROOT_URI);
            add(INDEX_URI);
            add(LOGIN_URI);
        }
    };

    private List<String> accessPattenList = new ArrayList<String>() {
        {
            add(SWAGGER_UI_HTML);
            add("/swagger-resources");
            add("/doc.html");
            add("/images/");
            add("/webjars/");
            add("/v2/api-docs");
            add("/configuration/ui");
            add("/index.html/");
            add("/manifest.json/");
            add("/robots.txt/");
        }
    };


    @Override
    public boolean isAccessSignUri(HttpServletRequest request) {
        return true;
    }


    @Override
    public boolean isAccessUri(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String contextPath = request.getContextPath();

        for (String accessUri : accessList) {
            if ((contextPath + accessUri).equals(requestUri)) {
                return true;
            }
        }
        for (String accessUri : accessPattenList) {
            if (null != requestUri && requestUri.startsWith(contextPath + accessUri)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 添加过滤路径（不包含项目名）
     *
     * @param accessUri
     * @return
     */
    protected boolean addAccessUri(String accessUri) {
        return accessList.add(accessUri);
    }

    /**
     * 添加起始通配过滤路径（不包含项目名）
     *
     * @param accessPattenUri
     * @return
     */
    protected boolean addAccessPartenUri(String accessPattenUri) {
        return accessPattenList.add(accessPattenUri);
    }
}
