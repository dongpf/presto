/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.facebook.presto.sql.planner.plan;

import com.facebook.presto.sql.planner.Symbol;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;

import javax.annotation.concurrent.Immutable;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Immutable
public class MaterializeSampleNode
        extends PlanNode
{
    private final PlanNode source;
    private final Symbol sampleWeightSymbol;

    @JsonCreator
    public MaterializeSampleNode(@JsonProperty("id") PlanNodeId id,
            @JsonProperty("source") PlanNode source,
            @JsonProperty("sampleWeightSymbol") Symbol sampleWeightSymbol)
    {
        super(id);

        this.source = checkNotNull(source, "source is null");
        this.sampleWeightSymbol = checkNotNull(sampleWeightSymbol, "sampleWeightSymbol is null");
        Preconditions.checkArgument(source.getOutputSymbols().contains(sampleWeightSymbol), "source does not output sample weight");
    }

    @Override
    public List<Symbol> getOutputSymbols()
    {
        return FluentIterable.from(source.getOutputSymbols())
                .filter(Predicates.not(Predicates.equalTo(sampleWeightSymbol)))
                .toList();
    }

    @Override
    public List<PlanNode> getSources()
    {
        return ImmutableList.of(source);
    }

    @JsonProperty
    public PlanNode getSource()
    {
        return source;
    }

    @JsonProperty
    public Symbol getSampleWeightSymbol()
    {
        return sampleWeightSymbol;
    }

    @Override
    public <C, R> R accept(PlanVisitor<C, R> visitor, C context)
    {
        return visitor.visitMaterializeSample(this, context);
    }
}
