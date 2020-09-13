package com.atguigu.gulimall.ware.constant;

/**
 * @author xuxing
 * @date 2020/9/13
 */
public class WareConstant {
    public enum PurchaseEnum{
        NEW_BUILT(0,"新建"),
        ASSIGNED(1,"已分配"),
        RECEIVED(2,"已领取"),
        COMPLETED(3,"已完成"),
        EXCEPTION(4,"有异常");
        private Integer code;
        private String desc;

        PurchaseEnum(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    public enum PurchaseDetailEnum{
        NEW_BUILT(0,"新建"),
        ASSIGNED(1,"已分配"),
        BE_PURCHASING(2,"正在采购"),
        COMPLETED(3,"已完成"),
        PURCHASE_FAIL(4,"采购失败");
        private Integer code;
        private String desc;

        PurchaseDetailEnum(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
