package com.mars.fw.web.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * @Author King
 * @create 2020/4/21 16:57
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "king_org入参实体")
public class KingOrgInput {


    @ApiModelProperty(value = "编码")
    @NotBlank
    private String kingCode;

    @ApiModelProperty(value = "King名称")
    private String kingName;


}
