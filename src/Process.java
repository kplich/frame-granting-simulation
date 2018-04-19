import java.util.*;

public class Process  {
	private int processSize;

	private ArrayList<Frame<LRUPage>> frameTable = new ArrayList<>();
	private ArrayList<LRUPage> pageTable = new ArrayList<>();

	private LinkedList<LRUPage> requestQueue = new LinkedList<>();

	private int pageFaults;

	public Process(int simulationSize, int pageNumber, int frameNumber) {
		for(int i = 0; i<pageNumber; ++i) {
			pageTable.add(new LRUPage(i, -1));
		}

		for(int i = 0; i<frameNumber; ++i) {
			frameTable.add(new Frame<>(i, null));
		}
	}

	public int run() {
		sortPagesByIndex();
		sortFramesByIndex();
		generateRequests();

		while (!requestQueue.isEmpty()) {
			dealWithPage(requestQueue.poll());
		}

		int tempPageFaults = pageFaults;
		pageFaults = 0;

		return tempPageFaults;
	}

	protected void sortPagesByIndex() {
		pageTable.sort(Comparator.comparingInt(Page::getPageNumber));
	}

	protected void sortFramesByIndex() {
		frameTable.sort(Comparator.comparingInt(Frame::getFrameIndex));
	}

	private void generateRequests(int simulationSize) {
		Random rng = new Random();

		for (int i = 0; i < simulationSize; ++i) {
			requestQueue.add(pageTable.get(rng.nextInt(pageTable.size() - 1)));
		}
	}

	private void dealWithPage(LRUPage requestedPage) {
		prepare(); //count some time/reference, etc.

		//printOut(requestedPage); //print out simulation details

		if (requestedPage.getFrameGiven() < 0) {//if the page isn't loaded into memory
			++pageFaults; //we have to deal with a page error

			freeUpSomeMemory();

			allocatePage(requestedPage);

			//aaaand... that's it for today!
		}
		else {
			whenPageWasLoaded(requestedPage);
		}
	}

	public void prepare() {
		markTimeSinceLastRef();
		sortPagesByIndex();
		sortFramesByIndex();
	}

	private void markTimeSinceLastRef() {
		for (Frame<LRUPage> frame : frameTable) {
			if (frame.getPageGiven() != null) {
				frame.getPageGiven().countTimeSinceLastReference();
			}
		}
	}

	public void freeUpSomeMemory() {
		//sprawdzamy czy sa jeszcze wolne ramki - wtedy ustawiamy je na poczatek
		if (framesUsed < numberOfFrames) {
			sortFramesByPageUsed();
		}
		//jesli nie ma, sortujemy ramki wedlug zadanego przez algorytm kryterium
		else {
			sortFramesByTimeSinceReference();
		}

		//jesli w ramce znajdowala sie jakas strona to znaczy
		//ze trzeba ja bylo usunac i faktycznie mamy mniej uzytych ramek
		if (frameTable.get(0).getPageGiven() != null) {
			--framesUsed;

			//usun poprzednie polaczenie!
			frameTable.get(0).getPageGiven().setFrameGiven(-1);
			frameTable.get(0).getPageGiven().setTimeSinceLastReference(0);
		}
	}


}
