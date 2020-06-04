package com.yhj.study.concurrency.rpc.provider.v2;

import com.yhj.study.concurrency.rpc.provider.ProcessorHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @date: 2019/06/06 13:05
 */
@Component
public class GpRpcServer implements ApplicationContextAware,InitializingBean {

    Map<String, Object> handlerMap = new HashMap<>();

    ExecutorService executorService = Executors.newCachedThreadPool();

    private int port;

    public GpRpcServer(int port) {
        this.port = port;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            while (true) {//不断接受请求
                Socket socket = serverSocket.accept();
                //每一个socket交给一个processorHandler来处理
                executorService.execute(new ProcessorHandler(socket, handlerMap));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(null != serverSocket){
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(RpcService.class);
        if(!serviceBeanMap.isEmpty()){
            for(Object serviceBean : serviceBeanMap.values()){
                //拿到注解
                RpcService rpcService = serviceBean.getClass().getAnnotation(RpcService.class);
                String serviceName = rpcService.value().getName();
                String version = rpcService.version();//拿到版本号
                if(!StringUtils.isEmpty(version)){
                    serviceName += "-" +version;
                }
                handlerMap.put(serviceName, serviceBean);
            }
        }
    }

}
