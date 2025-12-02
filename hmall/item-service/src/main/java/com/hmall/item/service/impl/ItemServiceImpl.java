package com.hmall.item.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmall.api.dto.ItemDTO;
import com.hmall.api.dto.OrderDetailDTO;
import com.hmall.common.exception.BizIllegalException;
import com.hmall.common.utils.BeanUtils;
import com.hmall.item.domain.po.Item;
import com.hmall.item.mapper.ItemMapper;
import com.hmall.item.service.IItemService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author 虎哥
 */
@Service
@RequiredArgsConstructor // 自动注入
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item> implements IItemService {
    private final ItemMapper itemMapper; // 注入Mapper

    @Override
    /*@Transactional*/ // 确保事务一致性
    @GlobalTransactional
    public void deductStock(List<OrderDetailDTO> items) {
        int totalUpdated = 0;
        // 1. 循环执行批量扣减操作
        for (OrderDetailDTO detail : items) {
            // 2. 调用Mapper方法，执行带库存检查的UPDATE
            int updatedRows = itemMapper.updateStock(detail);

            // 3. 检查影响行数。如果影响行数等于 0，则说明库存不足。
            if (updatedRows == 0) {
                // 如果是批量操作中的第一个或中间的项，并且前面已经有成功的更新，
                // 此时应该抛出异常并触发事务回滚。
                throw new BizIllegalException("商品id: " + detail.getItemId() + " 库存不足！");
            }
            totalUpdated++;
        }

        // 4. (可选) 确保所有项都成功更新
        if (totalUpdated != items.size()) {
            // 实际上由于上面的 if 检查和抛出异常，这一步基本不会执行到，
            // 除非updateStock方法返回了负数（不可能）或出现其它非预期情况
            throw new BizIllegalException("批量更新库存失败！");
        }

    }

    @Override
    public List<ItemDTO> queryItemByIds(Collection<Long> ids) {
        return BeanUtils.copyList(listByIds(ids), ItemDTO.class);
    }
}
