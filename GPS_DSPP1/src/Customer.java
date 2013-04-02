import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import edu.rit.ds.Lease;
import edu.rit.ds.RemoteEventListener;
import edu.rit.ds.registry.NotBoundException;
import edu.rit.ds.registry.RegistryProxy;

public class Customer {

	private static String origin;
	private static double x;
	private static double y;
	private static long trackingNumber;
	private static RegistryProxy registry;
	private static RemoteEventListener<GPSOfficeEvent> officeListener;

	public static void main(String[] args) {

		if (args.length != 5) {
			showUsage();
		}

		String host = args[0];
		origin = args[2];
		int port = 0;
		try {
			port = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			System.out.println("Port should be an integer value");
			e.printStackTrace();
			System.exit(1);
		}

		try {
			x = Double.parseDouble(args[3]);
			y = Double.parseDouble(args[4]);
		} catch (NumberFormatException e) {
			System.out.println("X and Y co-ordinates should be double value");
			e.printStackTrace();
			System.exit(1);
		}

		try {

			registry = new RegistryProxy(host, port);
			officeListener = new RemoteEventListener<GPSOfficeEvent>() {
				public void report(long seqnum, GPSOfficeEvent event) {
					// Print tracking info on the console.

					if (trackingNumber == event.getTrackingId()) {

						try {

							if (event.getStatus() == 3)
								System.out.println("Package number "
										+ event.getTrackingId()
										+ " lost by "
										+ event.getGpsOffice()
												.getGPSOfficeName());
							else if (event.getStatus() == 1)
								System.out.println("Package number "
										+ event.getTrackingId()
										+ " arrived at "
										+ event.getGpsOffice()
												.getGPSOfficeName());
							else if (event.getStatus() == 2) {
								System.out.println("Package number "
										+ event.getTrackingId()
										+ " departed from "
										+ event.getGpsOffice()
												.getGPSOfficeName());
							} else {
								System.out.println("Package number "
										+ event.getTrackingId()
										+ " delivered from "
										+ event.getGpsOffice()
												.getGPSOfficeName()
										+ " office to " + "(" + event.getX()
										+ "," + event.getY() + ")");
							}
						} catch (RemoteException e) {
							e.printStackTrace();
						}
					}
				}
			};
			UnicastRemoteObject.exportObject(officeListener, 0);

			listenToOffices();

			GPSOfficeRef gpsOffice = (GPSOfficeRef) registry.lookup(origin);
			trackingNumber = gpsOffice.checkPackage(0l, x, y);

		} catch (RemoteException e) {
			System.out.println("No Remote Server at host=" + host
					+ " and port=" + port);
			System.exit(1);
		} catch (NotBoundException e) {
			System.out.println("No GPS Office in " + origin);
			System.exit(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Packet lost");
			System.exit(1);
		}
	}

	private static void listenToOffices() {

		try {
			List<String> offices = registry.list();

			for (String office : offices) {
				GPSOfficeRef gpsOffice;

				gpsOffice = (GPSOfficeRef) registry.lookup(office);
				gpsOffice.addListener(officeListener);

			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}

	}

	private static void showUsage() {

		System.err.println("Usage: java Customer <host> <port> <name> <X> <Y>");
		System.err.println("<host> = Registry Server's host");
		System.err.println("<port> = Registry Server's port");
		System.err
				.println("<name> = name of the city in which GPS office is located");
		System.err.println("<X> = X co-ordinate of the destinataion");
		System.err.println("<Y> = Y co-ordinate of the destinataion");
		System.exit(1);
	}

}
