package next.controller.qna;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Answer;
import next.model.Question;
import core.mvc.AbstractController;
import core.mvc.ModelAndView;

/**
 * https://beyondj2ee.wordpress.com/2013/02/28/%EB%A9%80%ED%8B%B0-%EC%93%B0%EB%A0%88%EB%93%9C-%ED%99%98%EA%B2%BD%EC%97%90%EC%84%9C-%EC%8A%A4%ED%94%84%EB%A7%81%EB%B9%88-%EC%A3%BC%EC%9D%98%EC%82%AC%ED%95%AD/
 */
public class ShowController extends AbstractController {
    private QuestionDao questionDao = new QuestionDao();
    private AnswerDao answerDao = new AnswerDao();
    // ShowController 는 DispatcherServlet(서블릿 컨테이너)에 의해서 한번만 초기화된다.
    // 따라서 하나의 오브젝트를 여러 스레드가 공유해서 사용한다.
    // 힙 영역에 존재하는 멤버변수(필드변수)는 여러 스레드에서 공유되는 영역이기 때문에
    // 스레드마다 별도로 존재하는 스텍영역에서 관리되는 로컬변수로 해당 필드를 옮겨줘야 스레드 세이프하게 동작한다.
    private Question question;
    private List<Answer> answers;

    @Override
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse response) throws Exception {
        Long questionId = Long.parseLong(req.getParameter("questionId"));

        question = questionDao.findById(questionId);
        answers = answerDao.findAllByQuestionId(questionId);

        ModelAndView mav = jspView("/qna/show.jsp");
        mav.addObject("question", question);
        mav.addObject("answers", answers);
        return mav;
    }
}
