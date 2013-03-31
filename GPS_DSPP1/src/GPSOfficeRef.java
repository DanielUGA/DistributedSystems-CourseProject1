import java.rmi.Remote;
import java.rmi.RemoteException;


public interface GPSOfficeRef extends Remote{
	
	public void checkPackage() throws RemoteException;
	public void forwardPackage() throws RemoteException;
	
	// called from constructor
	public void generateNeighborSet() throws RemoteException;
	
	public String getGPSOfficeName() throws RemoteException;
	public double[] getGPSOfficeCoordinates() throws RemoteException;
	public Neighbor[] getNeighbors() throws RemoteException;
	public void setNeighgors(Neighbor[] offices) throws RemoteException;

}
