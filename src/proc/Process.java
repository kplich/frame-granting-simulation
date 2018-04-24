package proc;

import frames_pages.*;

import java.util.*;

/**
 * proc.Process class.
 * Size of the process corresponds to the number of process' pages.
 */
public class Process {
	protected int framesGranted;
	protected int processSize;
	protected int numberOfRequests;

	protected int pageFaults;
	protected int framesUsed;

	protected ArrayList<Frame> frameTable;
	protected ArrayList<Page> pageTable;
	protected LinkedList<Page> requestQueue;


	public Process(int processSize, int framesGranted, int numberOfRequests) {
		this.pageFaults = 0;
		this.processSize = processSize;
		this.numberOfRequests = numberOfRequests;

		pageTable = new ArrayList<>();
		for(int i = 0; i<processSize; ++i) {
			pageTable.add(new Page(i, -1));
		}

		this.framesGranted = framesGranted;

		frameTable = new ArrayList<>();
		for(int i = 0; i<framesGranted; ++i) {
			frameTable.add(new Frame(i, null));
		}

		requestQueue = new LinkedList<>();
		generateRequests();
	}

	public Process(int processSize, int framesGranted, int numberOfRequests, LinkedList<Page> requestQueue) {
		this.pageFaults = 0;
		this.processSize = processSize;
		this.numberOfRequests = numberOfRequests;

		pageTable = new ArrayList<>();
		for (int i = 0; i < processSize; ++i) {
			pageTable.add(new Page(i, -1));
		}

		this.framesGranted = framesGranted;

		frameTable = new ArrayList<>();
		for (int i = 0; i < framesGranted; ++i) {
			frameTable.add(new Frame(i, null));
		}

		this.requestQueue = requestQueue;
	}

	public Process(ArrayList<Frame> frameTable, ArrayList<Page> pageTable, LinkedList<Page> requestQueue) {
		this.frameTable = frameTable;
		this.pageTable = pageTable;
		this.requestQueue = requestQueue;

		this.framesGranted = frameTable.size();
		this.processSize = pageTable.size();
		this.numberOfRequests = requestQueue.size();

		this.pageFaults = 0;
		this.framesUsed = 0;
	}

	/**
	 * Generates a page request sequence for the process
	 */
	protected void generateRequests() {
		assert requestQueue != null: "Request queue mustn't be null!";
		assert pageTable != null: "frames_pages.Page table mustn't be null!";

		Random rng = new Random();

		while(requestQueue.size() < numberOfRequests) {
			double randomChance = rng.nextDouble();
			int newPageIndex = 0;

			try {
				if (requestQueue.size() == 0 || randomChance < 0.1) {
					newPageIndex = rng.nextInt(processSize);
				}
				else if (randomChance < 0.55) {
					newPageIndex = requestQueue.peekLast().getPageNumber() + rng.nextInt((int) (0.05 * processSize) + 1);
				}
				else {
					newPageIndex = requestQueue.peekLast().getPageNumber() - rng.nextInt((int) (0.05 * processSize) + 1);
				}

				requestQueue.add(pageTable.get(newPageIndex));
			}
			catch (IndexOutOfBoundsException e) {

			}
		}
	}

	public void dealWithRequest() {
		Page requestedPage = requestQueue.pollFirst();
		assert requestedPage != null: "frames_pages.Page mustn't be null!";

		markTimeSinceLastRef();

		//printOut(requestedPage);

		if(requestedPage.getFrameGiven() < 0) { //page requested isn't loaded into a frame
			++pageFaults;

			freeUpSomeMemory();

			allocate(requestedPage);
		}
		else {
			requestedPage.setTimeSinceLastReference(0);
		}
	}

	protected void markTimeSinceLastRef() {
		for (Frame frame : frameTable) {
			if (frame.getPageGiven() != null) {
				frame.getPageGiven().countTimeSinceLastReference();
			}
		}
	}

		public void freeUpSomeMemory() {
		//sprawdzamy czy sa jeszcze wolne ramki - wtedy ustawiamy je na poczatek
		if (framesUsed < framesGranted) {
			sortFramesByPageUsed(frameTable);
		}
		//jesli nie ma, sortujemy ramki wedlug zadanego przez algorytm kryterium
		else {
			sortFramesByTimeSinceReference(frameTable);
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

	//po prostu przesun puste ramki na poczatek listy
	public static void sortFramesByPageUsed(ArrayList<Frame> frameTable) {
		frameTable.sort((o1, o2) -> {
			if (o1.getPageGiven() == null)
				return -1;
			else if (o2.getPageGiven() == null)
				return 1;

			else return 0;
		});
	}

	//na poczatku listy niech znajda sie ramki ktore zostaly najdawniej uzyte
	public static void sortFramesByTimeSinceReference(ArrayList<Frame> frameTable) {
		frameTable.sort((o1, o2) -> {
			int timeOfPage1 = Integer.MAX_VALUE;
			int timeOfPage2 = Integer.MAX_VALUE;

			if(o1.getPageGiven() != null) {
				timeOfPage1 = o1.getPageGiven().getTimeSinceLastReference();
			}
			if(o2.getPageGiven() != null) {
				timeOfPage2 = o2.getPageGiven().getTimeSinceLastReference();
			}

			return -Integer.compare(timeOfPage1, timeOfPage2);
		});
	}

	protected void allocate(Page requestedPage) {
		//utworz nowe polaczenie!
		frameTable.get(0).setPageGiven(requestedPage);
		requestedPage.setFrameGiven(frameTable.get(0).getFrameIndex());
		requestedPage.setTimeSinceLastReference(0);
		++framesUsed;
	}

	protected void printOut(Page requestedPage) {
		System.out.println("number of page errors: " + pageFaults + "\n");
		System.out.println("requested page number: " + requestedPage.getPageNumber());
		System.out.println("requested page's frame: " + requestedPage.getFrameGiven());

		System.out.println("memory:");
		System.out.print("[");
		for (Frame frame : frameTable) {
			if (frame.getPageGiven() == null) {
				System.out.print("-\t");
			}
			else {
				System.out.print(frame.getPageGiven().getPageNumber() + "\t");
			}

		}
		System.out.println("]\n------------------\n");
	}

	public int getPageFaults() {
		return pageFaults;
	}

	public int getProcessSize() {
		return processSize;
	}

	public void setRequestQueue(LinkedList<Page> requestQueue) {
		this.requestQueue = requestQueue;
	}
}
