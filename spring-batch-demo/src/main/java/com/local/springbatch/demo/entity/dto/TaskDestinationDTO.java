package com.local.springbatch.demo.entity.dto;

/**
 * @Create-By: yanchen 2021/4/9 02:46
 * @Description: TODO
 */
public class TaskDestinationDTO {

    public static class  Parent{
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class Child{
        private String name;
        private Integer parentID;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getParentID() {
            return parentID;
        }

        public void setParentID(Integer parentID) {
            this.parentID = parentID;
        }
    }
}
