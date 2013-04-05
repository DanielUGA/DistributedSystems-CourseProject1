import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import edu.rit.ds.Lease;
import edu.rit.ds.RemoteEventFilter;
import edu.rit.ds.RemoteEventListener;
import edu.rit.ds.registry.NotBoundException;

public interface GPSOfficeRef extends Remote {

	/**
	 * This method is called from the <TT>Customer</TT> class. A tracking number
	 * for the package is generated (which is system time in milliseconds). Then
	 * it spawns a thread in which {@linkplain examinePackage} is called. After
	 * which it returns the tracking number back to the Customer.
	 * 
	 * @param x
	 * @param y
	 * @param officeListener
	 *            an instance of {@linkplain GPSOfficeEventListener} that is
	 *            added to the {@linkplain GPSOffice} instances to which the
	 *            packages are sent
	 * @return Tracking Number of the package
	 * @throws RemoteException
	 *             exception thrown when a Remote Object is not found
	 * @throws NotBoundException
	 *             exception thrown when lookup on Registry Server fails
	 * @throws InterruptedException exception thrown when a Thread is interrupted.
	 */
	public long checkPackage(double x, double y,
			final RemoteEventListener<GPSOfficeEvent> officeListener)
			throws RemoteException, NotBoundException, InterruptedException;

	/**
	 * @param office
	 * @param trackingNumber
	 * @param x2
	 * @param y2
	 * @param officeListener
	 * @throws RemoteException
	 */
	public void forwardPackage(String officeName, final long trackingNumber,
			final double x2, final double y2,
			final RemoteEventListener<GPSOfficeEvent> officeListener)
			throws RemoteException;

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
			final double y2,
			final RemoteEventListener<GPSOfficeEvent> officeListener)
			throws RemoteException;

	/**
	 * Calculates the distance between two GPS offices, given their X and Y
	 * co-ordinates
	 * 
	 * @param tempX1
	 *            X co-ordinate of the first GPS Office
	 * @param tempY1
	 *            Y co-ordinate of the first Office
	 * @param tempX2
	 *            X co-ordinate of the second GPS Office
	 * @param tempY2
	 *            Y co-ordinate of the second GPS Office
	 * @return distance between the two Offices, given their X and Y
	 *         co-ordinates
	 */
	public double getDistance(double tempX1, double tempY1, double tempX2,
			double tempY2);

}
