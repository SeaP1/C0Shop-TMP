package pojo;

import java.time.LocalDateTime;

public class Orders {
    private String userId;
    private LocalDateTime timestamp;
    private String itemId;

    public Orders(String UserId, LocalDateTime timestamp, String itemId) {
        this.userId = UserId;
        this.timestamp = timestamp;
        this.itemId = itemId;
    }

    public String getCUserId() { return this.userId; }

    public LocalDateTime getTimestamp() { return timestamp; }

    public String getItemId() { return itemId; }

    @Override
    public String toString() {
        return "OrderRecord{" +
                "userId='" + userId + '\'' +
                ", timestamp=" + timestamp +
                ", itemId='" + itemId + '\'' +
                '}';
    }
}
