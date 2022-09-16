package com.wy.service.impl;

import com.wy.entity.AddressBook;
import com.wy.mapper.AddressBookMapper;
import com.wy.service.AddressBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 地址管理 服务实现类
 * </p>
 *
 * @author wangyu
 * @since 2022-09-14
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

}
