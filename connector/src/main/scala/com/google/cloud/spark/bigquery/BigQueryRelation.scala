/*
 * Copyright 2018 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.cloud.spark.bigquery

import com.google.cloud.bigquery.connector.common.BigQueryUtil
import com.google.cloud.bigquery.{TableDefinition, TableId, TableInfo}
import org.apache.spark.internal.Logging
import org.apache.spark.sql._
import org.apache.spark.sql.sources._
import org.apache.spark.sql.types.StructType

/** Base BigQuery relation that uses google-cloud-bigquery to get table metadata */
private[bigquery] case class BigQueryRelation(options: SparkBigQueryConfig, table: TableInfo)
    (@transient val sqlContext: SQLContext)
    extends BaseRelation with Logging {

  val tableId: TableId = table.getTableId
  val tableName: String = BigQueryUtil.friendlyTableName(tableId)

  // Sharing this with subclasses with subtypes gets messy
  private val tableDefinition: TableDefinition = table.getDefinition[TableDefinition]

  override val schema: StructType = {
    options.getSchema.orElse(SchemaConverters.toSpark(tableDefinition.getSchema))
  }

}



