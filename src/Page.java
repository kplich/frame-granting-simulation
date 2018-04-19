/**
 * Simple wrapper class created to make creating a sortable page table possible.
 */
public class Page {
	/**
	 * Number of a page.
	 */
	protected int pageNumber;

	/**
	 * Index of a frame a page has been given (or -1 if page isn't loaded in to memory).
	 */
	protected int frameGiven;

	public Page(int pageNumber, int frameGiven) {
		this.pageNumber = pageNumber;
		this.frameGiven = frameGiven;
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
}
