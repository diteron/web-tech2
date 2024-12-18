package by.bsuir.hotelwebapp.entity;

import jakarta.persistence.Table;

import org.hibernate.proxy.HibernateProxy;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
@Table(name = "room_types")
public class RoomType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(name = "free_rooms", nullable = false)
    private Integer freeRooms;

    public RoomType() {
    }

    public RoomType(Long id, String name, Integer price, Integer freeRooms) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.freeRooms = freeRooms;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getFreeRooms() {
        return freeRooms;
    }

    public void setFreeRooms(Integer freeRooms) {
        this.freeRooms = freeRooms;
    }

    public void decrementFreeRooms() {
        if (freeRooms > 0) {
            --freeRooms;
        }
    }

    public void incrementFreeRooms() {
        ++freeRooms;
    }

    @Override
    public final boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }

        Class<?> oEffectiveClass = object instanceof HibernateProxy 
                ? ((HibernateProxy) object).getHibernateLazyInitializer()
                        .getPersistentClass() 
                : object.getClass();                                             
        Class<?> thisEffectiveClass = this instanceof HibernateProxy 
                ? ((HibernateProxy) this).getHibernateLazyInitializer()
                        .getPersistentClass() 
                : this.getClass();

        if (thisEffectiveClass != oEffectiveClass) { 
            return false;
        }

        RoomType user = (RoomType) object;

        return this.getId() != null && this.getId() == user.getId();
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy 
                ? ((HibernateProxy) this).getHibernateLazyInitializer()
                        .getPersistentClass().hashCode()
                : this.getClass().hashCode();
    }
}
