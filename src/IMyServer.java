import java.rmi.*;
import java.util.List;

public interface IMyServer extends Remote {
	public void runServer() throws RemoteException;
	public void stopServer() throws RemoteException;
	
	
	public String getHelloWorld() throws RemoteException;
	public String getString(String myString) throws RemoteException;
	public void registerClient(String name) throws RemoteException;
	
	public List<Product> searchProducts(SearchFilter myFilter) throws RemoteException;
	public List<Product> showAllProducts() throws RemoteException;
	public void buyProduct(Product myProduct) throws RemoteException;
	public Product showProduct(Product myProduct) throws RemoteException;	
}