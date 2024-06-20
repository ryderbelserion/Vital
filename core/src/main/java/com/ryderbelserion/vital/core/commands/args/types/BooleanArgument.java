package com.ryderbelserion.vital.core.commands.args.types;

import com.ryderbelserion.vital.core.commands.args.ArgumentType;
import java.util.List;

public class BooleanArgument extends ArgumentType {

    @Override
    public List<String> getPossibleValues() {
        return List.of("true", "false");
    }
}