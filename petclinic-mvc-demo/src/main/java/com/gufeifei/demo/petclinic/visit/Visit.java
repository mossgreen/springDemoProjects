package com.gufeifei.demo.petclinic.visit;


import com.gufeifei.demo.petclinic.model.BaseEntity;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/*Java Bean domain object, representing a visit*/


@Entity
@Table(name = "visits")
public class Visit  extends BaseEntity{

    @Column(name = "visit_date")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MMM-dd")
    private Date date;

    @NotEmpty
    @Column(name = "pet_id")
    private  String description;

    @Column(name = "pet_id")
    private Integer petId;

    /*
     * create a new instance of visit for current date */
    public Visit() {
        this.date = new Date();
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPetId() {
        return this.petId;
    }

    public void setPetId(Integer petId) {
        this.petId = petId;
    }

}
