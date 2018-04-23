package sortingTest;

import frames_pages.*;
import org.junit.jupiter.api.*;
import proc.Process;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

class ProcessTest {

	Page p1, p2, p3, p4, p5, p6, p7;
	Frame f1, f2, f3, f4;

	ArrayList<Page> pageTable;
	ArrayList<Frame> frameTable;

	ArrayList<Frame> sortedByPageUsed;
	ArrayList<Frame> sortedByTimeSinceRef;

	@BeforeEach
	void setUp() {
		p1 = new Page(1, -1);
		p1.setTimeSinceLastReference(30);
	    p2 = new Page(2, -1);
	    p3 = new Page(3, -1);
	    p4 = new Page(4, -1);
	    p4.setTimeSinceLastReference(15);
	    p5 = new Page(5, -1);
	    p6 = new Page(6, -1);
	    p7 = new Page(7, -1);
	    p7.setTimeSinceLastReference(90);

		pageTable = new ArrayList<>();
		pageTable.add(p1);
		pageTable.add(p2);
		pageTable.add(p3);
		pageTable.add(p4);
		pageTable.add(p5);
		pageTable.add(p6);
		pageTable.add(p7);

		f1 = new Frame(1, p4);
	    f2 = new Frame(2, p7);
		f3 = new Frame(3, null);
	    f4 = new Frame(4, p1);
		frameTable = new ArrayList<>();

		frameTable.add(f1);
		frameTable.add(f2);
		frameTable.add(f3);
		frameTable.add(f4);

		sortedByPageUsed = new ArrayList<>();
		sortedByPageUsed.add(f3);
		sortedByPageUsed.add(f1);
		sortedByPageUsed.add(f2);
		sortedByPageUsed.add(f4);

		sortedByTimeSinceRef = new ArrayList<>();
		sortedByTimeSinceRef.add(f2);
		sortedByTimeSinceRef.add(f4);
		sortedByTimeSinceRef.add(f1);
		sortedByTimeSinceRef.add(f3);
	}

	@Test
	void sortFramesByPageUsed() {
		Process.sortFramesByPageUsed(frameTable);
		assertThat(frameTable, is(sortedByPageUsed));
	}

	@Test
	void sortFramesByTimeSinceReference() {
		Process.sortFramesByTimeSinceReference(frameTable);
		assertThat(frameTable, is(sortedByTimeSinceRef));
	}
}