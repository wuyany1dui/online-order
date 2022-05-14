package com.design.onlineorder.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.design.onlineorder.dao.OrderDao;
import com.design.onlineorder.dao.ProductDao;
import com.design.onlineorder.dao.StoreDao;
import com.design.onlineorder.entity.Order;
import com.design.onlineorder.entity.Product;
import com.design.onlineorder.entity.Store;
import com.design.onlineorder.enums.OrderStatusEnum;
import com.design.onlineorder.enums.ResultEnum;
import com.design.onlineorder.exception.MyException;
import com.design.onlineorder.service.OrderService;
import com.design.onlineorder.vo.OrderListPageVo;
import com.design.onlineorder.vo.OrderListQueryVo;
import com.design.onlineorder.vo.OrderProductInfoVo;
import com.design.onlineorder.vo.OrderVo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author Created by DrEAmSs on 2022-04-26 19:21
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderDao orderDao;

    @Resource
    private ProductDao productDao;

    @Resource
    private StoreDao storeDao;

    @Override
    public String create(OrderVo orderVo) {
        Order order = new Order();
        String id = UUID.randomUUID().toString().replace("-", "");
        if (StringUtils.isNotBlank(orderVo.getId())) {
            id = orderVo.getId();
            Order oldOrder = orderDao.lambdaQuery().eq(Order::getId, id).one();
            List<String> storeIds = new ArrayList<>(Arrays.asList(oldOrder.getStoreId().split(",")));
            List<String> storeNames = new ArrayList<>(Arrays.asList(oldOrder.getStoreName().split(",")));
            List<String> merchantIds = new ArrayList<>(Arrays.asList(oldOrder.getMerchantId().split(",")));
            List<String> merchantNames = new ArrayList<>(Arrays.asList(oldOrder.getMerchantName().split(",")));
            if (!storeIds.contains(orderVo.getStoreId())) {
                storeIds.add(orderVo.getStoreId());
            }
            if (!storeNames.contains(orderVo.getStoreName())) {
                storeNames.add(orderVo.getStoreName());
            }
            if (!merchantIds.contains(orderVo.getMerchantId())) {
                merchantIds.add(orderVo.getMerchantId());
            }
            if (!merchantNames.contains(orderVo.getMerchantName())) {
                merchantNames.add(orderVo.getMerchantName());
            }
            orderVo.setStoreId(String.join(",", storeIds));
            orderVo.setStoreName(String.join(",", storeNames));
            orderVo.setMerchantId(String.join(",", merchantIds));
            orderVo.setMerchantName(String.join(",", merchantNames));
        } else {
            order.setCreateTime(new Timestamp(System.currentTimeMillis()));
        }
        BeanUtils.copyProperties(orderVo, order);
        order.setId(id);
        order.setProductInfo(JSON.toJSONString(orderVo.getProductInfos()));
        order.setStatus(OrderStatusEnum.NOT_PAID);
        List<Product> products = productDao.lambdaQuery()
                .in(Product::getId, orderVo.getProductInfos().stream().map(OrderProductInfoVo::getProductId)
                        .collect(Collectors.toList()))
                .list();
        AtomicReference<BigDecimal> price = new AtomicReference<>(BigDecimal.valueOf(0));
        AtomicReference<Float> tempPrice = new AtomicReference<>(0F);
        orderVo.getProductInfos().forEach(temp -> {
            Optional<Product> optional = products.stream()
                    .filter(tempProduct -> temp.getProductId().equals(tempProduct.getId()))
                    .findFirst();
            optional.ifPresent(product ->
                    tempPrice.updateAndGet(v -> v + temp.getCount() * product.getPrice().floatValue()));
        });
        price.set(BigDecimal.valueOf(tempPrice.get()));
        order.setPrice(price.get());
        order.setStoreId(orderVo.getStoreId());
        orderDao.saveOrUpdate(order);
        return id;
    }

    @Override
    public void pay(String id) {
        Optional<Order> orderOpt = orderDao.lambdaQuery().eq(Order::getId, id).oneOpt();
        if (orderOpt.isEmpty()) {
            throw new MyException(400, ResultEnum.ORDER_NOT_EXISTS.getLabel());
        } else {
            if (Objects.equals(orderOpt.get().getStatus(), OrderStatusEnum.PAID)) {
                throw new MyException(400, ResultEnum.ORDER_ALREADY_PAID.getLabel());
            } else {
                String storeId;
                List<OrderProductInfoVo> productInfoVos = JSON.parseObject(orderOpt.get().getProductInfo(),
                        new TypeReference<List<OrderProductInfoVo>>() {
                        });
                List<Product> products = productDao.lambdaQuery()
                        .in(Product::getId, productInfoVos.stream().map(OrderProductInfoVo::getProductId)
                                .collect(Collectors.toList()))
                        .list();
                storeId = products.get(0).getStoreId();
                List<Product> updateProducts = Lists.newArrayList();
                products.forEach(temp -> {
                    Product product = new Product();
                    product.setId(temp.getId());
                    Optional<OrderProductInfoVo> optional = productInfoVos.stream()
                            .filter(tempInfo -> temp.getId().equals(tempInfo.getProductId()))
                            .findFirst();
                    optional.ifPresent(o -> product.setSales(temp.getSales() + o.getCount()));
                    updateProducts.add(product);
                });
                productDao.saveOrUpdateBatch(updateProducts);
                List<Product> productList = productDao.lambdaQuery().eq(Product::getStoreId, storeId).list();
                AtomicReference<Integer> sales = new AtomicReference<>(0);
                AtomicReference<BigDecimal> salesVolume = new AtomicReference<>(BigDecimal.valueOf(0D));
                productList.forEach(tempProduct -> {
                    sales.updateAndGet(v -> v + tempProduct.getSales());
                    double tempSaleVolume = tempProduct.getSales() * tempProduct.getPrice().doubleValue();
                    salesVolume.set(BigDecimal.valueOf(tempSaleVolume + salesVolume.get().doubleValue()));
                });
                storeDao.lambdaUpdate().set(Store::getSales, sales.get()).set(Store::getSalesVolume, salesVolume.get())
                        .eq(Store::getId, storeId)
                        .update();
                orderDao.lambdaUpdate().eq(Order::getId, id)
                        .set(Order::getPayTime, new Timestamp(System.currentTimeMillis()))
                        .set(Order::getStatus, OrderStatusEnum.PAID)
                        .update();
            }
        }
    }

    @Override
    public OrderListPageVo queryList(OrderListQueryVo orderListQueryVo) {
        List<OrderVo> data = Lists.newArrayList();
        List<Order> orders = orderDao.lambdaQuery()
                .eq(StringUtils.isNotBlank(orderListQueryVo.getId()), Order::getId, orderListQueryVo.getId())
                .eq(StringUtils.isNotBlank(orderListQueryVo.getUserId()), Order::getUserId, orderListQueryVo.getUserId())
                .eq(Objects.nonNull(orderListQueryVo.getStatus()), Order::getStatus, orderListQueryVo.getStatus())
                .orderBy(true, false, Order::getCreateTime)
                .list();
        orders.stream()
                .filter(tempOrder -> Objects.equals(tempOrder.getStatus(), OrderStatusEnum.NOT_PAID))
                .collect(Collectors.toList())
                .forEach(tempOrder -> {
                    OrderVo orderVo = new OrderVo();
                    BeanUtils.copyProperties(tempOrder, orderVo);
                    orderVo.setProductInfos(JSON.parseObject(tempOrder.getProductInfo(),
                            new TypeReference<List<OrderProductInfoVo>>() {
                            }));
                    data.add(orderVo);
                });
        orders.stream()
                .filter(tempOrder -> !Objects.equals(tempOrder.getStatus(), OrderStatusEnum.NOT_PAID))
                .collect(Collectors.toList())
                .forEach(tempOrder -> {
                    OrderVo orderVo = new OrderVo();
                    BeanUtils.copyProperties(tempOrder, orderVo);
                    orderVo.setProductInfos(JSON.parseObject(tempOrder.getProductInfo(),
                            new TypeReference<List<OrderProductInfoVo>>() {
                            }));
                    data.add(orderVo);
                });
        return new OrderListPageVo(data.size(), data.stream()
                .skip((long) (orderListQueryVo.getPageIndex() - 1) * orderListQueryVo.getPageSize())
                .limit(orderListQueryVo.getPageSize())
                .collect(Collectors.toList()));
    }

    @Override
    public List<OrderProductInfoVo> queryDetail(String id) {
        Order order = orderDao.lambdaQuery().eq(Order::getId, id).one();
        List<OrderProductInfoVo> orderProductInfoVos = JSON.parseObject(order.getProductInfo(),
                new TypeReference<List<OrderProductInfoVo>>() {
                });
        List<Product> products = productDao.lambdaQuery()
                .in(Product::getId, orderProductInfoVos.stream().map(OrderProductInfoVo::getProductId)
                        .collect(Collectors.toList()))
                .list();
        orderProductInfoVos.forEach(temp -> {
            Optional<Product> optional = products.stream()
                    .filter(tempProduct -> temp.getProductId().equals(tempProduct.getId()))
                    .findFirst();
            optional.ifPresent(o -> {
                if (StringUtils.isNotBlank(o.getFirstImage())) {
                    temp.setFirstImage(o.getFirstImage());
                }
                temp.setPrice(o.getPrice());
            });
        });
        return orderProductInfoVos;
    }
}
