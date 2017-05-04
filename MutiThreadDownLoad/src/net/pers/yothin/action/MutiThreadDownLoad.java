package net.pers.yothin.action;

import java.util.concurrent.CountDownLatch;

public class MutiThreadDownLoad {

	/**
     * 同时下载的线程数
     */
    private int threadCount;
    /**
     * 服务器请求路径
     */
    private String serverPath;
    /**
     * 本地路径
     */
    private String localPath;
    /**
     * 线程计数同步辅助
     */
    private CountDownLatch latch;
}
