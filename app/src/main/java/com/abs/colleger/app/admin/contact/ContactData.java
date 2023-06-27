package com.abs.colleger.app.admin.contact;

public class ContactData {
    private String name,email,phone,image,school, department, designation,key;

    public ContactData() {
    }
    public ContactData(String name, String email, String phone, String image, String school, String department, String designation, String key) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.image = image;
        this.school = school;
        this.department = department;
        this.designation = designation;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) { this.image = image; }

    public String getSchool(){ return school; }
    public void setSchool(String school){ this.school=school; }

    public String getDepartment() { return department;}

    public void setDepartment(String department) { this.department = department; }

    public String getDesignation() { return designation; }

    public void setDesignation(String designation) { this.designation = designation; }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
