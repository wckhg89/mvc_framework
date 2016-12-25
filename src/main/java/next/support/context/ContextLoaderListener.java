package next.support.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import core.jdbc.ConnectionManager;


/**
 * http://yi-chi.tistory.com/20
 */
@WebListener
public class ContextLoaderListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(ContextLoaderListener.class);

    @Override
    // 웹 어플리케이션의 시작시 실행
    public void contextInitialized(ServletContextEvent sce) {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());

        logger.info("Completed Load ServletContext!");
    }

    // 웹 어플리케이션의 종료시 실행
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Destroyed ServletContext");
    }
}
