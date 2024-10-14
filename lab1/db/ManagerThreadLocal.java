package db;

import java.sql.Connection;
import java.sql.SQLException;

public class ManagerThreadLocal {
	private static ThreadLocal<Connection> tl = new ThreadLocal<>();
	 
    //�õ�һ������
    public static Connection getConnection(){
        Connection conn = tl.get(); //�ӵ�ǰ�߳���ȡ��һ������
        if (conn == null){
            conn = DBUtils.getConnection();    //�ӳ���ȡһ��
            tl.set(conn);   //��conn������뵽��ǰ�����߳���
        }
        return conn;
    }
 
    //��ʼ����
    public static void startTransacation(){
        try {
            Connection conn = getConnection();
            conn.setAutoCommit(false);   //�ӵ�ǰ�̶߳���ȡ�����ӣ�����ʼ����
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
 
    public static void commit(){
        try {
            getConnection().commit();   //�ύ����
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
 
    public static void rollback(){
        try {
            getConnection().rollback(); //�ع�����
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
 
    public static void close(){
        try {
            getConnection().close();    //�����ӷŻس���
            tl.remove();    //�ѵ�ǰ�̶߳����е�conn�Ƴ�
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
