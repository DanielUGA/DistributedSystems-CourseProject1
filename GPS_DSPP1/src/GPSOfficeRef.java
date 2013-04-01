import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import edu.rit.ds.Lease;
import edu.rit.ds.RemoteEventListener;

public interface GPSOfficeRef extends Remote {

	public String checkPackage() throws RemoteException;

	public String forwardPackage() throws RemoteException;

	// called from constructor
	public void generateNeighbors() throws RemoteException;

	public String getGPSOfficeName() throws RemoteException;

	public double[] getGPSOfficeCoordinates() throws RemoteException;

	public List<Neighbor> getNeighbors() throws RemoteException;

	public void setNeighbors(List<Neighbor> offices) throws RemoteException;

	public Lease addListener(RemoteEventListener<GPSOfficeEvent> listener)
			throws RemoteException;

}
