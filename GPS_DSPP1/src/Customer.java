// File: Customer.java
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import edu.rit.ds.RemoteEventListener;
import edu.rit.ds.registry.NotBoundException;
import edu.rit.ds.registry.RegistryProxy;

/**
 * Class Customer represents customer using the GPS (Geographic Package Service)
 * to send a package to destination, given X and Y co-ordinates of the
 * destination.
 * 
 * @author Punit
 * @version 04-05-2013
 * 
 */
public class Customer {

	private static String origin;
	private static double x;
	private static double y;
	private static long trackingNumber;
	private static RegistryProxy registry;
	private static RemoteEventListener<GPSOfficeEvent> officeListener;

	/**
	 * Main Program. Takes 5 arguments viz host, port, name (name of the origin
	 * Office), X(x co-ordinate of the destination), Y(y co-ordinate of the
	 * destination)
	 * 
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {

		// Check if 5 arguments are entered
		if (args.length != 5) {
			showUsage();
		}

		String host = args[0];
		origin = args[2];
		int port = 0;

		// Check if the port is an Integer
		try {
			port = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			System.out.println("Port should be an integer value");
			e.printStackTrace();
			System.exit(1);
		}

		// Check if the co-ordinates are Double
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

			officeListener = new GPSOfficeEventListener(true);

			UnicastRemoteObject.exportObject(officeListener, 0);

			// Look up the origin Office specified by the user
			GPSOfficeRef gpsOffice = (GPSOfficeRef) registry.lookup(origin);
			trackingNumber = gpsOffice.checkPackage(x, y, officeListener);

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

	/**
	 * Display the correct usage of the class execution(command line arguments)
	 * 
	 * @return void
	 */
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
