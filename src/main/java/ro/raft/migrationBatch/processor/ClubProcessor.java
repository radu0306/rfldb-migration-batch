package ro.raft.migrationBatch.processor;

import org.springframework.batch.item.ItemProcessor;

import ro.astl.services.rfldbapi.club.dto.ClubIn;

public class ClubProcessor implements ItemProcessor<ClubIn, ClubIn> {

	@Override
	public ClubIn process(ClubIn item) throws Exception {
		System.out.println("Club read: " + item);
		return item;
	}

}
