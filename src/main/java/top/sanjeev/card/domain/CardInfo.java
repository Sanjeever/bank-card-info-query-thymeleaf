package top.sanjeev.card.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CardInfo {

    /**
     * 卡类型（DC 借记卡, CC 贷记卡）
     */
    @JsonProperty("cardType")
    String cardType;

    /**
     * 行代码
     */
    @JsonProperty("bank")
    String bank;

    /**
     * 卡号
     */
    @JsonProperty("key")
    String key;

    /**
     * 错误信息
     */
    @JsonProperty("messages")
    List<Message> messages;

    /**
     * 验证是否通过
     */
    @JsonProperty("validated")
    Boolean validated;

    @JsonProperty("stat")
    String stat;

    @Data
    static class Message {

        /**
         * 错误码
         */
        private String errorCodes;

        /**
         * 字段名称
         */
        private String name;

    }
}