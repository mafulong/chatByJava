package app;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.Vector;

import javax.swing.*;

import java.awt.*;
import java.io.*;
import java.util.*;

/*import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;*/
import util.Conf;
import vo.Customer;
import vo.Message;
public class ChatFrame extends JFrame implements ActionListener,Runnable{
	private Socket socket = null;
	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;
	private boolean canRun = true;
	private String account; 
	private JLabel lbUser = new JLabel("������Ա����");
	private List lstUser = new List();
	private JLabel lbMsg = new JLabel("�����¼");
	private JTextArea taMsg = new JTextArea();
	private JScrollPane spMsg  = new JScrollPane(taMsg);
	private JTextField tfMsg = new JTextField();
	private JButton btSend = new JButton("����");
	private JButton btSendFile = new JButton("�����ļ�");
	private JPanel plUser = new JPanel(new BorderLayout());
	private JPanel plMsg = new JPanel(new BorderLayout());
	private JPanel plUser_Msg = new JPanel(new GridLayout());
	private JPanel plSend = new JPanel(new BorderLayout());
	public ChatFrame(ObjectInputStream ois,ObjectOutputStream oos,Message receiveMessage,String account){
		this.ois = ois;
		this.oos = oos;
		this.account = account;
		this.initFrame();
		this.initUserList(receiveMessage);
		new Thread(this).start();
	}
	public void initFrame(){
		this.setTitle("��ǰ����"+account);
		this.setBackground(Color.magenta);
		plUser.add(lbUser,BorderLayout.NORTH);
		plUser.add(lstUser,BorderLayout.CENTER);
		plUser_Msg.add(plUser);
		lstUser.setBackground(Color.pink);
		
		plMsg.add(lbMsg,BorderLayout.NORTH);
		plMsg.add(spMsg,BorderLayout.CENTER);
		plUser_Msg.add(plMsg);
		taMsg.setBackground(Color.pink);
		
		plSend.add(tfMsg,BorderLayout.CENTER);
		plSend.add(btSend,BorderLayout.EAST);
		plSend.add(btSendFile,BorderLayout.WEST);
		tfMsg.setBackground(Color.yellow);
		
		this.add(plUser_Msg,BorderLayout.CENTER);
		this.add(plSend,BorderLayout.SOUTH);
		
		btSend.addActionListener(this);
		btSendFile.addActionListener(this);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(300,300);
		this.setVisible(true);
	}
	public void initUserList(Message message){
		lstUser.removeAll();
		lstUser.add(Conf.ALL);
		lstUser.select(0);
		Vector<Customer>userListVector = (Vector<Customer>)message.getContent();
		for(Customer cus :userListVector){
			lstUser.add(cus.getAccount()+","+cus.getName());
		}
	}
	public void run(){
		try{
			while(canRun){
				Message msg = (Message)ois.readObject();
				if(msg.getType().equals(Conf.MESSAGE)){
					taMsg.append(msg.getContent()+"\n");
				}else if(msg.getType().equals(Conf.SERVERMESSAGE)){
					taMsg.append("ϵͳ��Ϣ ��"+msg.getContent());
				}
				else if(msg.getType().equals(Conf.USERLIST)){
					this.initUserList(msg);
				}
				else if(msg.getType().equals(Conf.LOGOUT)){
					Customer cus = (Customer)msg.getContent();
					taMsg.append("ϵͳ��Ϣ ��"+cus.getName()+"������"+"\n");
					lstUser.remove(cus.getAccount()+","+cus.getName());
				}
				else if(msg.getType().equals(Conf.EXITMESSAGE)){
					taMsg.append("ϵͳ��Ϣ:");
					canRun=false;
					javax.swing.JOptionPane.showMessageDialog(this, "�Բ������ѱ�������");
					System.exit(-1);
				}
				else if(msg.getType().equals(Conf.FILE)){
					int res=JOptionPane.showConfirmDialog(null, "�����ļ�","",JOptionPane.YES_NO_OPTION);
					if(res==JOptionPane.YES_OPTION){
						String info =JOptionPane.showInputDialog(null,"�������ļ���");
						RandomAccessFile raf = new RandomAccessFile(info,"rw");
						String str=(String)msg.getContent();
						raf.write(str.getBytes());
						raf.close();
					}else{}
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
			canRun = false;
			javax.swing.JOptionPane.showMessageDialog(this,"�Բ�������ǿ������");
			System.exit(-1);
		}
	}
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==btSend){
		try{
			Message msg = new Message();
			msg.setType(Conf.MESSAGE);
			msg.setContent(account+"˵"+tfMsg.getText());
			msg.setFrom(account);
			String toInfo = lstUser.getSelectedItem();
			msg.setTo(toInfo.split(",")[0]);
			oos.writeObject(msg);
			tfMsg.setText("");
		}catch (Exception ex){
			JOptionPane.showMessageDialog(this, "��Ϣ�����쳣");
		}
		}
	else if(e.getSource()==btSendFile){
		try{
		    Message msg = new Message();
		    msg.setType(Conf.FILE);
		    msg.setFrom(account);
		    String toInfo = lstUser.getSelectedItem();
		    msg.setTo(toInfo.split(",")[0]);
		
		    String info=JOptionPane.showInputDialog(null,"�������ļ���");
			RandomAccessFile raf = new RandomAccessFile(info,"r");
			byte[] data = new byte[(int)raf.length()];
			raf.read(data);
			msg.setContent(new String(data));
			raf.close();
			oos.writeObject(msg);
	        }catch(Exception ex){
		         JOptionPane.showMessageDialog(this,"�ļ������쳣");		
	       }
	}
}
}






