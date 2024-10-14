package action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Circle;
import bean.Friend;
import bean.User;
import dao.CircleDao;
import dao.FriendDao;
import dao.UserDao;

@WebServlet(name = "api", urlPatterns = { "/api" })
public class ApiAction extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ApiAction() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String ac = request.getParameter("ac");
		String path = "";
		if ("toAddFriend".equals(ac)) {
			List<User> users = UserDao.select();
			request.setAttribute("users", users);
			path = "userList.jsp";
		} else if ("addFriend".equals(ac)) {
			User u = (User) request.getSession().getAttribute("user");
			if (u == null) {
				path = "login.jsp";
			} else {
				String tid = request.getParameter("uid");
				System.out.println(tid);
				String fid = u.getId() + "";
				Friend friend = new Friend();
				friend.setCtime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
				friend.setFid(fid);
				friend.setFheadimg(u.getImg());
				friend.setFnickname(u.getNickname());
				friend.setFage(u.getAge());
				friend.setFs(u.getSignature());
				friend.setFsex(u.getGender());
				friend.setTid(tid + "");
				friend.setStatus("0");
				friend.setMemo("添加好友");
				User t = UserDao.getUserById(Integer.valueOf(tid));
				if (t != null) {
					friend.setTheadimg(t.getImg());
					friend.setTnickname(t.getNickname());
					friend.setTage(t.getAge());
					friend.setTs(t.getSignature());
					friend.setTsex(t.getGender());
				}
				FriendDao.insert(friend);

				// 查询我申请添加的好友信息
				List<Friend> m2os = FriendDao.selectm2o(fid, "1");
				request.setAttribute("m2os", m2os);
				// 查询别人添加我的好友信息
				List<Friend> o2ms = FriendDao.selecto2m(fid, "1");
				request.setAttribute("o2ms", o2ms);
				// 查询我的好友
				List<Friend> mfs = FriendDao.selectMyFriends(fid, "1");
				request.setAttribute("mfs", mfs);
				path = "contacts.jsp";
			}

		} else if ("logout".equals(ac)) {
			// 退出登录
			path = "login.jsp";
			request.getSession().removeAttribute("user");
		} else if ("agree".equals(ac)) {
			// 同意添加好友
			String tid = request.getParameter("tid");
			String fid = request.getParameter("fid");
			FriendDao.updateStatus(tid, fid, "1");
			User u = (User) request.getSession().getAttribute("user");
			// 查询我申请添加的好友信息
			List<Friend> m2os = FriendDao.selectm2o(u.getId() + "", "1");
			request.setAttribute("m2os", m2os);
			// 查询别人添加我的好友信息
			List<Friend> o2ms = FriendDao.selecto2m(u.getId() + "", "1");
			request.setAttribute("o2ms", o2ms);
			// 查询我的好友
			List<Friend> mfs = FriendDao.selectMyFriends(u.getId() + "", "1");
			request.setAttribute("mfs", mfs);
			path = "contacts.jsp";

		} else if ("lose".equals(ac)) {
			// 同意添加好友
			String tid = request.getParameter("tid");
			String fid = request.getParameter("fid");
			FriendDao.updateStatus(tid, fid, "2");
			User u = (User) request.getSession().getAttribute("user");
			// 查询我申请添加的好友信息
			List<Friend> m2os = FriendDao.selectm2o(u.getId() + "", "1");
			request.setAttribute("m2os", m2os);
			// 查询别人添加我的好友信息
			List<Friend> o2ms = FriendDao.selecto2m(u.getId() + "", "1");
			request.setAttribute("o2ms", o2ms);
			// 查询我的好友
			List<Friend> mfs = FriendDao.selectMyFriends(u.getId() + "", "1");
			request.setAttribute("mfs", mfs);
			path = "contacts.jsp";
		} else if ("toMyComments".equals(ac)) {
			// 我的朋友圈
			User u = (User) request.getSession().getAttribute("user");
			if (u == null) {
				path = "login.jsp";
			} else {
				List<Circle> clist = CircleDao.selectMyComments(u.getId());
				request.setAttribute("clist", clist);
				path = "moments.jsp";
			}
		} else if ("addComment".equals(ac)) {
			User u = (User) request.getSession().getAttribute("user");
			if (u == null) {
				path = "login.jsp";
			} else {
				String content = request.getParameter("content");
				Circle c = new Circle();
				c.setCtime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
				c.setContent(content);
				c.setHeadimg(u.getImg());
				c.setNickname(u.getNickname());
				c.setUid(u.getId() + "");
				c.setMemo("发表朋友圈");
				CircleDao.insert(c);

				List<Circle> clist = CircleDao.selectMyComments(u.getId());
				request.setAttribute("clist", clist);
				path = "moments.jsp";
			}

		} else if ("toContacts".equals(ac)) {
			User u = (User) request.getSession().getAttribute("user");
			if (u == null) {
				path = "login.jsp";
			} else {
				// 查询我申请添加的好友信息
				List<Friend> m2os = FriendDao.selectm2o(u.getId() + "", "1");
				request.setAttribute("m2os", m2os);
				// 查询别人添加我的好友信息
				List<Friend> o2ms = FriendDao.selecto2m(u.getId() + "", "1");
				request.setAttribute("o2ms", o2ms);
				// 查询我的好友
				List<Friend> mfs = FriendDao.selectMyFriends(u.getId() + "", "1");
				request.setAttribute("mfs", mfs);
				path = "contacts.jsp";
			}
		} else if ("toMainPage".equals(ac)) {
			// 去朋友主页
			String id = request.getParameter("id");
			User u = UserDao.getUserById(Integer.valueOf(id));
			if (u != null) {
				List<Circle> clist = CircleDao.select(u.getId() + "");
				request.setAttribute("clist", clist);
				request.setAttribute("u", u);
			}
			path = "mainPage.jsp";
		} else if ("del".equals(ac)) {
			
			User u = (User) request.getSession().getAttribute("user");
			if (u == null) {
				path = "login.jsp";
			} else {
				String id = request.getParameter("id");
				FriendDao.deleteMyFrd(id,u.getId()+"");
				// 查询我申请添加的好友信息
				List<Friend> m2os = FriendDao.selectm2o(u.getId() + "", "1");
				request.setAttribute("m2os", m2os);
				// 查询别人添加我的好友信息
				List<Friend> o2ms = FriendDao.selecto2m(u.getId() + "", "1");
				request.setAttribute("o2ms", o2ms);
				// 查询我的好友
				List<Friend> mfs = FriendDao.selectMyFriends(u.getId() + "", "1");
				request.setAttribute("mfs", mfs);
				path = "contacts.jsp";
			}
		} 

		request.getRequestDispatcher(path).forward(request, response);
	}

}
