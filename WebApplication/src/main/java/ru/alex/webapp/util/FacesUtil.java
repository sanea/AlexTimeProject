package ru.alex.webapp.util;

import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

public class FacesUtil implements Serializable {
    private static final long serialVersionUID = 1L;

    private FacesUtil() {
    }

    public static ExternalContext getExternalContext() {
        FacesContext facesContext = getFacesContext();
        ExternalContext externalContext = null;

        if (facesContext != null) {
            externalContext = facesContext.getExternalContext();
        }

        return externalContext;
    }

    public static FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    public static HttpServletRequest getHttpServletRequest() {
        Object request = getRequest();
        HttpServletRequest servletRequest = null;

        if (request != null) {
            servletRequest = (HttpServletRequest) request;
        }

        return servletRequest;
    }

    public static HttpServletResponse getHttpServletResponse() {
        Object response = getResponse();
        HttpServletResponse servletResponse = null;

        if (response != null) {
            servletResponse = (HttpServletResponse) response;
        }

        return servletResponse;
    }

    public static Object getRequest() {
        ExternalContext externalContext = getExternalContext();
        Object request = null;

        if (externalContext != null) {
            request = externalContext.getRequest();
        }

        return request;
    }

    public static Object getResponse() {
        ExternalContext externalContext = getExternalContext();
        Object response = null;

        if (externalContext != null) {
            response = externalContext.getResponse();
        }

        return response;
    }

    public static UIViewRoot getViewRoot() {
        FacesContext facesContext = getFacesContext();
        UIViewRoot uiViewRoot = null;

        if (facesContext != null) {
            uiViewRoot = facesContext.getViewRoot();
        }

        return uiViewRoot;
    }
}