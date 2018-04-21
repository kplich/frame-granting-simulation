import java.util.*;

/**
 * Process class.
 * Size of the process corresponds to the number of process' pages.
 */
public class Process {
	int processSize;
	int framesGranted;

	ArrayList<LRUPage> pageTable;
	ArrayList<Frame<LRUPage>> frameTable;

	public Process(final int processSize, int framesGranted) {
		this.processSize = processSize;

		pageTable = new ArrayList<>();
		for(int i = 0; i<processSize; ++i) {
			pageTable.add(new LRUPage(i, -1));
		}

		this.framesGranted = framesGranted;

		frameTable = new ArrayList<>();
		for(int i = 0; i<framesGranted; ++i) {
			frameTable.add(new Frame<>(i, null));
		}
	}
}
