package app;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.*;
import main.Main;
import util.Conf;
import util.GUIUtil;
import vo.Customer;
import vo.Message;
public class LoginFrame extends JFrame implements ActionListener{
	// Icon welcomeIcon = new ImageIcon("welcome.png");
	//private JLabel lbWelcome = new JLabel(welcomeIcon);
	private JLabel lbAccount = new JLabel("���������˺�");
	private JTextField tfAccount = new JTextField(10);
	private JLabel lbPassword = new JLabel("������������");
	private JPasswordField pfPassword = new JPasswordField(10);
	private JButton btLogin = new JButton ("��¼");
	private JButton btRegister = new JButton("ע��");
	private JButton btExit = new JButton("�˳�");
	private Socket socket = null;
	private ObjectOutputStream oos = null;
	private ObjectInputStream ois = null;
	public LoginFrame(){
		super("��¼");//����ĳ�ʼ����
		this.setLayout(new FlowLayout());
		//this.add(lbWelcome);
		this.add(lbAccount);
		this.add(tfAccount);
		this.add(lbPassword);
		this.add(pfPassword);
		this.add(btLogin);
		this.add(btRegister);
		this.add(btExit);
		this.setSize(500,200);
		GUIUtil.toCenter(this);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);//���ô�С�Ƿ�ɱ䣬Ҫ��Ҫ����ν
		this.setVisible(true);
		/******���Ӽ���**********/
		btLogin.addActionListener(this);
		btRegister.addActionListener(this);
		btExit.addActionListener(this);
	}
	public void login(){
		String account = tfAccount.getText();
		Customer cus = new Customer();
		cus.setAccount(account);
		cus.setPassword(new String(pfPassword.getPassword()));
		Message msg = new Message();
		msg.setType(Conf.LOGIN);
		msg.setContent(cus);
		try{
			socket = new Socket(Main.serverIP,Main.port);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			oos.writeObject(msg);
			Message receiveMsg = (Message) ois.readObject();
			String type = receiveMsg.getType();
			if(type.equals(Conf.LOGINFALL)){
				JOptionPane.showMessageDialog(this,"��¼ʧ��");
				socket.close();
				return;
			}
			JOptionPane.showMessageDialog(this,"��½�ɹ�");
			new ChatFrame(ois,oos,receiveMsg,account);
			this.dispose();
		}catch(Exception ex){
			JOptionPane.showMessageDialog(this, "�����쳣");
			System.exit(-1);
		}
	}
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == btLogin){
			this.login();
		}else if(e.getSource()  == btRegister){
			this.dispose();
			new RegisterFrame();
		}else{
			JOptionPane.showMessageDialog(this,"Byebye");
			System.exit(0);
		}
	}
}
