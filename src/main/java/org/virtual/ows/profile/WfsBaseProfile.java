package org.virtual.ows.profile;

import static org.virtual.ows.common.Utils.*;

import javax.xml.namespace.QName;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.virtual.ows.WfsClient;
import org.virtualrepository.Properties;

@Slf4j
@RequiredArgsConstructor
abstract class WfsBaseProfile implements WfsProfile {

	@NonNull
	final protected WfsClient client;
	
	@Getter
	private Properties properties = new Properties();
	
	@Override
	public void refresh() {
			
		QName name = client.service().name();
		
		log.info("refreshing profile for {}", name);
		
		try {
			
			//delegate for versions-specific behaviour
			$refresh();
			
			//we do this once for now
			if (properties.isEmpty()) {
				
				//delegate for versions-specific behaviour
				$update(properties);
			
				log.info("acquired properties for {} ({})",name,properties);
				
			}
		}
		catch(RuntimeException e) {
			
			rethrowUnchecked("cannot refresh profile for "+name,e);
		}
		
	}
	
	
	//delegate to subclasses dealing with version-specific request
	protected abstract void $refresh();
	protected abstract void $update(Properties properties);
	

}
