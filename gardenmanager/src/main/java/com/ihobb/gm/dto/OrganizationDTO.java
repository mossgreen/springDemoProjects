package com.ihobb.gm.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrganizationDTO implements Serializable {
    private String orgName;
    private String orgCode;
}
