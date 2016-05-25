import java.rmi.*;

public interface IMyServer extends Remote {
	public void runServer() throws RemoteException;
	public void stopServer() throws RemoteException;
	
	
	public String getHelloWorld() throws RemoteException;
	public String getString(String myString) throws RemoteException;
	public void registerClient(String name) throws RemoteException;
}