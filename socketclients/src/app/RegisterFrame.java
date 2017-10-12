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
public class RegisterFrame extends JFrame implements ActionListener{
	private JLabel lbAccount = new JLabel("请您输入账号");
	private JTextField tfAccount = new JTextField(10);
	private JLabel lbPassword1 = new JLabel("请您输入密码");  
	private JPasswordField pfPassword1 = new JPasswordField(10);
	private JLabel lbPassword2 = new JLabel("输入确认密码");  
	private JPasswordField pfPassword2 = new JPasswordField(10);
	private JLabel lbName = new JLabel("请您输入姓名");
	private JTextField tfName = new JTextField(10);
	private JButton btRegister = new JButton("注册");
	private JButton btLogin = new JButton("登录");
	private JButton btExit = new JButton("退出");
	private Socket socket = null;
	private ObjectOutputStream oos = null;
	private ObjectInputStream ois = null;
	public RegisterFrame(){
		super("注册");
		this.setLayout(new FlowLayout());
		this.add(lbAccount);
		this.add(tfAccount);
		this.add(lbPassword1);
		this.add(pfPassword1);
		this.add(lbPassword2);
		this.add(pfPassword2);
		this.add(lbName);
		this.add(tfName);
		this.add(btRegister);
		this.add(btLogin);
		this.add(btExit);
		this.setSize(200,300);
		GUIUtil.toCenter(this);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
		btLogin.addActionListener(this);
		btRegister.addActionListener(this);
		btExit.addActionListener(this);
	}
	public void register(){
		Customer cus = new Customer();
		cus.setAccount(tfAccount.getText());
		cus.setPassword(new String (pfPassword1.getPassword()));
		cus.setName(tfName.getText());
		Message msg = new Message();
		msg.setType(Conf.REGISTER);
		msg.setContent(cus);
		try{
		    socket = new Socket(Main.serverIP,Main.port);
		//	socket = new Socket("127.0.0.1",6666);

			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			Message receiveMsg = null;
			oos.writeObject(msg);
			receiveMsg = (Message)ois.readObject();
			String type = receiveMsg.getType();
			if(type.equals(Conf.REGISTER)){
				JOptionPane.showMessageDialog(this, "注册失败");
			}else{
				JOptionPane.showMessageDialog(this,"注册成功");
			}
			socket.close();
		}catch(Exception ex){
			JOptionPane.showMessageDialog(this, "网络连接异常");
			System.exit(-1);
		}
	}
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == btRegister){
			String password1 = new String(pfPassword1.getPassword());
			String password2 = new String(pfPassword2.getPassword());
			if(!password1.equals(password2)){
				JOptionPane.showMessageDialog(this, "两个密码不相同");
				return;
			}
			this.register();
		}else if(e.getSource() ==btLogin){
			this.dispose();
			new LoginFrame();
		}else{
			JOptionPane.showMessageDialog(this,"Byebye");
			System.exit(0);
		}
	}
}















