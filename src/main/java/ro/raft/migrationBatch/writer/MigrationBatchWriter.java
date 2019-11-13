package ro.raft.migrationBatch.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import ro.astl.services.rfldbapi.country.dto.CountryIn;

public class MigrationBatchWriter implements ItemWriter<CountryIn>{

	@Override
	public void write(List<? extends CountryIn> items) throws Exception {
		
		for(CountryIn countryIn : items) {
			System.out.println(countryIn.toString());
		}
		
	}

}
