/*
package com.tcs.poc.batch.config;

import com.tcs.poc.batch.domain.Employee;
import com.tcs.poc.batch.model.File1;
import com.tcs.poc.batch.repository.EmployeeRepo;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.util.List;


@Configuration
@EnableBatchProcessing
public class BatchConfig
{

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private EmployeeRepo employeeRepo;

   */
/* @Bean
    public Job readCSVFilesJob() {
        return jobBuilderFactory
                .get("readFile")
                .incrementer(new RunIdIncrementer())
                .start(getFirstStep())
                .build();
    }*//*


 */
/*   @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1").<File1, File1>chunk(5)
                .reader(reader())
                .writer(writer())
                .build();
    }*//*


    */
/*@Bean
    public Step getFirstStep() {
        StepBuilder stepBuilder = stepBuilderFactory.get("getFirstStep");
        SimpleStepBuilder<File1, File1> simpleStepBuilder = stepBuilder.chunk(1);
        return simpleStepBuilder
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }*//*



    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Bean
    public FlatFileItemReader<File1> reader()
    {
        //Create reader instance
        FlatFileItemReader<File1> reader = new FlatFileItemReader<File1>();

        //Set input file location
        reader.setResource(new FileSystemResource("src/main/resources/File1.txt"));

        //Set number of lines to skips. Use it if file has header rows.
        reader.setLinesToSkip(1);

        //Configure how each line will be parsed and mapped to different values
        reader.setLineMapper(new DefaultLineMapper() {
            {
                //3 columns in each row
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setDelimiter(DelimitedLineTokenizer.DELIMITER_TAB);
                        setNames(new String[] { "id", "firstName", "lastName" });
                        setIncludedFields(new int[]{0, 1, 2});
                    }
                });
                //Set values in File1 class
                setFieldSetMapper(new BeanWrapperFieldSetMapper<File1>() {
                    {
                        setTargetType(File1.class);
                    }
                });
            }
        });
        return reader;
    }

 */
/*   @Bean
    public ConsoleItemWriter<File1> writer()
    {
        return new ConsoleItemWriter<File1>();
    }*//*


*/
/*    @Bean
    public JdbcBatchItemWriter<File1> writer(@Qualifier("localdbSource") final DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<File1>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO tablename (column_name,column_name) VALUES (:filed1, :filed1)")
                .dataSource(dataSource)
                .build();
    }*//*



    @Bean
    public JdbcBatchItemWriter<File1> writer(@Qualifier("appEntityManagerFactory") EntityManagerFactory appEntityManagerFactory) {
        JdbcBatchItemWriter<File1> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("insert into employee(id,first_name,last_name) values (:id,:firstName,:lastName)");
        writer.setDataSource((DataSource) appEntityManagerFactory);
        return writer;
    }

    */
/*@Bean
    public Job writeFileDataIntoSqlDb() {
        JobBuilder jobBuilder = jobBuilderFactory.get("File1_JOB");
        jobBuilder.incrementer(new RunIdIncrementer());
        FlowJobBuilder flowJobBuilder = jobBuilder.flow(getFirstStep()).end();
        Job job = flowJobBuilder.build();
        return job;
    }*//*


    @Bean
    public FileItemProcessor processor() {
        return new FileItemProcessor();
    }





}
*/
