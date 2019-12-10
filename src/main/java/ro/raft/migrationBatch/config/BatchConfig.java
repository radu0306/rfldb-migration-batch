package ro.raft.migrationBatch.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import ro.astl.services.rfldbapi.country.dao.CountryDaoImpl;
import ro.astl.services.rfldbapi.country.dao.CountryDaoInterface;
import ro.astl.services.rfldbapi.country.dao.CountryRepository;
import ro.astl.services.rfldbapi.country.dto.CountryIn;
import ro.astl.services.rfldbapi.country.manager.CountryManager;
import ro.raft.migrationBatch.mapper.CountryMapper;
import ro.raft.migrationBatch.processor.MigrationBatchProcessor;
import ro.raft.migrationBatch.writer.MigrationBatchWriter;

@Configuration
@EnableTransactionManagement
@EnableBatchProcessing
@EnableJpaRepositories("ro.astl")
public class BatchConfig extends DefaultBatchConfigurer implements BatchConfigurer{

	@Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    
    @Autowired
    private EntityManagerFactory emf;
    
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
                .transactionManager(getBatchTransactionManager())
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
    public FieldSetMapper<CountryIn> getMapper(){
    	return new CountryMapper();
    }
    
    public PlatformTransactionManager getBatchTransactionManager() {
    	JpaTransactionManager transactionManager = new JpaTransactionManager();
    	transactionManager.setEntityManagerFactory(emf);
    	return transactionManager;

	}
}
