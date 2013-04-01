import edu.rit.ds.RemoteEvent;

public class GPSOfficeEvent extends RemoteEvent {

	private GPSOfficeRef gpsOffice;
	private String trackingId;
	private double x;
	private double y;
	private boolean arrived;

	public GPSOfficeRef getGpsOffice() {
		return gpsOffice;
	}

	public void setGpsOffice(GPSOfficeRef gpsOffice) {
		this.gpsOffice = gpsOffice;
	}

	public String getTrackingId() {
		return trackingId;
	}

	public void setTrackingId(String trackingId) {
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

	public boolean isArrived() {
		return arrived;
	}

	public void setArrived(boolean arrived) {
		this.arrived = arrived;
	}

	
	public GPSOfficeEvent(GPSOfficeRef office, String id, double xCoord,
			double yCoord, boolean arr) {

		gpsOffice = office;
		trackingId = id;
		x = xCoord;
		y = yCoord;
		arrived = arr;
	}

}
