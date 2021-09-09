package org.radiusfriend.controller.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
@Getter
public class BaseResponse {
    private int statusCode = 200;
    private String message = "Ok";
}