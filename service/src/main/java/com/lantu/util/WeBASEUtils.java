package com.lantu.util;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WeBASEUtils {

    private static String contractAddress = "0xc39f2ebb56852ab70fa7b01536d561a687713898";

    private static String ABI = "[{\"inputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"address\",\"name\":\"serviceProviderAddress\",\"type\":\"address\"},{\"indexed\":false,\"internalType\":\"address\",\"name\":\"user\",\"type\":\"address\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"rating\",\"type\":\"uint256\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"comment\",\"type\":\"string\"}],\"name\":\"EvaluationSubmitted\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"address\",\"name\":\"serviceProviderAddress\",\"type\":\"address\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"name\",\"type\":\"string\"}],\"name\":\"ServiceProviderAdded\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"address\",\"name\":\"serviceProviderAddress\",\"type\":\"address\"},{\"indexed\":false,\"internalType\":\"bool\",\"name\":\"trusted\",\"type\":\"bool\"}],\"name\":\"ServiceProviderUpdated\",\"type\":\"event\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"serviceProviderAddress\",\"type\":\"address\"},{\"internalType\":\"string\",\"name\":\"name\",\"type\":\"string\"}],\"name\":\"addServiceProvider\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"string\",\"name\":\"serviceProviderName\",\"type\":\"string\"}],\"name\":\"getEvaluationInformation\",\"outputs\":[{\"components\":[{\"internalType\":\"uint256\",\"name\":\"rating\",\"type\":\"uint256\"},{\"internalType\":\"string\",\"name\":\"comment\",\"type\":\"string\"}],\"internalType\":\"struct ServiceProviderContract.Evaluation[]\",\"name\":\"\",\"type\":\"tuple[]\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"string\",\"name\":\"serviceProviderName\",\"type\":\"string\"}],\"name\":\"getServiceInformation\",\"outputs\":[{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"},{\"internalType\":\"bool\",\"name\":\"\",\"type\":\"bool\"},{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"},{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"},{\"internalType\":\"address\",\"name\":\"\",\"type\":\"address\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"owner\",\"outputs\":[{\"internalType\":\"address\",\"name\":\"\",\"type\":\"address\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"\",\"type\":\"address\"}],\"name\":\"serviceProviders\",\"outputs\":[{\"internalType\":\"bool\",\"name\":\"exists\",\"type\":\"bool\"},{\"internalType\":\"address\",\"name\":\"serviceProviderAddress\",\"type\":\"address\"},{\"internalType\":\"string\",\"name\":\"name\",\"type\":\"string\"},{\"internalType\":\"bool\",\"name\":\"trusted\",\"type\":\"bool\"},{\"internalType\":\"uint256\",\"name\":\"totalRating\",\"type\":\"uint256\"},{\"internalType\":\"uint256\",\"name\":\"totalRatingsCount\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"string\",\"name\":\"serviceProviderName\",\"type\":\"string\"},{\"internalType\":\"bool\",\"name\":\"trustStatus\",\"type\":\"bool\"}],\"name\":\"setServiceProviderTrustStatus\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"string\",\"name\":\"serviceProviderName\",\"type\":\"string\"},{\"internalType\":\"uint256\",\"name\":\"rating\",\"type\":\"uint256\"},{\"internalType\":\"string\",\"name\":\"comment\",\"type\":\"string\"}],\"name\":\"submitEvaluation\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]";

    public static String commonReq(String userAddress,
                          String funcName,
                          List funcParam){
        JSONObject data = new JSONObject();
        JSONArray abiJSON = JSON.parseArray(ABI);
        data.put("user",userAddress);
        data.put("contractName","buoy");
        data.put("contractAddress",contractAddress);
        data.put("funcName",funcName);
        data.put("contractAbi",abiJSON);
        data.put("groupId",1);
        data.put("funcParam",funcParam);
        data.put("useCns",false);
        data.put("useCns", false);
        data.put("cnsName", "");
        String dataString = data.toString();

        String response = HttpRequest.post("http://47.100.76.220:5002/WeBASE-Front/trans/handle")
                .header(Header.CONTENT_TYPE,"application/json")
                .body(dataString)
                .execute()
                .body();
        return response;
    }

    public static String register(String username){
        String response = HttpRequest.get("http://47.100.76.220:5002/WeBASE-Front/privateKey?type=0&userName="+username)
                .header(Header.CONTENT_TYPE,"application/json")
                .execute()
                .body();
        return response;
    }
}
