import java.rmi.RemoteException;

import edu.rit.ds.RemoteEventListener;

/**
 * @author Punit
 * 
 */
public class GPSOfficeEventListener implements
		RemoteEventListener<GPSOfficeEvent> {

	private static final int ARRIVED = 1;
	private static final int DEPARTED = 2;
	private static final int LOST = 3;
	private static boolean shutDown;

	public GPSOfficeEventListener(boolean shutDown) {
		this.shutDown = shutDown;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.rit.ds.RemoteEventListener#report(long, edu.rit.ds.RemoteEvent)
	 */
	@Override
	public void report(long arg0, GPSOfficeEvent event) throws RemoteException {
		try {

			if (event.getStatus() == LOST) {
				System.out.println("Package number " + event.getTrackingId()
						+ " lost by " + event.getGpsOffice().getGPSOfficeName()
						+ " office");
				if (shutDown)
					System.exit(1);
			} else if (event.getStatus() == ARRIVED) {
				System.out.println("Package number " + event.getTrackingId()
						+ " arrived at "
						+ event.getGpsOffice().getGPSOfficeName() + " office");
			} else if (event.getStatus() == DEPARTED) {
				System.out.println("Package number " + event.getTrackingId()
						+ " departed from "
						+ event.getGpsOffice().getGPSOfficeName() + " office");
			} else {
				System.out.println("Package number " + event.getTrackingId()
						+ " delivered from "
						+ event.getGpsOffice().getGPSOfficeName()
						+ " office to " + "(" + event.getX() + ","
						+ event.getY() + ")");
				if (shutDown)
					System.exit(1);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

}
