package com.zifang.util.core.parser.xml;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@XmlRootElement(name = "RequestOrder")
@Data
public class TitleRequest {

    private List<Item> item;

    @XmlType(propOrder = {"code", "province", "city", "district"})
    @Data
    public static class Item {

        private String code;
        private String province;
        private String city;
        private String district;
    }
}
