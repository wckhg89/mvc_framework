package core.nmvc;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.ModelAndView;

public class HandlerExecution {
    private Object declaredObject;
    private Method refMethod;

    public HandlerExecution(Object declaredObject, Method refMethod) {
        this.declaredObject = declaredObject;
        this.refMethod = refMethod;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return (ModelAndView) refMethod.invoke(declaredObject, request, response);
    }
}
