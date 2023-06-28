package com.tcs.poc.batch.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="Employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @Column(name = "first_name")
    String firstName;
    @Column(name = "last_name")
    String lastName;
    @Column(name ="date")
    String date;
}
