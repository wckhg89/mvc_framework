package core.nmvc;

import com.google.common.collect.Maps;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

import core.annotation.Controller;

/**
 * Created by kanghonggu on 2017-01-02.
 */
public class ControllerScanner {
    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    private Reflections reflections;

    public ControllerScanner(Object... basePackages) {
        this.reflections = new Reflections(basePackages);

    }

    public Map<Class<?>, Object> getInstanceMap() {
        return this.createInstance(reflections.getTypesAnnotatedWith(Controller.class));
    }

    private Map<Class<?>, Object> createInstance(Set<Class<?>> preInstance) {
        Map<Class<?>, Object> controllers = Maps.newHashMap();
        try {
            for (Class<?> clazz : preInstance) {
                controllers.put(clazz, clazz.newInstance());
            }
        } catch (InstantiationException | IllegalAccessException e) {
            log.error(e.getMessage());
        }

        return controllers;
    }

}
