package com.zxw.baicizhanhelper.service;

import org.apache.thrift.TException;
import org.apache.thrift.TServiceClient;

/**
 * class description
 *
 * @author zhangxunwei
 * @date 2022/8/13
 */
@FunctionalInterface
interface ThriftClientOperator<T extends TServiceClient, R> {
    R apply(T t) throws TException;
}
