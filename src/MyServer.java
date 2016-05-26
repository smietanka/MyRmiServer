import java.io.File;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;

public class MyServer extends UnicastRemoteObject implements IMyServer {
	public List<Product> myAllProductList = new ArrayList<Product>();
	public List<Customer> myAllClients = new ArrayList<Customer>();
	
	public JTextArea myTextBox;

	public MyServer(JTextArea myTextBox) throws RemoteException {
		this.myTextBox = myTextBox;
	}
	
	public void WriteLog(String message)
	{
		myTextBox.append("[System]" + message + "\n");
		System.out.print("[System]" + message + "\n");
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
	public List<Product> searchProducts(SearchFilter myFilter) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
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
		Product temp = new Product(myProduct.getName(), myProduct.getPrice());
		myAllProductList.add(temp);
		WriteLog("Dodano produkt.");
	}

	@Override
	public boolean isClientExists(String name) throws RemoteException {
		WriteLog("Zostala wywolana metoda isClientExists z parametrem" + name);
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
}
