/**
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.wasp;

import java.util.UUID;

import com.alibaba.wasp.protobuf.generated.ClusterIdProtos;

import com.google.protobuf.InvalidProtocolBufferException;

/**
 * The identifier for this cluster. It is serialized to the filesystem and up
 * into zookeeper. This is a container for the id. Also knows how to serialize
 * and deserialize the cluster id.
 */
public class ClusterId {
  private final String id;

  /**
   * New ClusterID. Generates a uniqueid.
   */
  public ClusterId() {
    this(UUID.randomUUID().toString());
  }

  ClusterId(final String uuid) {
    this.id = uuid;
  }

  /**
   * @return The clusterid serialized using pb w/ pb magic prefix
   */
  public byte[] toByteArray() {
    return convert().toByteArray();
  }

  /**
   * @param bytes
   *          A pb serialized {@link ClusterId} instance with pb magic prefix
   * @return An instance of {@link ClusterId} made from <code>bytes</code>
   * @throws DeserializationException
   * @see {@link #toByteArray()}
   */
  public static ClusterId parseFrom(final byte[] bytes)
      throws DeserializationException {
    ClusterIdProtos.ClusterId.Builder builder = ClusterIdProtos.ClusterId
        .newBuilder();
    ClusterIdProtos.ClusterId cid = null;
    try {
      cid = builder.mergeFrom(bytes, 0, bytes.length).build();
    } catch (InvalidProtocolBufferException e) {
      throw new DeserializationException(e);
    }
    return convert(cid);
  }

  /**
   * @return A pb instance to represent this instance.
   */
  ClusterIdProtos.ClusterId convert() {
    ClusterIdProtos.ClusterId.Builder builder = ClusterIdProtos.ClusterId
        .newBuilder();
    return builder.setClusterId(this.id).build();
  }

  /**
   * @param cid
   * @return A {@link ClusterId} made from the passed in <code>cid</code>
   */
  static ClusterId convert(final ClusterIdProtos.ClusterId cid) {
    return new ClusterId(cid.getClusterId());
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return this.id;
  }

  public boolean hasId() {
    return this.id != null && !("").equals(id.trim());
  }

  public String getId() {
    return id;
  }
}
