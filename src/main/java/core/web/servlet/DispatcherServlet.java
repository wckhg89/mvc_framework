package core.web.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.web.request.RequestMapping;
import next.controller.Controller;

/**
 * Created by kanghonggu on 2016-12-22.
 */

/**
 * http://forarchitect.tistory.com/34
 * 서블릿은 최초 요청이 들어올때 초기화된다.
 *
 * 요청시 클래스 로딩 인스턴스화 초기화 설정을 해야한다.
 * 이렇게 되면 서버가 올라가고 맨처음 호출한 유저는 보통 시간보다 응답에 오랜 시간이 걸린다.
 *
 * 그것을 방지하기 위해 load-on-startup 이라는 속성을 사용한다.
 * 이 숫자가 0보다 크면 서버가 스타트되면서 서블릿을 초기화 한다.
 *
 * 숫자가 작을수록 우선순위가 높다.
 */
@WebServlet(name="dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet{
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";
    private RequestMapping requestMapping;

    /**
     * http://finerss.tistory.com/entry/%EC%84%9C%EB%B8%94%EB%A6%BF-%EC%83%9D%EB%AA%85%EC%A3%BC%EA%B8%B0%EC%99%80-API
     * 서블릿 생명주기
     * init() -> service() -> destroy()
     */


    /**
     * 컨테이너는 서블릿 인스턴스를 생성한 다음 init() 메소드를 호출한다.
     * 이 메소드는 service() 메소드전에 실행되어야 한다.
     *
     * 클라이언트의 요청을 처리하기전에 서블릿을 초기화할 기회를 주는것이다.
     * 초기화할 코드가  있다면 init() 메소드를 재정의할 수 있다.
     * @throws ServletException
     */
    @Override
    public void init() throws ServletException {
        requestMapping = new RequestMapping();
        requestMapping.initMapping();
    }


    /**
     * 최초 클라이언트의 요청을 받았을대, 컨테이너는 새로운 스레드를 생성하거나 스레드풀로부터 서블릿을 가져와서
     * 서블릿의 service() 메소드를 호출한다.
     *
     * service() 메소드를 재정의하지 않으면
     * 클라이언트의 HTTP 메소드 (GET, POST)를 참조하여 doGet() / doPost() 혹은 다른 메소드를 호출할지 판단한다.
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();

        Controller controller = requestMapping.findController(requestUri);

        try {
            String viewName = controller.execute(req,resp);
            move(viewName,req, resp);
        } catch (Exception e) {
            log.error("err - {}", e.getMessage());
            throw new ServletException(e.getMessage());
        }
    }

    private void move(String viewName, HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            resp.sendRedirect(viewName.substring(DEFAULT_REDIRECT_PREFIX.length()));
            return;
        }

        RequestDispatcher rd = req.getRequestDispatcher(viewName);
        rd.forward(req, resp);
    }
}
