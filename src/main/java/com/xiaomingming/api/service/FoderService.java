package com.xiaomingming.api.service;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.xiaomingming.api.utils.ProjectUtil;
import com.xiaomingming.api.vo.FoForder;
import com.xiaomingming.api.vo.InInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/2.
 */
public class FoderService {

    private InterfaceService interfaceService = new InterfaceService();

    private static final FoForder dao = new FoForder().dao();

    private static final String sql_total_search = "select t1.id as in_id, t1.in_name as in_name, t2.fo_name as fo_name, t2.id as fo_id from in_interface as t1 inner join fo_forder as t2 on t1.fo_id = t2.id inner join pr_project as t3 on t2.pr_id = t3.id where ( t1.in_url like ? or t1.in_name like ? or t2.fo_name like ? ) and t3.id = ?";

    public List<FoForder> getAllFoder(Integer pr_id) {
        return dao.find("select * from fo_forder where pr_id = ?", pr_id);
    }

    public boolean delFoder(String foId) {
        FoForder forder = dao.findById(foId);
        if (forder == null) {
            throw new RuntimeException("编号不存在");
        }
        if (interfaceService.getAllInterfaces(foId).size() > 0) {
            throw new RuntimeException("防止误删,请先删除此文件夹内的文件");
        }
        return forder.delete();
    }

    public FoForder addFoder(int pr_id, String fo_name) {
        FoForder forder = new FoForder();
        forder.setFoCreateTime(ProjectUtil.getDBDate());
        forder.setFoName(fo_name);
        forder.setPrId(pr_id);
        forder.save();
        return forder;
    }

    public FoForder editFoder(int fo_id, String foName) {
        FoForder forder = dao.findById(fo_id);
        if (forder == null) {
            throw new RuntimeException("编号不存在");
        }
        forder.setFoName(foName);
        forder.update();
        return forder;
    }

    public List<Object> queryTotal(String pr_id) {
        ArrayList<Object> resultList = new ArrayList<>();
        for (FoForder forder : getAllFoder(Integer.parseInt(pr_id))) {
            HashMap<Object, Object> itemMap = new HashMap<>();
            itemMap.put("name", forder.getFoName());
            itemMap.put("id", forder.getId());
            itemMap.put("isOpen", false);
            List<Object> ins = new ArrayList<>();
            for (InInterface item : interfaceService.getAllInterfaces(forder.getId())) {
                HashMap<Object, Object> inMap = new HashMap<>();
                inMap.put("name", item.getInName());
                inMap.put("id", item.getId());
                inMap.put("isshow", false);
                ins.add(inMap);
            }
            itemMap.put("ins", ins);
            resultList.add(itemMap);
        }
        return resultList;
    }

    public List<Object> searchTotal(String pr_id, String search) {
        search = ProjectUtil.formatLike(search);
        Map<Integer, FoForder> forders = new HashMap<>();
        List<Record> records = Db.find(sql_total_search, search, search, search, pr_id);
        for (Record item : records) {
            FoForder forder = forders.get(item.getInt("fo_id"));
            if (forder == null) {
                forder = new FoForder();
                forder.setId(item.getInt("fo_id"));
                forder.setFoName(item.getStr("fo_name"));
                forders.put(item.getInt("fo_id"), forder);
            }
        }
        ArrayList<Object> resultList = new ArrayList<>();
        for (Map.Entry<Integer, FoForder> forder : forders.entrySet()) {
            HashMap<Object, Object> itemMap = new HashMap<>();
            itemMap.put("name", forder.getValue().getFoName());
            itemMap.put("id", forder.getValue().getId());
            itemMap.put("isOpen", false);
            List<Object> ins = new ArrayList<>();
            for (Record item : records) {
                if (forder.getKey().equals(item.getInt("fo_id"))) {
                    HashMap<Object, Object> inMap = new HashMap<>();
                    inMap.put("name", item.getStr("in_name"));
                    inMap.put("id", item.getInt("in_id"));
                    inMap.put("isshow", false);
                    ins.add(inMap);
                }
            }
            itemMap.put("ins", ins);
            resultList.add(itemMap);
        }
        return resultList;
    }

}
