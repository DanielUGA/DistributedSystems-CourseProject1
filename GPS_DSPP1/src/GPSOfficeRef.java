import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import edu.rit.ds.Lease;
import edu.rit.ds.RemoteEventListener;

public interface GPSOfficeRef extends Remote {

	public long checkPackage(long trackingNumber, double x, double y)
			throws RemoteException;

	public void forwardPackage(final GPSOfficeRef office, final long trackingNumber, final double x2,
			final double y2) throws RemoteException;

	// called from constructor
	public void generateNeighbors() throws RemoteException;

	public String getGPSOfficeName() throws RemoteException;

	public double[] getGPSOfficeCoordinates() throws RemoteException;

	public List<Neighbor> getNeighbors() throws RemoteException;

	public void setNeighbors(List<Neighbor> offices) throws RemoteException;

	public Lease addListener(RemoteEventListener<GPSOfficeEvent> listener)
			throws RemoteException;

}
