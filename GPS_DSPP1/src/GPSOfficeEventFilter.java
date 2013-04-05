import edu.rit.ds.RemoteEventFilter;

/**
 * class GPSOfficeEventFilter defines the filter to filter the GPS office
 * events.
 * 
 * @author Punit
 * @version 04-05-2013
 * 
 */
public class GPSOfficeEventFilter implements RemoteEventFilter<GPSOfficeEvent> {

	private long trackingNumber;

	/**
	 * Set the tracking number in the filter.
	 * 
	 * @param trackingNumber
	 *            tracking number of the package
	 */
	public GPSOfficeEventFilter(long trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
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
