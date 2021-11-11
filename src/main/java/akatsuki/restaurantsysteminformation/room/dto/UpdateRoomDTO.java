package akatsuki.restaurantsysteminformation.room.dto;

import akatsuki.restaurantsysteminformation.restauranttable.dto.CreateRestaurantTableDTO;
import akatsuki.restaurantsysteminformation.restauranttable.dto.UpdateRestaurantTableDTO;

import java.util.List;

public class UpdateRoomDTO {
    private String name;
    List<CreateRestaurantTableDTO> newTables;
    List<UpdateRestaurantTableDTO> updateTables;
    List<Long> deleteTables;

    public UpdateRoomDTO(String name, List<CreateRestaurantTableDTO> newTables, List<UpdateRestaurantTableDTO> updateTables, List<Long> deleteTables) {
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

    public List<CreateRestaurantTableDTO> getNewTables() {
        return newTables;
    }

    public void setNewTables(List<CreateRestaurantTableDTO> newTables) {
        this.newTables = newTables;
    }

    public List<UpdateRestaurantTableDTO> getUpdateTables() {
        return updateTables;
    }

    public void setUpdateTables(List<UpdateRestaurantTableDTO> updateTables) {
        this.updateTables = updateTables;
    }

    public List<Long> getDeleteTables() {
        return deleteTables;
    }

    public void setDeleteTables(List<Long> deleteTables) {
        this.deleteTables = deleteTables;
    }
}
