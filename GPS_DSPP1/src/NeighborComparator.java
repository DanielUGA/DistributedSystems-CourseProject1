import java.util.Comparator;


public class NeighborComparator implements Comparator<Neighbor>{

	@Override
	public int compare(Neighbor o1, Neighbor o2) {
		return o1.getDistance()>o2.getDistance() ? 1:(o1.getDistance() == o2.getDistance() ? 0 : -1);
	}

}
