package com.data.spark.flow;

import com.data.spark.Storage;
import com.data.spark.Target;
import com.data.spark.Transformation;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.List;

@NoArgsConstructor(force = true)
@Value
public class BatchPlan {
    private List<Storage> inputs;
    private List<Transformation> transformations;
    private List<Target> targets;
}
