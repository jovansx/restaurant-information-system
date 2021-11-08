package akatsuki.restaurantsysteminformation.drinkitems.dto;

import akatsuki.restaurantsysteminformation.drinkitem.DrinkItem;
import akatsuki.restaurantsysteminformation.drinkitems.DrinkItems;
import akatsuki.restaurantsysteminformation.enums.ItemState;

public class DrinkItemsDTOActive {

    private Long id;
    private String initials;
    private String name;
    private ItemState state;

    public DrinkItemsDTOActive(DrinkItems drinkItems) {
        this.id = drinkItems.getId();
        this.state = drinkItems.getState();
        if(drinkItems.getBartender() != null)
            this.initials = drinkItems.getBartender().getFirstName().toUpperCase().charAt(0) + " " + drinkItems.getBartender().getLastName().toUpperCase().charAt(0);
        else
            this.initials = "";
        StringBuilder str = new StringBuilder();
        for (DrinkItem di: drinkItems.getDrinkItems()) {
            str.append(di.getItem().getName());
            str.append(",");
        }
        this.name = str.toString().substring(0, str.length()-1);
    }

    public ItemState getState() {
        return state;
    }

    public void setState(ItemState state) {
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
