package br.com.guerethes.sync.utils;

import java.util.List;

import android.content.Context;
import br.com.guerethes.orm.Factory.OffDroidManager;
import br.com.guerethes.orm.engine.CriteryaSQLite;
import br.com.guerethes.orm.engine.i.PersistDB;
import br.com.guerethes.orm.model.Sincronizacao;
import br.com.guerethes.synchronization.networkUtils.NetWorkUtils;
import br.com.guerethes.synchronization.webservice.JSONProcessor;
import br.com.guerethes.synchronization.webservice.WebService;

public class QueueSyncTask implements AsyncQueueOffDroid {

	@Override
	public void processarTask(Context context) {
		if ( OffDroidManager.service != null ) {
			WebService service = OffDroidManager.service;
			try {
				if ( NetWorkUtils.context != null ) {
					@SuppressWarnings("unchecked")
					List<Sincronizacao>  bdLocal = (List<Sincronizacao>) CriteryaSQLite.from(Sincronizacao.class).toList();
					if ( bdLocal != null && NetWorkUtils.isOnline(context) ) {
						for (Sincronizacao sync : bdLocal) {
							PersistDB entity = null;
							Class<?> clazz = Class.forName(sync.getClasse());
							PersistDB entitySinc = (PersistDB) JSONProcessor.toObject(sync.getJson(), clazz);

							/** Chamo o serviço para remover e depois realizo a remoção local */
							if ( sync.getOperacao() == OperacaoSync.DELETE ) {
								service.delete(entitySinc, sync.getIdClasse());
								OffDroidManager.deleteLocal(entitySinc);
							} else {
								/** Chamo o serviço anteriormente realizado e depois realizo a remoção local e posteriormente a inserção */
								if ( sync.getOperacao() == OperacaoSync.SAVE )
									entity = service.post(entitySinc);	
								if ( sync.getOperacao() == OperacaoSync.UPDATE )
									entity = service.put(entitySinc);

								OffDroidManager.deleteLocal(entitySinc);
								OffDroidManager.insertLocal(entity);
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}