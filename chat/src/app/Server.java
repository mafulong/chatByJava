package app;
import java.awt.*;
import java.awt.List;
import java.awt.event.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.util.Vector;
import java.util.Vector.*;

import javax.swing.*;

import util.*;
import vo.*;
public class Server extends JFrame implements Runnable, ActionListener{
	private Socket socket = null;
	private ServerSocket serverSocket = null;
	private Vector<ChatThread>clients = new Vector<ChatThread>();
	protected Vector<Customer>userList = new Vector<Customer>();
	private JButton jbt = new JButton("关闭服务器");
	private boolean canRun = true;
	private JLabel lbUser = new JLabel("在线人员名单：");
	protected List lstUser = new List();
	private JTextField tfMsg = new JTextField();
	private JButton btSend = new JButton("发送");
	private JPanel jpl = new JPanel();
	private JPanel plSend = new JPanel(new BorderLayout());
	private JButton btExit = new JButton("强制下线");
	
	public Server() throws Exception{
		jpl.setLayout(null);
		this.add(jpl);
		this.setTitle("服务器终端");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		/*this.add(jbt,BorderLayout.NORTH);
		jbt.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.exit(0);
			}
		});*/
		this.setBackground(Color.yellow);
		this.setSize(600,400);
		this.setVisible(true);
		this.setResizable(false);
		serverSocket = new ServerSocket(6666);
		new Thread(this).start();
		jpl.add(lbUser);
		jpl.add(lstUser);
		jpl.add(jbt);
		jpl.add(plSend);
		jpl.add(btExit);
		lstUser.setBackground(Color.blue);
		lbUser.setSize(500,30);
		lbUser.setLocation(10,0);
		lstUser.setSize(470,230);
		lstUser.setLocation(10,20);
		plSend.setSize(470,30);
		plSend.setLocation(10,255);
		btExit.setSize(470,30);
		btExit.setLocation(10,290);
		jbt.setSize(470,30);
		jbt.setLocation(10,325);
		
		plSend.add(tfMsg,BorderLayout.CENTER);
		plSend.add(btSend,BorderLayout.EAST);
		tfMsg.setBackground(Color.green);
		
		//增加监听
		jbt.addActionListener(this);
		btSend.addActionListener(this);
		tfMsg.addActionListener(this);
		btExit.addActionListener(this);

	}
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==jbt){
				System.exit(0);
		}
		else if(e.getSource()==btSend||e.getSource()==tfMsg){
			try{
				Message message = new Message();
				message.setType(Conf.SERVERMESSAGE);
				message.setContent(tfMsg.getText()+"\n");
				String toInfo = lstUser.getSelectedItem();
				message.setTo(toInfo.split(",")[0]);
				String to = message.getTo();
				ChatThread.sendMessage(message,to);
				tfMsg.setText("");
			 }catch(Exception ex){
				JOptionPane.showMessageDialog(this, "系统消息发送异常");
			}
		}
		else if(e.getSource()==btExit){
			try{
				Message message = new Message();
				message.setType(Conf.EXITMESSAGE);
				String toInfo = lstUser.getSelectedItem();
				message.setTo(toInfo.split(",")[0]);
				String to = message.getTo();
				ChatThread.sendMessage(message,to);
			}catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "强制下线失败");
			}
			
		}
	}

public void run(){
	try {
		while(canRun){
			socket  = serverSocket.accept();
			ChatThread ct = new ChatThread(socket,this);
			ct.start();
		}
	}catch(Exception ex){
		canRun = false;
		try{
			serverSocket.close();
		}catch(Exception e){}
	}
}
public Vector<ChatThread>getClients(){
	return clients;
}
public Vector<Customer>getUserList(){
	return userList;
}
}





