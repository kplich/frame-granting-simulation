public class LRU extends Simulator<LRUPage> {
	public LRU(int numberOfPages, int numberOfFrames, int simulationSize, double threshold) {
		super(numberOfPages, numberOfFrames, simulationSize, threshold);

		for (int i = 0; i < numberOfPages; ++i) {
			pageTable.add(new LRUPage(i, -1));
		}
	}

	@Override
	public void prepare() {
		markTimeSinceLastRef();
		sortPagesByIndex();
		sortFramesByIndex();
	}

	@Override
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

	@Override
	public void allocatePage(LRUPage requestedPage) {
		//utworz nowe polaczenie!
		frameTable.get(0).setPageGiven(requestedPage);
		requestedPage.setFrameGiven(frameTable.get(0).getFrameIndex());
		requestedPage.setTimeSinceLastReference(0);
		++framesUsed;
	}

	@Override
	public void whenPageWasLoaded(LRUPage requestedPage) {
		requestedPage.setTimeSinceLastReference(0); //gdy ramka zostala uzyta, wyzeruj jej czas od ostatniej referencji
	}

	private void markTimeSinceLastRef() {
		for (Frame<LRUPage> frame: frameTable) {
			if (frame.getPageGiven() != null) {
				frame.getPageGiven().countTimeSinceLastReference();
			}
		}
	}

	//na poczatku listy niech znajda sie ramki ktore zostaly najdawniej uzyte
	private void sortFramesByTimeSinceReference() {
		frameTable.sort((o1, o2) -> {
			int timeOfPage1 = o1.getPageGiven().getTimeSinceLastReference();
			int timeOfPage2 = o2.getPageGiven().getTimeSinceLastReference();

			return -Integer.compare(timeOfPage1, timeOfPage2);
		});
	}
}
