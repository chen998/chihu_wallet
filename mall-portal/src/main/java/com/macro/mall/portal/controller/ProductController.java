//package com.macro.mall.portal.controller;
//
//import com.macro.mall.common.api.CommonPage;
//import com.macro.mall.common.api.CommonResult;
//import com.macro.mall.model.PmsProduct;
//import com.macro.mall.portal.dto.ProductQueryParam;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.util.List;
//
//@Controller
//@Api(tags = "ProductController", description = "app端商品")
//@RequestMapping("/product")
//public class ProductController {
//    @ApiOperation("根据分类查询商品列表")
//    @RequestMapping(value = "/list", method = RequestMethod.GET)
//    @RequestBody
//    public CommonResult<CommonPage<PmsProduct>> getList(ProductQueryParam productQueryParam,
//                                                        @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
//                                                        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
//        List<PmsProduct> productList = productService.list(productQueryParam, pageSize, pageNum);
//        return CommonResult.success(CommonPage.restPage(productList));
//    }
//}
