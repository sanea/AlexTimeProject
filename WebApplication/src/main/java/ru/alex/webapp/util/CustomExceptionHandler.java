package ru.alex.webapp.util;

import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import java.util.Iterator;

/**
 * @author Alexander.Isaenco
 */
public class CustomExceptionHandler extends ExceptionHandlerWrapper {

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
                FacesContext fc = context.getContext();
                NavigationHandler navHandler = fc.getApplication().getNavigationHandler();
                try {
                    navHandler.handleNavigation(fc, null, "/pages/error?faces-redirect=true&expired=true");
                } finally {
                    it.remove();
                }
            }
        }

        this.wrapped.handle();
    }
}
