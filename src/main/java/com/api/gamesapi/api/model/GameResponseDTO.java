package com.api.gamesapi.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameResponseDTO {

    @Schema(description = "This is the Game's id", example = "1")
    private Long id;

    @Schema(description = "This is the Game's name", example = "Super Mario World")
    private String name;

    @Schema(description = "This is the Company's name", example = "Nintendo")
    private String companyName;


}
