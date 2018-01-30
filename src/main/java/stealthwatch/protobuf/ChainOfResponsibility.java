//--------------------------------------------------------------------------
// Copyright (C) 2017-2018 Cisco and/or its affiliates. All rights reserved.
//
// This source code is distributed under the terms of the MIT license.
//--------------------------------------------------------------------------

package stealthwatch.protobuf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

class ChainOfResponsibility<Input, Output> implements Function<Input, Optional<Output>>, Predicate<Input> {

    private final Collection<PredicateFunction<Input, Output>> predicateFunctions = new ArrayList<>();

    void add(Predicate<Input> predicate, Function<Input, Output> function) {
        predicateFunctions.add(new PredicateFunction<>(predicate, function));
    }

    @Override
    public Optional<Output> apply(Input input) {
        return predicateFunctions.stream().filter(p -> p.test(input)).findFirst()
                                 .map(function -> function.apply(input));
    }

    @Override
    public boolean test(Input input) {
        return predicateFunctions.stream().anyMatch(p -> p.test(input));
    }

    private static class PredicateFunction<Input, Output> implements Predicate<Input>, Function<Input, Output> {
        private final Predicate<Input>        predicate;
        private final Function<Input, Output> function;

        PredicateFunction(Predicate<Input> predicate, Function<Input, Output> function) {
            this.predicate = predicate;
            this.function = function;
        }

        @Override
        public Output apply(Input input) {
            return function.apply(input);
        }

        @Override
        public boolean test(Input input) {
            return predicate.test(input);
        }
    }
}
