public class Frame<P extends Page> {
	int frameIndex;
	P pageGiven;

	public Frame(int frameIndex, P pageGiven) {
		this.frameIndex = frameIndex;
		this.pageGiven = pageGiven;
	}

	public int getFrameIndex() {
		return frameIndex;
	}

	public void setFrameIndex(int frameIndex) {
		this.frameIndex = frameIndex;
	}

	public P getPageGiven() {
		return pageGiven;
	}

	public void setPageGiven(P pageGiven) {
		this.pageGiven = pageGiven;
	}
}
