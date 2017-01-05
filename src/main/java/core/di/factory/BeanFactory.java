package core.di.factory;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import core.annotation.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import org.springframework.beans.BeanUtils;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    // 아직 인스턴스화 되지 않은 bean들
    private Set<Class<?>> preInstanticateBeans;

    // 인스턴스화 된 객체들 관리
    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(Set<Class<?>> preInstanticateBeans) {
        this.preInstanticateBeans = preInstanticateBeans;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public Map<Class<?>, Object> getControllers() {
        Map<Class<?>, Object> controllers = Maps.newHashMap();
        for (Class<?> clazz : preInstanticateBeans) {
            if (clazz.isAnnotationPresent(Controller.class)) {
                controllers.put(clazz, beans.get(clazz));
            }
        }
        return controllers;
    }

    public void initialize() {
        for (Class<?> clazz : preInstanticateBeans) {
            instantiateClass(clazz);
        }
    }

    private Object instantiateClass(Class<?> clazz) {
        // Bean 저장소에 clazz에 해당하는 인스턴스가 이미 존재하면 해당 인스턴스 반환
        Object bean = getBean(clazz);
        if (bean != null) {
            return bean;
        }
        // clazz에 @Inject가 설정되어 있는 생성자를 찾는다. BeanFactoryUtils 활용
        Constructor<?> constructor = BeanFactoryUtils.getInjectedConstructor(clazz);
        // @Inject로 설정한 생성자가 없으면 Default 생성자로 인스턴스 생성 후 Bean 저장소에 추가 후 반환
        if (constructor == null) {
            bean = BeanUtils.instantiate(clazz);
            beans.put(clazz, bean);

            return bean;
        }
        // @Inject로 설정한 생성자가 있으면 찾은 생성자를 활용해 인스턴스 생성 후 Bean 저장소에 추가 후 반환
        bean = this.instantiateConstructor(constructor);
        beans.put(clazz, bean);

        return bean;
    }
    private Object instantiateConstructor(Constructor<?> constructor) {
        // 생성자 파라미터 인스턴스화 되기 전의 클래스
        Class<?>[] constructorParameterClazzs = constructor.getParameterTypes();
        // 생성자 파라민터 객체를 담기위함
        List<Object> argBeans = Lists.newArrayList();

        // 파라미터 클래스(인스터스전)들을 돌면서 인스턴스화 해서 argBeans에 담는다.
        for (Class<?> clazz : constructorParameterClazzs) {
            Object argBean = beans.get(clazz);

            // 이미 생성된 빈이면
            if (argBean != null) {
                argBeans.add(argBean);
            }

            // 아직 생성되지 않은 빈이면
            argBean = this.instantiateClass(BeanFactoryUtils.findConcreteClass(clazz, preInstanticateBeans));
            argBeans.add(argBean);
        }

        // 생성자를 통한 빈생성
        return BeanUtils.instantiateClass(constructor, argBeans.toArray());
    }

}
