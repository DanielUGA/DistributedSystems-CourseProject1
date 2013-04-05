import java.util.Collections;
import java.util.Comparator;


/**
 * class NeighborComparator compares the {@linkplain Neighbor}s while sorting using {@linkplain Collections}.sort()
 * @author Punit 
 *
 */
public class NeighborComparator implements Comparator<Neighbor>{

	@Override
	public int compare(Neighbor o1, Neighbor o2) {
		return o1.getDistance()>o2.getDistance() ? 1:(o1.getDistance() == o2.getDistance() ? 0 : -1);
	}

}
