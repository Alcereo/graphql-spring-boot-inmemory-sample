package com.github.alcereo.gqlsample.types;

import org.jetbrains.annotations.NotNull;

public interface Timestamped extends Comparable<Timestamped> {

    String getTimestamp();

    @Override
    default int compareTo(@NotNull Timestamped o) {
        return o.getTimestamp().compareTo(getTimestamp());
    }
}
