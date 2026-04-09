package FoodDelivery.auth.Order;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private String userEmail;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "order_items", joinColumns = @JoinColumn(name = "order_id"))
    @Column(name = "item")
    private List<String> items;

    private Double totalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.CREATED;

    private LocalDateTime createdAt;

    private long acceptedTime;
    private long preparedTime;

    // Default Constructor
    public Order() {}

    // Parameterized Constructor
    public Order(String userEmail, List<String> items, Double totalAmount) {
        this.userEmail = userEmail;
        this.items = items;
        this.totalAmount = totalAmount;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // ✅ Setters
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;

        if (status == OrderStatus.ACCEPTED) {
            this.acceptedTime = System.currentTimeMillis();
        }

        if (status == OrderStatus.PREPARED) {
            this.preparedTime = System.currentTimeMillis();
        }
    }

    // ✅ Getters
    public Long getOrderId() {
        return orderId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public List<String> getItems() {
        return items;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public long getAcceptedTime() {
        return acceptedTime;
    }

    public long getPreparedTime() {
        return preparedTime;
    }

    public int getTotalMinutes() {
        if (preparedTime <= 0 || acceptedTime <= 0)
            return 0;
        return (int) ((preparedTime - acceptedTime) / (1000 * 60));
    }
}