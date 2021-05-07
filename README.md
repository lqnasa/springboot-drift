
# springboot整合drift(基于注解实现thrift服务)、netty构建高性能服务

```text
Drift是一个易于使用的基于注释的Java库，用于创建Thrift客户端和可序列化的类型。客户端库类似于JAX-RS（HTTP Rest），而序列化库类似于
JaxB（XML）和Jackson（JSON），但适用于Thrift。

thrift: https://thrift.apache.org/ (稳定)
swift: https://github.com/facebookarchive/swift (该项目已存档，不再维护。建议您看一下airlift/drift)
nifty: https://github.com/facebookarchive/nifty (该项目已存档，不再维护。建议您看一下airlift/drift)
drift: https://github.com/airlift/drift (原facebook的swift、nifty的替代品，活跃)
drift: https://github.com/prestodb/drift (最活跃，fork airlift/drift)
netty: https://netty.io/ (稳定)
客户端调用同时支持基于注释的drift调用方式，也支持原生的 thrift 通过 service.thrift 生成的客户端调用，本例只提供第一种用例。
```

## 生成netty的ssl单向认证（https://github.com/airlift/drift/tree/master/drift-integration-tests中含ssl认证案例）
```text
netty需要keystore(jks)开启ssl认证，则需要.crt，.key证书文件。关键步骤记录如下：
1.通过jdk工具生成keystore（jks）文件
设置自签口令
keytool -genkey -keyalg RSA -alias drift -keystore keystore.jks -storepass drift0 -validity 360 -keysize 2048

2.转换为p12
输入新**库口令（至少6位）
源秘钥库口令若未设置为空，直接回车
输入自签口令（至少6位）
keytool -importkeystore -srckeystore keystore.jks -destkeystore keystore.p12 -deststoretype PKCS12

3.使用openssl命令导出.crt
openssl pkcs12 -in keystore.p12 -nokeys -out rsa.crt

4.使用openssl命令导出.key
openssl pkcs12 -in keystore.p12 -nocerts -nodes -out rsa.key

5. 将rsa.crt的公钥复制粘贴到rsa.key中
```