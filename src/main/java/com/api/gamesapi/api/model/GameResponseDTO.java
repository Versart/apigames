package com.api.gamesapi.api.model;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameResponseDTO {

    private Long id;

    private String name;


    private String companyName;


}
