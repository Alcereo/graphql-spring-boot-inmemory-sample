package com.github.alcereo.gqlsample.types;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Issue implements Timestamped{
    String uid;
    Integer deviceId;
    String timestamp;
    String status;
}
