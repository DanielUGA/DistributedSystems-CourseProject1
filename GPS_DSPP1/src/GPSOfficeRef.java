import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import edu.rit.ds.Lease;
import edu.rit.ds.RemoteEventFilter;
import edu.rit.ds.RemoteEventListener;
import edu.rit.ds.registry.NotBoundException;

public interface GPSOfficeRef extends Remote {

	/**
	 * @param trackingNumber
	 * @param x
	 * @param y
	 * @param officeListener
	 * @return
	 * @throws RemoteException
	 * @throws NotBoundException
	 * @throws InterruptedException
	 */
	public long checkPackage(long trackingNumber, double x, double y, final RemoteEventListener<GPSOfficeEvent> officeListener)
			throws RemoteException, NotBoundException, InterruptedException;

	/**
	 * @param office
	 * @param trackingNumber
	 * @param x2
	 * @param y2
	 * @param officeListener
	 * @throws RemoteException
	 */
	public void forwardPackage(final GPSOfficeRef office, final long trackingNumber, final double x2,
			final double y2, final RemoteEventListener<GPSOfficeEvent> officeListener) throws RemoteException;

	// called from constructor
	/**
	 * @param trackingNumber
	 * @param x2
	 * @param y2
	 * @throws RemoteException
	 */
	public void generateNeighbors(long trackingNumber, final double x2,
			final double y2) throws RemoteException;

	/**
	 * @return
	 * @throws RemoteException
	 */
	public String getGPSOfficeName() throws RemoteException;

	/**
	 * @return
	 * @throws RemoteException
	 */
	public double[] getGPSOfficeCoordinates() throws RemoteException;

	/**
	 * @return
	 * @throws RemoteException
	 */
	public List<Neighbor> getNeighbors() throws RemoteException;

	/**
	 * @param offices
	 * @throws RemoteException
	 */
	public void setNeighbors(List<Neighbor> offices) throws RemoteException;

	/**
	 * @param listener
	 * @return
	 * @throws RemoteException
	 */
	public Lease addListener(RemoteEventListener<GPSOfficeEvent> listener)
			throws RemoteException;
	
	/**
	 * @param listener
	 * @param filter
	 * @return
	 * @throws RemoteException
	 */
	public Lease addListener(RemoteEventListener<GPSOfficeEvent> listener,
			RemoteEventFilter<GPSOfficeEvent> filter) throws RemoteException;
	
	/**
	 * @param trackingNumber
	 * @param x2
	 * @param y2
	 * @param officeListener
	 * @return 
	 * @throws RemoteException
	 */
	public void examinePackage(final long trackingNumber, final double x2,
			final double y2, final RemoteEventListener<GPSOfficeEvent> officeListener) throws RemoteException;


}
