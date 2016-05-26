import java.io.Serializable;

public class Product implements Serializable {
	private static int Count = 0;
	private int Id;
	private String Name;
	private String Price;
	
	public Product(String name, String price)
	{
		this.Name = name;
		this.Price = price;
		this.Id = Count++;
	}
	
	public Product getProduct()
	{
		return this;
	}
	
	public int getId()
	{
		return this.Id;
	}
	
	public String getName()
	{
		return this.Name;
	}
	
	public String getPrice()
	{
		return this.Price;
	}
}
