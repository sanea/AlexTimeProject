package ru.alex.webapp.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;

/**
 * @author Alexander.Isaenco
 */
public class CustomExceptionHandler extends ExceptionHandlerWrapper {

    private static final Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

    private ExceptionHandler wrapped;

    public CustomExceptionHandler(ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return this.wrapped;
    }

    @Override
    public void handle() throws FacesException {
        Iterable<ExceptionQueuedEvent> events = this.wrapped.getUnhandledExceptionQueuedEvents();
        for (Iterator<ExceptionQueuedEvent> it = events.iterator(); it.hasNext(); ) {
            ExceptionQueuedEvent event = it.next();
            ExceptionQueuedEventContext context = event.getContext();
            Throwable t = context.getException();
            if (t instanceof ViewExpiredException) {
                logger.debug("handle ViewExpiredException");
                FacesContext fc = context.getContext();

                HttpServletRequest req = (HttpServletRequest)fc.getExternalContext().getRequest();
                String path = req.getServletPath().replace(".xhtml", "").replace(".jsf", "");
                logger.info("redirect to " + path);

                NavigationHandler navHandler = fc.getApplication().getNavigationHandler();
                try {
                    navHandler.handleNavigation(fc, null, path + "?faces-redirect=true&expired=true");
                } finally {
                    it.remove();
                }
            }
        }

        this.wrapped.handle();
    }
}
