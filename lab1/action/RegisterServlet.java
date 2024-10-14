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
		// 2������һ���ļ��ϴ�������
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
			// ���fileitem�з�װ������ͨ�����������
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
				// ���fileitem�з�װ�����ϴ��ļ�
				InputStream stream = item.getInputStream();// �ϴ��ļ���Ҫ���ļ�������
				filename = item.getName(); // �ϴ��ļ���Ҫ�Ĳ���
				String savepath = getServletContext().getRealPath("/upload");
				File path = new File(savepath); // ���Ҫ�Լ�д�����·��������Ҫ�ϴ��ļ���Ҫ�Ĳ���
				uploadFile(stream, path, filename); // ���ù����෽��
				if (filename == null || filename.trim().equals("")) {
					// �пմ���}
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
		// ʹ��Apache�ļ��ϴ���������ļ��ϴ����裺
		// 1������һ��DiskFileItemFactory����
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		// ����ϴ��ļ�������������
		upload.setHeaderEncoding("UTF-8");
		// ����һ���ļ������
		// ע�⣺��ͬ��������ύ���ļ����ǲ�һ���ģ���Щ������ύ�������ļ����Ǵ���·���ģ��磺
		// c:a1.txt������Щֻ�ǵ������ļ������磺1.txt
		// �����ȡ�����ϴ��ļ����ļ�����·�����֣�ֻ�����ļ�������
		filename = filename.substring(filename.lastIndexOf("\\") + 1);
		String realSavePath = savaPath + "\\" + filename;
		// ����һ�������
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(realSavePath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ����һ��������
		byte buffer[] = new byte[1024];
		// �ж��������е������Ƿ��Ѿ�����ı�ʶ
		int len = 0;
		// ѭ�������������뵽���������У�(len=in.read(buffer))>0�ͱ�ʾin���滹������
		try {
			while ((len = filestream.read(buffer)) > 0) {
				// ʹ��FileOutputStream�������������������д�뵽ָ����Ŀ¼(savePath + "\" +
				// filename)����
				out.write(buffer, 0, len);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("�����������Ŀ¼:" + realSavePath);
		// �ر�������
		try {
			filestream.close();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// �ر������
		// ɾ�������ļ��ϴ�ʱ���ɵ���ʱ�ļ�
		// item.delete();
	}
}
