package ro.raft.migrationBatch.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import ro.astl.services.rfldbapi.league.dto.LeagueIn;
import ro.astl.services.rfldbapi.league.manager.LeagueManager;

public class LeagueWriter implements ItemWriter<LeagueIn> {

	@Autowired
	private LeagueManager leagueManager;
	
	@Override
	public void write(List<? extends LeagueIn> items) throws Exception {

		for(LeagueIn league : items) {
			leagueManager.createLeague(league);
		}
		
	}

}
