import java.io.File;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import javax.swing.JTextArea;

public class MyServer extends UnicastRemoteObject implements IMyServer {
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
	public void registerClient(String name) throws RemoteException {
		WriteLog(name + " sie polaczyl.");
	}
}
