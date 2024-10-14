package dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import bean.User;
import db.DBUtils;
import db.ManagerThreadLocal;

public class UserDao {
	/**
	 * �������ݿ�����һ���û�������Ϣ�ķ���
	 *
	 * @param s
	 * @return
	 */
	public static void insert(User u) {
		QueryRunner qr = new QueryRunner();
		try {
			qr.update(ManagerThreadLocal.getConnection(),
					"INSERT INTO `t_user`(`nickname`, `password`, `phone`,`img`,`username`,`age`,`gender`,`birthday`,`signature`,`memo`) VALUES (?,?,?,?,?,?,?,?,?,?)",
					u.getNickname(), u.getPassword(), u.getPhone(), u.getImg(), u.getUsername(), u.getAge(),
					u.getGender(), u.getBirthday(), u.getSignature(), u.getMemo());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ManagerThreadLocal.close();
		}
	}

	/**
	 * �������ݿ�id�������Ա�����յķ���
	 *
	 * @param s
	 * @return @throws
	 */
	public static void update(User u) {
		QueryRunner qr = new QueryRunner();
		try {
			qr.update(ManagerThreadLocal.getConnection(),
					"UPDATE `t_user` SET `nickname` = ?, `sex` = ?, `brithday` = ? WHERE `id` = ?", u.getNickname(),
					u.getGender(), u.getBirthday(), u.getId());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ManagerThreadLocal.close();
		}
	}

	/**
	 * ���ݶ�����ɾ�����ݿ������Ϣ�ķ���
	 *
	 * @param
	 * @return
	 * @throws SQLException
	 */
	public static void delete(String nickname) {
		QueryRunner qr = new QueryRunner();
		try {
			qr.update(ManagerThreadLocal.getConnection(), "DELETE FROM `t_user` WHERE `nickname` = ?", nickname);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ManagerThreadLocal.close();
		}
	}

	/**
	 * ��ѯ���ķ���
	 *
	 * @throws SQLException
	 */
	public static List<User> select() {
		QueryRunner qr = new QueryRunner(DBUtils.getDataSource());
		List<User> users = null;
		try {
			users = qr.query("SELECT * FROM `t_user`;", new BeanListHandler<>(User.class));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ManagerThreadLocal.close();
		}
		return users;
	}

	/**
	 * ��ѯ
	 *
	 * @throws SQLException
	 */
	public static User getUserById(int id) {
		QueryRunner qr = new QueryRunner(DBUtils.getDataSource());
		User user = null;
		try {
			String sql = "SELECT * FROM `t_user` where `id`="+id;
			user = (User) qr.query(sql, new BeanHandler<>(User.class));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ManagerThreadLocal.close();
		}
		return user;
	}
	/**
	 * ��ѯ���ķ���
	 *
	 * @throws SQLException
	 */
	public static List<User> login(String name, String password) {
		QueryRunner qr = new QueryRunner(DBUtils.getDataSource());
		List<User> users = null;
		try {
			String sql = "SELECT * FROM `t_user` where `nickname` ='" + name + "' and `password`='" + password + "'";
			users = qr.query(sql, new BeanListHandler<>(User.class));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ManagerThreadLocal.close();
		}
		return users;
	}

	public static void main(String[] args) {
		System.out.println(UserDao.select());
	}
}
