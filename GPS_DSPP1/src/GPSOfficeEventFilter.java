import edu.rit.ds.RemoteEventFilter;

/**
 * @author Punit
 *
 */
public class GPSOfficeEventFilter implements RemoteEventFilter<GPSOfficeEvent> {


	private long trackingNumber;

	/**
	 * @param trackingNumber
	 */
	public GPSOfficeEventFilter(long trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	/* (non-Javadoc)
	 * @see edu.rit.ds.RemoteEventFilter#doReportEvent(edu.rit.ds.RemoteEvent)
	 */
	@Override
	public boolean doReportEvent(GPSOfficeEvent event) {

		if (event.getTrackingId() == trackingNumber) {
			return true;
		} else
			return false;
	}

}
