package demo;

import frames_pages.*;

import java.util.*;

public class CloneUtils {
	public static List<Page> clonePageList(List<Page> list) {
		List<Page> result;
		if (list instanceof LinkedList) {
			result = new LinkedList<>();
		}
		else if (list instanceof ArrayList) {
			result = new ArrayList<>();
		}
		else {
			return null;
		}

		for (Page p: list) {
			result.add(new Page(p));
		}

		return result;
	}

	public static ArrayList<Frame> cloneFrameList(List<Frame> list) {
		ArrayList<Frame> result = new ArrayList<>();

		for (Frame f: list) {
			result.add(new Frame(f));
		}

		return result;
	}

	public static LinkedList<Page> cloneRequests(LinkedList<Page> originalQueue, ArrayList<Page> newPageTable) {
		LinkedList<Page> resultQueue = new LinkedList<>();

		for(Page page: originalQueue) {
			int index = page.getPageNumber();
			resultQueue.add(newPageTable.get(index));
		}

		return resultQueue;
	}
}
