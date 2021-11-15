package akatsuki.restaurantsysteminformation.room.dto;

import akatsuki.restaurantsysteminformation.restauranttable.dto.RestaurantTableCreateDTO;
import akatsuki.restaurantsysteminformation.restauranttable.dto.RestaurantTableDTO;

import java.util.List;

public class UpdateRoomDTO {
    List<RestaurantTableCreateDTO> newTables;
    List<RestaurantTableDTO> updateTables;
    List<Long> deleteTables;
    private String name;

    public UpdateRoomDTO(String name, List<RestaurantTableCreateDTO> newTables, List<RestaurantTableDTO> updateTables, List<Long> deleteTables) {
        this.name = name;
        this.newTables = newTables;
        this.updateTables = updateTables;
        this.deleteTables = deleteTables;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RestaurantTableCreateDTO> getNewTables() {
        return newTables;
    }

    public void setNewTables(List<RestaurantTableCreateDTO> newTables) {
        this.newTables = newTables;
    }

    public List<RestaurantTableDTO> getUpdateTables() {
        return updateTables;
    }

    public void setUpdateTables(List<RestaurantTableDTO> updateTables) {
        this.updateTables = updateTables;
    }

    public List<Long> getDeleteTables() {
        return deleteTables;
    }

    public void setDeleteTables(List<Long> deleteTables) {
        this.deleteTables = deleteTables;
    }
}
