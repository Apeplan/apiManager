package com.xiaomingming.api.service;

import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.xiaomingming.api.vo.InInterface;
import com.xiaomingming.api.vo.PaParam;

import java.util.List;

/**
 * Created by Administrator on 2018/1/2.
 */
public class InterfaceService {

    private static final InInterface inDao = new InInterface().dao();
    private static final PaParam paDao = new PaParam().dao();

    public List<InInterface> getAllInterfaces(Object fo_id) {
        return inDao.find("select * from in_interface where fo_id = ?", fo_id);
    }

    public int delInterfacesByForder(Object fo_id) {
        return Db.delete("delete from in_interface where fo_id = ?", fo_id);
    }

    public InInterface getInterfaceByID(int in_id) {
        return inDao.findById(in_id);
    }

    public List<PaParam> getParamsByInID(int in_id) {
        return paDao.find("select * from pa_param where in_id = ?", in_id);
    }

    @Before(Tx.class)
    public InInterface createItem(String fo_id, String in_url, String in_name, String params, String responsesOk, String responsesErr) {
        InInterface inInterface = new InInterface();
        inInterface.setFoId(Integer.parseInt(fo_id));
        inInterface.setInName(in_name);
        inInterface.setInUrl(in_url);
        inInterface.setInMethod("POST");
        inInterface.setInResponseOk(responsesOk);
        inInterface.setInResponseErr(responsesErr);
        inInterface.setFoId(Integer.parseInt(fo_id));
        if (inInterface.save()) {
            List<PaParam> arrParam = JSON.parseArray(params, PaParam.class);
            if (arrParam != null) {
                for (PaParam item : arrParam) {
                    item.setInId(inInterface.getId());
                    item.save();
                }
            }
        }
        return inInterface;
    }

    @Before(Tx.class)
    public InInterface updateItem(String fo_id, String in_id, String in_url, String in_name, String params, String responsesOk, String responsesErr) {
        InInterface inInterface = inDao.findById(in_id);
        inInterface.setFoId(Integer.parseInt(fo_id));
        inInterface.setInName(in_name);
        inInterface.setInUrl(in_url);
        inInterface.setInMethod("POST");
        inInterface.setFoId(Integer.parseInt(fo_id));
        inInterface.setInResponseOk(responsesOk);
        inInterface.setInResponseErr(responsesErr);
        if (inInterface.update()) {
            List<PaParam> arrParam = JSON.parseArray(params, PaParam.class);
            if (arrParam != null) {
                List<PaParam> oldParam = getParamsByInID(Integer.parseInt(in_id));
                for (PaParam item : oldParam) {
                    item.delete();
                }
                for (PaParam item : arrParam) {
                    item.setInId(inInterface.getId());
                    item.save();
                }
            }
        }
        return inInterface;
    }

    public Boolean deleteDocByID(int in_id) {
        return inDao.deleteById(in_id);
    }
}
