/**
 * Custom Pair to help implement the Gale Shapely Algorithm exactly how it is laid out
 * @author Anthony Zhao
 * 300130883
 *
 */
public class RankPair implements Comparable<RankPair>{
	private int employeeRanking;
	private int student;
	
	public RankPair(int employeeRanking, int student) {
		this.employeeRanking = employeeRanking;
		this.student = student;
	}
	
	//this allows me to set natural order when it is put in an PriorityQueue
	@Override
	public int compareTo(RankPair other) {
		// TODO Auto-generated method stub
		return Integer.compare(getEmployeeRanking(), other.getEmployeeRanking());
	}

	public int getStudent() {
		return student;
	}

	public int getEmployeeRanking() {
		return employeeRanking;
	}

}
