package com.github.alcereo.gqlsample.types;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Event  implements Timestamped{
    private String uid;
    private Integer deviceId;
    private String timestamp;
    private String status;
}
