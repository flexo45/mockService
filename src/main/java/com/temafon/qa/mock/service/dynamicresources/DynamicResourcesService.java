package com.temafon.qa.mock.service.dynamicresources;

import com.temafon.qa.mock.service.accounts.AccountService;
import com.temafon.qa.mock.service.accounts.UserProfile;
import com.temafon.qa.mock.service.data.DBException;
import com.temafon.qa.mock.service.data.DBManager;
import com.temafon.qa.mock.service.data.dataSet.DispatchStrategy;
import com.temafon.qa.mock.service.data.dataSet.DynamicResource;
import com.temafon.qa.mock.service.data.dataSet.Method;
import com.temafon.qa.mock.service.data.dataSet.Script;

import java.util.ArrayList;
import java.util.List;

public class DynamicResourcesService {

    private static DynamicResourcesService instance;

    public static DynamicResourcesService getInstance(){
        if(instance == null){
            instance = new DynamicResourcesService();
        }
        return instance;
    }

    private List<DynamicResourceItem> dynamicResourceItemList;



    private DynamicResourcesService(){

    }

    public List<DynamicResourceItem> getDynamicResourceItemList(){

        if(dynamicResourceItemList == null){
            try {
                List<DynamicResource> list = DBManager.getInstance().getDynamicResourceDataManager()
                        .selectAllDynamicResources();

                this.dynamicResourceItemList = new ArrayList<>();

                for(DynamicResource it : list){
                    dynamicResourceItemList.add(new DynamicResourceItem(it.getPath(), it.getMethod().getName(), it.getDispatch_strategy().getName()));
                }

                return  dynamicResourceItemList;
            }
            catch (DBException e){
                e.printStackTrace();
            }
        }
        return dynamicResourceItemList;
    }

    public void addDynamicResource(DynamicResourceItem item){
        try {
            Method method = DBManager.getInstance().getDynamicResourceDataManager().getMethod(item.getMethod());
            DispatchStrategy strategy = DBManager.getInstance().getDynamicResourceDataManager().getStrategy(item.getStrategy());

            long resourceId = DBManager.getInstance().getDynamicResourceDataManager()
                    .addDynamicResource(item.getPath(), method.getId(), strategy.getId());

            if(item.getScript() != null){
                DBManager.getInstance().getDynamicResourceDataManager().addScript(item.getScript(), resourceId);
            }
        }
        catch (DBException ex){
            ex.printStackTrace();
        }
    }

    public DynamicResourceItem getDynamicResource(String path){
        try {
            DynamicResource dynamicResource = DBManager.getInstance().getDynamicResourceDataManager()
                    .getDynamicResource(path);

            return new DynamicResourceItem(dynamicResource.getPath()
                    , dynamicResource.getMethod().getName()
                    , dynamicResource.getDispatch_strategy().getName());
        }
        catch (DBException ex){
            ex.printStackTrace();
        }
        return null;
    }
}
