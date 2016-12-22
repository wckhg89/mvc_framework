package next.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by kanghonggu on 2016-12-22.
 */
public interface Controller {

    String execute (HttpServletRequest request, HttpServletResponse response) throws Exception;

}
