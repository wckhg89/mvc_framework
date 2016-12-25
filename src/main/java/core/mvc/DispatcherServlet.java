package core.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * DispatcherServlet 이란
 * 프론트 컨트롤러이다. 자바 서버의 서블릿 컨테이너에서 HTTP 프로토콜을 통해 들어오는
 * 모든 요청을 프리젠테이션 계층의 제일 앞에 둬서 중앙집중식으로 처리할 수 있는 컨트롤러이다.
 *
 * DispatcherServlet은 서블릿 컨테이너가 생성하고 관리하는 오브젝트이다.
 * 즉, 스프링이 관여하는 오브젝트가 아니므로 직접 DI 해줄 방법이 없다. 대신 web.xml 에서
 * 설정한 웹어플리케이션 컨텍스트를 참고하여 필요한 전략을 DI 하여 사용할 수 있다.
 */

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private RequestMapping rm;

    @Override
    public void init() throws ServletException {
        rm = new RequestMapping();
        rm.initMapping();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        Controller controller = rm.findController(req.getRequestURI());
        ModelAndView mav;
        try {
            mav = controller.execute(req, resp);
            View view = mav.getView();
            view.render(mav.getModel(), req, resp);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }
}
