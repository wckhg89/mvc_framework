package core.nmvc;

import com.google.common.collect.Maps;

import org.reflections.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import core.annotation.RequestMapping;
import core.annotation.RequestMethod;

public class AnnotationHandlerMapping {
    private Object[] basePackage;
    private ControllerScanner controllerScanner;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    @SuppressWarnings("unchecked")
    public void initialize() {
        controllerScanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> instanceMap = controllerScanner.getInstanceMap();

        for (Class<?> clazz : instanceMap.keySet()) {
            Set<Method> methodSet = ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(RequestMapping.class));

            for (Method method : methodSet) {
                RequestMapping rm = method.getAnnotation(RequestMapping.class);
                handlerExecutions.put(createHandlerKey(rm),
                        new HandlerExecution(instanceMap.get(method.getDeclaringClass()), method));
            }
        }

    }

    private HandlerKey createHandlerKey(RequestMapping rm) {
        return new HandlerKey(rm.value(), rm.method());
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
