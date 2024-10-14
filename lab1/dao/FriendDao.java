package dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import bean.Friend;
import db.DBUtils;
import db.ManagerThreadLocal;

public class FriendDao {
	//modified for R14
	/**
	 * 添加
	 * 
	 * @param f
	 */
	public static void insert(Friend f) {
		QueryRunner qr = new QueryRunner();
		try {
			qr.update(ManagerThreadLocal.getConnection(),
					"INSERT INTO `t_friend`(`fid`, `fnickname`, `fheadimg`,`ctime`,`status`,`tid`,`memo`,`tnickname`,`theadimg`,`fs`,`ts`,`fage`,`tage`,`fsex`,`tsex`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
					f.getFid(), f.getFnickname(), f.getFheadimg(), f.getCtime(), f.getStatus(), f.getTid(), f.getMemo(),
					f.getTnickname(), f.getTheadimg(), f.getFs(), f.getTs(), f.getFage(), f.getTage(), f.getFsex(),
					f.getTsex());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ManagerThreadLocal.close();
		}
	}

	/**
	 * 删除
	 * 
	 * @param id
	 */
	public static void delete(int id) {
		QueryRunner qr = new QueryRunner();
		try {
			qr.update(ManagerThreadLocal.getConnection(), "DELETE FROM `t_friend` WHERE `id` = ?", id);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ManagerThreadLocal.close();
		}
	}

	/**
	 * 查询别人添加我
	 * 
	 * @param uid
	 * @return
	 */
	public static List<Friend> selecto2m(String uid, String status) {
		QueryRunner qr = new QueryRunner(DBUtils.getDataSource());
		List<Friend> friends = null;
		try {
			String sql = "SELECT * FROM `t_friend` where `tid` = '" + uid + "'";
			friends = qr.query(sql, new BeanListHandler<>(Friend.class));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ManagerThreadLocal.close();
		}
		return friends;
	}

	/**
	 * 查询我添加别人
	 * 
	 * @param uid
	 * @return
	 */
	public static List<Friend> selectm2o(String fid, String status) {
		QueryRunner qr = new QueryRunner(DBUtils.getDataSource());
		List<Friend> friends = null;
		try {
			String sql = "SELECT * FROM `t_friend` where `fid` = '" + fid + "'";
			friends = qr.query(sql, new BeanListHandler<>(Friend.class));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ManagerThreadLocal.close();
		}
		return friends;
	}

	/**
	 * 查询我的好友
	 * 
	 * @param uid
	 * @return
	 */
	public static List<Friend> selectMyFriends(String fid, String status) {
		QueryRunner qr = new QueryRunner(DBUtils.getDataSource());
		List<Friend> friends = null;
		try {
			String sql = "SELECT * FROM `t_friend` where (`fid` = '" + fid + "' or `tid` = '" + fid
					+ "') and `status` = '" + status + "';";
			friends = qr.query(sql, new BeanListHandler<>(Friend.class));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ManagerThreadLocal.close();
		}
		return friends;
	}

	public static void updateStatus(String tid, String fid, String string) {
		QueryRunner qr = new QueryRunner();
		try {
			String sql = "update `t_friend` set `status`='" + string + "' where `fid`='" + fid + "' and `tid`='" + tid
					+ "'";
			int re=qr.update(ManagerThreadLocal.getConnection(), sql);
			System.out.println(tid+"-----"+fid+"--------"+re);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ManagerThreadLocal.close();
		}
	}

	public static void deleteMyFrd(String id, String uid) {
		QueryRunner qr = new QueryRunner();
		try {
			String sql="DELETE FROM `t_friend` WHERE (`fid` = '"+id+"' or `tid`='"+id+"') and (`fid` = '"+uid+"' or `tid`='"+uid+"')";
			qr.update(ManagerThreadLocal.getConnection(),sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ManagerThreadLocal.close();
		}
	}
}
