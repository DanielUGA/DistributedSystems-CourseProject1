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
	 *            x co-ordinate of the destination
	 * @param y
	 *            y co-ordinate of the destination
	 * @param officeListener
	 *            an instance of {@linkplain GPSOfficeEventListener} that is
	 *            added to the {@linkplain GPSOffice} instances to which the
	 *            packages are sent
	 * @return Tracking Number of the package
	 * @throws RemoteException
	 *             exception thrown when a Remote Object is not found
	 * @throws NotBoundException
	 *             exception thrown when lookup on Registry Server fails
	 * @throws InterruptedException
	 *             exception thrown when a Thread is interrupted.
	 */
	public long checkPackage(double x, double y,
			final RemoteEventListener<GPSOfficeEvent> officeListener)
			throws RemoteException, NotBoundException, InterruptedException;

	/**
	 * @param officeName
	 *            name of the {@linkplain GPSOffice} to which the package is to
	 *            be sent
	 * @param trackingNumber
	 *            tracking number of the package
	 * @param x2
	 *            x co-ordinate of the destination
	 * @param y2
	 *            y co-ordination of the destionation
	 * @param officeListener
	 *            {@linkplain GPSOfficeEventListener} listener to be added to
	 *            the next GPS Office
	 * @throws RemoteException
	 *             exception thrown when the next GPS Office is not found during
	 *             lookup
	 */
	public void forwardPackage(String officeName, final long trackingNumber,
			final double x2, final double y2,
			final RemoteEventListener<GPSOfficeEvent> officeListener)
			throws RemoteException;

	/**
	 * Generates the neighbors of the current node by looking up in the registry
	 * and fetching all the {@linkplain GPSOffice} in the registry. It will
	 * update the Neighbor list in the {@linkplain GPSOffice} class
	 * 
	 * @throws RemoteException
	 *             exception thrown in the Remote object is not available
	 */
	public void generateNeighbors() throws RemoteException;

	/**
	 * Returns the name of the <TT>this</TT> office
	 * 
	 * @return name of the {@linkplain GPSOffice}
	 * @throws RemoteException
	 *             exception thrown in the Remote object is not available
	 */
	public String getGPSOfficeName() throws RemoteException;

	/**
	 * Returns the x and y (double) co-ordinates of <tt>this</tt> office
	 * 
	 * @return a double array with two elements
	 * @throws RemoteException
	 *             exception thrown in the Remote object is not available
	 */
	public double[] getGPSOfficeCoordinates() throws RemoteException;

	/**
	 * Returns the <TT>List</TT> of {@linkplain Neighbor}s of <TT>this</TT>
	 * office
	 * 
	 * @return List of {@linkplain Neighbor}s
	 * @throws RemoteException
	 *             exception thrown in the Remote object is not available
	 */
	public List<Neighbor> getNeighbors() throws RemoteException;

	/**
	 * Sets the neighbors of <tt>this</tt> office
	 * 
	 * @param offices
	 *            <tt>List</tt> of neighbors
	 * @throws RemoteException
	 *             exception thrown in the Remote object is not available
	 */
	public void setNeighbors(List<Neighbor> offices) throws RemoteException;

	/**
	 * Add listener to the office
	 * 
	 * @param listener
	 *            {@linkplain GPSOfficeEventListener}
	 * @return Lease of the Listener
	 * @throws RemoteException
	 *             exception thrown in the Remote object is not available
	 */
	public Lease addListener(RemoteEventListener<GPSOfficeEvent> listener)
			throws RemoteException;

	/**
	 * Add listener to the office along with the filter
	 * 
	 * @param listener
	 *            {@linkplain GPSOfficeEventListener}
	 * @param filter
	 *            {@linkplain GPSOfficeEventFilter}
	 * @return Lease of the listener
	 * @throws RemoteException
	 *             exception thrown in the Remote object is not available
	 */
	public Lease addListener(RemoteEventListener<GPSOfficeEvent> listener,
			RemoteEventFilter<GPSOfficeEvent> filter) throws RemoteException;

	/**
	 * Examines the package (with a 3 seconds delay), generates neighbors and
	 * finds out where the package should be sent next.
	 * 
	 * @param trackingNumber
	 *            tracking number of the package
	 * @param x2
	 *            x co-ordinate of the destination
	 * @param y2
	 *            y co-ordinate of the destionation
	 * @param officeListener
	 *            listener of the office
	 * @return void
	 * @throws RemoteException
	 *             exception thrown in the Remote object is not available
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
