import java.rmi.RemoteException;

import edu.rit.ds.RemoteEventListener;

public class GPSOfficeEventListener implements
		RemoteEventListener<GPSOfficeEvent> {

	private long trackingNumber;
	private String origin;

	public GPSOfficeEventListener(long trackingNo, String origin) {
		trackingNumber = trackingNo;
		this.origin = origin;
	}

	@Override
	public void report(long arg0, GPSOfficeEvent event) throws RemoteException {
		try {
			if (trackingNumber == event.getTrackingId()
					|| (event.getGpsOffice().getGPSOfficeName().equals(origin) && trackingNumber == 0l)) {

				try {

					if (event.getStatus() == 3)
						System.out.println("Package number "
								+ event.getTrackingId() + " lost by "
								+ event.getGpsOffice().getGPSOfficeName());
					else if (event.getStatus() == 1)
						System.out.println("Package number "
								+ event.getTrackingId() + " arrived at "
								+ event.getGpsOffice().getGPSOfficeName());
					else if (event.getStatus() == 2) {
						System.out.println("Package number "
								+ event.getTrackingId() + " departed from "
								+ event.getGpsOffice().getGPSOfficeName());
					} else {
						System.out.println("Package number "
								+ event.getTrackingId() + " delivered from "
								+ event.getGpsOffice().getGPSOfficeName()
								+ " office to " + "(" + event.getX() + ","
								+ event.getY() + ")");
					}
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

}
