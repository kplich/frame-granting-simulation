package proc;

import frames_pages.*;


import java.util.*;

/**
 * proc.Process class.
 * Size of the process corresponds to the number of process' pages.
 */
public class ProcessWithWorkingSet extends Process {
	private int pastReferencesNumber = 50;

	private LinkedList<Page> completedRequests;


	public ProcessWithWorkingSet(final int processSize, int framesGranted, int numberOfRequests) {
		super(processSize, framesGranted, numberOfRequests);
		completedRequests = new LinkedList<>();
	}

	@Override
	public void dealWithRequest() {
		Page requestedPage = requestQueue.pollFirst();
		assert requestedPage != null : "frames_pages.Page mustn't be null!";

		markTimeSinceLastRef();

		//printOut(requestedPage);

		if (requestedPage.getFrameGiven() < 0) { //page requested isn't loaded into a frame
			++pageFaults;

			freeUpSomeMemory();

			allocate(requestedPage);
		}
		else {
			requestedPage.setTimeSinceLastReference(0);
		}

		int workingSetSize = calculateWorkingSet(requestedPage);

		if(workingSetSize > 0) {
			if (workingSetSize < framesGranted) {
				removeFrame();
			}
			else if (workingSetSize > framesGranted) {
				grantFrame();
			}
		}
	}

	private int calculateWorkingSet(Page requestedPage) {
		if(completedRequests.size() > pastReferencesNumber/3) {
			if(completedRequests.size() > pastReferencesNumber) {
				completedRequests.removeFirst();
			}
			completedRequests.add(requestedPage);

			ArrayList<Page> distinctPages = new ArrayList<>();
			for(Page page: completedRequests) {
				if(!distinctPages.contains(page)) {
					distinctPages.add(page);
				}
			}

			return distinctPages.size();
		}
		return -1;
	}

	private void removeFrame() {
		if (framesGranted > 1) {
			sortFramesByTimeSinceReference();
			frameTable.remove(0);
			--framesGranted;
		}
	}

	private void grantFrame() {
		frameTable.add(new Frame(framesGranted++, null));

	}
}
