package com.zifang.util.zex.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

// 服务实现
public class ServiceImpl extends UnicastRemoteObject implements IService {

    /**
     */
    private static final long serialVersionUID = 682805210518738166L;

    /**
     * @throws RemoteException
     */
    protected ServiceImpl() throws RemoteException {
        super();
    }

    /* (non-Javadoc)
     *
     */
    @Override
    public String queryName(String no) throws RemoteException {
        // 方法的具体实现
        System.out.println("hello" + no);
        return String.valueOf(System.currentTimeMillis());
    }

}