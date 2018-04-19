import java.util.*;

public class Process {
	private int size;

	private ArrayList<Frame<LRUPage>> frameTable = new ArrayList<>();
	private ArrayList<LRUPage> pageTable = new ArrayList<>();

	private LinkedList<LRUPage> requestQueue = new LinkedList<>();

	public Process(int simulationSize, int pageNumber, int frameNumber) {
		for(int i = 0; i<pageNumber; ++i) {
			pageTable.add(new LRUPage(i, -1));
		}

		for(int i = 0; i<frameNumber; ++i) {
			frameTable.add(new Frame<>(i, null));
		}

		Random rng = new Random();

		//losowo generateRequests();
		for(int i = 0; i<simulationSize; ++i) {
			requestQueue.add(pageTable.get(rng.nextInt(pageNumber)));
		}
	}
}
