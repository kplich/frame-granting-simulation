package frames_pages;

/**
 * Simple wrapper class created to make creating a sortable page table possible.
 */
public class Page {
	/**
	 * Number of a page.
	 */
	private int pageNumber;

	/**
	 * Index of a frame a page has been given (or -1 if page isn't loaded in to memory).
	 */
	private int frameGiven;

	private int timeSinceLastReference;

	public Page(int pageNumber, int frameGiven) {
		this.pageNumber = pageNumber;
		this.frameGiven = frameGiven;
		timeSinceLastReference = Integer.MAX_VALUE;
	}

	public Page(Page page) {
		this.pageNumber = page.getPageNumber();
		this.frameGiven = page.getFrameGiven();
		this.timeSinceLastReference = page.getTimeSinceLastReference();
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getFrameGiven() {
		return frameGiven;
	}

	public void setFrameGiven(int frameGiven) {
		this.frameGiven = frameGiven;
	}

	public int getTimeSinceLastReference() {
		return timeSinceLastReference;
	}

	public void setTimeSinceLastReference(int timeSinceLastReference) {
		this.timeSinceLastReference = timeSinceLastReference;
	}

	public void countTimeSinceLastReference() {
		++timeSinceLastReference;
	}
}
