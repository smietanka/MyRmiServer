import java.io.Serializable;

public class Customer implements Serializable {
	private int Id;
	private static int Count = 0;
	private String Name;
	private int ConnectionId;
	
	public Customer(String name)
	{
		this.Name = name;
		this.Id = Count++;
	}
	
	public void setConnectionId(int id)
	{
		this.ConnectionId = id;
	}
	
	public int getConnectionId()
	{
		return this.ConnectionId;
	}
	
	public int getId()
	{
		return this.Id;
	}
	
	public String getName()
	{
		return this.Name;
	}
}
