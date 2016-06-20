package br.com.guerethes.sync.utils;

import java.util.List;

public class SyncUtils {
	
	public static void sincronizar() {
		try {
			QueueTaskService.add(new QueueSyncTask());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void syncAll(final List<?> array, @SuppressWarnings("rawtypes") final Class classe) {
		try {
			QueueSyncAllTask queAllTask = new QueueSyncAllTask(array, classe);
			QueueTaskService.add(queAllTask);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}