package app;
import java.sql.*;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.*;

import util.GUIUtil;
import util.Conf;
import vo.Customer;
import vo.Message;
import main.Main;
import app.Chatframe;
public class Create extends JFrame  implements ActionListener {
	private JLabel lbFriendname =new JLabel("���������������");
	private JTextField tfFriendname= new JTextField(10);
	private JLabel lbTel =new JLabel("�����������");
	private JTextField tfTel = new JTextField(20);
	private JButton btsave = new JButton("�洢");
	public Create(){
		super("�½���ϵ��");
		this.setLayout(new FlowLayout());
		this.add(lbFriendname);
		this.add(tfFriendname);
		this.add(lbTel);
		this.add(tfTel);
		this.add(btsave);
		this.setSize(240, 180);
		GUIUtil.toCenter(this);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
	}
	public void save(){
		String fname =tfFriendname.getText();
		String ftel = tfTel.getText();
		 try{
	            Class.forName("com.mysql.jdbc.Driver");
	            Connection  ss = DriverManager.getConnection("jdbc:mysql://localhost:3306/addresslist", "root","123456");
	            JOptionPane.showMessageDialog(this,"�����������ݿ�");
	            Statement stmt = ss.createStatement();  
		         //�����¼�����ݿ���
//		         String sqlstr1 = "create table "+clientCname+"(name varchar(20),tel char(20));";
//		         String sqlstr2 = "create table "+serverCname+"(name varchar(20),tel char(20));";
//		         stmt.executeUpdate(sqlstr1);
//		         stmt.executeUpdate(sqlstr2);
//		         System.out.println("�����ɹ���");
		         stmt.close();
		         ss.close();
	        }
	         catch(Exception e1)
	         {
	        	 JOptionPane.showMessageDialog(this,"���Ӵ�������������");
	            e1.printStackTrace();    
	        }
	}
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==btsave){
			this.save();
		}
		this.dispose();
	}
}
