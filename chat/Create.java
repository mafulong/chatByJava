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
	private JLabel lbFriendname =new JLabel("请您输入好友姓名");
	private JTextField tfFriendname= new JTextField(10);
	private JLabel lbTel =new JLabel("请您输入号码");
	private JTextField tfTel = new JTextField(20);
	private JButton btsave = new JButton("存储");
	public Create(){
		super("新建联系人");
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
	            JOptionPane.showMessageDialog(this,"已连接至数据库");
	            Statement stmt = ss.createStatement();  
		         //插入记录到数据库中
//		         String sqlstr1 = "create table "+clientCname+"(name varchar(20),tel char(20));";
//		         String sqlstr2 = "create table "+serverCname+"(name varchar(20),tel char(20));";
//		         stmt.executeUpdate(sqlstr1);
//		         stmt.executeUpdate(sqlstr2);
//		         System.out.println("创建成功！");
		         stmt.close();
		         ss.close();
	        }
	         catch(Exception e1)
	         {
	        	 JOptionPane.showMessageDialog(this,"连接错误，请重新连接");
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
