package action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Friend;
import bean.User;
import dao.FriendDao;
import dao.UserDao;


@WebServlet(name = "LoginServlet", urlPatterns = { "/LoginServlet" })
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		List<User> users = UserDao.login(name, password);
		if (users != null && users.size() == 1) {
			request.getSession().setAttribute("user", users.get(0));
			// 查询我申请添加的好友信息
			List<Friend> m2os = FriendDao.selectm2o(users.get(0).getId()+"", "1");
			request.setAttribute("m2os", m2os);
			// 查询别人添加我的好友信息
			List<Friend> o2ms = FriendDao.selecto2m(users.get(0).getId()+"", "1");
			request.setAttribute("o2ms", o2ms);
			// 查询我的好友
			List<Friend> mfs = FriendDao.selectMyFriends(users.get(0).getId()+"", "1");
			request.setAttribute("mfs", mfs);
			request.getRequestDispatcher("contacts.jsp").forward(request, response);
		} else {
			response.sendRedirect("login.jsp");
		}
	}
}
