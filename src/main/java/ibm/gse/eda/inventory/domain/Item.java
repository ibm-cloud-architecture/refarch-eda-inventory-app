package ibm.gse.eda.inventory.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

import com.ibm.db2.cmx.annotation.Id;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Item extends PanacheEntity {
    @Column(length = 10, unique = true)
    public String sku;
    @Column(length = 100)
    public String title;
    public double price;

    public Item(){}
}