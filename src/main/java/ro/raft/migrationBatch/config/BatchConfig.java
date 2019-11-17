package ro.raft.migrationBatch.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import ro.astl.services.rfldbapi.country.dto.CountryIn;
import ro.raft.migrationBatch.mapper.CountryMapper;
import ro.raft.migrationBatch.processor.MigrationBatchProcessor;
import ro.raft.migrationBatch.writer.MigrationBatchWriter;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	@Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    
    @Autowired
    private CountryMapper countryFieldMapper;
    
    
    
    @Value("classPath:/input/countryList.csv")
    private Resource inputResource;
    
    @Bean
    public Job readCSVFileJob() {
        return jobBuilderFactory
                .get("readCountryCSVJob")
                .incrementer(new RunIdIncrementer())
                .start(step())
                .build();
    }
    
    @Bean
    public Step step() {
        return stepBuilderFactory
                .get("step")
                .<CountryIn, CountryIn>chunk(5)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public ItemProcessor<CountryIn, CountryIn> processor() {
        return new MigrationBatchProcessor();
    }

    @Bean
    public FlatFileItemReader<CountryIn> reader() {
        FlatFileItemReader<CountryIn> itemReader = new FlatFileItemReader<CountryIn>();
        itemReader.setLineMapper(lineMapper());
        itemReader.setLinesToSkip(1);
        itemReader.setResource(inputResource);
        return itemReader;
    	
    }
    
    @Bean
    public LineMapper<CountryIn> lineMapper() {
        DefaultLineMapper<CountryIn> lineMapper = new DefaultLineMapper<CountryIn>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

        lineTokenizer.setDelimiter(",");
        lineTokenizer.setNames(new String[]{"CountryName", "CountryShortName" });
        lineTokenizer.setIncludedFields(new int[]{0, 1});
        
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(countryFieldMapper);

        return lineMapper;
    }
    
    @Bean
    public ItemWriter<CountryIn> writer() {

        return new MigrationBatchWriter();
    }
    
   
    
    @Bean
    public DataSource dataSource() {

        EmbeddedDatabaseBuilder embeddedDatabaseBuilder = new EmbeddedDatabaseBuilder();

        return embeddedDatabaseBuilder.addScript("classpath:org/springframework/batch/core/schema-drop-h2.sql")
                .addScript("classpath:org/springframework/batch/core/schema-h2.sql")
                .setType(EmbeddedDatabaseType.H2)
                .build();
    }
}
