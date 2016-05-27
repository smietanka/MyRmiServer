import java.io.Serializable;

public class Product implements Serializable {
	private static int Count = 0;
	private int Id;
	private String Name;
	private String Price;
	private String Brand;
	private int InStock;
	
	public Product()
	{
		
	}
	
	public Product(String name, String price)
	{
		this.Name = name;
		this.Price = price;
		this.Id = Count++;
	}
	
	public Product(String name, String price, String brand, int inStock)
	{
		this.Name = name;
		this.Price = price;
		this.Brand = brand;
		this.InStock = inStock;
		this.Id = Count++;
	}
	public void setBrand(String brand)
	{
		this.Brand = brand;
	}
	public void setInStock(int ins)
	{
		this.InStock = ins;
	}
	public void setName(String namee)
	{
		this.Name = namee;
	}
	public void setPrice(String price)
	{
		this.Price = price;
	}
	public void setId(int id)
	{
		this.Id = id;
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
	
	public String getBrand()
	{
		return this.Brand;
	}
	
	public int getInStock()
	{
		return this.InStock;
	}
}
