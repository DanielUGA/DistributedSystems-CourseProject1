import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


public interface GPSOfficeRef extends Remote{
	
	public void checkPackage() throws RemoteException;
	public void forwardPackage() throws RemoteException;
	
	// called from constructor
	public void generateNeighborSet() throws RemoteException;
	
	public String getGPSOfficeName() throws RemoteException;
	public double[] getGPSOfficeCoordinates() throws RemoteException;
	public List<Neighbor> getNeighbors() throws RemoteException;
	public void setNeighgors(List<Neighbor> offices) throws RemoteException;

}
