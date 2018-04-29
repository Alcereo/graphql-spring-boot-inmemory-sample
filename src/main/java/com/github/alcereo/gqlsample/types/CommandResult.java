package com.github.alcereo.gqlsample.types;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommandResult implements Timestamped{
    String commandUid;
    Integer deviceId;
    String timestamp;
    String result;
}
