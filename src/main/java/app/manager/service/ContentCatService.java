package app.manager.service;

import app.mapper.ContentCatMapper;
import app.model.TaoTaoResult;
import app.model.TreeNodeResult;
import app.pojo.ContentCat;
import app.pojo.ItemCat;
import com.sun.xml.internal.ws.server.ServerRtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xdcao on 2017/5/30.
 */
@Service
public class ContentCatService {

    @Autowired
    private ContentCatMapper contentCatMapper;

    public List<TreeNodeResult> getContentCatTree(long parent_id){
        List<ContentCat> contentCats=contentCatMapper.getContentCatByParentId(parent_id);
        List<TreeNodeResult> treeNodeResults=new ArrayList<TreeNodeResult>();
        for (ContentCat contentCat:contentCats){
            TreeNodeResult treeNodeResult=new TreeNodeResult();
            treeNodeResult.setId(contentCat.getId());
            treeNodeResult.setText(contentCat.getName());
            treeNodeResult.setState(contentCat.isIs_parent()?"closed":"open");
            treeNodeResults.add(treeNodeResult);
        }
        return treeNodeResults;
    }

    @Transactional
    public TaoTaoResult createNewContentCat(long parentId,String name){
        ContentCat contentCat=new ContentCat();
        contentCat.setParent_id(parentId);
        contentCat.setName(name);
        contentCat.setStatus(1);
        contentCat.setSort_order(1);
        Date date=new Date();
        contentCat.setCreated(date);
        contentCat.setUpdated(date);
        contentCatMapper.insert(contentCat);
        ContentCat parentNode=contentCatMapper.getOne(parentId);
        if (!parentNode.isIs_parent()){
            parentNode.setIs_parent(true);
            contentCatMapper.update(parentNode);
        }
        TaoTaoResult taoTaoResult=new TaoTaoResult(200,"ok",contentCat.getId());
        return taoTaoResult;
    }


}