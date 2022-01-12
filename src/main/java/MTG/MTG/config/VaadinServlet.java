package MTG.MTG.config;

import com.vaadin.flow.server.*;
import javax.servlet.ServletException;

public class VaadinServlet extends com.vaadin.flow.server.VaadinServlet
        implements SessionInitListener, SessionDestroyListener {

    @Override
    protected VaadinServletService createServletService() throws ServletException, ServiceException {
        return super.createServletService();
    }
    @Override
    protected void servletInitialized() throws ServletException  {
        super.servletInitialized();
        getService().addSessionInitListener(this);
        getService().addSessionDestroyListener(this);
    }

    @Override
    public void sessionInit(SessionInitEvent event) throws ServiceException {

    }

    @Override
    public void sessionDestroy(SessionDestroyEvent event) {

    }
}