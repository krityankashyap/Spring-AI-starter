package com.example.SpringAI_Starter.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDto {
 private String title;
 private String genre;
 private String year;
 private String ratings;
 private String description;
 List<String> actors;
}
