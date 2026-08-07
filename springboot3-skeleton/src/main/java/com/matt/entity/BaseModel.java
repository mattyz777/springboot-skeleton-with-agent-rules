package com.matt.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

@Data
public abstract class BaseModel {
    public BaseModel() {
        this.createdAt = System.currentTimeMillis();
    }

    /**
     * Primary key ID with auto-increment
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * @TableLogic
     *      logic delete - 1:deleted, 0: not deleted
     *      not working in xml
     */
    @TableField("is_deleted")
    @TableLogic
    private Integer deleted;

    /**
     * Creator ID
     */
    @TableField("created_by")
    private Long createdBy;

    /**
     * Last updater ID
     */
    @TableField("updated_by")
    private Long updatedBy;

    /**
     * Creation timestamp (milliseconds)
     */
    @TableField("created_at")
    private Long createdAt;

    /**
     * Last update timestamp (milliseconds)
     */
    @TableField("updated_at")
    private Long updatedAt;
}
