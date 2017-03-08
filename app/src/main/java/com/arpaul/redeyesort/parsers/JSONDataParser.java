package com.arpaul.redeyesort.parsers;

import android.content.Context;

import com.arpaul.redeyesort.dataobject.ImageCellDO;
import com.arpaul.redeyesort.dataobject.ParserDO;
import com.arpaul.redeyesort.webservices.WebServiceResponse;
import com.arpaul.utilitieslib.JSONUtils;
import com.arpaul.utilitieslib.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Aritra on 01-03-2017.
 */

public class JSONDataParser {

    private WebServiceResponse response;
    private Context context;

    public static final String TAG_DATA                     = "data";
    public static final String TAG_IMAGES                   = "images";
    public static final String TAG_RESOL                    = "standard_resolution";
    public static final String TAG_URL                      = "url";
    public static final String TAG_HEIGHT                   = "height";
    public static final String TAG_WIDTH                    = "width";


    public JSONDataParser(WebServiceResponse response, Context context) {
        this.response = response;
        this.context = context;
    }

    public ParserDO readInstaJSONData(String data) {
        ParserDO parseDO = new ParserDO();

        ArrayList<ImageCellDO> arrInstaDO = null;
        ImageCellDO objImageCellDO = null;
        try {
            JSONObject jsonMember = new JSONObject(data);
            if(JSONUtils.hasJSONtag(jsonMember, TAG_DATA)) {
                JSONArray jsonResponse = jsonMember.getJSONArray(TAG_DATA);
                if(jsonResponse != null && jsonResponse.length() > 0) {
                    arrInstaDO = new ArrayList<>();
                    for(int i= 0; i < jsonResponse.length(); i++) {
                        JSONObject body = jsonResponse.getJSONObject(i);
                        objImageCellDO = new ImageCellDO();
                        if(JSONUtils.hasJSONtag(body, TAG_IMAGES)) {
                            JSONObject joImages = body.getJSONObject(TAG_IMAGES);
                            if(JSONUtils.hasJSONtag(joImages, TAG_RESOL)) {
                                JSONObject joResol = joImages.getJSONObject(TAG_RESOL);

                                if(JSONUtils.hasJSONtag(joResol, TAG_URL))
                                    objImageCellDO.imageURL     = joResol.getString(TAG_URL);
                                if(JSONUtils.hasJSONtag(joResol, TAG_HEIGHT))
                                    objImageCellDO.imageHeight  = StringUtils.getInt(joResol.getString(TAG_HEIGHT));
                                if(JSONUtils.hasJSONtag(joResol, TAG_WIDTH))
                                    objImageCellDO.imageWidth   = StringUtils.getInt(joResol.getString(TAG_WIDTH));
                            }
                        }
                        arrInstaDO.add(objImageCellDO);
                    }
                }
            }

            if(arrInstaDO != null)
                parseDO.linkHash.put(ParserDO.ParseType.TYPE_INSTA_DATA, arrInstaDO);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            return parseDO;
        }
    }
}
