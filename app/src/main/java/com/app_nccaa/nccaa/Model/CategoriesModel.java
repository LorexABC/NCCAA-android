package com.app_nccaa.nccaa.Model;

import java.util.ArrayList;

public class CategoriesModel {

    private String id;
    private String name;
    private String fill;
    private String description;
    private String target;
    private String fill_from_subcategory;
    private String is_selectable;
    private String select_multiple;
    private String display_target;
    private String display_completed;

    private ArrayList<SubcategoriesModel> subcatArrayList;

    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getDisplay_target() {
        return display_target;
    }

    public void setDisplay_target(String display_target) {
        this.display_target = display_target;
    }

    public String getDisplay_completed() {
        return display_completed;
    }

    public void setDisplay_completed(String display_completed) {
        this.display_completed = display_completed;
    }

    public String getSelect_multiple() {
        return select_multiple;
    }

    public void setSelect_multiple(String select_multiple) {
        this.select_multiple = select_multiple;
    }

    public String getIs_selectable() {
        return is_selectable;
    }

    public void setIs_selectable(String is_selectable) {
        this.is_selectable = is_selectable;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFill() {
        return fill;
    }

    public void setFill(String fill) {
        this.fill = fill;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getFill_from_subcategory() {
        return fill_from_subcategory;
    }

    public void setFill_from_subcategory(String fill_from_subcategory) {
        this.fill_from_subcategory = fill_from_subcategory;
    }

    public ArrayList<SubcategoriesModel> getSubcatArrayList() {
        return subcatArrayList;
    }

    public void setSubcatArrayList(ArrayList<SubcategoriesModel> subcatArrayList) {
        this.subcatArrayList = subcatArrayList;
    }
}
