import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import edu.rit.ds.RemoteEventListener;
import edu.rit.ds.registry.NotBoundException;
import edu.rit.ds.registry.RegistryEvent;
import edu.rit.ds.registry.RegistryEventFilter;
import edu.rit.ds.registry.RegistryEventListener;
import edu.rit.ds.registry.RegistryProxy;

/**
 * @author Punit
 *
 */
public class Headquarters {

	private static RegistryProxy registry;
	private static RegistryEventListener registryListener;
	private static RegistryEventFilter registryFilter;
	private static RemoteEventListener<GPSOfficeEvent> officeListener;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length != 2) {
			showUsage();
		}

		String host = args[0];
		int port;
		try {
			port = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			throw new NumberFormatException("Port has to be an Integer value");
		}

		try {
			registry = new RegistryProxy(host, port);
		} catch (RemoteException e2) {
			e2.printStackTrace();
		}
		
		registryListener = new RegistryEventListener() {
			public void report(long seqnum, RegistryEvent event) {
				listenToOffice(event.objectName());
			}
		};
		try {
			UnicastRemoteObject.exportObject(registryListener, 0);
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}

		officeListener = new RemoteEventListener<GPSOfficeEvent>() {
			public void report(long seqnum, GPSOfficeEvent event) {
				// Print tracking info on the console.

				try {

					if (event.getStatus() == 3)
						System.out.println("Package number "
								+ event.getTrackingId() + " lost by "
								+ event.getGpsOffice().getGPSOfficeName()+" office");
					else if (event.getStatus() == 1)
						System.out.println("Package number "
								+ event.getTrackingId() + " arrived at "
								+ event.getGpsOffice().getGPSOfficeName()+" office");
					else if (event.getStatus() == 2) {
						System.out.println("Package number "
								+ event.getTrackingId() + " departed from "
								+ event.getGpsOffice().getGPSOfficeName()+" office");
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
		};
		
		try {
			UnicastRemoteObject.exportObject(officeListener, 0);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		
		registryFilter = new RegistryEventFilter().reportType("GPSOffice")
				.reportBound();
		try {
			registry.addEventListener(registryListener, registryFilter);
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
		
		try {
			for (String office : registry.list("GPSOffice")) {
				listenToOffice(office);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param office
	 */
	private static void listenToOffice(String office) {

		try {
			GPSOfficeRef gpsOffice = (GPSOfficeRef) registry.lookup(office);
			gpsOffice.addListener(officeListener);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 */
	private static void showUsage() {

		System.err.println("Usage: java Headquarters <host> <port>");
		System.err.println("<host> = Registry Server's host");
		System.err.println("<port> = Registry Server's port");
		System.exit(1);
	}

}
