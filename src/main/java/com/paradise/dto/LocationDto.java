package com.paradise.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LocationDto{

                @NotBlank
                private String name;

                @NotBlank
                private String address;

                @Min(5)
                @NotNull
                private Integer capacity;

                private String description;
}
