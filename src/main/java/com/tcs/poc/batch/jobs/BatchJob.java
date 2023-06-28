package com.tcs.poc.batch.jobs;

import com.tcs.poc.batch.domain.Employee;
import com.tcs.poc.batch.model.File1;
import com.tcs.poc.batch.repository.EmployeeRepo;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.util.Arrays;

@Configuration
public class BatchJob {

    @Value("${srm.source.resource.path}")
    private Resource[] inputResources;
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private EmployeeRepo employeeRepository;

    @Bean
    public Job fileProcessJob(Step importStep, Step totalCountStep) {
        return jobBuilderFactory.get("fileProcessJob")
                .incrementer(new RunIdIncrementer())
                .flow(importStep)
                .next(totalCountStep)
                .end()
                .build();
    }


    @Bean
    public Step importStep(ItemWriter<Employee> writer, PlatformTransactionManager appTransactionManager) throws Exception {
        return stepBuilderFactory.get("importStep")
                .transactionManager(appTransactionManager)
                .<File1, Employee> chunk(100)
                .reader(multiResourceItemReader())
                .processor(processor())
                .writer(writer)
                .build();
    }

    @Bean
    public MultiResourceItemReader<File1> multiResourceItemReader() throws Exception {
        MultiResourceItemReader<File1> resourceItemReader = new MultiResourceItemReader<File1>();
        System.out.println("Resource Path: "+ Arrays.stream(inputResources).findAny().get().getURI());
        resourceItemReader.setResources(inputResources);
       // resourceItemReader.setDelegate(reader());
        resourceItemReader.setDelegate(customFlatFileItemReader(resourceItemReader));
        return resourceItemReader;
    }


    private FlatFileItemReader<File1> customFlatFileItemReader(MultiResourceItemReader delegator) throws Exception {
        FlatFileItemReader<File1> reader = new FlatFileItemReader<>();
        reader.setLinesToSkip(1); // header row
        reader.setLineMapper(new CustomLineMapper(delegator));
        reader.afterPropertiesSet();
        return reader;
    }


  /*  @SuppressWarnings({ "rawtypes", "unchecked" })
    @Bean
    public FlatFileItemReader<File1> reader()
    {
        //Create reader instance
        FlatFileItemReader<File1> reader = new FlatFileItemReader<File1>();

        //Set input file location
        System.out.println("Currently processing the resource:: ");
        //reader.setResource(new FileSystemResource("src/main/resources/File1.txt"));

        //Set number of lines to skips. Use it if file has header rows.
        reader.setLinesToSkip(1);

        //Configure how each line will be parsed and mapped to different values
        reader.setLineMapper(new DefaultLineMapper() {
            {
                //3 columns in each row
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setDelimiter(DelimitedLineTokenizer.DELIMITER_TAB);
                        setNames(new String[] { "id", "firstName", "lastName", "Hi" });
                        //setIncludedFields(new int[]{0, 1, 2});
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
    } */

    @Bean
    public ItemProcessor<File1, Employee> processor() {
        return new ItemProcessor<File1, Employee>(){
            @Override
            public Employee process(File1 file) throws Exception {
                System.out.println("To Be Process:"+  file.getResource().getFilename());
                Employee employee = new Employee();
                employee.setId(Long.parseLong(file.getId()));
                employee.setFirstName(file.getFirstName());
                employee.setLastName(file.getLastName());
              //  System.out.println("Processed:"+ employee.toString());
                return employee;
            }
        };
    }

   /* @Bean
    public ItemWriter<Employee> writer(@Qualifier("appEntityManagerFactory") EntityManagerFactory appEntityManagerFactory) {
        ItemWriter<Employee> writer = new ItemWriter<Employee>() {

            @Override
            public void write(List<? extends Employee> items) throws Exception {
                for (Employee item : items) {
                    System.out.println("Writer:: "+ item.toString());
                   Employee employee = employeeRepository.save(item);
                   //System.out.println("Written successfully :"+ employee.toString());
                }
            }
        };
        return writer;
    }*/


    @Bean
    public JdbcBatchItemWriter<Employee> writer(@Qualifier("appEntityManagerFactory") EntityManagerFactory appEntityManagerFactory) {
        JdbcBatchItemWriter<Employee> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("insert into employee(id, first_name,last_name, date) values (nextval('hibernate_sequence'), :firstName,:lastName, :date)");
        writer.setDataSource(((EntityManagerFactoryInfo) appEntityManagerFactory).getDataSource());
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        return writer;
    }

    @Bean
    public Step totalCountStep(){
        return stepBuilderFactory.get("totalCountStep")
                .tasklet(new Tasklet() {

                    @Override
                    public RepeatStatus execute(StepContribution contribution,
                                                ChunkContext chunkContext) throws Exception {

                        System.out.println("Total Employee count: "+employeeRepository.count());
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }
}
