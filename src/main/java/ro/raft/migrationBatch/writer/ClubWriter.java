package ro.raft.migrationBatch.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import ro.astl.services.rfldbapi.club.dto.ClubIn;
import ro.astl.services.rfldbapi.club.manager.ClubManager;

public class ClubWriter implements ItemWriter<ClubIn> {

	@Autowired
	private ClubManager clubManager;
	
	@Override
	public void write(List<? extends ClubIn> items) throws Exception {
		
		for(ClubIn club : items) {
			clubManager.createClub(club);
		}

	}

}
