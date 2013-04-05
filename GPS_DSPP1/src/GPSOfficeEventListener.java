import java.rmi.RemoteException;

import edu.rit.ds.RemoteEventListener;

/**
 * @author Punit
 *
 */
public class GPSOfficeEventListener implements
		RemoteEventListener<GPSOfficeEvent> {

	/* (non-Javadoc)
	 * @see edu.rit.ds.RemoteEventListener#report(long, edu.rit.ds.RemoteEvent)
	 */
	@Override
	public void report(long arg0, GPSOfficeEvent event) throws RemoteException {
		try {

			if (event.getStatus() == 3) {
				System.out.println("Package number "
						+ event.getTrackingId()
						+ " lost by "
						+ event.getGpsOffice()
								.getGPSOfficeName()+" office");
				System.exit(1);
			} else if (event.getStatus() == 1) {
				System.out.println("Package number "
						+ event.getTrackingId()
						+ " arrived at "
						+ event.getGpsOffice()
								.getGPSOfficeName()+" office");
			} else if (event.getStatus() == 2) {
				System.out.println("Package number "
						+ event.getTrackingId()
						+ " departed from "
						+ event.getGpsOffice()
								.getGPSOfficeName()+" office");
			} else {
				System.out.println("Package number "
						+ event.getTrackingId()
						+ " delivered from "
						+ event.getGpsOffice()
								.getGPSOfficeName()
						+ " office to " + "(" + event.getX()
						+ "," + event.getY() + ")");
				System.exit(1);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

}
