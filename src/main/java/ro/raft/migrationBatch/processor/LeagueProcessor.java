package ro.raft.migrationBatch.processor;

import org.springframework.batch.item.ItemProcessor;

import ro.astl.services.rfldbapi.league.dto.LeagueIn;

public class LeagueProcessor implements ItemProcessor<LeagueIn, LeagueIn> {

	@Override
	public LeagueIn process(LeagueIn league) throws Exception {
		
		System.out.println("League read: " + league);
		return league;
	}

}