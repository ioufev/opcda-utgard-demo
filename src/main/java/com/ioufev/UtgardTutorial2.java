package com.ioufev;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.core.JIArray;
import org.jinterop.dcom.core.JIVariant;
import org.openscada.opc.lib.common.ConnectionInformation;
import org.openscada.opc.lib.da.AccessBase;
import org.openscada.opc.lib.da.DataCallback;
import org.openscada.opc.lib.da.Group;
import org.openscada.opc.lib.da.Item;
import org.openscada.opc.lib.da.ItemState;
import org.openscada.opc.lib.da.Server;
import org.openscada.opc.lib.da.SyncAccess;


public class UtgardTutorial2 {

    public static void main(String[] args) throws Exception {

        // 连接信息
        final ConnectionInformation ci = new ConnectionInformation();

        ci.setHost("localhost");            // 电脑IP
        ci.setDomain("");                   // 域，为空就行
        ci.setUser("OPCUser");              // 用户名，配置DCOM时配置的
        ci.setPassword("123456");           // 密码

        // 使用MatrikonOPC Server的配置
        // ci.setClsid("F8582CF2-88FB-11D0-B850-00C0F0104305"); // MatrikonOPC的注册表ID，可以在“组件服务”里看到
        // final String itemId = "组 1.标记 1";    // 项的名字按实际

        // 使用KEPServer的配置
        ci.setClsid("7BC0CC8E-482C-47CA-ABDC-0FE7F9C6E729"); // KEPServer的注册表ID，可以在“组件服务”里看到
        final String itemId = "通道 1.设备 1.标记 1";    // 项的名字按实际，没有实际PLC，用的模拟器：simulator
        // final String itemId = "通道 1.设备 1.标记 2";

        // create a new server，启动服务
        final Server server = new Server(ci, Executors.newSingleThreadScheduledExecutor());
        try {
            // connect to server，连接到服务
            server.connect();

            // add sync access, poll every 500 ms，启动一个同步的access用来读取地址上的值，线程池每500ms读值一次
            // 这个是用来循环读值的，只读一次值不用这样
            final AccessBase access = new SyncAccess(server, 500);
            // 这是个回调函数，就是读到值后执行再执行下面的代码，是用匿名类写的，当然也可以写到外面去
            access.addItem(itemId, (item, state) -> {
                // also dump value，转存值
                try {
                    int type = state.getValue().getType(); // 类型实际是数字，用常量定义的
                    switch(type){
                        case JIVariant.VT_I2: // 整形占2个字节，即 short 类型
                            System.out.println("监控项的数据类型是：-----short 类型");
                            System.out.println("监控项的值是：-----" + state.getValue().getObjectAsShort());
                            break;
                        case JIVariant.VT_I4: // 整形占4个字节，即 int 类型，KEPServerEX 上的类型是 Long
                            System.out.println("监控项的数据类型是：-----int 类型");
                            System.out.println("监控项的值是：-----" + state.getValue().getObjectAsInt());
                            break;
                        case JIVariant.VT_I8: // 整形占8个字节，即 long 类型，KEPServerEX 上的类型是 LLong
                            System.out.println("监控项的数据类型是：-----long 类型");
                            System.out.println("监控项的值是：-----" + state.getValue().getObjectAsLong());
                            break;
                        case JIVariant.VT_R4: // 浮点数占4个字节，即 float 类型
                            System.out.println("监控项的数据类型是：-----float 类型");
                            System.out.println("监控项的值是：-----" + state.getValue().getObjectAsFloat());
                            break;
                        case JIVariant.VT_R8: // 浮点数占8个字节，即 double 类型
                            System.out.println("监控项的数据类型是：-----double 类型");
                            System.out.println("监控项的值是：-----" + state.getValue().getObjectAsDouble());
                            break;
                        case JIVariant.VT_BSTR: // 字符串
                            System.out.println("监控项的数据类型是：-----字符串 类型");
                            System.out.println("监控项的值是：-----" + state.getValue().getObjectAsString());
                            break;
                        case JIVariant.VT_UI2: // 无符号占2个字节，即 word 类型，字
                            System.out.println("监控项的数据类型是：-----无符号占2个字节的 word 类型");
                            System.out.println("监控项的值是：-----" + state.getValue().getObjectAsUnsigned().getValue());
                            break;
                        case JIVariant.VT_UI4: // 无符号占4个字节，即 Dword 类型，双字
                            System.out.println("监控项的数据类型是：-----无符号占4个字节 Dword 类型");
                            System.out.println("监控项的值是：-----" + state.getValue().getObjectAsUnsigned().getValue());
                            break;
                        case JIVariant.VT_DATE: // 日期 类型
                            System.out.println("监控项的数据类型是：-----日期 类型");
                            System.out.println("监控项的值是：-----" + state.getValue().getObjectAsDate());
                            break;
                        case JIVariant.VT_BOOL: // 布尔类型
                            System.out.println("监控项的数据类型是：-----int 类型");
                            System.out.println("监控项的值是：-----" + state.getValue().getObjectAsBoolean());
                            break;
                        case JIVariant.VT_ARRAY | JIVariant.VT_I2: // short 数组 类型
                            System.out.println("监控项的数据类型是：-----short 数组 类型");
                            System.out.println("监控项的值是：-----" + state.getValue().getObjectAsArray().toString());

                            Short[] arrayInstanceShort = (Short[]) state.getValue().getObjectAsArray().getArrayInstance();
                            for (Short value : arrayInstanceShort) {
                                System.out.println("<<<-----" + value);
                            }
                            break;
                        case JIVariant.VT_ARRAY | JIVariant.VT_R4: // float 数组 类型
                            System.out.println("监控项的数据类型是：-----float 数组 类型");
                            System.out.println("监控项的值是：-----" + state.getValue().getObjectAsArray().toString());

                            Float[] arrayInstanceFloat = (Float[]) state.getValue().getObjectAsArray().getArrayInstance();
                            for (Float value : arrayInstanceFloat) {
                                System.out.println("<<<-----" + value);
                            }
                            break;
                        case JIVariant.VT_ARRAY | JIVariant.VT_BSTR: // 字符串 数组 类型
                            System.out.println("监控项的数据类型是：-----字符串 数组 类型");
                            System.out.println("监控项的值是：-----" + state.getValue().getObjectAsArray().toString());

                            String[] arrayInstanceString = (String[]) state.getValue().getObjectAsArray().getArrayInstance();
                            for (String value : arrayInstanceString) {
                                System.out.println("<<<-----" + value);
                            }
                            break;
                        case JIVariant.VT_ARRAY | JIVariant.VT_I8: // long 数组 类型
                            System.out.println("监控项的数据类型是：-----long 数组 类型");
                            System.out.println("监控项的值是：-----" + state.getValue().getObjectAsArray().toString());

                            Long[] arrayInstanceLong = (Long[]) state.getValue().getObjectAsArray().getArrayInstance();
                            for (Long value : arrayInstanceLong) {
                                System.out.println("<<<-----" + value);
                            }
                            break;
                        case JIVariant.VT_ARRAY | JIVariant.VT_I4: // int 数组 类型
                            System.out.println("监控项的数据类型是：-----int 数组 类型");
                            System.out.println("监控项的值是：-----" + state.getValue().getObjectAsArray().toString());

                            Integer[] arrayInstanceInteger = (Integer[]) state.getValue().getObjectAsArray().getArrayInstance();
                            for (Integer value : arrayInstanceInteger) {
                                System.out.println("<<<-----" + value);
                            }
                            break;
                        default:
                            System.out.println("监控项的数据类型是：-----" + type + " 类型");
                            System.out.println("监控项的值是：-----" + state.getValue().getObject());
                            break;

                    }

                    System.out.println("监控项的时间戳是：-----" + state.getTimestamp().getTime());
                    System.out.println("监控项的详细信息是：-----" + state);
                } catch (JIException e) {
                    e.printStackTrace();
                }
            });

            // Add a new group，添加一个组，这个用来就读值或者写值一次，而不是循环读取或者写入
            // 组的名字随意，给组起名字是因为，server可以addGroup也可以removeGroup，读一次值，就先添加组，然后移除组，再读一次就再添加然后删除
            final Group group = server.addGroup("test");
            // Add a new item to the group，
            // 将一个item加入到组，item名字就是MatrikonOPC Server或者KEPServer上面建的项的名字比如：u.u.TAG1，PLC.S7-300.TAG1
            final Item item = group.addItem(itemId);

            // start reading，开始循环读值
            access.bind();

            // add a thread for writing a value every 3 seconds
            // 写入一次就是item.write(value)，循环写入就起个线程一直执行item.write(value)
            ScheduledExecutorService writeThread = Executors.newSingleThreadScheduledExecutor();
            writeThread.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    try {

                        final JIVariant value = new JIVariant("24");  // 写入24
                        System.out.println(">>> " + "写入值：  " + "24");
                        item.write(value);

//                        final JIVariant value = new JIVariant("24");  // 写入24
//                        System.out.println(">>> " + "写入值：  " + "24");
//                        int errorCode = item.write(value);
//                        System.out.println("var---" + errorCode);

                        // 写入3位Long类型的数组，，KEPServerEX 上的类型是 LLong
//                        Long[] array = {(long) 1,(long) 2,(long) 3};
//                        final JIVariant value = new JIVariant(new JIArray(array));
//                        System.out.println(">>> " + "写入值： long 数组" + "{1,2,3}");
//                        item.write(value);

                        // 写入3位 int 类型的数组，，KEPServerEX 上的类型是 Long
//                        Integer[] array = {4, 5, 6};
//                        final JIVariant value = new JIVariant(new JIArray(array));
//                        System.out.println(">>> " + "写入值： int 数组" + "{1,2,3}");
//                        item.write(value);

                        // 写入时间类型
//                        final JIVariant value = new JIVariant(new Date());  // 写入日期
//                        System.out.println(">>> " + "写入值：  " + "当前日期");
//                        item.write(value);

                    } catch (JIException e) {
                        e.printStackTrace();
                    }
                }
            }, 5, 3, TimeUnit.SECONDS); // 启动后5秒第一次执行代码，以后每3秒执行一次代码

            // wait a little bit ，延时20秒
            Thread.sleep(10 * 1000);
            writeThread.shutdownNow();  // 关掉一直写入的线程
            // stop reading，停止循环读取数值
            access.unbind();
        } catch (final JIException e) {
            System.out.println(String.format("%08X: %s", e.getErrorCode(), server.getErrorMessage(e.getErrorCode())));
        }
    }
}

