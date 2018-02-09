package com.example.reservationservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.print.DocFlavor;
import java.util.Collection;
import java.util.stream.Stream;

@SpringBootApplication
public class ReservationServiceApplication {

    public static void main( String[] args ) {
        SpringApplication.run(ReservationServiceApplication.class, args);
    }
}
@RestController
@RefreshScope
@PropertySource (value = {"classpath:bootstrapt.properties"})
class MessageRestController {
    private final String value;

    @RequestMapping(method = RequestMethod.GET, value = "/message")
    public String read() {
        return value;
    }

    @Autowired
    public MessageRestController( @Value ( "${message}" ) String value ) {
        this.value = value;
    }
}

@Component
class LivelessonhealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        return Health.status("I <3 LiveLessons").build();
    }
}



@Component
class SampleDataCLR implements CommandLineRunner {

    private final ResersvtionRepository resersvtionRepository;

    @Autowired
    public SampleDataCLR( ResersvtionRepository resersvtionRepository ) {
        this.resersvtionRepository = resersvtionRepository;
    }

    @Override
    public void run( String... strings ) throws Exception {
        Stream.of("josh", "philp", "dave", "Spencer",
                "Stephan", "Stephen", "Mark", "Mark"
                , "Rod", "Rob", "Jennifer", "Tamao")
                .forEach(name -> resersvtionRepository.save(new Reservation(name)));
        resersvtionRepository.findAll().forEach(System.out::println);
    }
}

@RepositoryRestResource
interface ResersvtionRepository extends JpaRepository<Reservation, Long> {
    @RestResource(path = "by-name")
    Collection<Reservation> findByReservationName( @Param("rn") String rn );
}

@Entity
class Reservation {
    @Id
    @GeneratedValue
    private Long id;
    private String reservationName;

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", reservationName='" + reservationName + '\'' +
                '}';
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public Reservation() {
    }

    public Reservation( String reservationName ) {
        this.reservationName = reservationName;
    }

    public String getReservationName() {
        return reservationName;
    }

    public void setReservationName( String reservationName ) {
        this.reservationName = reservationName;
    }
}

