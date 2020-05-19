package com.mars.fw.web.ctrl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mars.fw.web.dto.KingOrgInput;
import com.mars.fw.web.entity.KingOrgEntity;
import com.mars.fw.web.reponse.King;
import com.mars.fw.web.reponse.KingCode;
import com.mars.fw.web.service.KingOrgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @Author King
 * @create 2020/4/20 12:20
 */
@Api(tags = "【King】王储API")
@RestController
@RequestMapping("/mars")
public class MarsController {

    @Autowired
    private KingOrgService kingOrgService;

    @ApiOperation(value = "King 视察接口")
    @PostMapping(value = "/ctrl")
    public King<Page<KingOrgEntity>> ctrl() {
        List<KingOrgEntity> list = kingOrgService.listKingOrg();
        Page<KingOrgEntity> page = kingOrgService.pageKingOrg();
        return King.success(KingCode.SUCCESS, page);
    }

    @ApiOperation(value = "King 扩展领土")
    @PostMapping(value = "/add")
    public void add(@RequestBody KingOrgInput input) {
        kingOrgService.addKingOrg(input);
    }

    public static void main(String[] args) {

    }
}
