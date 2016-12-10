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
package io.confluent.ksql.planner.plan;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.confluent.ksql.metastore.DataSource;
import io.confluent.ksql.serde.KQLTopicSerDe;

import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Schema;

import javax.annotation.concurrent.Immutable;

import java.util.List;

import static java.util.Objects.requireNonNull;

@Immutable
public class SourceKafkaTopicNode
    extends SourceNode {

  private final Schema schema;
  private final String topicName;
  private final Field keyField;
  private final String alias;
  private final KQLTopicSerDe topicSerDe;

  // TODO: pass in the "assignments" and the "outputs" separately (i.e., get rid if the symbol := symbol idiom)
  @JsonCreator
  public SourceKafkaTopicNode(@JsonProperty("id") PlanNodeId id,
                              @JsonProperty("schema") Schema schema,
                              @JsonProperty("keyField") Field keyField,
                              @JsonProperty("topicName") String topicName,
                              @JsonProperty("alias") String alias,
                              @JsonProperty("dataSourceType") DataSource.DataSourceType
                                    dataSourceType,
                              @JsonProperty("topicSerDe") KQLTopicSerDe topicSerDe) {
    super(id, dataSourceType);

    this.schema = schema;
    requireNonNull(topicName, "topicName is null");

    this.topicName = topicName;
    this.keyField = keyField;
    this.alias = alias;
    this.topicSerDe = topicSerDe;
  }

  public String getTopicName() {
    return topicName;
  }

  @Override
  public Schema getSchema() {
    return schema;
  }

  @Override
  public Field getKeyField() {
    return keyField;
  }

  public String getAlias() {
    return alias;
  }

  public KQLTopicSerDe getTopicSerDe() {
    return topicSerDe;
  }

  @Override
  public List<PlanNode> getSources() {
    return null;
  }

  @Override
  public <C, R> R accept(PlanVisitor<C, R> visitor, C context) {
    return visitor.visitKafkaTopic(this, context);
  }
}