package ibm.gse.eda.inventory.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Inventory extends PanacheEntity {
    public String storeName;
    public String sku;
    public Long quantity;
    public String timestamp;
    
    public Inventory(){}
}