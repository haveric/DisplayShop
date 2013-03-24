package haveric.displayShop.log;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.avaje.ebean.validation.NotNull;

@Entity()
@Table(name = "displayshop_shoplogs")
public class ShopLog {

    @Id
    private int id;

    @NotNull
    private int shopId;

    @NotNull
    private String type;

    @NotNull
    private String message;

    public void setId(int newId) {
        id = newId;
    }

    public int getId() {
        return id;
    }

    public void setShopId(int newShopId) {
        shopId = newShopId;
    }

    public int getShopId() {
        return shopId;
    }

    public void setType(String newType) {
        type = newType;
    }

    public String getType() {
        return type;
    }

    public void setMessage(String newMessage) {
        message = newMessage;
    }

    public String getMessage() {
        return message;
    }
}
