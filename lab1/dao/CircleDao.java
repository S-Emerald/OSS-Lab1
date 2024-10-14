package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import bean.Circle;
import bean.Friend;
import db.DBUtils;
import db.ManagerThreadLocal;

public class CircleDao {
	//modified for R14
	/**
	 * ���
	 * 
	 * @param c
	 */
	public static void insert(Circle c) {
		QueryRunner qr = new QueryRunner();
		try {
			qr.update(ManagerThreadLocal.getConnection(),
					"INSERT INTO `t_circle`(`uid`, `nickname`, `headimg`,`content`,`ctime`,`memo`) VALUES (?,?,?,?,?,?)",
					c.getUid(), c.getNickname(), c.getHeadimg(), c.getContent(), c.getCtime(), c.getMemo());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ManagerThreadLocal.close();
		}
	}

	/**
	 * ɾ��
	 * 
	 * @param id
	 */
	public static void delete(int id) {
		QueryRunner qr = new QueryRunner();
		try {
			qr.update(ManagerThreadLocal.getConnection(), "DELETE FROM `t_circle` WHERE `id` = ?", id);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ManagerThreadLocal.close();
		}
	}

	/**
	 * ��ѯ
	 * 
	 * @param uid
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static List<Circle> select(String uid) {
		QueryRunner qr = new QueryRunner(DBUtils.getDataSource());
		List<Circle> circles = null;
		try {
			circles = qr.query("SELECT * FROM `t_circle` where `uid` = ?;", uid, new BeanListHandler<>(Circle.class));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ManagerThreadLocal.close();
		}
		return circles;
	}

	/**
	 * ��ѯ�ú��ѵ�����Ȧ
	 * @param id
	 * @return
	 */
	public static List<Circle> selectMyComments(int id) {
		List<Circle> clist = new ArrayList<Circle>();
		// ��ѯ�ҵĺ���
		List<Friend> myfs = FriendDao.selectMyFriends(id + "", "1");
		if (myfs != null && myfs.size() > 0) {
			for (int i = 0; i < myfs.size(); i++) {
				if (!myfs.get(i).getFid().equals(id + "")) {
					clist.addAll(CircleDao.select(myfs.get(i).getFid()));
				}
				if (!myfs.get(i).getTid().equals(id + "")) {
					clist.addAll(CircleDao.select(myfs.get(i).getTid()));
				}
			}
		}
		clist.addAll(CircleDao.select(id + ""));
		//clist.sort((x, y) -> String.compare(x.getCtime(), y.getCtime()));//�ⷽ����Ҫjdk1.8����
		return clist;
	}
}
