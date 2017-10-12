package vo;
import java.io.Serializable;
public class Message implements Serializable{
	private String type;
	private Object content;
	private String to;
	private String from;
	public void setType(String type){
		this.type = type;
	}
	public void setContent(Object content){
		this.content = content;
	}
	public void setTo(String to){
		this.to = to;
	}
	public void setFrom(String from){
		this.from = from;
	}
	public String getType(){
		return(this.type);
	}
	public Object getContent(){
		return(this.content);
	}
	public String getTo(){
		return(this.to);
	}
	public String getFrom(){
		return(this.from);
	}
}
