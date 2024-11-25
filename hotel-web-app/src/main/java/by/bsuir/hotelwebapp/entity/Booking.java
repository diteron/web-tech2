package by.bsuir.hotelwebapp.entity;

import java.time.LocalDateTime;

import org.hibernate.proxy.HibernateProxy;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne()
    @JoinColumn(name = "room_type_id", nullable = false)
    private RoomType roomType;

    @Column(name = "number_of_nights", nullable = false)
    private Integer numberOfNights;

    @Column(name = "booking_date", nullable = false)
    private LocalDateTime bookingDate;

    public Booking() {
        bookingDate = LocalDateTime.now();
    }
    
    public Booking(Long id, User user, RoomType roomType, Integer numberOfNights, LocalDateTime bookingDate) {
        this.id = id;
        this.user = user;
        this.roomType = roomType;
        this.numberOfNights = numberOfNights;
        this.bookingDate = bookingDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public Integer getNumberOfNights() {
        return numberOfNights;
    }

    public void setNumberOfNights(Integer numberOfNights) {
        this.numberOfNights = numberOfNights;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public void decrementNumberOfNights() {
        if (numberOfNights > 0) {
            --numberOfNights;
        }
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

        Booking user = (Booking) object;

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
