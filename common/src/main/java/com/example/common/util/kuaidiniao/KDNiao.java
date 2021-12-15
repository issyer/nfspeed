package com.example.common.util.kuaidiniao;

import cn.hutool.http.HttpUtil;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author SunWanghe
 * @date 2019/9/4 9:07
 */
public class KDNiao {

	//用户id
	private static String USERID = "1627815";

	//用户的APIkey
	private static String APIKEY = "e34ae063-b4b3-4acf-a948-21324d68ff13";

	//查询物流信息的RUL，调用限制：3000 次/天，该地址需要的参数和返回结果如下：
	/** 请求参数：
	 * ShipperCode：快递公司编码 列举几个常见的编码：顺丰：SF，百世：HTKY，中通：ZTO，申通：STO，圆通：YTO，韵达：YD，其他还是将近400多家快递编码，有份excel对照表，考虑存档进数据库
	 * LogisticCode：快递单号
	 * CustomerName ：ShipperCode 为 JD，必填，对应京东的青龙配送编码，也叫商家编码，格式：数字 ＋字母＋数字，9 位数字加一个字母，共 10 位，举例：001K123450； ShipperCode 为 SF，且快递单号非快递鸟 渠道返回时，必填，对应收件人/寄件人手机号后四位； ShipperCode 为 SF，且快递单号为快递鸟 渠道返回时，不填； ShipperCode 为其他快递时，不填
	 * OrderCode：不用填，是他们系统订单的订单号，跟我们没关系
	 */
	/**
	 * 返回结果
	 * EBusinessID：用户 ID
	 * OrderCode：订单编号
	 * ShipperCode：快递公司编码
	 * LogisticCode：快递单号
	 * Success：成功与否(true/false)
	 * Reason：失败原因
	 * State：物流状态 ： 0-无轨迹 1-已揽收 2-在途中 3-签收 4-问题件
	 * Traces.AcceptTime：轨迹发生时间 Date类型
	 * Traces.AcceptStation：轨迹描述
	 * Traces是一个json数组，
	 */
	private static String KD_QUERY_URL = "https://api.kdniao.com/Ebusiness/EbusinessOrderHandle.aspx";


	public static String getTransportCompany(String logisticCode) throws Exception {

		String requestData = "{'LogisticCode':'" + logisticCode + "'}";
		String data = URLEncoder.encode(requestData, "UTF-8");

		Base64.Encoder encoder = Base64.getEncoder();
		String dataSign = encoder.encodeToString(MD5(requestData + APIKEY, "UTF-8").getBytes("UTF-8"));
		String json = initJsonData(USERID, URLEncoder.encode(dataSign, "UTF-8"), data, "2002");
		return HttpUtil.post(KD_QUERY_URL, json);
	}

	/**
	 * 发起请求的方法
	 *
	 * @param code         快递公司编码，
	 * @param logisticCode 物流单号
	 * @return 请求到的数据
	 * @throws Exception
	 */
	public static String getOrderTraces(String memo, String code, String logisticCode) throws Exception {

		String requestData = "{'OrderCode':'','CustomerName':'" + memo + "','ShipperCode':'" + code + "','LogisticCode':'" + logisticCode + "'}";
		String data = URLEncoder.encode(requestData, "UTF-8");

		Base64.Encoder encoder = Base64.getEncoder();
		String dataSign = encoder.encodeToString(MD5(requestData + APIKEY, "UTF-8").getBytes("UTF-8"));
		String json = initJsonData(USERID, URLEncoder.encode(dataSign, "UTF-8"), data, "8001");
		return HttpUtil.post(KD_QUERY_URL, json);
	}

	/**
	 * 用户md5加密，我试过用Java自带的api进行md5加密，不过貌似不对，直接调用下面这个方法可以的
	 *
	 * @param str
	 * @param charset
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	private static String MD5(String str, String charset) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(str.getBytes(charset));
		byte[] result = md.digest();
		StringBuffer sb = new StringBuffer(32);
		for (int i = 0; i < result.length; i++) {
			int val = result[i] & 0xff;
			if (val <= 0xf) {
				sb.append("0");
			}
			sb.append(Integer.toHexString(val));
		}
		return sb.toString().toLowerCase();
	}

	/**
	 * 封装了个json数据
	 *
	 * @param userid   用户的userid
	 * @param dataSign 数据内容签名
	 * @param data     数据内容
	 * @return json字符串
	 */
	private static String initJsonData(String userid, String dataSign, String data, String instructNo) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("RequestData", data);//数据内容(URL 编码:UTF-8)
		params.put("EBusinessID", userid);//用户的userid
		params.put("RequestType", instructNo);//查询路径的请求类型就是1002，确定下来的

		/**
		 * dataSign：数据内容签名：构造这个参数相当复杂
		 * 第一步：把(请求内容(未编码前)+ApiKey)进行 MD5 加密，
		 * 第二步：然后 Base64 编码，
		 * 第三步：最后进行 URL(utf-8)编码
		 *
		 */
		params.put("DataSign", dataSign);

		params.put("DataType", "2");//该参数值为2

		StringBuilder param = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			if (param.length() > 0) {
				param.append("&");
			}
			param.append(entry.getKey());
			param.append("=");
			param.append(entry.getValue());
		}
		return param.toString();
	}

}
