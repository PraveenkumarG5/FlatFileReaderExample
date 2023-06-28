package com.tcs.poc.batch.model;

import lombok.Data;
import org.springframework.batch.item.ResourceAware;
import org.springframework.core.io.Resource;

@Data
public class File1 implements ResourceAware {

    String id;
    String firstName;
    String lastName;
    String date;
    private Resource resource;
}
