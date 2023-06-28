package com.tcs.poc.batch.jobs;

import com.tcs.poc.batch.model.File1;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.util.Assert;

public class CustomLineMapper extends DefaultLineMapper<File1> {

    public LineTokenizer tokenizer;
    public FieldSetMapper<File1> fieldSetMapper;

    private MultiResourceItemReader delegator;

    public CustomLineMapper(MultiResourceItemReader delegator) {
        this.delegator = delegator;
    }


    @Override
    public File1 mapLine(String line, int lineNumber) throws Exception {
        System.out.println("File Name:"+ delegator.getCurrentResource().getFilename());

        System.out.println("======" + line);

        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setDelimiter(DelimitedLineTokenizer.DELIMITER_TAB);
        delimitedLineTokenizer.setNames(new String[] { "id", "firstName", "lastName" });
        setLineTokenizer(delimitedLineTokenizer);
        setFieldSetMapper(new BeanWrapperFieldSetMapper<File1>() {
            {
                setTargetType(File1.class);
            }
        });
        return fieldSetMapper.mapFieldSet(tokenizer.tokenize(line));
    }

    public void setLineTokenizer(LineTokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    public void setFieldSetMapper(FieldSetMapper<File1> fieldSetMapper) {
        this.fieldSetMapper = fieldSetMapper;
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(tokenizer, "The LineTokenizer must be set");
        Assert.notNull(fieldSetMapper, "The FieldSetMapper must be set");
    }
}
