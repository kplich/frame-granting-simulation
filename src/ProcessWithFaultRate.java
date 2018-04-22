import java.util.*;

/**
 * Process class.
 * Size of the process corresponds to the number of process' pages.
 */
public class ProcessWithFaultRate extends Process {
	private int pastReferencesNumber = 50;

	//hit - true, miss - false
	private LinkedList<Boolean> hitQueue;
	private double faultRate;

	public ProcessWithFaultRate(final int processSize, int framesGranted, int numberOfRequests) {
		super(processSize, framesGranted, numberOfRequests);

		hitQueue = new LinkedList<>();
	}

	public void dealWithRequest() {
		Page requestedPage = requestQueue.pollFirst();
		assert requestedPage != null : "Page mustn't be null!";

		markTimeSinceLastRef();

		//printOut(requestedPage);

		if (requestedPage.getFrameGiven() < 0) { //page requested isn't loaded into a frame
			++pageFaults;

			freeUpSomeMemory();

			allocate(requestedPage);

			calculateFaultRate(false);
		}
		else {
			requestedPage.setTimeSinceLastReference(0);
			calculateFaultRate(true);
		}

		if(faultRate < 0.25) {
			removeFrame();
		}
		else if(faultRate > 0.75) {
			grantFrame();
		}
	}

	private void calculateFaultRate(boolean hit) {
		if(hitQueue.size() > pastReferencesNumber) {
			hitQueue.removeFirst();
		}
		hitQueue.add(hit);

		int missCount = 0;
		for(Boolean b: hitQueue) {
			if(!b) {
				++missCount;
			}
		}
		faultRate = (double) (missCount/pastReferencesNumber);
	}

	private void removeFrame() {
		if(framesGranted > 1) {
			sortFramesByTimeSinceReference();
			frameTable.remove(0);
			--framesGranted;
		}
	}

	private void grantFrame() {
		frameTable.add(new Frame(framesGranted++, null));

	}
}
