package com.hmall.item.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hmall.api.dto.OrderDetailDTO;
import com.hmall.item.domain.po.Item;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * 商品表 Mapper 接口
 * </p>
 *
 * @author 虎哥
 * @since 2023-05-05
 */
// com.hmall.item.mapper.ItemMapper
public interface ItemMapper extends BaseMapper<Item> {

    // UPDATE 语句必须添加 WHERE stock >= #{num} 来进行乐观锁库存检查
    // 并返回影响的行数 (int)，MyBatis 会自动处理
    @Update("UPDATE item SET stock = stock - #{num} WHERE id = #{itemId} AND stock >= #{num}")
    int updateStock(OrderDetailDTO orderDetail); // 将返回值改为 int
}