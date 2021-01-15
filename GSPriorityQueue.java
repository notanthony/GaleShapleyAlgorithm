import java.util.PriorityQueue;

/**
 * Custom PriorityQueue to help implement the Gale Shapely Algorithm exactly how it is laid out
 * @author Anthony Zhao
 * 300130883
 *
 */
public class GSPriorityQueue {
	PriorityQueue<RankPair> queue;

	public GSPriorityQueue(int size) {
		queue = new PriorityQueue<>(size);
	}
	
	/* Uses the default add which is O(logn) the only additional steps are from constructing the RankPair but 
	 * the constant number of steps means it does not influence the O
	 * 
	 */
	public void insert(int employeeRanking, int student) {
		queue.add(new RankPair(employeeRanking, student));
	}
	
	/* Uses the default remove which is O(logn) the only additional steps are from getting the student but 
	 * the constant number of steps means it does not influence the O
	 * 
	 */
	public int removeMin() {
		// it should never be empty so remove is better since it will throw an exception
		return queue.remove().getStudent();
	}

}
