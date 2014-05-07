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
package com.facebook.presto.execution;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import io.airlift.json.JsonCodec;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class TestInput
{
    private static final JsonCodec<Input> codec = JsonCodec.jsonCodec(Input.class);

    @Test
    public void testRoundTrip()
            throws Exception
    {
        Input expected = new Input("connectorId", "schema", "table", ImmutableList.of(
                new Column("column1", "string", Optional.<SimpleDomain>absent()),
                new Column("column2", "string", Optional.<SimpleDomain>absent()),
                new Column("column3", "string", Optional.<SimpleDomain>absent())));

        String json = codec.toJson(expected);
        Input actual = codec.fromJson(json);

        assertEquals(actual, expected);
    }
}
