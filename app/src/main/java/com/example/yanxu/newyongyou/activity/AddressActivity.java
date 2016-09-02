package com.example.yanxu.newyongyou.activity;


import com.example.yanxu.newyongyou.model.CityModel;
import com.example.yanxu.newyongyou.model.DistrictModel;
import com.example.yanxu.newyongyou.model.ProvinceModel;
import com.example.yanxu.newyongyou.utils.XmlParserHandler;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 *省市联动的的辅助Activty
 * @author yanxu
 *
 */
public class AddressActivity extends BaseActivity {

    /**
     * 所有省
     */
    protected String[] mProvinceDatas;
    protected String[] mProvinceDatasId;
    /**
     * key - 省 value - 市
     */
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    /**
     * key - 市 values - 区
     */
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

    /**
     * key - 区 values - 邮编
     */
    protected Map<String, String> mDistrictMap = new HashMap<String, String>();
    /**
     * key - 省 values - 邮编
     */
    protected Map<String, String> mProviceMap = new HashMap<String, String>();
    /**
     * key - 市 values - 邮编
     */
    protected Map<String, String> mCityMap = new HashMap<String, String>();

    /**
     * 当前省的名称
     */
    protected String mCurrentProviceName;
    protected String mCurrentProviceId;
    /**
     * 当前市的名称
     */
    protected String mCurrentCityName;
    protected String mCurrentCityId;
    /**
     * 当前区的名称
     */
    protected String mCurrentDistrictName = "";
    protected String mCurrentDistrictId;

    /**
     * 当前区的邮政编码
     */
    protected String mDistrictCode = "";
    /**
     * 当前市的邮政编码
     */
    protected String mCityCode = "";
    /**
     * 当前省的邮政编码
     */
    protected String mProviceCode = "";

    /**
     * 解析省市区的XML数据
     */

    protected void initProvinceDatas() {
        List<ProvinceModel> provinceList = null;
        try {
            InputStream input = AddressActivity.this.getClass().getClassLoader().getResourceAsStream("assets/"+"add.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            // */ 初始化默认选中的省、市、区
            if (provinceList != null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                mCurrentProviceId = provinceList.get(0).getProvId();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    mCurrentCityId = cityList.get(0).getCityId();
                    List<DistrictModel> districtList = cityList.get(0)
                            .getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentDistrictId = districtList.get(0).getAreaId();
//					mDistrictCode = districtList.get(0).getZipcode();
                }
            }
            // */
            mProvinceDatas = new String[provinceList.size()];
            mProvinceDatasId = new String[provinceList.size()];
            for (int i = 0; i < provinceList.size(); i++) {
                // 遍历所有省的数据
                mProvinceDatas[i] = provinceList.get(i).getName();
                mProvinceDatasId[i] = provinceList.get(i).getProvId();
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                String[] cityNamesId = new String[cityList.size()];
                for (int j = 0; j < cityList.size(); j++) {
                    // 遍历省下面的所有市的数据
                    cityNames[j] = cityList.get(j).getName();
                    cityNamesId[j] = cityList.get(j).getCityId();
                    List<DistrictModel> districtList = cityList.get(j)
                            .getDistrictList();
                    String[] distrinctNameArray = new String[districtList
                            .size()];
                    String[] distrinctIdArray = new String[districtList.size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList
                            .size()];
                    for (int k = 0; k < districtList.size(); k++) {
                        // 遍历市下面所有区/县的数据
                        DistrictModel districtModel = new DistrictModel(
                                districtList.get(k).getName(), districtList
                                .get(k).getAreaId(), districtList
                                .get(k).getZipcode());
                        // 区/县对于的邮编，保存到mZipcodeDatasMap
                        //区/县 name = key
                        mDistrictMap.put(districtList.get(k).getName(),
                                districtList.get(k).getAreaId());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                        distrinctIdArray[k] = districtModel.getAreaId();
                    }
                    // 市-区/县的数据，保存到mDistrictDatasMap
                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                    mCityMap.put(cityList.get(j).getName(),
                            cityList.get(j).getCityId());

                }
                // 省-市的数据，保存到mCitisDatasMap
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
                mProviceMap.put(provinceList.get(i).getName(),
                        provinceList.get(i).getProvId());
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
    }

}