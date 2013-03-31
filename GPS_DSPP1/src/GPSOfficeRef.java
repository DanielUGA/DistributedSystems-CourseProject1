import java.rmi.Remote;
import java.rmi.RemoteException;


public interface GPSOfficeRef extends Remote{
	
	public void checkPackage() throws RemoteException;
	public void forwardPackage() throws RemoteException;
	
	// called from constructor
	public void generateNeighborSet() throws RemoteException;

}
