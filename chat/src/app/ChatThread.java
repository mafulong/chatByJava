package app;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import util.Conf;
import util.FileOpe;
import vo.Customer;
import vo.Message;

public class ChatThread extends Thread{
	private Socket socket = null;
	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;
	protected Customer customer = null;
	private static Server server;
	private boolean canRun = true;
	public ChatThread(Socket socket,Server server)throws Exception{
		this.socket = socket;
		this.server = server;
		oos = new ObjectOutputStream(socket.getOutputStream());
		ois = new ObjectInputStream(socket.getInputStream());
	}
public void run(){
	try{
		while (canRun){
			Message msg = (Message) ois.readObject();
			String type = msg.getType();
			if(type.equals(Conf.LOGIN)){
				this.handleLogin(msg);
			}else if(type.equals(Conf.REGISTER)){
				this.handleRegister(msg);
			}else if(type.equals(Conf.MESSAGE)){
				this.handleMessage(msg);
			}else if(type.equals(Conf.FILE)){
				this.handleMessage(msg);
			}
		}
	}catch (Exception ex){
		this.handleLogout();
	}
}
public void handleLogin(Message msg) throws Exception{
	Customer loginCustomer = (Customer)msg.getContent();
	String account = loginCustomer.getAccount();
	String password = loginCustomer.getPassword();
	Customer cus = FileOpe.getCustomerByAccount(account);
	Message newMsg = new Message();
	if(cus == null|| !cus.getPassword().equals(password)){
		newMsg.setType(Conf.LOGINFALL);
        oos.writeObject(newMsg);
        canRun = false;
        socket.close();
        return;      
	}
	this.customer = cus;
	server.getClients().add(this);//将该线程放入clients集合
	server.getUserList().add(this.customer);//将customer加到userlist中
	newMsg.setType(Conf.USERLIST);//将所有的在线用户发送给客户端
	newMsg.setContent(server.getUserList().clone());
	this.sendMessage(newMsg,Conf.ALL);//将该用户登陆的信息发送给所有用户
	server.setTitle("当前在线：" + server.getClients().size() + "人");
	
	server.lstUser.removeAll();
	server.lstUser.add(Conf.ALL);
	server.lstUser.select(0);
	for(Customer cus1 : server.userList ){
		server.lstUser.add(cus1.getAccount()+","+cus1.getName());
	}

}
public void handleRegister(Message msg) throws Exception{
	Customer registerCustomer = (Customer)msg.getContent();
	String account = registerCustomer.getAccount();
	Customer cus = FileOpe.getCustomerByAccount(account);
	Message newMsg = new Message();
	if(cus!=null){
		newMsg.setType(Conf.REGISTERFALL);
	}else {
		String password = registerCustomer.getPassword();
		String name = registerCustomer.getName();
		FileOpe.insertCustomer(account,password,name);
		newMsg.setType(Conf.REGISTERSUCCESS);
		oos.writeObject(newMsg);
	}
	oos.writeObject(newMsg);
	canRun = false;
	socket.close();
}
public void handleMessage(Message msg) throws Exception{
	String to = msg.getTo();
	sendMessage(msg,to);
}
public void handleLogout(){
	Message logoutMessage = new Message();
	logoutMessage.setType(Conf.LOGOUT);
	logoutMessage.setContent(this.customer);
	server.getClients().remove(this);
	server.getUserList().remove(this.customer);
	try{
		sendMessage(logoutMessage,Conf.ALL);
		canRun = false;
		socket.close();
	}catch(Exception ex){
		ex.printStackTrace();
	}
	server.setTitle("当前在线："+ server.getClients().size()+"人");
	server.lstUser.removeAll();
	server.lstUser.add(Conf.ALL);
	server.lstUser.select(0);
	for(Customer cus1 : server.userList ){
		server.lstUser.add(cus1.getAccount()+","+cus1.getName());
	}
}
public static void sendMessage(Message msg,String to)throws Exception{
	for(ChatThread ct: server.getClients()){
		if(ct.customer.getAccount().equals(to)|| to.equals(Conf.ALL)){
			ct.oos.writeObject(msg);
		}
	}
}
}











