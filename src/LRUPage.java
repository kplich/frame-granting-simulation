public class LRUPage
  extends Page {
	int timeSinceLastReference;

	public LRUPage(int pageNumber, int frameGiven) {
		super(pageNumber, frameGiven);
		timeSinceLastReference = 0;
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
