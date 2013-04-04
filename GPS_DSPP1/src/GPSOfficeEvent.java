import edu.rit.ds.RemoteEvent;

/**
 * @author Punit
 *
 */
public class GPSOfficeEvent extends RemoteEvent {

	private GPSOfficeRef gpsOffice;
	private long trackingId;
	private double x;
	private double y;
	private int status;

	/**
	 * @return
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return
	 */
	public GPSOfficeRef getGpsOffice() {
		return gpsOffice;
	}

	/**
	 * @param gpsOffice
	 */
	public void setGpsOffice(GPSOfficeRef gpsOffice) {
		this.gpsOffice = gpsOffice;
	}

	/**
	 * @return
	 */
	public long getTrackingId() {
		return trackingId;
	}

	/**
	 * @param trackingId
	 */
	public void setTrackingId(long trackingId) {
		this.trackingId = trackingId;
	}

	/**
	 * @return
	 */
	public double getX() {
		return x;
	}

	/**
	 * @param x
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * @return
	 */
	public double getY() {
		return y;
	}

	/**
	 * @param y
	 */
	public void setY(double y) {
		this.y = y;
	}


	
	/**
	 * @param office
	 * @param id
	 * @param xCoord
	 * @param yCoord
	 * @param status
	 */
	public GPSOfficeEvent(GPSOfficeRef office, long id, double xCoord,
			double yCoord, int status) {

		gpsOffice = office;
		trackingId = id;
		x = xCoord;
		y = yCoord;
		this.status = status;
	}

}
