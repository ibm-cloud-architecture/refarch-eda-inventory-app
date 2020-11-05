package ibm.gse.eda.inventory.domain;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@Cacheable
public class Store extends PanacheEntity  {

    @Column(length = 50, unique = true)
    public String name;
    @Column(length = 50)
    public String city;
    @Column(length = 3)
    public String state;
    @Column(length = 10)
    public String zipcode;

    public Store(){}
}