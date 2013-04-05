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

			registryListener = new RegistryEventListener() {
				public void report(long seqnum, RegistryEvent event) {
					listenToOffice(event.objectName());
				}
			};
			UnicastRemoteObject.exportObject(registryListener, 0);

			officeListener = new GPSOfficeEventListener();
			UnicastRemoteObject.exportObject(officeListener, 0);

			registryFilter = new RegistryEventFilter().reportType("GPSOffice")
					.reportBound();
			registry.addEventListener(registryListener, registryFilter);

			for (String office : registry.list("GPSOffice")) {
				listenToOffice(office);
			}

		} catch (RemoteException e2) {
			e2.printStackTrace();
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
