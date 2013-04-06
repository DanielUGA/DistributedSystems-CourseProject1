import java.rmi.RemoteException;
import java.util.HashMap;

import edu.rit.ds.RemoteEventListener;

/**
 * class GPSOfficeEventListener listens to the GPS office events.
 * 
 * @author Punit
 * @version 04-05-2013
 * 
 */
public class GPSOfficeEventListener implements
		RemoteEventListener<GPSOfficeEvent> {

	/**
	 * an <tt>int</tt> variable to represent package arrival
	 */
	private static final int ARRIVED = 1;
	/**
	 * an <tt>int</tt> variable to represent package departure
	 */
	private static final int DEPARTED = 2;
	/**
	 * an <tt>int</tt> variable to represent package lost
	 */
	private static final int LOST = 3;
	/**
	 * a <tt>boolean</tt> variable to check if the client has to shut down after
	 * package is either lost or delivered
	 */
	private static boolean shutDown;

	private static HashMap<Long, String> arrivalMap;

	/**
	 * Constructor which takes in a boolean which represents whether to shut
	 * down after package is either lost or delivered, or not.
	 * 
	 * @param shutDown
	 */
	public GPSOfficeEventListener(boolean shut) {
		shutDown = shut;
		arrivalMap = new HashMap<Long,String>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.rit.ds.RemoteEventListener#report(long, edu.rit.ds.RemoteEvent)
	 */
	@Override
	public void report(long arg0, GPSOfficeEvent event) throws RemoteException {

		if (event.getStatus() == LOST) {
			if (arrivalMap.containsKey(event.getTrackingId())
					&& arrivalMap.get(event.getTrackingId()).equals(
							event.getOfficeName())) {
				arrivalMap.remove(event.getTrackingId());
				System.out.println("Package number " + event.getTrackingId()
						+ " lost by " + event.getOfficeName() + " office");
				if (shutDown)
					System.exit(1);
			}
		} else if (event.getStatus() == ARRIVED) {
			System.out.println("Package number " + event.getTrackingId()
					+ " arrived at " + event.getOfficeName() + " office");
			arrivalMap.put(event.getTrackingId(), event.getOfficeName());
		} else if (event.getStatus() == DEPARTED) {
			System.out.println("Package number " + event.getTrackingId()
					+ " departed from " + event.getOfficeName() + " office");
		} else {
			arrivalMap.remove(event.getTrackingId());
			System.out.println("Package number " + event.getTrackingId()
					+ " delivered from " + event.getOfficeName()
					+ " office to " + "(" + event.getX() + "," + event.getY()
					+ ")");
			if (shutDown)
				System.exit(1);
		}
	}

}
