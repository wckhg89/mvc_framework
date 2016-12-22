package next.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by kanghonggu on 2016-12-22.
 */
public class FowardController {

    private String forwardUrl;

    public FowardController(String forwardUrl) {
        this.forwardUrl = forwardUrl;
    }

    public void forward(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        RequestDispatcher rd = req.getRequestDispatcher(forwardUrl);
        rd.forward(req, resp);
    }
}
