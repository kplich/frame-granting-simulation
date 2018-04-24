package demo;

import frames_pages.*;
import proc.*;
import proc.Process;

import java.util.*;

public class Demo {
	private Random rng = new Random();

	private int numberOfProcesses;
	private int maxProcessSize;
	private int numberOfFrames;
	private int simulationSize;

	private ArrayList<Process> equalGranting;
	private ArrayList<Process> proportionalGranting;
	private ArrayList<ProcessWithFaultRate> faultRateGranting;
	private ArrayList<ProcessWithWorkingSet> workingSetGranting;

	public Demo(int numberOfProcesses, int maxProcessSize, int numberOfFrames, int simulationSize) {
		this.numberOfProcesses = numberOfProcesses;
		this.maxProcessSize = maxProcessSize;
		this.numberOfFrames = numberOfFrames;
		this.simulationSize = simulationSize;

		equalGranting = new ArrayList<>();
		proportionalGranting = new ArrayList<>();
		faultRateGranting = new ArrayList<>();
		workingSetGranting = new ArrayList<>();

		for(int i = 0; i < numberOfProcesses; ++i) {
			//smallest possible process size is 10
			int currentProcessSize = rng.nextInt(maxProcessSize - 24) + 25;

			ArrayList<Frame> currentFrameTable = generateFrameTable(numberOfFrames);
			ArrayList<Frame> proportionalFrameTable = generateFrameTable( (5*currentProcessSize)/6 - 10);
			ArrayList<Page> currentPageTable = generatePageTable(currentProcessSize);
			LinkedList<Page> originalRequestQueue = generateRequests(simulationSize, currentPageTable);

			ArrayList<Page> pt1 = (ArrayList<Page>) CloneUtils.clonePageList(currentPageTable);
			ArrayList<Page> pt2 = (ArrayList<Page>) CloneUtils.clonePageList(currentPageTable);
			ArrayList<Page> pt3 = (ArrayList<Page>) CloneUtils.clonePageList(currentPageTable);
			ArrayList<Page> pt4 = (ArrayList<Page>) CloneUtils.clonePageList(currentPageTable);


			equalGranting.add(new Process(CloneUtils.cloneFrameList(currentFrameTable), pt1,
			  CloneUtils.cloneRequests(originalRequestQueue, pt1)));

			proportionalGranting.add(new Process(CloneUtils.cloneFrameList(proportionalFrameTable), pt2,
			  (CloneUtils.cloneRequests(originalRequestQueue, pt2))));

			faultRateGranting.add(new ProcessWithFaultRate(CloneUtils.cloneFrameList(currentFrameTable), pt3,
			  CloneUtils.cloneRequests(originalRequestQueue, pt3)));

			workingSetGranting.add(new ProcessWithWorkingSet(CloneUtils.cloneFrameList(currentFrameTable), pt4,
			  CloneUtils.cloneRequests(originalRequestQueue, pt4)));
		}
	}

	public ArrayList<Page> generatePageTable(int processSize) {
		ArrayList<Page> resultTable = new ArrayList<>();

		for (int i = 0; i < processSize; ++i) {
			resultTable.add(new Page(i, -1));
		}

		return resultTable;
	}

	public ArrayList<Frame> generateFrameTable(int numberOfFrames) {
		ArrayList<Frame> resultTable = new ArrayList<>();

		for (int i = 0; i < numberOfFrames; ++i) {
			resultTable.add(new Frame(i, null));
		}

		return resultTable;
	}

	public LinkedList<Page> generateRequests(int simulationSize, ArrayList<Page> pageTable) {
		int processSize = pageTable.size();
		LinkedList<Page> resultQueue = new LinkedList<>();
		Random rng = new Random();

		while (resultQueue.size() < simulationSize) {
			double randomChance = rng.nextDouble();
			int newPageIndex = 0;

			try {
				if (resultQueue.size() == 0 || randomChance < 0.1) {
					newPageIndex = rng.nextInt(processSize);
				}
				else if (randomChance < 0.55) {
					newPageIndex = resultQueue.peekLast().getPageNumber() + rng.nextInt((int) (0.05 * processSize) + 1);
				}
				else {
					newPageIndex = resultQueue.peekLast().getPageNumber() - rng.nextInt((int) (0.05 * processSize) + 1);
				}

				resultQueue.add(pageTable.get(newPageIndex));
			}
			catch (IndexOutOfBoundsException e) {
				//System.out.println("krwa");
			}
		}

		return resultQueue;
	}

	public void runByAlgorithm() {
		System.out.println("EQUAL GRANTING");
		runGrantingMethod(equalGranting);

		System.out.println("PROPORTIONAL GRANTING");
		runGrantingMethod(proportionalGranting);

		System.out.println("FAULT RATE GRANTING");
		runGrantingMethod(faultRateGranting);

		System.out.println("WORKING SET GRANTING");
		runGrantingMethod(workingSetGranting);
	}

	public void runByProcess() {
		for(int i = 0; i < numberOfProcesses; ++i) {
			runSingleProcess(i);
		}
	}

	private void runGrantingMethod(ArrayList<? extends Process> processes) {
		int sum = 0;
		for (Process p: processes) {
			for (int i = 0; i < simulationSize; ++i) {
				p.dealWithRequest();
			}
			System.out.println("process size: " + p.getProcessSize() + "\tpage faults: " + p.getPageFaults());
			sum += p.getPageFaults();
		}
		System.out.println("Page faults: " + sum + "\n\n");
	}

	private void runSingleProcess(int processIndex) {
		Process equalProcess = equalGranting.get(processIndex);
		Process proportionalProcess = proportionalGranting.get(processIndex);
		ProcessWithFaultRate faultRateProcess = faultRateGranting.get(processIndex);
		ProcessWithWorkingSet workingSetProcess = workingSetGranting.get(processIndex);

		int processSize = equalProcess.getProcessSize();

		for(int i = 0; i < simulationSize; ++i) {
			equalProcess.dealWithRequest();
			proportionalProcess.dealWithRequest();
			faultRateProcess.dealWithRequest();
			workingSetProcess.dealWithRequest();
		}

		System.out.println("process size: " + processSize + "\t" + equalProcess.getPageFaults() + "\t" + proportionalProcess.getPageFaults() + "\t" +
						   faultRateProcess.getPageFaults() + "\t" + workingSetProcess.getPageFaults());
	}
}
