package ro.raft.migrationBatch.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import ro.astl.services.rfldbapi.country.dto.RegionIn;
import ro.astl.services.rfldbapi.country.manager.RegionManager;

public class RegionWriter implements ItemWriter<RegionIn> {

	@Autowired
	private RegionManager regionManager;
	
	@Override
	public void write(List<? extends RegionIn> items) throws Exception {
		
		for(RegionIn region : items) {
			regionManager.createRegion(region);
		}
	}

}
