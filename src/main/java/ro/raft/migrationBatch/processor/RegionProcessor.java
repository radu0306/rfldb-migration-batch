package ro.raft.migrationBatch.processor;

import org.springframework.batch.item.ItemProcessor;

import ro.astl.services.rfldbapi.country.dto.RegionIn;

public class RegionProcessor implements ItemProcessor<RegionIn, RegionIn> {

	@Override
	public RegionIn process(RegionIn region) throws Exception {
		
		System.out.println("Region read: " + region);
		return region;
	}

}
