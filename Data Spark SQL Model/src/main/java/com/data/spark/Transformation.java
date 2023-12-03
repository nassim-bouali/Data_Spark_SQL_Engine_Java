package com.data.spark;

import lombok.NoArgsConstructor;
import lombok.Value;

@NoArgsConstructor(force = true)
@Value
public class Transformation {
    private String id;
    private String sql;
}
