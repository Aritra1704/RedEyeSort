package com.arpaul.redeyesort.webservices;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.arpaul.redeyesort.dataobject.ImageCellDO;
import com.arpaul.redeyesort.dataobject.ParserDO;
import com.arpaul.redeyesort.parsers.JSONDataParser;

import java.util.ArrayList;

import static com.arpaul.redeyesort.common.AppConstant.REST_URL;

/**
 * Created by Aritra on 01-08-2016.
 */
public class FetchDataService extends AsyncTaskLoader {

    private Context context;
    private String param;
    private WEBSERVICE_TYPE type;
    private WEBSERVICE_CALL call;
    private ArrayList<ImageCellDO> response;
    private ParserDO objParseDO;

    public FetchDataService(Context context, WEBSERVICE_TYPE type, WEBSERVICE_CALL call){
        super(context);
        this.context = context;
        this.type = type;
        this.call = call;
    }

    @Override
    public ArrayList<ImageCellDO> loadInBackground() {

        switch (call){
            default:
                response = assignGroupLead(context);
                break;

        }
        return response;
    }

    public ArrayList<ImageCellDO> assignGroupLead(Context context){

        ArrayList<ImageCellDO> arrHackathonDO = new ArrayList<>();
        final WebServiceResponse response = new RestServiceCalls(REST_URL, null, null, type).getData();

        ParserDO parseDO = new JSONDataParser(response, context).readInstaJSONData(response.getResponseMessage());
        if(parseDO.linkHash.containsKey(ParserDO.ParseType.TYPE_INSTA_DATA)) {
            arrHackathonDO = (ArrayList<ImageCellDO>) parseDO.linkHash.get(ParserDO.ParseType.TYPE_INSTA_DATA);
        }
        return arrHackathonDO;
    }
}
