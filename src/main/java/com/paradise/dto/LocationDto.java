package com.paradise.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LocationDto{

                @NotBlank
                private String name;

                @NotBlank
                private String address;

                @Min(5)
                private Integer capacity;

                private String description;
}
