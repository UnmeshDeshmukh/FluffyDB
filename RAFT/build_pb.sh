#!/bin/bash
#
# build the protobuf classes from the .proto. Note tested with 
# protobuf 2.4.1. Current version is 2.5.0.
#
# Building: 
# 
# Running this script is only needed when the protobuf structures 
# have change.
#

project_base="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# which protoc that you built (if not in your path)
PROTOC_HOME=/usr/local/

if [ -d ${project_base}/src/raft/proto ]; then
  echo "removing contents of ${project_base}/src/raft/proto and ${project_base}/src/adapter/server/proto/"
  rm -r ${project_base}/src/raft/proto/*
  rm -rf ${project_base}/src/adapter/server/proto/*
else
  echo "creating directory ${project_base}/src/raft/proto and ${project_base}/src/adapter/server/proto/"
  mkdir ${project_base}/src/raft/proto
  mkdir	${project_base}/src/adapter/server/proto
fi

#Inter Cluster Adapter Protoc Files
protoc --proto_path=${project_base}/proto --java_out=${project_base}/src ${project_base}/proto/common.proto
protoc --proto_path=${project_base}/proto --java_out=${project_base}/src ${project_base}/proto/storage.proto
protoc --proto_path=${project_base}/proto --java_out=${project_base}/src ${project_base}/proto/global.proto


#RAFT Protoc files
protoc --proto_path=${project_base}/proto --java_out=${project_base}/src ${project_base}/proto/imageTransfer.proto

protoc --proto_path=${project_base}/proto --java_out=${project_base}/src ${project_base}/proto/Ping.proto

protoc --proto_path=${project_base}/proto --java_out=${project_base}/src ${project_base}/proto/AppendEntriesRPC.proto

protoc --proto_path=${project_base}/proto --java_out=${project_base}/src ${project_base}/proto/HeartBeatRPC.proto

protoc --proto_path=${project_base}/proto --java_out=${project_base}/src ${project_base}/proto/VoteRPC.proto

protoc --proto_path=${project_base}/proto --java_out=${project_base}/src ${project_base}/proto/Work.proto


#Deven's monitors protoc files
protoc --proto_path=${project_base}/proto --java_out=${project_base}/src ${project_base}/proto/monitor.proto



