package com.ex.echo.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.ex.echo.common.Constants;
import com.ex.echo.common.JsonModel;
import com.ex.echo.config.AlipayConfig;
import com.ex.echo.dto.OrdersDTO;
import com.ex.echo.service.OrdersService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Exception
 * @Date: 2022/5/5
 * @Description: 支付管理
 */
@Slf4j
@RestController
public class AlipayController {

    @Autowired
    private OrdersService orderService;

    /**
     * 订单支付
     *
     * @param request   .
     * @param response  .
     * @param remark    .
     * @param addressId .
     * @param ordersId  .
     * @throws ServletException .
     * @throws IOException      .
     */
    @RequestMapping(value = "/alipay/buy", method = RequestMethod.GET)
    @ApiOperation(value = "支付", tags = "支付宝沙箱支付接口")
    public void pay(HttpServletRequest request, HttpServletResponse response, String remark, Long addressId, Long ordersId) throws ServletException, IOException {
        log.info("开始支付订单,remark:[{}],addressId:[{}],ordersId:[{}]", remark, addressId,ordersId);

        AlipayConfig ac = new AlipayConfig();

        Long userId = (Long) request.getSession().getAttribute(Constants.USER);

        OrdersDTO ordersDTO = new OrdersDTO();
        ordersDTO.setId(ordersId);
        ordersDTO.setAddressId(addressId);

        OrdersDTO result = orderService.editOrdersAddressId(ordersDTO,userId);

        String total = (result.getPrice().divide(new BigDecimal("100")) ).toString();
        String orderSn = result.getId().toString();
        String title = "支付订单" + orderSn;
        String message = "支付订单" + orderSn;

        //向支付宝发送请求
        AlipayClient alipayClient = new DefaultAlipayClient(ac.getGatewayUrl(), ac.getAppId(),
                ac.getMerchantPrivateKey(), "json", ac.getCharset(), ac.getAlipayPublicKey(),
                ac.getSignType());
        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();

        String url = getUrlStart(request) + "/alipay/notify";
        alipayRequest.setNotifyUrl(url);
        alipayRequest.setReturnUrl(url);

        alipayRequest.setBizContent("{\"out_trade_no\":\"" + orderSn + "\","
                + "\"total_amount\":\"" + total + "\","
                + "\"subject\":\"" + title + "\","
                + "\"body\":\"" + message + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        AlipayTradePagePayResponse alipayResponse = null;
        try {
            alipayResponse = alipayClient.pageExecute(alipayRequest);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=UTF-8");
        assert alipayResponse != null;
        response.getWriter().write(alipayResponse.getBody());
    }

    /**
     * 支付宝支付回调
     *
     * @param request .
     * @param response .
     * @throws ServletException .
     */
    @GetMapping("/alipay/notify")
    public JsonModel notify(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("支付宝回调");
        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<>();
        Map requestParams = request.getParameterMap();
        for (Object o : requestParams.keySet()) {
            String name = (String) o;
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }
        AlipayConfig alipayConfig = new AlipayConfig();
        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)
        //商户订单号
        String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
        //支付宝交易号
        String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");  //支付宝交易号

        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)
        //计算得出通知验证结果
        //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
        boolean verify_result = false;
        try {
            verify_result = AlipaySignature.rsaCheckV1(params, alipayConfig.getAlipayPublicKey(), alipayConfig.getCharset(), "RSA2");
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        log.info("支付宝回调验证结果：" + verify_result);

        //验证成功
        if (verify_result) {
            OrdersDTO ordersDTO = new OrdersDTO();
            ordersDTO.setId(Long.parseLong(out_trade_no));
            ordersDTO.setPayStatus(2);
            boolean result = orderService.editOrdersPayStatus(ordersDTO);
            if (result) {
                String url = getUrlStart(request) + "/front/page/pay-success.html";
                response.sendRedirect(url);
            }
        }

        log.info("商户订单号{}支付宝交易号{}", out_trade_no, trade_no);

        return JsonModel.success(params);
    }

    public static String getUrlStart(HttpServletRequest request) {
        StringBuilder url = new StringBuilder();
        url.append(request.getScheme());
        System.out.println(url);
        url.append("://").append(request.getServerName());
        System.out.println(url);
        url.append(":").append(request.getServerPort());
        System.out.println(url);
        return url.toString();
    }
}
