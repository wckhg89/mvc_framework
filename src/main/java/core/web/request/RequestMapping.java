package core.web.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import core.web.servlet.DispatcherServlet;
import next.controller.Controller;
import next.controller.ListUserController;

/**
 * Created by kanghonggu on 2016-12-22.
 */
public class RequestMapping {
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private Map<String, Controller> mappings = new HashMap<>();

    public void initMapping() {
        mappings.put("/users", new ListUserController());

        logger.info("Initialized Request Mapping!");
    }

    public Controller findController(String url) {
        return mappings.get(url);
    }

    void put(String url, Controller controller) {
        mappings.put(url, controller);
    }
}
