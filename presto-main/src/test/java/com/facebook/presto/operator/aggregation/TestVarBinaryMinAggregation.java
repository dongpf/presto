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
package com.facebook.presto.operator.aggregation;

import com.facebook.presto.spi.block.Block;
import com.facebook.presto.spi.block.BlockBuilder;
import com.facebook.presto.spi.block.BlockBuilderStatus;
import com.google.common.base.Charsets;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;
import io.airlift.slice.Slice;
import io.airlift.slice.Slices;

import static com.facebook.presto.operator.aggregation.VarBinaryMinAggregation.VAR_BINARY_MIN;
import static com.facebook.presto.spi.type.VarcharType.VARCHAR;

public class TestVarBinaryMinAggregation
        extends AbstractTestAggregationFunction
{
    @Override
    public Block getSequenceBlock(int start, int length)
    {
        BlockBuilder blockBuilder = VARCHAR.createBlockBuilder(new BlockBuilderStatus());
        for (int i = 0; i < length; i++) {
            blockBuilder.appendSlice(Slices.wrappedBuffer(Ints.toByteArray(i)));
        }
        return blockBuilder.build();
    }

    @Override
    public AggregationFunction getFunction()
    {
        return VAR_BINARY_MIN;
    }

    @Override
    public Object getExpectedValue(int start, int length)
    {
        if (length == 0) {
            return null;
        }
        Slice min = null;
        for (int i = 0; i < length; i++) {
            Slice slice = Slices.wrappedBuffer(Ints.toByteArray(i));
            min = (min == null) ? slice : Ordering.natural().min(min, slice);
        }
        return min.toString(Charsets.UTF_8);
    }
}
