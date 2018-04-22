package frames_pages;

public class Frame {
	int frameIndex;
	Page pageGiven;

	public Frame(int frameIndex, Page pageGiven) {
		this.frameIndex = frameIndex;
		this.pageGiven = pageGiven;
	}

	public int getFrameIndex() {
		return frameIndex;
	}

	public void setFrameIndex(int frameIndex) {
		this.frameIndex = frameIndex;
	}

	public Page getPageGiven() {
		return pageGiven;
	}

	public void setPageGiven(Page pageGiven) {
		this.pageGiven = pageGiven;
	}
}
