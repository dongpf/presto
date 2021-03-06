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
package com.facebook.presto.block.rle;

import com.facebook.presto.block.AbstractTestBlock;
import com.facebook.presto.spi.block.Block;
import com.facebook.presto.spi.block.BlockBuilderStatus;
import io.airlift.slice.Slices;

import static com.facebook.presto.block.BlockAssertions.createStringsBlock;
import static com.facebook.presto.spi.type.VarcharType.VARCHAR;

public class TestRunLengthEncodedBlock
        extends AbstractTestBlock
{
    @Override
    protected Block createTestBlock()
    {
        Block value = VARCHAR.createBlockBuilder(new BlockBuilderStatus())
                .appendSlice(Slices.utf8Slice("cherry"))
                .build();

        return new RunLengthEncodedBlock(value, 11);
    }

    @Override
    protected Block createExpectedValues()
    {
        return createStringsBlock(
                "cherry",
                "cherry",
                "cherry",
                "cherry",
                "cherry",
                "cherry",
                "cherry",
                "cherry",
                "cherry",
                "cherry",
                "cherry");
    }
}
