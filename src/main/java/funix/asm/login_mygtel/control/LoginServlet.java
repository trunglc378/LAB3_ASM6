package funix.asm.login_mygtel.control;

import funix.asm.login_mygtel.dao.HintQuestionDAO;
import funix.asm.login_mygtel.dao.UserDAO;
import funix.asm.login_mygtel.model.HintQuestion;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.*;

//@WebServlet(name = "hello", value = "/loginMyGtel")
public class LoginServlet extends HttpServlet {
    private UserDAO userDAO;
    private HintQuestionDAO hintQuestionDAO;

    public void init() {
        String url = "jdbc:mysql://localhost:3306/schooldb";
        String user = "root";
        String password = "trunglc378gmail";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);
            userDAO = new UserDAO(connection);
            hintQuestionDAO = new HintQuestionDAO(connection);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        try {
            if (userId == null || userId.isEmpty() || password == null || password.isEmpty()) {
                req.setAttribute("loginFail", "Vui lòng nhập User ID và Password.");
                req.getRequestDispatcher("login.jsp").forward(req, resp);
                return;
            }
            //Kiem tra trang thai tai khoan
            AccountStatus accountStatus = userDAO.getAccountStatus(userId);
            if (accountStatus == AccountStatus.ACCOUNT_LOCKED) {
                req.setAttribute("loginFail", "Tài khoản của bạn đã bị khóa. Vui lòng liên hệ tổng đài để mở khóa USER ID");
                req.getRequestDispatcher("login.jsp").forward(req, resp);
                return;
            }
            LoginState loginState = userDAO.validateLogin(userId, password);
            HttpSession session = req.getSession();

            switch (loginState) {
                case LOGIN_SUCCESS: {
                    session.setAttribute("userId", userId);
//                    resp.sendRedirect(req.getContextPath() + "/home");
                    req.getRequestDispatcher("home.jsp").forward(req, resp);
                    break;
                }
                case LOGIN_ID_RIGHT_PASS_FAIL: {
                    int remainingAttemp = 3 - userDAO.getFailedLoginAttemps(userId);
                    if (remainingAttemp > 0) {
                        req.setAttribute("loginFail", "Nhập sai User ID/Password. Số lượt thử lại còn: " + remainingAttemp);
                    } else {
                        userDAO.updateAccountLocked(userId, AccountStatus.ACCOUNT_LOCKED);
                        req.setAttribute("loginFail", "Tài khoản của bạn đã bị khóa. Vui lòng liên hệ tổng đài để mở khóa.");
                    }
                    req.getRequestDispatcher("login.jsp").forward(req, resp);
                    break;
                }
                case LOGIN_SUCCESS_AND_FIRST_LOGIN: {
                    session.setAttribute("userId", userId);
                    List<HintQuestion> hintQuestionList = hintQuestionDAO.getAllHintQuestions();
                    session.setAttribute("hintQuestions", hintQuestionList);
                    resp.sendRedirect(req.getContextPath() + "/first-login");
                    break;
                }
                case LOGIN_ID_AND_PASS_FAIL: {
                    req.setAttribute("loginFail", "ID và Password không hợp lệ. Vui lòng nhập lại");
                    req.getRequestDispatcher("login.jsp").forward(req, resp);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void destroy() {
    }
}