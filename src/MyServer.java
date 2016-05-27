import java.io.File;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTextArea;

public class MyServer extends UnicastRemoteObject implements IMyServer {
	public List<Product> myAllProductList = new ArrayList<Product>();
	public List<Customer> myAllClients = new ArrayList<Customer>();
	
	public JTextArea myTextBox;

	public MyServer(JTextArea myTextBox) throws RemoteException {
		this.myTextBox = myTextBox;
		Product p1 = new Product("LAPTOP LENOVO Z50-70 i5 4GB 500GB GF840M FHD W8", "1700", "Lenovo", 26);
		Product p2 = new Product("Lenovo ThinkPad T540p 15,6 i3-4100M 8GB 500GB Win7", "2500", "Lenovo", 10);
		Product p3 = new Product("SAMSUNG GALAXY S7 EDGE G935F 32GB BLACK Wys24H Fv", "1500", "Samsung", 3);
		Product p4 = new Product("Telefon Sony XPERIA Z3 Dual SIM Black 16GB 20,7 MP", "2333", "Sony", 55);
		Product p5 = new Product("4 NOWE OPONY LETNIE 205/65R16C KORMORAN VANPRO HIT", "800", "Kormoran", 200);
		myAllProductList.add(p1);
		myAllProductList.add(p2);
		myAllProductList.add(p3);
		myAllProductList.add(p4);
		myAllProductList.add(p5);
	}
	
	public void WriteLog(String message)
	{
		myTextBox.append("[System]" + message + "\n");
		System.out.print("[System]" + message + "\n");
	}
	
	public boolean tryParseInt(String value)
	{
		try {
			Integer.parseInt(value);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	public String getHelloWorld() throws RemoteException {
		WriteLog("Zostala wywolana zdalna metoda getHelloWorld");
		return "Hello World";
	}

	@Override
	public String getString(String myString) throws RemoteException {
		WriteLog("Zostala wywolana zdalna metoda getString");
		return "To jest twoj tekst: " + myString;
	}

	@Override
	public void runServer() throws RemoteException {
		try {
			WriteLog("Uruchamianie serwera.");
			Runtime.getRuntime().exec("cmd /c start runServer.bat");
			
			if (LocateRegistry.getRegistry(1099) == null) {
				Registry reg = LocateRegistry.createRegistry(1099);
			}
			//System.setProperty("java.rmi.server.hostname", "127.0.0.1");

			IMyServer myServer = new MyServer(myTextBox);
			Naming.rebind("rmi://127.0.0.1/myabc", myServer);
			WriteLog("Serwer zostal uruchomiony.");
		} catch (Exception e) {
			WriteLog("Wystapil blad: " + e.getMessage());
		}
	}

	@Override
	public void stopServer() throws RemoteException {
		try {
			// Unregister ourself
			Naming.unbind("rmi://127.0.0.1/myabc");

			// Unexport; this will also remove us from the RMI runtime
			UnicastRemoteObject.unexportObject(this, true);
			WriteLog("Serwer zostal zatrzymany.");
		} catch (Exception e) {
			WriteLog("Wystapil blad. " + e);
		}
	}

	@Override
	public boolean registerClient(Customer client) throws RemoteException {
		WriteLog("Zostala wywolana metoda registerClient");
		Customer temp = new Customer(client.getName());
		myAllClients.add(temp);
		WriteLog(client.getName() + " sie polaczyl.");
		if(client.getName().equals("admin"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public List<Product> searchProducts(String myFilter) throws RemoteException {
		List<Product> result = new ArrayList<Product>();
		Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
		
		
		for(Product eachProduct : myAllProductList)
		{
			if(tryParseInt(myFilter.toUpperCase()))
			{
				int myParsed = Integer.parseInt(myFilter.toUpperCase());
				int eachProductPrice = Integer.parseInt(eachProduct.getPrice().toUpperCase());
				if(eachProductPrice <= myParsed)
				{
					result.add(eachProduct);
				}
			}
			else
			{
				String[] splittedProductName = eachProduct.getName().toUpperCase().split("\\s+");
				String[] splittedMyFilter = myFilter.toUpperCase().split("\\s+");
				int countedWords = 0;
				for(String x : splittedProductName)
				{
					for(String y : splittedMyFilter)
					{
						int score = LevenshteinDistance.computeLevenshteinDistance(y, x);
						if(score < 2 )
						{
							countedWords++;
						}
					}
				}
				if(countedWords >= splittedMyFilter.length)
				{
					result.add(eachProduct);
				}
			}
		}
		return result;
	}

	@Override
	public List<Product> showAllProducts() throws RemoteException {
		WriteLog("Zostala wywolana metoda showAllProducts");
		return myAllProductList;
	}

	@Override
	public void buyProduct(Product myProduct) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addProduct(Product myProduct) throws RemoteException {
		WriteLog("Zostala wywolana metoda addProduct");
		Product temp = new Product(myProduct.getName(), myProduct.getPrice(), myProduct.getBrand(), myProduct.getInStock());
		myAllProductList.add(temp);
		WriteLog("Dodano produkt.");
	}

	@Override
	public boolean isClientExists(String name) throws RemoteException {
		WriteLog("Zostala wywolana metoda isClientExists z parametrem: " + name);
		boolean result = false;
		
		for(Customer eachCustomer : myAllClients)
		{
			if(eachCustomer.getName().equals(name))
			{
				result = true;
				break;
			}
		}
		
		return result;
	}

	@Override
	public void logoutClient(Customer client) throws RemoteException {
		WriteLog("Zostala wywolana metoda logoutClient");
		String currentName = client.getName();
		int clientIdx = -1;
		for(Customer x : myAllClients)
		{
			if(x.getName().equals(currentName))
			{
				clientIdx = myAllClients.indexOf(x);
				break;
			}
		}
		if(clientIdx != -1)
		{
			myAllClients.remove(clientIdx);
			WriteLog(client.getName() + " wylogowa³ siê.");	
		}
	}

	@Override
	public void deleteProduct(Product myProduct) throws RemoteException {
		WriteLog("Zostala wywolana metoda deleteProduct");
		int currentId = myProduct.getId();
		int productIdx = -1;
		
		for(Product x : myAllProductList)
		{
			if(x.getId() == currentId)
			{
				productIdx = myAllProductList.indexOf(x);
				break;
			}
		}
		if(productIdx != -1)
		{
			myAllProductList.remove(productIdx);
			WriteLog("Usunalem produkt z listy.");
		}
	}

	@Override
	public void logoutAllClient() throws RemoteException {
		WriteLog("Zostala wywolana metoda logoutAllClient");
		myAllClients = new ArrayList<Customer>();
	}
}
