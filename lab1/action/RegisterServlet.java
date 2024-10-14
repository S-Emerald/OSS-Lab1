package action;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import bean.User;
import dao.UserDao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "RegisterServlet", urlPatterns = { "/RegisterServlet" })
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		User u = new User();
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 2、创建一个文件上传解析器
		ServletFileUpload upload = new ServletFileUpload(factory);
		List<FileItem> list = null;
		String filename = null;
		try {
			list = upload.parseRequest(request);
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (FileItem item : list) {
			// 如果fileitem中封装的是普通输入项的数据
			if (item.isFormField()) {
				String value = item.getString("UTF-8");
				System.out.println(item.getFieldName() + ":" + value);
				if ("nickname".equals(item.getFieldName())) {
					u.setNickname(value);
				}
				if ("password".equals(item.getFieldName())) {
					u.setPassword(value);
				}
				if ("phone".equals(item.getFieldName())) {
					u.setPhone(value);
				}
				if ("gender".equals(item.getFieldName())) {
					u.setGender(value);
				}
				if ("username".equals(item.getFieldName())) {
					u.setUsername(value);
				}
				if ("age".equals(item.getFieldName())) {
					u.setAge(value);
				}
				if ("birthday".equals(item.getFieldName())) {
					u.setBirthday(value);
				}
				if ("signature".equals(item.getFieldName())) {
					u.setSignature(value);
				}
				continue;
			} else {
				// 如果fileitem中封装的是上传文件
				InputStream stream = item.getInputStream();// 上传文件需要的文件流参数
				filename = item.getName(); // 上传文件需要的参数
				String savepath = getServletContext().getRealPath("/upload");
				File path = new File(savepath); // 这个要自己写具体的路径，是需要上传文件需要的参数
				uploadFile(stream, path, filename); // 调用工具类方法
				if (filename == null || filename.trim().equals("")) {
					// 判空处理}
					continue;
				}
			}

		}
		filename = filename.substring(filename.lastIndexOf("\\") + 1);
		u.setImg("upload/"+filename);
		UserDao.insert(u);
		request.getRequestDispatcher("login.jsp").forward(request, response);
	}

	public static void uploadFile(InputStream filestream, File savaPath, String filename) {
		// 使用Apache文件上传组件处理文件上传步骤：
		// 1、创建一个DiskFileItemFactory工厂
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		// 解决上传文件名的中文乱码
		upload.setHeaderEncoding("UTF-8");
		// 创建一个文件输出流
		// 注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如：
		// c:a1.txt，而有些只是单纯的文件名，如：1.txt
		// 处理获取到的上传文件的文件名的路径部分，只保留文件名部分
		filename = filename.substring(filename.lastIndexOf("\\") + 1);
		String realSavePath = savaPath + "\\" + filename;
		// 创建一个输出流
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(realSavePath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 创建一个缓冲区
		byte buffer[] = new byte[1024];
		// 判断输入流中的数据是否已经读完的标识
		int len = 0;
		// 循环将输入流读入到缓冲区当中，(len=in.read(buffer))>0就表示in里面还有数据
		try {
			while ((len = filestream.read(buffer)) > 0) {
				// 使用FileOutputStream输出流将缓冲区的数据写入到指定的目录(savePath + "\" +
				// filename)当中
				out.write(buffer, 0, len);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("这才是真正的目录:" + realSavePath);
		// 关闭输入流
		try {
			filestream.close();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 关闭输出流
		// 删除处理文件上传时生成的临时文件
		// item.delete();
	}
}
