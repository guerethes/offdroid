package br.com.guerethes.sync.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import br.com.guerethes.orm.Factory.OffDroidManager;
import br.com.guerethes.orm.engine.CriteryaSQLite;

public class QueueSyncAllTask implements AsyncQueueOffDroid {

	private List<?> array;
	private Class<?> classe;
	
	public QueueSyncAllTask(List<?> array, Class<?> classe) {
		this.array = array;
		this.classe = classe;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void processarTask(Context context) {
		try {
			ArrayList<?> all = new ArrayList(array);
			if ( all != null && !all.isEmpty() ) {
				ArrayList<?> arrayBD;
				arrayBD = (ArrayList<?>) OffDroidManager.find(CriteryaSQLite.from(classe, true), false);
			if ( arrayBD != null && !arrayBD.isEmpty() ) {
				all.removeAll(arrayBD);
				if ( !all.isEmpty() )
					OffDroidManager.insertAllLocal(all);	
			} else
				OffDroidManager.insertAllLocal(all);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}