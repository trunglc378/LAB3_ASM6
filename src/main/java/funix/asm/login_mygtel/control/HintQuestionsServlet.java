package funix.asm.login_mygtel.control;

import funix.asm.login_mygtel.dao.HintQuestionDAO;
import funix.asm.login_mygtel.dao.UserDAO;
import funix.asm.login_mygtel.model.HintQuestion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/first-login")
public class HintQuestionsServlet extends HttpServlet {
    private HintQuestionDAO hintQuestionDAO;
    private UserDAO userDAO;

    @Override
    public void init() {
        hintQuestionDAO = new HintQuestionDAO();
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);//lấy session hiện tại, không tạo mới
        if (session == null || session.getAttribute("userId") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        try {
            List<HintQuestion> hintQuestionList = hintQuestionDAO.getAllHintQuestions();
            req.setAttribute("hintQuestions", hintQuestionList);
            req.getRequestDispatcher("firstLogin.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        String userId = (String) session.getAttribute("userId");
        String answer1 = req.getParameter("answer1");
        String answer2 = req.getParameter("answer2");
        String answer3 = req.getParameter("answer3");
        int selectedQuestionId1 = Integer.parseInt(req.getParameter("hintQuestion1").split(" - ")[0]);
        int selectedQuestionId2 = Integer.parseInt(req.getParameter("hintQuestion2").split(" - ")[0]);
        int selectedQuestionId3 = Integer.parseInt(req.getParameter("hintQuestion3").split(" - ")[0]);
        Map<Integer, String> answerMap = new HashMap<>();
        answerMap.put(selectedQuestionId1, answer1);
        answerMap.put(selectedQuestionId2, answer2);
        answerMap.put(selectedQuestionId3, answer3);

        String oldPassword = req.getParameter("oldPass");
        String newPassword = req.getParameter("newPass");
        String confirmNewPassword = req.getParameter("confirmNewPass");
        if (session.getAttribute("userId") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        //2. Kiểm tra mật khẩu
        try {
            if (!userDAO.checkPassword(userId, oldPassword)) {
                req.setAttribute("error", "Mật khẩu cũ không đúng!");
                req.getRequestDispatcher("firstLogin.jsp").forward(req, resp);
                return;
            } else if (oldPassword.equals(newPassword)) {
                req.setAttribute("error", "Mật khẩu cũ phải khác mật khẩu mới");
                req.getRequestDispatcher("firstLogin.jsp").forward(req, resp);
                return;
            } else if (!newPassword.equals(confirmNewPassword)) {
                req.setAttribute("error", "Mật khẩu mới không khớp");
                req.getRequestDispatcher("firstLogin.jsp").forward(req, resp);
                return;
            } else {
                userDAO.updateNewPassword(userId, newPassword);
                req.getRequestDispatcher("login.jsp").forward(req, resp);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            List<HintQuestion> hintQuestionList = hintQuestionDAO.getAllHintQuestions();
            List<Integer> questionIdList = hintQuestionList.stream().map(question -> question.getQuestionId()).collect(Collectors.toList());
            for (Integer id : questionIdList) {
                if (answerMap.containsKey(id)) {
                    hintQuestionDAO.saveSercurityAnswer(userId, id, answerMap.get(id));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
