package br.com.guerethes.sync.utils;

import java.util.LinkedList;
import java.util.Queue;

import br.com.guerethes.synchronization.networkUtils.NetWorkUtils;

public class QueueTaskService {

	private static Queue<AsyncQueueOffDroid> queue = null;
	private static Thread thread = null;

	public static void add(AsyncQueueOffDroid task) throws InterruptedException {
		if (queue == null)
			queue = new LinkedList<AsyncQueueOffDroid>();

		queue.add(task);
		
		if (thread == null || (!thread.isAlive() && !thread.isInterrupted())) {
			thread = createThread();
			thread.start();
		}
	}

	private static Thread createThread() {
		Thread thread = new Thread() {
			public void run() {
				if ( NetWorkUtils.context == null ) {
					interrupt();
				} else if ( !isInterrupted()) {
					while (!queue.isEmpty()) {
						AsyncQueueOffDroid task = queue.peek();
						task.processarTask(NetWorkUtils.context);
						queue.remove();
					}
					interrupt();
				}
			}
		};
		return thread;
	}

}