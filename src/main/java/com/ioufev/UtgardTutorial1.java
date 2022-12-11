package com.ioufev;

import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.core.JIString;
import org.jinterop.dcom.core.JIVariant;
import org.openscada.opc.lib.common.ConnectionInformation;
import org.openscada.opc.lib.da.*;

import java.util.concurrent.Executors;

public class UtgardTutorial1 {

    public static void main(String[] args) throws Exception {
        // 连接信息
        final ConnectionInformation ci = new ConnectionInformation();
        ci.setHost("localhost");           // 本机IP
        ci.setDomain("");                  // 域，为空就行
        ci.setUser("OPCUser");             // 本机上自己建好的用户名
        ci.setPassword("123456");          // 密码

        // 使用MatrikonOPC Server的配置
//         ci.setClsid("F8582CF2-88FB-11D0-B850-00C0F0104305"); // MatrikonOPC的注册表ID，可以在“组件服务”里看到
//         final String itemId = "组 1.标记 1";    // MatrikonOPC Server上配置的项的名字按实际

        // 使用KEPServer的配置
        ci.setClsid("7BC0CC8E-482C-47CA-ABDC-0FE7F9C6E729"); // KEPServer的注册表ID，可以在“组件服务”里看到，上面有图片说明
        final String itemId = "通道 1.设备 1.标记 1";    // KEPServer上配置的项的名字，没有实际PLC，用的模拟器：simulator
        // final String itemId = "通道 1.设备 1.标记 2";
        // final String itemId = "通道 1.设备 1.标记 3";
        // final String itemId = "通道 1.设备 1.标记 4";
        // final String itemId = "通道 1.设备 1.标记 5";
        // final String itemId = "通道 1.设备 1.标记 6";
        // final String itemId = "通道 1.设备 1.标记 7";
        // final String itemId = "通道 1.设备 1.标记 8";
        // final String itemId = "通道 1.设备 1.标记 9";
        // final String itemId = "通道 1.设备 1.标记 10";
        // final String itemId = "通道 1.设备 1.标记 11";
        // final String itemId = "通道 1.设备 1.标记 12";
        // final String itemId = "通道 1.设备 1.标记 13";
        // final String itemId = "通道 1.设备 1.标记 14";
        // final String itemId = "通道 1.设备 1.标记 15";

        // 启动服务
        final Server server = new Server(ci, Executors.newSingleThreadScheduledExecutor());

        try {
            // 连接到服务
            server.connect();
            // add sync access, poll every 500 ms，启动一个同步的access用来读取地址上的值，线程池每500ms读值一次
            // 这个是用来循环读值的，只读一次值不用这样
            final AccessBase access = new SyncAccess(server, 500);
            // 这是个回调函数，就是读到值后执行这个打印，是用匿名类写的，当然也可以写到外面去
            access.addItem(itemId, new DataCallback() {
                @Override
                public void changed(Item item, ItemState state) {
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
                }
            });
            // start reading，开始读值
            access.bind();
            // wait a little bit，有个10秒延时
            Thread.sleep(10 * 1000);
            // stop reading，停止读取
            access.unbind();
        } catch (final JIException e) {
            System.out.println(String.format("%08X: %s", e.getErrorCode(), server.getErrorMessage(e.getErrorCode())));
        }
    }
}
