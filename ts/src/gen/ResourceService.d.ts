//
// Autogenerated by Thrift Compiler (0.16.0)
//
// DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
//

import Int64 = require('node-int64');
import Thrift from 'thrift';
import { SearchWordResultV2, TopicKey, GetTopicResourceChannel, TopicResourceV2 } from "./baicizhan_types";


export declare class Client {
  input: Thrift.TFramedTransport;
  output: Thrift.TProtocolConstructor;
  seqid: number;

  constructor(input: Thrift.TFramedTransport, output?: Thrift.TProtocolConstructor);

  search_word_v2(query_str: string): SearchWordResultV2[];

  search_word_v2(query_str: string, callback?: (err: Error | undefined, data: SearchWordResultV2[])=>void): void;

  get_topic_resource_v2(key: TopicKey, channel: GetTopicResourceChannel, with_zpk: boolean, with_dict: boolean, with_dict_wiki: boolean, with_media: boolean, with_similal_words: boolean): TopicResourceV2;

  get_topic_resource_v2(key: TopicKey, channel: GetTopicResourceChannel, with_zpk: boolean, with_dict: boolean, with_dict_wiki: boolean, with_media: boolean, with_similal_words: boolean, callback?: (err: Error | undefined, data: TopicResourceV2)=>void): void;
}