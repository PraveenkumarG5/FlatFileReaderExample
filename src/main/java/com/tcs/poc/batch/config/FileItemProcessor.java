package com.tcs.poc.batch.config;

import com.tcs.poc.batch.domain.Employee;
import com.tcs.poc.batch.model.File1;
import org.springframework.batch.item.ItemProcessor;

public class FileItemProcessor  implements ItemProcessor<File1, Employee> {

    @Override
    public Employee process(File1 file) throws Exception {
        Employee employee = new Employee();
        employee.setFirstName(file.getFirstName());
        employee.setLastName(file.getLastName());
        return employee;
    }
}
