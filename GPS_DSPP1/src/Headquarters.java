import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import edu.rit.ds.RemoteEventListener;
import edu.rit.ds.registry.NotBoundException;
import edu.rit.ds.registry.RegistryEvent;
import edu.rit.ds.registry.RegistryEventFilter;
import edu.rit.ds.registry.RegistryEventListener;
import edu.rit.ds.registry.RegistryProxy;

/**
 * class Headquarters represnts the Headquarters of the Geographic Package Service (GPS). 
 * It also listens to all the GPS office events and displays them.
 * @author Punit
 * 
 */
public class Headquarters {

	private static RegistryProxy registry;
	private static RegistryEventListener registryListener;
	private static RegistryEventFilter registryFilter;
	private static RemoteEventListener<GPSOfficeEvent> officeListener;

	/**
	 * Main Program. Takes the host and port values from command line.
	 * @param args
	 */
	public static void main(String[] args) {

		// checks if the number of command line arguments is correct
		if (args.length != 2) {
			showUsage();
		}

		String host = args[0];
		int port;
		
		// checks is the port is an Integer
		try {
			port = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			throw new NumberFormatException("Port has to be an Integer value");
		}

		// creates a registry listener to listens GPS Office binding in the registry
		try {
			registry = new RegistryProxy(host, port);

			registryListener = new RegistryEventListener() {
				public void report(long seqnum, RegistryEvent event) {
					listenToOffice(event.objectName());
				}
			};
			UnicastRemoteObject.exportObject(registryListener, 0);

			officeListener = new GPSOfficeEventListener(false);
			
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
	 * Listens to all the office which are already bound to the registry
	 * @param office name of the office
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
	 * Shows the usage for the class
	 */
	private static void showUsage() {

		System.err.println("Usage: java Headquarters <host> <port>");
		System.err.println("<host> = Registry Server's host");
		System.err.println("<port> = Registry Server's port");
		System.exit(1);
	}

}
