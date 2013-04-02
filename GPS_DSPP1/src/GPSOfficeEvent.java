import edu.rit.ds.RemoteEvent;

public class GPSOfficeEvent extends RemoteEvent {

	private GPSOfficeRef gpsOffice;
	private long trackingId;
	private double x;
	private double y;
	private int status;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public GPSOfficeRef getGpsOffice() {
		return gpsOffice;
	}

	public void setGpsOffice(GPSOfficeRef gpsOffice) {
		this.gpsOffice = gpsOffice;
	}

	public long getTrackingId() {
		return trackingId;
	}

	public void setTrackingId(long trackingId) {
		this.trackingId = trackingId;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}


	
	public GPSOfficeEvent(GPSOfficeRef office, long id, double xCoord,
			double yCoord, int status) {

		gpsOffice = office;
		trackingId = id;
		x = xCoord;
		y = yCoord;
		this.status = status;
	}

}
