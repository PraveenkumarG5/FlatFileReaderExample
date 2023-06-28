package com.tcs.poc.batch.repository;

import com.tcs.poc.batch.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepo  extends JpaRepository<Employee, Long> {}
